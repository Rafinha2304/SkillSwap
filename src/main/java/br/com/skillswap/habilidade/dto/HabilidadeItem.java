package br.com.skillswap.habilidade.dto;

public record HabilidadeItem(
        Long vinculoId,
        Long habilidadeId,
        String nome,
        String categoria) {
}
