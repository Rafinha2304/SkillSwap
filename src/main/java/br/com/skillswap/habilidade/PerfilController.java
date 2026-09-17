package br.com.skillswap.habilidade;

import br.com.skillswap.habilidade.dto.EstudanteResponse;
import br.com.skillswap.habilidade.dto.HabilidadeCatalogoResponse;
import br.com.skillswap.habilidade.dto.PerfilResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
