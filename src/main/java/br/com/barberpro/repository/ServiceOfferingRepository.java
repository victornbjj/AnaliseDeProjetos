package br.com.barberpro.repository;

import br.com.barberpro.domain.ServiceOffering;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {
  List<ServiceOffering> findAllByActiveTrueOrderByIdAsc();
}
