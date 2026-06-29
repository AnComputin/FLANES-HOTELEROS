package cl.duoc.flanhotel.ms_facturacion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FacturaNotFoundException extends RuntimeException {

    public FacturaNotFoundException(String mensaje) {
        super(mensaje);
    }
}