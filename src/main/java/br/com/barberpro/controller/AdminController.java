package br.com.barberpro.controller;

import br.com.barberpro.enums.DayOfWeek;
import br.com.barberpro.service.BarberService;
import br.com.barberpro.controller.form.BarberForm;
import br.com.barberpro.controller.form.DailyScheduleForm;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {
  private final BarberService barberService;

  public AdminController(BarberService service) {
    barberService = service;
  }

  @GetMapping({"", "/login"})
  public String login() {
    return "admin/login";
  }

  @GetMapping("/barbeiros")
  public String barbers(Model model, @RequestParam(required = false) String criado) {
    model.addAttribute("barbers", barberService.findAll());
    if (criado != null) model.addAttribute("success", "Barbeiro cadastrado com sucesso.");
    return "admin/barbers";
  }

  @GetMapping("/barbeiros/novo")
  public String newBarber(Model model) {
    model.addAttribute("barberForm", newForm());
    model.addAttribute("services", barberService.availableServices());
    return "admin/barber-form";
  }

  @PostMapping("/barbeiros")
  public String createBarber(
      @Valid @ModelAttribute("barberForm") BarberForm form,
      BindingResult result,
      @RequestParam(name = "photo", required = false) MultipartFile photo,
      Model model,
      RedirectAttributes redirect) {
    barberService.validate(form).forEach(message -> result.reject("barber.invalid", message));
    if (!result.hasErrors() && photo != null && !photo.isEmpty()) {
      if (photo.getSize() > 2 * 1024 * 1024)
        result.reject("photo.invalid", "A foto deve ter no máximo 2 MB.");
      else if (!List.of("image/jpeg", "image/png", "image/webp").contains(photo.getContentType()))
        result.reject("photo.invalid", "Envie uma imagem JPEG, PNG ou WEBP.");
    }
    if (result.hasErrors()) {
      model.addAttribute("services", barberService.availableServices());
      return "admin/barber-form";
    }
    try {
      barberService.create(form, photo);
    } catch (IOException | IllegalArgumentException ex) {
      result.reject(
          "photo.invalid",
          ex.getMessage() == null ? "Não foi possível salvar a foto." : ex.getMessage());
      model.addAttribute("services", barberService.availableServices());
      return "admin/barber-form";
    }
    redirect.addAttribute("criado", "true");
    return "redirect:/admin/barbeiros";
  }

  private BarberForm newForm() {
    BarberForm form = new BarberForm();
    List<DailyScheduleForm> days = new ArrayList<>();
    for (DayOfWeek day : DayOfWeek.values()) {
      boolean works = day != DayOfWeek.SUNDAY;
      LocalTime end = day == DayOfWeek.SATURDAY ? LocalTime.of(14, 0) : LocalTime.of(18, 0);
      days.add(
          new DailyScheduleForm(day, works, works ? LocalTime.of(8, 0) : null, works ? end : null));
    }
    form.setSchedules(days);
    return form;
  }
}
