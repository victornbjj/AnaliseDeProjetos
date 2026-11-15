package br.com.barberpro.config;

import br.com.barberpro.database.model.Servico;
import br.com.barberpro.database.repository.ServicoRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogInitializer {
  @Bean
  CommandLineRunner initializeServiceCatalog(ServicoRepository repository) {
    return args -> {
      if (repository.count() == 0) {
        repository.save(new Servico("Corte de cabelo", 45, new BigDecimal("45.00")));
        repository.save(new Servico("Barba", 30, new BigDecimal("30.00")));
        repository.save(new Servico("Corte + barba", 60, new BigDecimal("70.00")));
        repository.save(new Servico("Acabamento", 15, new BigDecimal("15.00")));
      }
    };
  }
}
