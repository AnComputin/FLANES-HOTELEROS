package cl.duoc.flanhotel.ms_habitacion.service;

import cl.duoc.flanhotel.ms_habitacion.dto.HabitacionDTO;
import cl.duoc.flanhotel.ms_habitacion.entidad.Habitacion;
import cl.duoc.flanhotel.ms_habitacion.repository.HabitacionRepository;
import lombok.extern.slf4j.Slf4j; // <-- Importante
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j // <-- Esta anotación activa los logs
@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    public Habitacion crearHabitacion(HabitacionDTO dto) {
        log.info("Iniciando proceso para crear la habitación número: {}", dto.getNumero()); // <-- Log de info

        Habitacion habitacion = new Habitacion();
        habitacion.setNumero(dto.getNumero());
        habitacion.setTipo(dto.getTipo());
        habitacion.setPrecioNoche(dto.getPrecioNoche());
        habitacion.setEstado(dto.getEstado());

        Habitacion guardada = habitacionRepository.save(habitacion);
        log.info("Habitación guardada exitosamente con ID: {}", guardada.getId()); // <-- Log de éxito

        return guardada;
    }

    public List<Habitacion> listarTodas() {
        log.info("Consultando la lista de todas las habitaciones");
        return habitacionRepository.findAll();
    }
}