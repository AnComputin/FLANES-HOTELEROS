package cl.duoc.flanhotel.ms_habitacion;

import cl.duoc.flanhotel.ms_habitacion.client.ReservaClient;
import cl.duoc.flanhotel.ms_habitacion.entidad.Habitacion;
import cl.duoc.flanhotel.ms_habitacion.repository.HabitacionRepository;
import cl.duoc.flanhotel.ms_habitacion.service.HabitacionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HabitacionServiceTest {

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private ReservaClient reservaClient;

    @InjectMocks
    private HabitacionService habitacionService;

    @Test
    void testListarTodas() {
        Habitacion hab1 = new Habitacion();
        hab1.setId(1L);
        hab1.setNumero("101");

        Habitacion hab2 = new Habitacion();
        hab2.setId(2L);
        hab2.setNumero("102");

        List<Habitacion> listaFalsa = Arrays.asList(hab1, hab2);

        when(habitacionRepository.findAll()).thenReturn(listaFalsa);

        List<Habitacion> resultado = habitacionService.listarTodas();

        assertNotNull(resultado, "La lista no debería ser nula");
        assertEquals(2, resultado.size(), "La lista debería tener 2 habitaciones");
        assertEquals("101", resultado.get(0).getNumero(), "La primera habitación debería ser la 101");

        verify(habitacionRepository, times(1)).findAll();
    }
}