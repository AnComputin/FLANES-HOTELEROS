package cl.duoc.flanhotel.ms_reserva.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ReservaException {

    // 1. metodo para atrapar errores de validación en los DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }

    // 2. Atrapa las excepciones de lógica del Service (Ej: Fechas inválidas, IDs 404)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> manejarErroresDeLogica(ResponseStatusException ex) {
        Map<String, Object> errorDetalle = new HashMap<>();

        // Retorna un JSON limpio con el código de estado y el mensaje exacto
        errorDetalle.put("status", ex.getStatusCode().value());
        errorDetalle.put("mensaje", ex.getReason());

        return new ResponseEntity<>(errorDetalle, ex.getStatusCode());
    }
}