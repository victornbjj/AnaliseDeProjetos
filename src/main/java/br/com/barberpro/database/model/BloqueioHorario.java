package br.com.barberpro.database.model;

import br.com.barberpro.enums.TipoBloqueio;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bloqueio_horario")
public class BloqueioHorario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "barbeiro_id", nullable = false)
  private Barbeiro barbeiro;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TipoBloqueio tipo;

  @Column(nullable = false)
  private LocalDateTime dataHoraInicio;

  @Column(nullable = false)
  private LocalDateTime dataHoraFim;

  private Integer diaSemanaRecorrencia;

  protected BloqueioHorario() {}

  public BloqueioHorario(
      Barbeiro barbeiro,
      TipoBloqueio tipo,
      LocalDateTime dataHoraInicio,
      LocalDateTime dataHoraFim,
      Integer diaSemanaRecorrencia) {
    this.barbeiro = barbeiro;
    this.tipo = tipo;
    this.dataHoraInicio = dataHoraInicio;
    this.dataHoraFim = dataHoraFim;
    this.diaSemanaRecorrencia = diaSemanaRecorrencia;
  }

  public Long getId() {
    return id;
  }

  public Barbeiro getBarbeiro() {
    return barbeiro;
  }

  public TipoBloqueio getTipo() {
    return tipo;
  }

  public LocalDateTime getDataHoraInicio() {
    return dataHoraInicio;
  }

  public LocalDateTime getDataHoraFim() {
    return dataHoraFim;
  }

  public Integer getDiaSemanaRecorrencia() {
    return diaSemanaRecorrencia;
  }
}
