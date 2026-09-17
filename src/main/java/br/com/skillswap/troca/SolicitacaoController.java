package br.com.skillswap.troca;

import br.com.skillswap.troca.dto.SolicitacaoEnvioRequest;
import br.com.skillswap.troca.dto.SolicitacaoItem;
import br.com.skillswap.troca.dto.SolicitacaoRespostaRequest;
import br.com.skillswap.troca.dto.SolicitacoesResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SolicitacaoController {

    private final SolicitacaoService service;

    public SolicitacaoController(SolicitacaoService service) {
        this.service = service;
    }

    @PostMapping("/solicitacoes")
    public ResponseEntity<SolicitacaoItem> enviar(@RequestBody SolicitacaoEnvioRequest request) {
        SolicitacaoItem item = service.enviar(request.deUsuarioId(), request.paraUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/solicitacoes/{id}")
    public SolicitacaoItem responder(@PathVariable Long id, @RequestBody SolicitacaoRespostaRequest request) {
        return service.responder(id, request.usuarioId(), request.status());
    }

    @GetMapping("/usuarios/{id}/solicitacoes")
    public SolicitacoesResponse listar(@PathVariable Long id) {
        return service.listar(id);
    }
}
