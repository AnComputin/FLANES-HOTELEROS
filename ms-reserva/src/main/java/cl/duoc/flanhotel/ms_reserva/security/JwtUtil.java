package cl.duoc.flanhotel.ms_reserva.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    // 1. LA LLAVE COMPARTIDA: Debe ser EXACTAMENTE la misma que tiene tu compañero en su ms-auth
    private final String SECRET_KEY_STRING = "EstaEsUnaLlaveSuperSecretaYMuyLargaParaElHotelFlanes2026";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());

    // 2. Extrae el nombre de usuario (o correo) que viene escondido dentro del token
    public String extraerUsuario(String token) {
        return obtenerClaims(token).getSubject();
    }

    // 3. Valida si el token es real (firmado con nuestra llave) y si aún no ha expirado
    public boolean validarToken(String token) {
        try {
            obtenerClaims(token);
            return true; // Es válido
        } catch (Exception e) {
            return false; // Es falso, fue alterado o ya pasó 1 hora y expiró
        }
    }

    // 4. Método interno de apoyo: Desencripta el token usando la llave secreta
    private Claims obtenerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
