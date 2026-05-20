package cl.duoc.flanhotel.ms_reserva.config;


import cl.duoc.flanhotel.ms_reserva.dto.UsuarioCompartidoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-auth", url = "http://localhost:8081/api/usuarios")
public interface UsuarioFeignClient {

    @GetMapping("/{id}") // Apunta directo al nuevo @GetMapping("/{id}") que pusimos en el controlador
    UsuarioCompartidoDTO obtenerUsuarioPorId(@PathVariable("id") Long id);
}
