package cl.duoc.flanhotel.ms_facturacion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// @RestControllerAdvice: Esta anotación le avisa a Spring Boot que esta clase es un
// interceptor global de excepciones para todos los @RestController del proyecto.
// Captura cualquier error que ocurra en los controladores y permite unificar las respuestas.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler: Le indica a Spring que si en CUALQUIER parte del microservicio
    // se lanza un error del tipo 'FacturaNotFoundException', detenga el flujo común y ejecute este método.
    @ExceptionHandler(FacturaNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarFacturaNoEncontrada(FacturaNotFoundException ex) {

        // Creamos una instancia de nuestra respuesta limpia usando el molde 'ErrorResponse'
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(), // .value() transforma el estado NOT_FOUND al número entero 404
                "Not Found",                  // Texto descriptivo del código de estado
                ex.getMessage()               // Extrae el mensaje específico que escribimos en el Service
        );

        // ResponseEntity: Clase especial de Spring que envuelve tanto el cuerpo del JSON (error)
        // como el código de estado HTTP real (404 Not Found) que viajará en las cabeceras hacia Postman.
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // @ExceptionHandler(Exception.class): Este método sirve como una red de seguridad global.
    // Si ocurre un error imprevisto (ej: base de datos MySQL desconectada, error de sintaxis nula),
    // entrará aquí en lugar de romper el servidor con una pantalla fea llena de código Java.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErroresGlobales(Exception ex) {

        // Armamos una estructura de error de código 500 (Falla interna del servidor)
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // Convierte el estado de servidor al número entero 500
                "Internal Server Error",                  // Texto estándar internacional del error 500
                "Ocurrió un error interno inesperado: " + ex.getMessage() // Adjunta la descripción técnica
        );

        // Retornamos el JSON envuelto con un estado HTTP 500
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}