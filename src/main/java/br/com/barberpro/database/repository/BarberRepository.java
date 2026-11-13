package br.com.barberpro.database.repository;

import br.com.barberpro.database.model.Barber;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarberRepository extends JpaRepository<Barber, Long> {
  @Override
  @EntityGraph(attributePaths = {"services", "schedules"})
  List<Barber> findAll();
}
