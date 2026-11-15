package br.com.barberpro.web;

import br.com.barberpro.domain.ServiceOffering;
import br.com.barberpro.repository.BarberRepository;
import br.com.barberpro.repository.ServiceOfferingRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminPagesController {
  private final BarberRepository barbers;
  private final ServiceOfferingRepository services;

  public AdminPagesController(BarberRepository barbers, ServiceOfferingRepository services) {
    this.barbers = barbers;
    this.services = services;
  }

  @GetMapping
  public String overview(Model model) {
    model.addAttribute("barberCount", barbers.count());
    model.addAttribute("serviceCount", services.findAllByActiveTrueOrderByIdAsc().size());
    return "admin/overview";
  }

  @GetMapping({"/agenda", "/agendamentos", "/bloqueios", "/relatorios"})
  public String prototypePage(HttpServletRequest request, Model model) {
    String section = request.getRequestURI().substring(request.getRequestURI().lastIndexOf('/') + 1);
    model.addAttribute("section", section);
    model.addAttribute("pageTitle", switch (section) {
      case "agenda" -> "Agenda";
      case "agendamentos" -> "Agendamentos";
      case "bloqueios" -> "Bloqueios de horário";
      default -> "Relatórios";
    });
    return "admin/prototype";
  }

  @GetMapping("/servicos")
  public String services(Model model) {
    model.addAttribute("services", services.findAll());
    return "admin/services";
  }

  @PostMapping("/servicos")
  public String createService(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String durationMinutes,
      @RequestParam(required = false) String price,
      Model model,
      RedirectAttributes redirect) {
    String error = validate(name, durationMinutes, price);
    if (error != null) {
      model.addAttribute("services", services.findAll());
      model.addAttribute("error", error);
      model.addAttribute("nameValue", name);
      model.addAttribute("durationValue", durationMinutes);
      model.addAttribute("priceValue", price);
      return "admin/services";
    }
    services.save(new ServiceOffering(name.trim(), Integer.parseInt(durationMinutes), new BigDecimal(price.replace(',', '.'))));
    redirect.addFlashAttribute("success", "Serviço cadastrado e salvo.");
    return "redirect:/admin/servicos";
  }

  @PostMapping("/servicos/{id}/inativar")
  public String deactivateService(@PathVariable Long id, RedirectAttributes redirect) {
    ServiceOffering service = services.findById(id).orElse(null);
    if (service != null) {
      service.setActive(false);
      services.save(service);
      redirect.addFlashAttribute("success", "Serviço inativado. O registro foi preservado.");
    }
    return "redirect:/admin/servicos";
  }

  private String validate(String name, String duration, String price) {
    if (name == null || name.trim().isEmpty() || name.trim().length() > 60)
      return "Informe um nome com até 60 caracteres.";
    try {
      int minutes = Integer.parseInt(duration);
      if (minutes < 15 || minutes % 15 != 0) return "A duração deve ser múltipla de 15 minutos.";
    } catch (RuntimeException exception) {
      return "Informe uma duração válida em minutos.";
    }
    try {
      BigDecimal amount = new BigDecimal(price.replace(',', '.'));
      if (amount.signum() <= 0 || amount.scale() > 2) return "Informe um preço maior que zero com até duas casas decimais.";
    } catch (RuntimeException exception) {
      return "Informe um preço válido em reais.";
    }
    return null;
  }
}
