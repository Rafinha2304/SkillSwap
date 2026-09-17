package br.com.skillswap.habilidade;

import br.com.skillswap.habilidade.dto.EstudanteResponse;
import br.com.skillswap.habilidade.dto.HabilidadeCatalogoResponse;
import br.com.skillswap.habilidade.dto.HabilidadeItem;
import br.com.skillswap.habilidade.dto.HabilidadeRequest;
import br.com.skillswap.habilidade.dto.PerfilResponse;
import br.com.skillswap.usuario.dto.PerfilUpdateRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PerfilController {

    private final PerfilService service;

    public PerfilController(PerfilService service) {
        this.service = service;
    }

    @GetMapping("/habilidades")
    public List<HabilidadeCatalogoResponse> catalogo() {
        return service.catalogoDeHabilidades();
    }

    @GetMapping("/estudantes")
    public List<EstudanteResponse> buscarEstudantes(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String categoria) {
        return service.buscarEstudantes(busca, categoria);
    }

    @GetMapping("/usuarios/{id}")
    public PerfilResponse perfil(@PathVariable Long id) {
        return service.obterPerfil(id);
    }

    @PutMapping("/usuarios/{id}")
    public PerfilResponse atualizarPerfil(@PathVariable Long id, @RequestBody PerfilUpdateRequest request) {
        return service.atualizarPerfil(id, request);
    }

    @PostMapping("/usuarios/{id}/habilidades")
    public ResponseEntity<HabilidadeItem> adicionarHabilidade(
            @PathVariable Long id,
            @RequestBody HabilidadeRequest request) {
        HabilidadeItem item = service.adicionarHabilidade(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @DeleteMapping("/usuarios/{id}/habilidades/{vinculoId}")
    public ResponseEntity<Void> removerHabilidade(@PathVariable Long id, @PathVariable Long vinculoId) {
        service.removerHabilidade(id, vinculoId);
        return ResponseEntity.noContent().build();
    }
}
