package br.com.skillswap.habilidade;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabilidadeRepository extends JpaRepository<Habilidade, Long> {

    Optional<Habilidade> findByNomeIgnoreCaseAndCategoriaIgnoreCase(String nome, String categoria);
}
