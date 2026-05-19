package cl.duoc.flanhotel.ms_habitacion.client;

import cl.duoc.flanhotel.ms_habitacion.config.FeignConfig; // Importa la clase que acabas de crear
import cl.duoc.flanhotel.ms_habitacion.dto.ReservaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// Aquí le decimos que use la configuración (FeignConfig) que tiene el Logger y el Token
@FeignClient(name = "ms-reserva", url = "${ms.reserva.url}", configuration = FeignConfig.class)
public interface ReservaClient {

    // Esta ruta debe ser la que tiene tu compañera en su controlador de reserva
    @GetMapping("/api/reservas/habitacion/{id}")
    Object obtenerReservasPorHabitacion(@PathVariable("id") Long id);

    @GetMapping("/api/reservas/listar")
    List<ReservaDTO> listarTodasLasReservas();
}