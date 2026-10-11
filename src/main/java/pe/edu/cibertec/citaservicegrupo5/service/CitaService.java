package pe.edu.cibertec.citaservicegrupo5.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.cibertec.citaservicegrupo5.dto.AtencionMedicaResponseDTO;
import pe.edu.cibertec.citaservicegrupo5.dto.DiagnosticoDTO;
import pe.edu.cibertec.citaservicegrupo5.dto.PacienteHistorialDTO;
import pe.edu.cibertec.citaservicegrupo5.dto.ResumenFichaMedicaConsolidadaDTO;
import pe.edu.cibertec.citaservicegrupo5.restclient.paciente.iclient.PacienteClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final PacienteClient pacienteClient;

    private final Map<Long, AtencionMedicaResponseDTO> citas =
            new ConcurrentHashMap<>();

    @PostConstruct
    public void cargarCitas() {

        ArrayList<DiagnosticoDTO> diagnosticosCita1 = new ArrayList<>();

        diagnosticosCita1.add(new DiagnosticoDTO(
                1L,
                "J00",
                "Rinofaringitis aguda",
                LocalDateTime.now(),
                "LEVE"
        ));

        citas.put(1L, new AtencionMedicaResponseDTO(
                1L,
                "ATN-001",
                LocalDateTime.now(),
                1L,
                "Dr. Carlos Mendoza",
                "Medicina General",
                "PROGRAMADA",
                diagnosticosCita1,
                200.00,
                0.00
        ));

        ArrayList<DiagnosticoDTO> diagnosticosCita2 = new ArrayList<>();

        diagnosticosCita2.add(new DiagnosticoDTO(
                2L,
                "I10",
                "Hipertensión esencial",
                LocalDateTime.now(),
                "MODERADO"
        ));

        citas.put(2L, new AtencionMedicaResponseDTO(
                2L,
                "ATN-002",
                LocalDateTime.now(),
                2L,
                "Dra. Ana Torres",
                "Cardiología",
                "PROGRAMADA",
                diagnosticosCita2,
                300.00,
                0.00
        ));
    }

    public AtencionMedicaResponseDTO obtenerCita(Long idCita) {

        AtencionMedicaResponseDTO cita = citas.get(idCita);

        if (cita == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cita no encontrada"
            );
        }

        return cita;
    }

    public AtencionMedicaResponseDTO evaluarYCompletar(Long idCita) {

        AtencionMedicaResponseDTO cita = obtenerCita(idCita);

        PacienteHistorialDTO paciente =
                pacienteClient.obtenerPaciente(cita.getPacienteId());

        double montoCopagoBase = 50.00;
        double copagoAplicado;

        if ("EPS".equalsIgnoreCase(paciente.getTipoSeguro())) {

            copagoAplicado = montoCopagoBase * (1 - 0.80);

        } else if ("ESSALUD".equalsIgnoreCase(paciente.getTipoSeguro())) {

            copagoAplicado = 0.00;

        } else {

            copagoAplicado = montoCopagoBase;
        }

        cita.setCopagoAplicado(copagoAplicado);
        cita.setEstadoCita("COMPLETADA");

        return cita;
    }

    public ResumenFichaMedicaConsolidadaDTO generarFichaMedica(
            Long pacienteId) {

        PacienteHistorialDTO paciente =
                pacienteClient.obtenerPaciente(pacienteId);

        List<DiagnosticoDTO> diagnosticos =
                pacienteClient.obtenerDiagnosticos(pacienteId);

        List<AtencionMedicaResponseDTO> historialAtenciones =
                citas.values()
                        .stream()
                        .filter(cita ->
                                cita.getPacienteId().equals(pacienteId))
                        .toList();

        int totalCitasAtendidas =
                (int) historialAtenciones
                        .stream()
                        .filter(cita ->
                                "COMPLETADA".equalsIgnoreCase(
                                        cita.getEstadoCita()
                                ))
                        .count();

        double montoTotalGasto =
                historialAtenciones
                        .stream()
                        .filter(cita ->
                                cita.getCopagoAplicado() != null)
                        .mapToDouble(
                                AtencionMedicaResponseDTO::getCopagoAplicado
                        )
                        .sum();

        boolean requiereAtencionEspecializada =
                diagnosticos
                        .stream()
                        .anyMatch(diagnostico ->
                                "SEVERO".equalsIgnoreCase(
                                        diagnostico.getGravedad()
                                ));

        String clasificacionRiesgo =
                requiereAtencionEspecializada
                        ? "ALTO_RIESGO"
                        : "BAJO_RIESGO";

        return new ResumenFichaMedicaConsolidadaDTO(
                pacienteId,
                paciente.getNombreCompleto(),
                null,
                paciente.getTipoSeguro(),
                totalCitasAtendidas,
                montoTotalGasto,
                historialAtenciones,
                diagnosticos,
                requiereAtencionEspecializada,
                clasificacionRiesgo
        );
    }
}