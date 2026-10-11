package pe.edu.cibertec.citaservicegrupo5.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.citaservicegrupo5.dto.AtencionMedicaResponseDTO;
import pe.edu.cibertec.citaservicegrupo5.service.CitaService;

@RestController
@RequestMapping("/api/v1/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @GetMapping("/{idCita}")
    public AtencionMedicaResponseDTO obtenerCita(
            @PathVariable("idCita") Long idCita) {

        return citaService.obtenerCita(idCita);
    }

    @GetMapping("/evaluar-y-completar/{idCita}")
    public AtencionMedicaResponseDTO evaluarYCompletar(
            @PathVariable("idCita") Long idCita) {

        return citaService.evaluarYCompletar(idCita);
    }
}