package br.com.skillswap.troca;

import br.com.skillswap.troca.dto.SolicitacaoEnvioRequest;
import br.com.skillswap.troca.dto.SolicitacaoItem;
import br.com.skillswap.troca.dto.SolicitacaoRespostaRequest;
import br.com.skillswap.troca.dto.SolicitacoesResponse;
import br.com.skillswap.usuario.Usuario;
import br.com.skillswap.usuario.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class SolicitacaoController {

    private final SolicitacaoService service;
    private final UsuarioService usuarioService;

    public SolicitacaoController(SolicitacaoService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/solicitacoes")
    public ResponseEntity<SolicitacaoItem> enviar(
            @RequestHeader(value = "X-Auth-Token", required = false) String token,
            @RequestBody SolicitacaoEnvioRequest request) {
        Usuario sessao = usuarioService.validarToken(token);
        SolicitacaoItem item = service.enviar(sessao, request.paraUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/solicitacoes/{id}")
    public SolicitacaoItem responder(@PathVariable Long id,
                                     @RequestHeader(value = "X-Auth-Token", required = false) String token,
                                     @RequestBody SolicitacaoRespostaRequest request) {
        Usuario sessao = usuarioService.validarToken(token);
        return service.responder(id, sessao, request.status());
    }

    @GetMapping("/usuarios/{id}/solicitacoes")
    public SolicitacoesResponse listar(@PathVariable Long id,
                                       @RequestHeader(value = "X-Auth-Token", required = false) String token) {
        Usuario sessao = usuarioService.validarToken(token);
        if (!sessao.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você só pode ver as suas próprias trocas.");
        }
        return service.listar(id);
    }
}
