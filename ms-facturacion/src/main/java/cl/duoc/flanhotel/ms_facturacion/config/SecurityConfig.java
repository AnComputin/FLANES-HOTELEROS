package cl.duoc.flanhotel.ms_facturacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitamos CSRF para poder hacer POST desde Postman sin tokens adicionales
                .csrf(csrf -> csrf.disable())

                // 2. Protegemos los endpoints exigiendo autenticación básica
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )

                // 3. Habilitamos el cuadro de inicio de sesión básico
                .httpBasic(withDefaults());

        return http.build();
    }
}