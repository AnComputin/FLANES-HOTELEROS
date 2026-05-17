package cl.duoc.flanhotel.ms_reserva.service;

import cl.duoc.flanhotel.ms_reserva.dto.ReservaDTO;
import cl.duoc.flanhotel.ms_reserva.entidad.Reserva;
import cl.duoc.flanhotel.ms_reserva.repository.ReservaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j // Activa logs
@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public Reserva crearReserva(ReservaDTO dto) {
        log.info("Iniciando proceso para crear una reserva para el cliente ID: {}", dto.getIdCliente());

        // Mapeo: Pasamos los datos del DTO (mensajero) a la Entidad (base de datos)
        Reserva reserva = new Reserva();
        reserva.setIdCliente(dto.getIdCliente());
        reserva.setIdHabitacion(dto.getIdHabitacion());
        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaFin(dto.getFechaFin());
        reserva.setEstado("PENDIENTE");

        Reserva guardada = reservaRepository.save(reserva);

        log.info("Reserva guardada exitosamente con ID: {}", guardada.getId());
        return guardada;
    }

    public List<Reserva> listarTodas() {
        log.info("Consultando la lista de todas las reservas en la base de datos");
        return reservaRepository.findAll();
    }

    // Metodo: Busca una reserva por su ID y actualiza su estado
    public Reserva actualizarEstado(Long id, String nuevoEstado) {

        // 1. Busca la reserva en la base de datos usando el ID. Si no existe, lanza un error.
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con el ID: " + id));

        // 2. Modifica el estado antiguo con el nuevo texto recibido.
        reserva.setEstado(nuevoEstado);

        // 3. Guarda los cambios actualizados en la base de datos y los retorna.
        return reservaRepository.save(reserva);
    }

    // Metodo: Busca una reserva por su ID y la elimina físicamente de la base de datos
    public void eliminarReserva(Long id) {

        // 1. Verifica si la reserva existe antes de intentar borrarla. Si no está, lanza un error.
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Reserva no encontrada con el ID: " + id);
        }

        // 2. Ejecuta la orden de borrado en la base de datos (DELETE FROM reservas WHERE id = ...).
        reservaRepository.deleteById(id);
    }
}