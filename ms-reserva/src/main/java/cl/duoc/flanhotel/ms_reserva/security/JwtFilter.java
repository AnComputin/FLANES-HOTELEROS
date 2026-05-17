package cl.duoc.flanhotel.ms_reserva.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // Inyectamos nuestro JwtUtil (el experto en leer tokens)
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extraer el header de la petición llamado "Authorization"
        String authHeader = request.getHeader("Authorization");

        // 2. Verificar si el cliente envió el token con el formato correcto ("Bearer eyJhbGci...")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // 3. Quitamos la palabra "Bearer " para quedarnos solo con el código del token
            String token = authHeader.substring(7);

            try {
                // 4. Validamos el token usando los métodos que creaste en JwtUtil
                if (jwtUtil.validarToken(token)) {

                    // Si es válido, sacamos el nombre del usuario
                    String username = jwtUtil.extraerUsuario(token);

                    // 5. Le avisamos a Spring Security que este usuario está autenticado y lo dejamos pasar
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, new ArrayList<>() // Aquí irían los roles (ADMIN, USER), por ahora vacío
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Si el token es falso o expiró, atrapamos el error para que la app no explote
                System.out.println("Acceso denegado. Token inválido: " + e.getMessage());
            }
        }

        // 6. Finalmente, dejamos que la petición siga su camino hacia el controlador (o sea rechazada si no se autenticó)
        filterChain.doFilter(request, response);
    }
}