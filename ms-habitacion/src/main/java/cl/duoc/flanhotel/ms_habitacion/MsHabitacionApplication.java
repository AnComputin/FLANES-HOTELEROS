package cl.duoc.flanhotel.ms_habitacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

// IMPORTS DE SWAGGER PARA SEGURIDAD
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@EnableFeignClients
@SpringBootApplication // <-- Aquí quitamos el (excludeName = ...)
@EnableDiscoveryClient
// CONFIGURACIÓN DEL CANDADO DE SWAGGER
@OpenAPIDefinition(
		info = @Info(title = "API de Habitaciones", version = "1.0"),
		security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT"
)
public class MsHabitacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsHabitacionApplication.class, args);
	}
}