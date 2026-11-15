package br.com.barberpro.database.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "barbeiro")
public class Barbeiro {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String nome;

  @Column(length = 255)
  private String foto;

  @Column(unique = true, length = 50)
  private String nomeUsuario;

  @Column(length = 100)
  private String senhaHash;

  @Column(nullable = false)
  private int tentativasLoginFalhas;

  private LocalDateTime bloqueadoAte;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "barbeiro_servico",
      joinColumns = @JoinColumn(name = "barbeiro_id"),
      inverseJoinColumns = @JoinColumn(name = "servico_id"))
  private Set<Servico> servicos = new HashSet<>();

  @OneToMany(mappedBy = "barbeiro", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<HorarioTrabalho> horariosTrabalho = new HashSet<>();

  protected Barbeiro() {}

  public Barbeiro(String nome, String foto) {
    this.nome = nome;
    this.foto = foto;
  }

  public void addServico(Servico servico) {
    servicos.add(servico);
  }

  public void addHorarioTrabalho(HorarioTrabalho horario) {
    horariosTrabalho.add(horario);
    horario.setBarbeiro(this);
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getFoto() {
    return foto;
  }

  public String getNomeUsuario() {
    return nomeUsuario;
  }

  public String getSenhaHash() {
    return senhaHash;
  }

  public int getTentativasLoginFalhas() {
    return tentativasLoginFalhas;
  }

  public LocalDateTime getBloqueadoAte() {
    return bloqueadoAte;
  }

  public Set<Servico> getServicos() {
    return servicos;
  }

  public Set<HorarioTrabalho> getHorariosTrabalho() {
    return horariosTrabalho;
  }
}
