package br.com.barberpro.controller.form;

import br.com.barberpro.enums.DayOfWeek;
import java.time.LocalTime;

public class DailyScheduleForm {
  private DayOfWeek dayOfWeek;
  private boolean working;
  private LocalTime startTime;
  private LocalTime endTime;

  public DailyScheduleForm() {}

  public DailyScheduleForm(DayOfWeek day, boolean working, LocalTime start, LocalTime end) {
    this.dayOfWeek = day;
    this.working = working;
    this.startTime = start;
    this.endTime = end;
  }

  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(DayOfWeek v) {
    dayOfWeek = v;
  }

  public boolean isWorking() {
    return working;
  }

  public void setWorking(boolean v) {
    working = v;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime v) {
    startTime = v;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime v) {
    endTime = v;
  }

  public String getLabel() {
    return dayOfWeek == null ? "" : dayOfWeek.getLabel();
  }
}
