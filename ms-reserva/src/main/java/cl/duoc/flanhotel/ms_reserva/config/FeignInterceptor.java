package cl.duoc.flanhotel.ms_reserva.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 1. Capturamos la petición HTTP original que viene de Postman hacia ms-reserva
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 2. Extraemos el Header de Authorization (el token JWT)
            String authorizationHeader = request.getHeader("Authorization");

            // 3. Si el token existe, se lo inyectamos de forma automática a la petición saliente de Feign
            if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
                template.header("Authorization", authorizationHeader);
            }
        }
    }
}