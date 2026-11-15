package br.com.barberpro.database.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.barberpro.enums.StatusAgendamento;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class AgendamentoTest {
  private static Agendamento agendamentoConfirmadoEm(LocalDateTime dataHora) {
    Agendamento agendamento =
        new Agendamento(null, null, null, null, dataHora, "(83) 99999-0000");
    agendamento.confirmar();
    return agendamento;
  }

  @Test
  void geraTokenAcessoAleatorioComPeloMenos32Caracteres() {
    Agendamento a = agendamentoConfirmadoEm(LocalDateTime.now().plusDays(1));
    Agendamento b = agendamentoConfirmadoEm(LocalDateTime.now().plusDays(1));
    assertTrue(a.getTokenAcesso().length() >= 32);
    assertFalse(a.getTokenAcesso().equals(b.getTokenAcesso()));
  }

  @Test
  void cancelamentoComMaisDeDuasHorasDeAntecedenciaNaoETardio() {
    Agendamento agendamento = agendamentoConfirmadoEm(LocalDateTime.now().plusHours(3));
    assertTrue(agendamento.estaDentroPrazoCancelamento());
    agendamento.cancelar();
    assertEquals(StatusAgendamento.CANCELADO, agendamento.getStatus());
  }

  @Test
  void cancelamentoComMenosDeDuasHorasEhTardio() {
    Agendamento agendamento = agendamentoConfirmadoEm(LocalDateTime.now().plusHours(1));
    assertFalse(agendamento.estaDentroPrazoCancelamento());
    agendamento.cancelar();
    assertEquals(StatusAgendamento.CANCELADO_TARDIO, agendamento.getStatus());
  }

  @Test
  void remarcacaoRespeitaPrazoDeDuasHoras() {
    LocalDateTime novoHorario = LocalDateTime.now().plusDays(2);
    Agendamento dentroDoPrazo = agendamentoConfirmadoEm(LocalDateTime.now().plusHours(5));
    dentroDoPrazo.remarcar(novoHorario);
    assertEquals(novoHorario, dentroDoPrazo.getDataHora());

    Agendamento foraDoPrazo = agendamentoConfirmadoEm(LocalDateTime.now().plusMinutes(30));
    assertThrows(IllegalStateException.class, () -> foraDoPrazo.remarcar(novoHorario));
  }

  @Test
  void concluirERegistrarFaltaSoValemParaAgendamentoConfirmado() {
    Agendamento concluido = agendamentoConfirmadoEm(LocalDateTime.now().minusHours(1));
    concluido.concluir();
    assertEquals(StatusAgendamento.CONCLUIDO, concluido.getStatus());
    assertThrows(IllegalStateException.class, concluido::registrarFalta);
    assertThrows(IllegalStateException.class, concluido::confirmar);

    Agendamento falta = agendamentoConfirmadoEm(LocalDateTime.now().minusHours(1));
    falta.registrarFalta();
    assertEquals(StatusAgendamento.FALTA, falta.getStatus());
  }
}
