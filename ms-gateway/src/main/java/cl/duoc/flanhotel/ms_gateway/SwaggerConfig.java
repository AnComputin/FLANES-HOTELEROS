package cl.duoc.flanhotel.ms_gateway; // <-- Asegúrate de que este sea el package real de tu proyecto

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("API Gateway Unificado");

        return new OpenAPI().servers(List.of(localServer));
    }
}