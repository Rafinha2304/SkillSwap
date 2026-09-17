package br.com.skillswap.habilidade;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioHabilidadeRepository extends JpaRepository<UsuarioHabilidade, Long> {

    List<UsuarioHabilidade> findByUsuarioId(Long usuarioId);

    Optional<UsuarioHabilidade> findByIdAndUsuarioId(Long id, Long usuarioId);

    boolean existsByUsuarioIdAndHabilidadeIdAndTipo(Long usuarioId, Long habilidadeId, TipoVinculo tipo);
}
