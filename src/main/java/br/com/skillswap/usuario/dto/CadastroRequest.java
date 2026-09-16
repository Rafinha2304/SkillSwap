package br.com.skillswap.usuario.dto;

public record CadastroRequest(
        String nome,
        String email,
        String senha,
        String biografia) {
}
