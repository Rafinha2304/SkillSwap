package br.com.skillswap.habilidade.dto;

import java.util.List;

public record EstudanteResponse(
        Long id,
        String nome,
        String biografia,
        List<String> oferece,
        List<String> desejaAprender) {
}
