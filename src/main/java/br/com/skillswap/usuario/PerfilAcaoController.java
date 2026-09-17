package br.com.skillswap.usuario;

import br.com.skillswap.habilidade.PerfilService;
import br.com.skillswap.habilidade.dto.HabilidadeItem;
import br.com.skillswap.habilidade.dto.HabilidadeRequest;
import br.com.skillswap.habilidade.dto.PerfilResponse;
import br.com.skillswap.usuario.dto.PerfilUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class PerfilAcaoController {

    private final UsuarioService usuarioService;
    private final PerfilService perfilService;

    public PerfilAcaoController(UsuarioService usuarioService, PerfilService perfilService) {
        this.usuarioService = usuarioService;
        this.perfilService = perfilService;
    }

    @PutMapping("/usuarios/{id}")
    public PerfilResponse atualizarPerfil(@PathVariable Long id,
                                          @RequestHeader(value = "X-Auth-Token", required = false) String token,
                                          @RequestBody PerfilUpdateRequest request) {
        Usuario sessao = usuarioService.validarToken(token);
        exigirProprioPerfil(sessao, id);
        return perfilService.atualizarPerfil(id, request);
    }

    @PostMapping("/usuarios/{id}/habilidades")
    public HabilidadeItem adicionarHabilidade(@PathVariable Long id,
                                              @RequestHeader(value = "X-Auth-Token", required = false) String token,
                                              @RequestBody HabilidadeRequest request) {
        Usuario sessao = usuarioService.validarToken(token);
        exigirProprioPerfil(sessao, id);
        return perfilService.adicionarHabilidade(id, request);
    }

    @DeleteMapping("/usuarios/{id}/habilidades/{vinculoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerHabilidade(@PathVariable Long id,
                                  @PathVariable Long vinculoId,
                                  @RequestHeader(value = "X-Auth-Token", required = false) String token) {
        Usuario sessao = usuarioService.validarToken(token);
        exigirProprioPerfil(sessao, id);
        perfilService.removerHabilidade(id, vinculoId);
    }

    private void exigirProprioPerfil(Usuario sessao, Long id) {
        if (!sessao.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você só pode alterar o seu próprio perfil.");
        }
    }
}
