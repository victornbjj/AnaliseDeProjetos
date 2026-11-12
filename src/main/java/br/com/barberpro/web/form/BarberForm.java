package br.com.barberpro.web.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BarberForm {
  @NotBlank(message = "Informe o nome do profissional.")
  @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
  private String name;

  private List<Long> serviceIds = new ArrayList<>();
  @Valid private List<DailyScheduleForm> schedules = new ArrayList<>();
  private LocalTime lunchStart = LocalTime.of(12, 0);
  private LocalTime lunchEnd = LocalTime.of(13, 0);

  public String getName() {
    return name;
  }

  public void setName(String v) {
    name = v;
  }

  public List<Long> getServiceIds() {
    return serviceIds;
  }

  public void setServiceIds(List<Long> v) {
    serviceIds = v == null ? new ArrayList<>() : v;
  }

  public List<DailyScheduleForm> getSchedules() {
    return schedules;
  }

  public void setSchedules(List<DailyScheduleForm> v) {
    schedules = v == null ? new ArrayList<>() : v;
  }

  public LocalTime getLunchStart() {
    return lunchStart;
  }

  public void setLunchStart(LocalTime v) {
    lunchStart = v;
  }

  public LocalTime getLunchEnd() {
    return lunchEnd;
  }

  public void setLunchEnd(LocalTime v) {
    lunchEnd = v;
  }
}
