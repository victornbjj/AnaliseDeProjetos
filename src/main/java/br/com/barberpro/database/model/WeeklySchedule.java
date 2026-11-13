package br.com.barberpro.database.model;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(
    name = "weekly_schedules",
    uniqueConstraints = @UniqueConstraint(columnNames = {"barber_id", "day_of_week"}))
public class WeeklySchedule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "barber_id", nullable = false)
  private Barber barber;

  @Enumerated(EnumType.STRING)
  @Column(name = "day_of_week", nullable = false)
  private DayOfWeek dayOfWeek;

  @Column(nullable = false)
  private boolean working;

  private LocalTime startTime;
  private LocalTime endTime;

  protected WeeklySchedule() {}

  public WeeklySchedule(DayOfWeek day, boolean working, LocalTime start, LocalTime end) {
    this.dayOfWeek = day;
    this.working = working;
    this.startTime = start;
    this.endTime = end;
  }

  void setBarber(Barber barber) {
    this.barber = barber;
  }

  public Long getId() {
    return id;
  }

  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public boolean isWorking() {
    return working;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }
}
