package br.com.barberpro.service;

import br.com.barberpro.enums.DayOfWeek;
import br.com.barberpro.database.repository.BarbeiroRepository;
import br.com.barberpro.database.repository.ServicoRepository;
import br.com.barberpro.database.model.Barbeiro;
import br.com.barberpro.database.model.HorarioTrabalho;
import br.com.barberpro.database.model.Servico;
import br.com.barberpro.controller.form.BarberForm;
import br.com.barberpro.controller.form.DailyScheduleForm;
import java.io.IOException;
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
  private final BarbeiroRepository barbeiroRepository;
  private final ServicoRepository servicoRepository;
  private final PhotoStorageService photoStorage;

  public BarberService(BarbeiroRepository b, ServicoRepository s, PhotoStorageService p) {
    barbeiroRepository = b;
    servicoRepository = s;
    photoStorage = p;
  }

  @Transactional(readOnly = true)
  public List<Servico> availableServices() {
    return servicoRepository.findAllByAtivoTrueOrderByIdAsc();
  }

  @Transactional(readOnly = true)
  public List<Barbeiro> findAll() {
    return barbeiroRepository.findAll();
  }

  public List<String> validate(BarberForm form) {
    List<String> errors = new ArrayList<>();
    List<Long> ids = form.getServiceIds();
    if (ids == null || ids.isEmpty())
      errors.add("Selecione ao menos um serviço que o profissional realiza.");
    else if (new HashSet<>(ids).size() != ids.size()
        || servicoRepository.findAllById(ids).size() != ids.size()
        || servicoRepository.findAllByAtivoTrueOrderByIdAsc().stream()
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
  public Barbeiro create(BarberForm form, MultipartFile photo) throws IOException {
    String foto = photoStorage.store(photo);
    Barbeiro barbeiro = new Barbeiro(form.getName().trim(), foto);
    for (Servico servico : servicoRepository.findAllById(form.getServiceIds()))
      barbeiro.addServico(servico);
    for (DailyScheduleForm day : form.getSchedules()) {
      if (!day.isWorking()) continue;
      barbeiro.addHorarioTrabalho(
          new HorarioTrabalho(
              java.time.DayOfWeek.valueOf(day.getDayOfWeek().name()).getValue(),
              day.getStartTime(),
              day.getEndTime(),
              form.getLunchStart(),
              form.getLunchEnd()));
    }
    return barbeiroRepository.save(barbeiro);
  }
}
