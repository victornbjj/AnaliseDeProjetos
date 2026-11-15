package br.com.barberpro.database.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cliente")
public class Cliente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String nome;

  private LocalDate dataNascimento;

  @Column(length = 14)
  private String cpf;

  @Column(nullable = false, length = 254)
  private String email;

  protected Cliente() {}

  public Cliente(String nome, LocalDate dataNascimento, String cpf, String email) {
    this.nome = nome;
    this.dataNascimento = dataNascimento;
    this.cpf = cpf;
    this.email = email;
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public String getCpf() {
    return cpf;
  }

  public String getEmail() {
    return email;
  }
}
