package cl.duoc.flanhotel.ms_facturacion.exception;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @Data: Anotación de Lombok que genera automáticamente en segundo plano
 * todos los métodos Getter, Setter, toString, equals y hashCode.
 * Nos ahorra escribir decenas de líneas de código repetitivo.
 */
@Data
public class ErrorResponse {

    // Almacena la fecha y hora exacta en la que ocurrió el error en el servidor
    private LocalDateTime timestamp;

    // Almacena el código de estado HTTP numérico (ej: 404, 500)
    private int status;

    // Almacena el nombre del error HTTP (ej: "Not Found", "Internal Server Error")
    private String error;

    // Almacena nuestro mensaje detallado y amigable (ej: "La factura con ID X no existe")
    private String message;

    // Constructor personalizado para inicializar el objeto fácilmente desde el manejador de errores
    public ErrorResponse(int status, String error, String message) {
        // Inicializa el timestamp con la fecha y hora exacta del momento del fallo
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
    }
}