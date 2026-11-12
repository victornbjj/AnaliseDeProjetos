package br.com.barberpro.domain;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "barbers")
public class Barber {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(length = 255)
  private String photoPath;

  private LocalTime lunchStart;
  private LocalTime lunchEnd;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "barber_services",
      joinColumns = @JoinColumn(name = "barber_id"),
      inverseJoinColumns = @JoinColumn(name = "service_id"))
  private Set<ServiceOffering> services = new HashSet<>();

  @OneToMany(mappedBy = "barber", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<WeeklySchedule> schedules = new HashSet<>();

  protected Barber() {}

  public Barber(String name, String photoPath, LocalTime lunchStart, LocalTime lunchEnd) {
    this.name = name;
    this.photoPath = photoPath;
    this.lunchStart = lunchStart;
    this.lunchEnd = lunchEnd;
  }

  public void addService(ServiceOffering s) {
    services.add(s);
  }

  public void addSchedule(WeeklySchedule s) {
    schedules.add(s);
    s.setBarber(this);
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPhotoPath() {
    return photoPath;
  }

  public LocalTime getLunchStart() {
    return lunchStart;
  }

  public LocalTime getLunchEnd() {
    return lunchEnd;
  }

  public Set<ServiceOffering> getServices() {
    return services;
  }

  public Set<WeeklySchedule> getSchedules() {
    return schedules;
  }
}
