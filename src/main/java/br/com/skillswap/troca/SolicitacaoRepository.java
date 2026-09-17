package br.com.skillswap.troca;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    List<Solicitacao> findByParaUsuarioIdOrderByCriadaEmDesc(Long paraUsuarioId);

    List<Solicitacao> findByDeUsuarioIdOrderByCriadaEmDesc(Long deUsuarioId);

    boolean existsByDeUsuarioIdAndParaUsuarioIdAndStatus(Long deUsuarioId, Long paraUsuarioId, StatusSolicitacao status);
}
