package br.com.barberpro.database.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacao")
public class Avaliacao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "agendamento_id", nullable = false, unique = true)
  private Agendamento agendamento;

  @Min(1)
  @Max(5)
  @Column(nullable = false)
  private int nota;

  @Size(max = 500)
  @Column(length = 500)
  private String comentario;

  @Column(nullable = false)
  private LocalDateTime dataAvaliacao;

  protected Avaliacao() {}

  public Avaliacao(Agendamento agendamento, int nota, String comentario) {
    this.agendamento = agendamento;
    this.nota = nota;
    this.comentario = comentario;
    this.dataAvaliacao = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public Agendamento getAgendamento() {
    return agendamento;
  }

  public int getNota() {
    return nota;
  }

  public String getComentario() {
    return comentario;
  }

  public LocalDateTime getDataAvaliacao() {
    return dataAvaliacao;
  }
}
