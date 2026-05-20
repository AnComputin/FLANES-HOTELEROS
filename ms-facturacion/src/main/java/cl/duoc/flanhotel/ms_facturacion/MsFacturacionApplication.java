package cl.duoc.flanhotel.ms_facturacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients; // 👈 Asegúrate de importar esto

@EnableFeignClients // 🔥 ESTA ES LA ANOTACIÓN CLAVE QUE FALTA
@SpringBootApplication
public class MsFacturacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsFacturacionApplication.class, args);
	}
}
