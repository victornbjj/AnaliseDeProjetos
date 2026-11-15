package br.com.barberpro.database.repository;

import br.com.barberpro.database.model.Servico;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
  List<Servico> findAllByAtivoTrueOrderByIdAsc();
}
