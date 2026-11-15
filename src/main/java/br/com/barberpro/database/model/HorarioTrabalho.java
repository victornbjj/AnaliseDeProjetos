package br.com.barberpro.database.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(
    name = "horario_trabalho",
    uniqueConstraints = @UniqueConstraint(columnNames = {"barbeiro_id", "dia_semana"}))
public class HorarioTrabalho {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "barbeiro_id", nullable = false)
  private Barbeiro barbeiro;

  @Column(nullable = false)
  private int diaSemana;

  @Column(nullable = false)
  private LocalTime horarioInicio;

  @Column(nullable = false)
  private LocalTime horaFim;

  private LocalTime inicioAlmoco;
  private LocalTime fimAlmoco;

  protected HorarioTrabalho() {}

  public HorarioTrabalho(
      int diaSemana,
      LocalTime horarioInicio,
      LocalTime horaFim,
      LocalTime inicioAlmoco,
      LocalTime fimAlmoco) {
    this.diaSemana = diaSemana;
    this.horarioInicio = horarioInicio;
    this.horaFim = horaFim;
    this.inicioAlmoco = inicioAlmoco;
    this.fimAlmoco = fimAlmoco;
  }

  void setBarbeiro(Barbeiro barbeiro) {
    this.barbeiro = barbeiro;
  }

  public Long getId() {
    return id;
  }

  public Barbeiro getBarbeiro() {
    return barbeiro;
  }

  public int getDiaSemana() {
    return diaSemana;
  }

  public LocalTime getHorarioInicio() {
    return horarioInicio;
  }

  public LocalTime getHoraFim() {
    return horaFim;
  }

  public LocalTime getInicioAlmoco() {
    return inicioAlmoco;
  }

  public LocalTime getFimAlmoco() {
    return fimAlmoco;
  }
}
