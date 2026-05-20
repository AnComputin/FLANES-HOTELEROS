package cl.duoc.flanhotel.ms_reserva.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

// 🚀 Llamamos directo al puerto de facturación esquivando el Gateway (8080)
@FeignClient(name = "ms-facturacion", url = "http://localhost:8084/api/facturas")
public interface FacturaClient {

    @PostMapping
    Object enviarFacturaANueva(@RequestBody Map<String, Object> facturaBody);
}
