package br.com.barberpro.web;

import br.com.barberpro.service.BarberService;
import br.com.barberpro.service.CatalogInitializer;
import br.com.barberpro.repository.ServiceOfferingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookingController {
  private final BarberService barberService;
  private final ServiceOfferingRepository serviceRepository;

  public BookingController(BarberService barberService, ServiceOfferingRepository serviceRepository) {
    this.barberService = barberService;
    this.serviceRepository = serviceRepository;
  }
  @GetMapping("/agendar")
  public String booking(Model model) {
    model.addAttribute("barbers", barberService.findAll());
    model.addAttribute("services", serviceRepository.findAllByActiveTrueOrderByIdAsc());
    return "booking/index";
  }
}
