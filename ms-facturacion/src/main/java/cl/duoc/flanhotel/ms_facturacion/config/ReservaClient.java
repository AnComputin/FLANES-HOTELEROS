package cl.duoc.flanhotel.ms_facturacion.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

// 🚀 Apunta directo al puerto de tu microservicio de reservas (8083)
@FeignClient(name = "ms-reserva", url = "http://ms-reserva:8083/api/reservas")
public interface ReservaClient {

    // Cambia la ruta "/" por la que uses en tu controlador para buscar por ID (ej: "/{id}")
    @GetMapping("/{id}")
    Map<String, Object> obtenerReservaPorId(@PathVariable("id") Long id);
}