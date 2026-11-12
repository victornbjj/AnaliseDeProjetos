package br.com.barberpro.service;

import br.com.barberpro.domain.ServiceOffering;
import br.com.barberpro.repository.ServiceOfferingRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogInitializer {
  @Bean
  CommandLineRunner initializeServiceCatalog(ServiceOfferingRepository repository) {
    return args -> {
      if (repository.count() == 0) {
        repository.save(new ServiceOffering("Corte de cabelo", 45, new BigDecimal("45.00")));
        repository.save(new ServiceOffering("Barba", 30, new BigDecimal("30.00")));
        repository.save(new ServiceOffering("Corte + barba", 60, new BigDecimal("70.00")));
        repository.save(new ServiceOffering("Acabamento", 15, new BigDecimal("15.00")));
      }
    };
  }
}
