package br.com.barberpro.database.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RegistroFidelidadeTest {
  @Test
  void normalizaEmailUsadoComoIdentificador() {
    assertEquals("cliente@email.com", new RegistroFidelidade("  Cliente@Email.com ").getEmail());
  }

  @Test
  void ficaElegivelACadaDezAtendimentos() {
    RegistroFidelidade registro = new RegistroFidelidade("cliente@email.com");
    assertFalse(registro.elegivelParaRecompensa());
    for (int i = 0; i < 9; i++) registro.incrementar();
    assertFalse(registro.elegivelParaRecompensa());
    registro.incrementar();
    assertEquals(10, registro.getQuantidadeAtendimentos());
    assertNotNull(registro.getUltimaAtualizacao());
    assertTrue(registro.elegivelParaRecompensa());
    registro.incrementar();
    assertFalse(registro.elegivelParaRecompensa());
  }
}
