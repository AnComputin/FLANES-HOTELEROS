package cl.duoc.flanhotel.ms_reserva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient // <--- Hace que el servicio sea detectable
public class MsReservaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsReservaApplication.class, args);
	}

}
