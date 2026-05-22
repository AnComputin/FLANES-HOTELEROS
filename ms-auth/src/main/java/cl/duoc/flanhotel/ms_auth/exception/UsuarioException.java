package cl.duoc.flanhotel.ms_auth.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Le dice a Spring que escuche los errores de todos los controllers
public class UsuarioException {

    // 1. Tu metodo original para errores de validación de campos del DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresDeValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String nombreCampo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(nombreCampo, mensaje);
        });

        return ResponseEntity.badRequest().body(errores);
    }

    // 2. 🔥 EL NUEVO METODO: Ataja la validación de ID no encontrado y lo deja súper limpio
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> manejarUsuarioNoExiste(ResponseStatusException ex) {
        Map<String, String> respuestaCorta = new HashMap<>();

        // Saca el texto "Error: No se puede eliminar..." que escribiste en el Service
        respuestaCorta.put("",ex.getReason());

        // Devuelve el código de estado correspondiente (ej: 404 NOT FOUND) y solo el mensaje corto
        return new ResponseEntity<>(respuestaCorta, ex.getStatusCode());
    }
}
