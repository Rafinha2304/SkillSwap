package br.com.skillswap.usuario.dto;

public record LoginRequest(
        String email,
        String senha) {
}
