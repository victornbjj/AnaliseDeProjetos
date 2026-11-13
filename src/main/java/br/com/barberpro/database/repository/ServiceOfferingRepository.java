package br.com.barberpro.database.repository;
import java.util.List;

import br.com.barberpro.database.model.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.barberpro.database.repository.ServiceOfferingRepository;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {
  List<ServiceOffering> findAllByActiveTrueOrderByIdAsc();
}
