package br.com.skillswap.usuario;

import br.com.skillswap.usuario.dto.CadastroRequest;
import br.com.skillswap.usuario.dto.LoginRequest;
import br.com.skillswap.usuario.dto.UsuarioResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody CadastroRequest request) {
        UsuarioResponse response = service.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public UsuarioResponse login(@RequestBody LoginRequest request) {
        return service.login(request);
    }
}
