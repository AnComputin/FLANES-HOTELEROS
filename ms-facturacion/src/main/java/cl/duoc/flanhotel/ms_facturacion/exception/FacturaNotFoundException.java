package cl.duoc.flanhotel.ms_facturacion.exception;

// Definición de nuestra excepción personalizada.
// Hereda de RuntimeException para que Spring Boot pueda manejarla en tiempo de ejecución
// sin obligarnos a usar bloques try-catch repetitivos por todo el código.
public class FacturaNotFoundException extends RuntimeException {

    // Constructor de la clase que recibe el mensaje de texto personalizado
    public FacturaNotFoundException(String mensaje) {
        // Envia el mensaje directamente a la clase padre (RuntimeException)
        // para que quede registrado en el hilo del error
        super(mensaje);
    }
}