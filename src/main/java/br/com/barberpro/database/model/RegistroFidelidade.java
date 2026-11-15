package br.com.barberpro.database.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Locale;

/** Fidelidade rastreada pelo e-mail informado no agendamento (RF20). */
@Entity
@Table(name = "registro_fidelidade")
public class RegistroFidelidade {

  public static final int ATENDIMENTOS_POR_RECOMPENSA = 10;

  @Id
  @Column(length = 254)
  private String email;

  @Column(nullable = false)
  private int quantidadeAtendimentos;

  private LocalDateTime ultimaAtualizacao;

  protected RegistroFidelidade() {}

  public RegistroFidelidade(String email) {
    this.email = email.trim().toLowerCase(Locale.ROOT);
  }

  public void incrementar() {
    quantidadeAtendimentos++;
    ultimaAtualizacao = LocalDateTime.now();
  }

  public boolean elegivelParaRecompensa() {
    return quantidadeAtendimentos > 0
        && quantidadeAtendimentos % ATENDIMENTOS_POR_RECOMPENSA == 0;
  }

  public String getEmail() {
    return email;
  }

  public int getQuantidadeAtendimentos() {
    return quantidadeAtendimentos;
  }

  public LocalDateTime getUltimaAtualizacao() {
    return ultimaAtualizacao;
  }
}
