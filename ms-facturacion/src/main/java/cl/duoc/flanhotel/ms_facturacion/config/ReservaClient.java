package cl.duoc.flanhotel.ms_facturacion.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "ms-reserva", url = "http://ms-reserva:8083/api/reservas",
        configuration = FeignClientConfig.class)
public interface ReservaClient {

    @GetMapping("/{id}")
    Map<String, Object> obtenerReservaPorId(@PathVariable("id") Long id);
}