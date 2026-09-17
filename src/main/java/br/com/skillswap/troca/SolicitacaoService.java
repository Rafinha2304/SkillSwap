package br.com.skillswap.troca;

import br.com.skillswap.troca.dto.SolicitacaoItem;
import br.com.skillswap.troca.dto.SolicitacoesResponse;
import br.com.skillswap.usuario.Usuario;
import br.com.skillswap.usuario.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public SolicitacaoService(SolicitacaoRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public SolicitacaoItem enviar(Usuario deUsuario, Long paraUsuarioId) {
        if (paraUsuarioId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe quem receberá a solicitação.");
        }
        if (deUsuario.getId().equals(paraUsuarioId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você não pode solicitar uma troca consigo mesmo.");
        }
        Usuario paraUsuario = buscarUsuario(paraUsuarioId);

        boolean pendenteDireta = repository.existsByDeUsuarioIdAndParaUsuarioIdAndStatus(
                deUsuario.getId(), paraUsuarioId, StatusSolicitacao.PENDENTE);
        boolean pendenteContraria = repository.existsByDeUsuarioIdAndParaUsuarioIdAndStatus(
                paraUsuarioId, deUsuario.getId(), StatusSolicitacao.PENDENTE);
        if (pendenteDireta || pendenteContraria) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma solicitação de troca pendente entre vocês.");
        }

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setDeUsuario(deUsuario);
        solicitacao.setParaUsuario(paraUsuario);
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
        solicitacao.setCriadaEm(LocalDateTime.now());
        solicitacao = repository.save(solicitacao);
        return new SolicitacaoItem(solicitacao.getId(), solicitacao.getStatus().name(),
                solicitacao.getCriadaEm(), paraUsuario.getId(), paraUsuario.getNome());
    }

    @Transactional
    public SolicitacaoItem responder(Long solicitacaoId, Usuario respondente, String novoStatus) {
        Solicitacao solicitacao = repository.findById(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação não encontrada."));

        if (!solicitacao.getParaUsuario().getId().equals(respondente.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas quem recebeu a solicitação pode respondê-la.");
        }
        if (solicitacao.getStatus() != StatusSolicitacao.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta solicitação já foi respondida.");
        }

        StatusSolicitacao status;
        try {
            status = StatusSolicitacao.valueOf(novoStatus);
        } catch (IllegalArgumentException | NullPointerException excecao) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Resposta inválida para a solicitação.");
        }
        if (status == StatusSolicitacao.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Resposta inválida para a solicitação.");
        }

        solicitacao.setStatus(status);
        repository.save(solicitacao);
        Usuario outro = solicitacao.getDeUsuario();
        return new SolicitacaoItem(solicitacao.getId(), status.name(), solicitacao.getCriadaEm(),
                outro.getId(), outro.getNome());
    }

    @Transactional(readOnly = true)
    public SolicitacoesResponse listar(Long usuarioId) {
        buscarUsuario(usuarioId);
        List<SolicitacaoItem> recebidas = repository.findByParaUsuarioIdOrderByCriadaEmDesc(usuarioId).stream()
                .map(solicitacao -> item(solicitacao, solicitacao.getDeUsuario()))
                .toList();
        List<SolicitacaoItem> enviadas = repository.findByDeUsuarioIdOrderByCriadaEmDesc(usuarioId).stream()
                .map(solicitacao -> item(solicitacao, solicitacao.getParaUsuario()))
                .toList();
        return new SolicitacoesResponse(recebidas, enviadas);
    }

    private SolicitacaoItem item(Solicitacao solicitacao, Usuario outro) {
        return new SolicitacaoItem(solicitacao.getId(), solicitacao.getStatus().name(),
                solicitacao.getCriadaEm(), outro.getId(), outro.getNome());
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
    }
}
