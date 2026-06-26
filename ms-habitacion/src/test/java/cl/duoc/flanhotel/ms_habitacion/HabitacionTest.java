package cl.duoc.flanhotel.ms_habitacion;

import cl.duoc.flanhotel.ms_habitacion.entidad.Habitacion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HabitacionTest {

    @Test
    void testCreacionHabitacion() {
        // 1. ARRANGE (Preparar: Inicializamos lo que vamos a probar)
        Habitacion habitacion = new Habitacion();

        // 2. ACT (Actuar: Ejecutamos la acción, en este caso, asignar valores)
        habitacion.setNumero("101");
        habitacion.setTipo("Sencilla");
        habitacion.setEstado("DISPONIBLE");
        habitacion.setPrecioNoche(50000);

        // 3. ASSERT (Afirmar: Comprobamos que el resultado es el que esperamos)
        // Assertions.assertEquals(Lo que espero, Lo que realmente es);
        assertEquals("101", habitacion.getNumero(), "El número de habitación debería ser 101");
        assertEquals("Sencilla", habitacion.getTipo(), "El tipo debería ser Sencilla");
        assertEquals("DISPONIBLE", habitacion.getEstado(), "El estado debería ser DISPONIBLE");
        assertEquals(50000, habitacion.getPrecioNoche(), "El precio debería ser 50000");
    }
}