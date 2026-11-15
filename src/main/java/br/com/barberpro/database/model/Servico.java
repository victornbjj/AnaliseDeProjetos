package br.com.barberpro.database.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servico")
public class Servico {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 60)
  private String nome;

  @Column(nullable = false)
  private int duracaoMinutos;

  @Column(nullable = false, precision = 8, scale = 2)
  private BigDecimal preco;

  @Column(nullable = false)
  private boolean ativo = true;

  protected Servico() {}

  public Servico(String nome, int duracaoMinutos, BigDecimal preco) {
    this.nome = nome;
    this.duracaoMinutos = duracaoMinutos;
    this.preco = preco;
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public int getDuracaoMinutos() {
    return duracaoMinutos;
  }

  public BigDecimal getPreco() {
    return preco;
  }

  public boolean isAtivo() {
    return ativo;
  }
}
