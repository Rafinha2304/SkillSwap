package br.com.skillswap.habilidade;

import br.com.skillswap.habilidade.dto.EstudanteResponse;
import br.com.skillswap.habilidade.dto.HabilidadeCatalogoResponse;
import br.com.skillswap.habilidade.dto.HabilidadeItem;
import br.com.skillswap.habilidade.dto.HabilidadeRequest;
import br.com.skillswap.habilidade.dto.PerfilResponse;
import br.com.skillswap.usuario.Usuario;
import br.com.skillswap.usuario.UsuarioRepository;
import br.com.skillswap.usuario.dto.PerfilUpdateRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PerfilService {

    private final UsuarioRepository usuarioRepository;
    private final HabilidadeRepository habilidadeRepository;
    private final UsuarioHabilidadeRepository vinculoRepository;

    public PerfilService(UsuarioRepository usuarioRepository,
                         HabilidadeRepository habilidadeRepository,
                         UsuarioHabilidadeRepository vinculoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.habilidadeRepository = habilidadeRepository;
        this.vinculoRepository = vinculoRepository;
    }

    @Transactional(readOnly = true)
    public PerfilResponse obterPerfil(Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        return montarPerfil(usuario);
    }

    @Transactional
    public PerfilResponse atualizarPerfil(Long usuarioId, PerfilUpdateRequest request) {
        Usuario usuario = buscarUsuario(usuarioId);
        if (request.nome() == null || request.nome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o seu nome.");
        }
        usuario.setNome(request.nome().trim());
        usuario.setBiografia(request.biografia());
        usuarioRepository.save(usuario);
        return montarPerfil(usuario);
    }

    @Transactional
    public HabilidadeItem adicionarHabilidade(Long usuarioId, HabilidadeRequest request) {
        Usuario usuario = buscarUsuario(usuarioId);
        if (request.nome() == null || request.nome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o nome da habilidade.");
        }
        if (request.categoria() == null || request.categoria().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Escolha uma categoria para a habilidade.");
        }
        TipoVinculo tipo;
        try {
            tipo = TipoVinculo.valueOf(request.tipo());
        } catch (IllegalArgumentException | NullPointerException excecao) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de habilidade inválido.");
        }

        String nome = request.nome().trim();
        String categoria = request.categoria().trim();
        Habilidade habilidade = habilidadeRepository.findByNomeIgnoreCaseAndCategoriaIgnoreCase(nome, categoria)
                .orElseGet(() -> {
                    Habilidade nova = new Habilidade();
                    nova.setNome(nome);
                    nova.setCategoria(categoria);
                    return habilidadeRepository.save(nova);
                });

        if (vinculoRepository.existsByUsuarioIdAndHabilidadeIdAndTipo(usuarioId, habilidade.getId(), tipo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta habilidade já está registrada no seu perfil.");
        }

        UsuarioHabilidade vinculo = new UsuarioHabilidade();
        vinculo.setUsuario(usuario);
        vinculo.setHabilidade(habilidade);
        vinculo.setTipo(tipo);
        vinculo = vinculoRepository.save(vinculo);
        return new HabilidadeItem(vinculo.getId(), habilidade.getId(), habilidade.getNome(), habilidade.getCategoria());
    }

    @Transactional
    public void removerHabilidade(Long usuarioId, Long vinculoId) {
        UsuarioHabilidade vinculo = vinculoRepository.findByIdAndUsuarioId(vinculoId, usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habilidade não encontrada no seu perfil."));
        vinculoRepository.delete(vinculo);
    }

    @Transactional(readOnly = true)
    public List<HabilidadeCatalogoResponse> catalogoDeHabilidades() {
        return habilidadeRepository.findAll().stream()
                .sorted(Comparator.comparing(habilidade -> habilidade.getNome().toLowerCase()))
                .map(habilidade -> new HabilidadeCatalogoResponse(
                        habilidade.getId(), habilidade.getNome(), habilidade.getCategoria()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EstudanteResponse> buscarEstudantes(String busca, String categoria) {
        String textoBusca = busca == null ? "" : busca.trim();
        String filtroCategoria = categoria == null ? "" : categoria.trim();

        Map<Long, List<UsuarioHabilidade>> vinculosPorUsuario = new LinkedHashMap<>();
        for (UsuarioHabilidade vinculo : vinculoRepository.findAll()) {
            vinculosPorUsuario.computeIfAbsent(vinculo.getUsuario().getId(), chave -> new ArrayList<>()).add(vinculo);
        }

        List<EstudanteResponse> estudantes = new ArrayList<>();
        for (Usuario usuario : usuarioRepository.findAll()) {
            List<UsuarioHabilidade> vinculos = vinculosPorUsuario.getOrDefault(usuario.getId(), List.of());
            if (!correspondeBusca(vinculos, textoBusca, filtroCategoria)) {
                continue;
            }
            estudantes.add(new EstudanteResponse(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getBiografia(),
                    nomesPorTipo(vinculos, TipoVinculo.OFERECE),
                    nomesPorTipo(vinculos, TipoVinculo.DESEJA_APRENDER)));
        }
        return estudantes;
    }

    private boolean correspondeBusca(List<UsuarioHabilidade> vinculos, String textoBusca, String filtroCategoria) {
        if (textoBusca.isEmpty() && filtroCategoria.isEmpty()) {
            return true;
        }
        for (UsuarioHabilidade vinculo : vinculos) {
            boolean nomeCasa = textoBusca.isEmpty()
                    || vinculo.getHabilidade().getNome().toLowerCase().contains(textoBusca.toLowerCase());
            boolean categoriaCasa = filtroCategoria.isEmpty()
                    || vinculo.getHabilidade().getCategoria().equalsIgnoreCase(filtroCategoria);
            if (nomeCasa && categoriaCasa) {
                return true;
            }
        }
        return false;
    }

    private List<String> nomesPorTipo(List<UsuarioHabilidade> vinculos, TipoVinculo tipo) {
        return vinculos.stream()
                .filter(vinculo -> vinculo.getTipo() == tipo)
                .map(vinculo -> vinculo.getHabilidade().getNome())
                .sorted(Comparator.comparing(String::toLowerCase))
                .toList();
    }

    private PerfilResponse montarPerfil(Usuario usuario) {
        List<UsuarioHabilidade> vinculos = vinculoRepository.findByUsuarioId(usuario.getId());
        return new PerfilResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getBiografia(),
                usuario.getCriadoEm(),
                itensPorTipo(vinculos, TipoVinculo.OFERECE),
                itensPorTipo(vinculos, TipoVinculo.DESEJA_APRENDER));
    }

    private List<HabilidadeItem> itensPorTipo(List<UsuarioHabilidade> vinculos, TipoVinculo tipo) {
        return vinculos.stream()
                .filter(vinculo -> vinculo.getTipo() == tipo)
                .map(vinculo -> new HabilidadeItem(
                        vinculo.getId(),
                        vinculo.getHabilidade().getId(),
                        vinculo.getHabilidade().getNome(),
                        vinculo.getHabilidade().getCategoria()))
                .sorted(Comparator.comparing(item -> item.nome().toLowerCase()))
                .toList();
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
    }
}
