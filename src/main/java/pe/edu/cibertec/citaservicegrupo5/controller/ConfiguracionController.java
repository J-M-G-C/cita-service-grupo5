package pe.edu.cibertec.citaservicegrupo5.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RefreshScope
@RestController
@RequestMapping("/api/v1/citas")
public class ConfiguracionController {

    @Value("${salud.monto-copago-base}")
    private Double montoCopagoBase;

    @Value("${salud.descuento-eps}")
    private Double descuentoEps;

    @Value("${salud.mensaje-bienvenida}")
    private String mensajeBienvenida;

    @Value("${salud.api-key-aseguradora}")
    private String apiKeyAseguradora;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @GetMapping("/configuracion")
    public Map<String, Object> obtenerConfiguracion() {

        Map<String, Object> configuracion = new LinkedHashMap<>();

        configuracion.put("montoCopagoBase", montoCopagoBase);
        configuracion.put("descuentoEps", descuentoEps);
        configuracion.put("mensajeBienvenida", mensajeBienvenida);
        configuracion.put("apiKeyAseguradora", apiKeyAseguradora);
        configuracion.put("datasourcePassword", datasourcePassword);

        return configuracion;
    }
}