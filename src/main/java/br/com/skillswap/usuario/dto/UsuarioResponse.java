package br.com.skillswap.usuario.dto;

import br.com.skillswap.usuario.Usuario;
import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String biografia,
        LocalDateTime criadoEm,
        String token) {

    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getBiografia(),
                usuario.getCriadoEm(),
                usuario.getToken());
    }
}
