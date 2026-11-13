package br.com.barberpro.service;

import br.com.barberpro.enums.DayOfWeek;
import br.com.barberpro.database.repository.BarberRepository;
import br.com.barberpro.database.repository.ServiceOfferingRepository;
import br.com.barberpro.database.model.Barber;
import br.com.barberpro.database.model.ServiceOffering;
import br.com.barberpro.database.model.WeeklySchedule;
import br.com.barberpro.controller.form.BarberForm;
import br.com.barberpro.controller.form.DailyScheduleForm;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BarberService {
  private final BarberRepository barberRepository;
  private final ServiceOfferingRepository serviceRepository;
  private final PhotoStorageService photoStorage;

  public BarberService(BarberRepository b, ServiceOfferingRepository s, PhotoStorageService p) {
    barberRepository = b;
    serviceRepository = s;
    photoStorage = p;
  }

  @Transactional(readOnly = true)
  public List<ServiceOffering> availableServices() {
    return serviceRepository.findAllByActiveTrueOrderByIdAsc();
  }

  @Transactional(readOnly = true)
  public List<Barber> findAll() {
    return barberRepository.findAll();
  }

  public List<String> validate(BarberForm form) {
    List<String> errors = new ArrayList<>();
    List<Long> ids = form.getServiceIds();
    if (ids == null || ids.isEmpty())
      errors.add("Selecione ao menos um serviço que o profissional realiza.");
    else if (new HashSet<>(ids).size() != ids.size()
        || serviceRepository.findAllById(ids).size() != ids.size()
        || serviceRepository.findAllByActiveTrueOrderByIdAsc().stream()
                .filter(s -> ids.contains(s.getId()))
                .count()
            != ids.size())
      errors.add("A seleção de serviços é inválida. Atualize a página e tente novamente.");
    List<DailyScheduleForm> days = form.getSchedules();
    if (days == null || days.size() != DayOfWeek.values().length) {
      errors.add("Informe os horários para os sete dias da semana.");
      return errors;
    }
    Set<DayOfWeek> configured = EnumSet.noneOf(DayOfWeek.class);
    for (DailyScheduleForm day : days) {
      if (day.getDayOfWeek() == null || !configured.add(day.getDayOfWeek())) {
        errors.add("A configuração dos dias da semana é inválida.");
        break;
      }
      if (day.isWorking()
          && (day.getStartTime() == null
              || day.getEndTime() == null
              || !day.getStartTime().isBefore(day.getEndTime()))) {
        errors.add("Para cada dia trabalhado, o início deve ser anterior ao fim.");
        break;
      }
      if (day.isWorking()
          && day.getStartTime() != null
          && day.getEndTime() != null
          && (day.getStartTime().toSecondOfDay() % 900 != 0
              || day.getEndTime().toSecondOfDay() % 900 != 0)) {
        errors.add("O expediente deve começar e terminar em intervalos de 15 minutos.");
        break;
      }
      if (day.isWorking() && (form.getLunchStart() == null) != (form.getLunchEnd() == null)) {
        errors.add("Informe início e fim do almoço, ou deixe ambos vazios.");
        break;
      }
      if (day.isWorking()
          && form.getLunchStart() != null
          && (!form.getLunchStart().isBefore(form.getLunchEnd())
              || form.getLunchStart().isBefore(day.getStartTime())
              || form.getLunchEnd().isAfter(day.getEndTime()))) {
        errors.add("O intervalo de almoço deve estar dentro do expediente e ter duração válida.");
        break;
      }
      if (day.isWorking()
          && form.getLunchStart() != null
          && form.getLunchEnd() != null
          && (form.getLunchStart().toSecondOfDay() % 900 != 0
              || form.getLunchEnd().toSecondOfDay() % 900 != 0)) {
        errors.add("O almoço deve usar intervalos de 15 minutos.");
        break;
      }
    }
    if (configured.size() != DayOfWeek.values().length && errors.isEmpty())
      errors.add("Informe cada dia da semana uma única vez.");
    return errors;
  }

  @Transactional
  public Barber create(BarberForm form, MultipartFile photo) throws IOException {
    String path = photoStorage.store(photo);
    Barber barber =
        new Barber(form.getName().trim(), path, form.getLunchStart(), form.getLunchEnd());
    for (ServiceOffering service : serviceRepository.findAllById(form.getServiceIds()))
      barber.addService(service);
    for (DailyScheduleForm day : form.getSchedules()) {
      LocalTime start = day.isWorking() ? day.getStartTime() : null;
      LocalTime end = day.isWorking() ? day.getEndTime() : null;
      barber.addSchedule(
          new WeeklySchedule(
              java.time.DayOfWeek.valueOf(day.getDayOfWeek().name()),
              day.isWorking(),
              start,
              end));
    }
    return barberRepository.save(barber);
  }
}
