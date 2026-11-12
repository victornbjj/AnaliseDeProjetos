package br.com.barberpro.repository;

import br.com.barberpro.domain.Barber;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarberRepository extends JpaRepository<Barber, Long> {
  @Override
  @EntityGraph(attributePaths = {"services", "schedules"})
  List<Barber> findAll();
}
