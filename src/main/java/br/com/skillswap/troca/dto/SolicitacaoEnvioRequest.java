package br.com.skillswap.troca.dto;

public record SolicitacaoEnvioRequest(
        Long deUsuarioId,
        Long paraUsuarioId) {
}
