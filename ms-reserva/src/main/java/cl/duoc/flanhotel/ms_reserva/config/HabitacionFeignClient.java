package cl.duoc.flanhotel.ms_reserva.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@FeignClient(name = "ms-habitacion", url = "http://ms-habitacion:8082/api/habitaciones",
        configuration = FeignClientConfig.class)
public interface HabitacionFeignClient {

    @GetMapping("/buscar/{id}")
    Map<String, Object> obtenerHabitacionPorId(@PathVariable("id") Long id);

    @PutMapping("/{id}/estado")
    void actualizarEstadoHabitacion(
            @PathVariable("id") Long id,
            @RequestParam("nuevoEstado") String nuevoEstado);
}