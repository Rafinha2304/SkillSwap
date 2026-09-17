package br.com.skillswap.troca.dto;

import java.util.List;

public record SolicitacoesResponse(
        List<SolicitacaoItem> recebidas,
        List<SolicitacaoItem> enviadas) {
}
