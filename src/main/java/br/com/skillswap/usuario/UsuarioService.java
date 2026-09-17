package br.com.skillswap.usuario;

import br.com.skillswap.usuario.dto.CadastroRequest;
import br.com.skillswap.usuario.dto.LoginRequest;
import br.com.skillswap.usuario.dto.PerfilUpdateRequest;
import br.com.skillswap.usuario.dto.UsuarioResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse cadastrar(CadastroRequest request) {
        if (request.email() == null || request.email().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um e-mail.");
        }
        if (request.senha() == null || request.senha().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha deve ter no mínimo 6 caracteres.");
        }
        String email = request.email().trim().toLowerCase();
        if (repository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este e-mail já está cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setBiografia(request.biografia());
        usuario.setCriadoEm(LocalDateTime.now());
        return UsuarioResponse.from(repository.save(usuario));
    }

    public UsuarioResponse login(LoginRequest request) {
        String email = request.email() == null ? "" : request.email().trim().toLowerCase();
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos."));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos.");
        }
        return UsuarioResponse.from(usuario);
    }
}
