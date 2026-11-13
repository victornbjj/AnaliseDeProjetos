package br.com.barberpro.enums;

public enum DayOfWeek {
  MONDAY("Segunda-feira"),
  TUESDAY("Terça-feira"),
  WEDNESDAY("Quarta-feira"),
  THURSDAY("Quinta-feira"),
  FRIDAY("Sexta-feira"),
  SATURDAY("Sábado"),
  SUNDAY("Domingo");
  private final String label;

  DayOfWeek(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
