package cl.duoc.flanhotel.ms_habitacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsHabitacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsHabitacionApplication.class, args);
	}

}
