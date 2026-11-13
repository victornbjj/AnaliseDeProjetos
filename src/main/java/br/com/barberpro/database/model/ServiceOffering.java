package br.com.barberpro.database.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "service_offerings")
public class ServiceOffering {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 60)
  private String name;

  @Column(nullable = false)
  private int durationMinutes;

  @Column(nullable = false, precision = 8, scale = 2)
  private BigDecimal price;

  @Column(nullable = false)
  private boolean active = true;

  protected ServiceOffering() {}

  public ServiceOffering(String name, int durationMinutes, BigDecimal price) {
    this.name = name;
    this.durationMinutes = durationMinutes;
    this.price = price;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getDurationMinutes() {
    return durationMinutes;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public boolean isActive() {
    return active;
  }
}
