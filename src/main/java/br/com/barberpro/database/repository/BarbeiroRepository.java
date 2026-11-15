package br.com.barberpro.database.repository;

import br.com.barberpro.database.model.Barbeiro;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {
  @Override
  @EntityGraph(attributePaths = {"servicos", "horariosTrabalho"})
  List<Barbeiro> findAll();
}
