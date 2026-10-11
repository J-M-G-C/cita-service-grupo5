package pe.edu.cibertec.citaservicegrupo5.restclient.paciente.iclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.citaservicegrupo5.dto.DiagnosticoDTO;
import pe.edu.cibertec.citaservicegrupo5.dto.PacienteHistorialDTO;

import java.util.List;

@FeignClient(name = "PACIENTE-SERVICE-GRUPO5")
public interface PacienteClient {

    @GetMapping("/api/v1/pacientes/{pacienteId}")
    PacienteHistorialDTO obtenerPaciente(
            @PathVariable("pacienteId") Long pacienteId
    );

    @GetMapping("/api/v1/pacientes/{pacienteId}/diagnosticos")
    List<DiagnosticoDTO> obtenerDiagnosticos(
            @PathVariable("pacienteId") Long pacienteId
    );
}