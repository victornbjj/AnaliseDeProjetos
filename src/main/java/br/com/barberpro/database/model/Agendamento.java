package br.com.barberpro.database.model;

import br.com.barberpro.enums.StatusAgendamento;
import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "agendamento")
public class Agendamento {

  public static final Duration PRAZO_CANCELAMENTO = Duration.ofHours(2);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime dataHora;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private StatusAgendamento status;

  @Column(nullable = false, length = 20)
  private String telefoneCliente;

  @Column(nullable = false, unique = true, length = 36)
  private String tokenAcesso;

  @Column(nullable = false, updatable = false)
  private LocalDateTime criadoEm;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "barbeiro_id", nullable = false)
  private Barbeiro barbeiro;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "servico_id", nullable = false)
  private Servico servico;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "registro_fidelidade_email", nullable = false)
  private RegistroFidelidade registroFidelidade;

  protected Agendamento() {}

  public Agendamento(
      Barbeiro barbeiro,
      Servico servico,
      Cliente cliente,
      RegistroFidelidade registroFidelidade,
      LocalDateTime dataHora,
      String telefoneCliente) {
    this.barbeiro = barbeiro;
    this.servico = servico;
    this.cliente = cliente;
    this.registroFidelidade = registroFidelidade;
    this.dataHora = dataHora;
    this.telefoneCliente = telefoneCliente;
    this.tokenAcesso = UUID.randomUUID().toString();
  }

  @PrePersist
  void registrarCriacao() {
    criadoEm = LocalDateTime.now();
  }

  public void confirmar() {
    if (status != null && status != StatusAgendamento.CONFIRMADO)
      throw new IllegalStateException("Agendamento já encerrado não pode ser confirmado.");
    status = StatusAgendamento.CONFIRMADO;
  }

  public void cancelar() {
    exigirConfirmado();
    status =
        estaDentroPrazoCancelamento()
            ? StatusAgendamento.CANCELADO
            : StatusAgendamento.CANCELADO_TARDIO;
  }

  public void remarcar(LocalDateTime novoHorario) {
    exigirConfirmado();
    if (!estaDentroPrazoCancelamento())
      throw new IllegalStateException("A remarcação só é permitida até 2 horas antes do horário.");
    if (novoHorario == null || !novoHorario.isAfter(LocalDateTime.now()))
      throw new IllegalArgumentException("O novo horário deve estar no futuro.");
    dataHora = novoHorario;
  }

  public void concluir() {
    exigirConfirmado();
    status = StatusAgendamento.CONCLUIDO;
  }

  public void registrarFalta() {
    exigirConfirmado();
    status = StatusAgendamento.FALTA;
  }

  public boolean estaDentroPrazoCancelamento() {
    return LocalDateTime.now().isBefore(dataHora.minus(PRAZO_CANCELAMENTO));
  }

  private void exigirConfirmado() {
    if (status != StatusAgendamento.CONFIRMADO)
      throw new IllegalStateException("Operação permitida apenas para agendamentos confirmados.");
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getDataHora() {
    return dataHora;
  }

  public StatusAgendamento getStatus() {
    return status;
  }

  public String getTelefoneCliente() {
    return telefoneCliente;
  }

  public String getTokenAcesso() {
    return tokenAcesso;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public Barbeiro getBarbeiro() {
    return barbeiro;
  }

  public Servico getServico() {
    return servico;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public RegistroFidelidade getRegistroFidelidade() {
    return registroFidelidade;
  }
}
