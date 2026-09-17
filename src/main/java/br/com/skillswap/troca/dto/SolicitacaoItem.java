package br.com.skillswap.troca.dto;

import java.time.LocalDateTime;

public record SolicitacaoItem(
        Long id,
        String status,
        LocalDateTime criadaEm,
        Long outroUsuarioId,
        String outroNome) {
}
