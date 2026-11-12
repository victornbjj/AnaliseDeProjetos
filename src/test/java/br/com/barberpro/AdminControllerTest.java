package br.com.barberpro;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import br.com.barberpro.domain.Barber;
import br.com.barberpro.domain.DayOfWeek;
import br.com.barberpro.repository.BarberRepository;
import br.com.barberpro.repository.ServiceOfferingRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {
  @Autowired MockMvc mockMvc;
  @Autowired BarberRepository barberRepository;
  @Autowired ServiceOfferingRepository serviceRepository;

  @BeforeEach
  void clearBarbers() {
    barberRepository.deleteAll();
  }

  @Test
  void rendersLoginAndBarberForm() throws Exception {
    mockMvc
        .perform(get("/admin/login"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("ainda não está conectada")));
    mockMvc
        .perform(get("/admin/barbeiros/novo"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/barber-form"))
        .andExpect(content().string(containsString("Horários de trabalho")));
  }

  @Test
  void savesBarberServicesAndWeeklySchedule() throws Exception {
    Long serviceId = serviceRepository.findAllByActiveTrueOrderByIdAsc().get(0).getId();
    var request =
        multipart("/admin/barbeiros")
            .param("name", "João da Silva")
            .param("serviceIds", serviceId.toString())
            .param("lunchStart", "12:00")
            .param("lunchEnd", "13:00");
    List<DayOfWeek> days = List.of(DayOfWeek.values());
    for (int i = 0; i < days.size(); i++) {
      request
          .param("schedules[" + i + "].dayOfWeek", days.get(i).name())
          .param("schedules[" + i + "].working", Boolean.toString(days.get(i) != DayOfWeek.SUNDAY));
      if (days.get(i) != DayOfWeek.SUNDAY)
        request
            .param("schedules[" + i + "].startTime", "08:00")
            .param(
                "schedules[" + i + "].endTime",
                days.get(i) == DayOfWeek.SATURDAY ? "14:00" : "18:00");
    }
    mockMvc
        .perform(request)
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/barbeiros?criado=true"));
    Barber saved = barberRepository.findAll().get(0);
    org.junit.jupiter.api.Assertions.assertEquals("João da Silva", saved.getName());
    org.junit.jupiter.api.Assertions.assertEquals(1, saved.getServices().size());
    org.junit.jupiter.api.Assertions.assertEquals(7, saved.getSchedules().size());
    org.junit.jupiter.api.Assertions.assertEquals(12, saved.getLunchStart().getHour());
    mockMvc
        .perform(get("/admin/barbeiros?criado=true"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("João da Silva")));
  }

  @Test
  void rejectsInvalidHoursWithoutPersisting() throws Exception {
    Long serviceId = serviceRepository.findAllByActiveTrueOrderByIdAsc().get(0).getId();
    var request =
        multipart("/admin/barbeiros")
            .param("name", "Barbeiro válido")
            .param("serviceIds", serviceId.toString())
            .param("lunchStart", "12:00")
            .param("lunchEnd", "13:00");
    for (int i = 0; i < DayOfWeek.values().length; i++) {
      DayOfWeek day = DayOfWeek.values()[i];
      request
          .param("schedules[" + i + "].dayOfWeek", day.name())
          .param("schedules[" + i + "].working", Boolean.toString(day != DayOfWeek.SUNDAY));
      if (day != DayOfWeek.SUNDAY)
        request
            .param("schedules[" + i + "].startTime", "08:05")
            .param("schedules[" + i + "].endTime", "18:00");
    }
    mockMvc
        .perform(request)
        .andExpect(status().isOk())
        .andExpect(view().name("admin/barber-form"))
        .andExpect(content().string(containsString("intervalos de 15 minutos")));
    org.junit.jupiter.api.Assertions.assertTrue(barberRepository.findAll().isEmpty());
  }
}
