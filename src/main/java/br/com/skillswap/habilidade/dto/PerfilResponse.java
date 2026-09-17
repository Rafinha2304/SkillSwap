package br.com.skillswap.habilidade.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PerfilResponse(
        Long id,
        String nome,
        String email,
        String biografia,
        LocalDateTime criadoEm,
        List<HabilidadeItem> oferece,
        List<HabilidadeItem> desejaAprender) {
}
