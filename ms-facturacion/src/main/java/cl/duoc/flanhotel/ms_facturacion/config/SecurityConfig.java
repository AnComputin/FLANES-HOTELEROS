package cl.duoc.flanhotel.ms_facturacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitamos CSRF por completo (Obligatorio para recibir POSTs)
                .csrf(csrf -> csrf.disable())

                // 2. Dejamos todos los endpoints 100% PÚBLICOS para el testeo de los microservicios
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 👈 Cambiamos a permitAll() total para asegurar el flujo
                )

                // 3. Desactivamos el formulario de login básico que causaba el error 401
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}