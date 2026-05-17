package cl.duoc.flanhotel.ms_reserva.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // Inyectamos a nuestro guardia (JwtFilter)
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desactivamos CSRF porque nuestra API es REST y usaremos JWT
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Le decimos a Spring que no guarde sesiones (cada petición debe traer su propio token)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configuramos las reglas de acceso
                .authorizeHttpRequests(auth -> auth
                        // EJEMPLO: Si tienes alguna ruta pública en este microservicio, se pone aquí:
                        // .requestMatchers("/api/publica/**").permitAll()

                        // Exigimos que cualquier otra petición esté autenticada (tenga un token válido)
                        .anyRequest().authenticated()
                )

                // 4. Ponemos a nuestro guardia antes del filtro tradicional de Spring Security
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}