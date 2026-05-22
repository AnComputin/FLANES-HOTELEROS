package cl.duoc.flanhotel.ms_reserva.service;

import cl.duoc.flanhotel.ms_reserva.config.HabitacionFeignClient;
import cl.duoc.flanhotel.ms_reserva.config.UsuarioFeignClient;
import cl.duoc.flanhotel.ms_reserva.config.FacturaClient; // 👈 Asegúrate de que esta ruta apunte a tu FacturaClient
import cl.duoc.flanhotel.ms_reserva.dto.ReservaDTO;
import cl.duoc.flanhotel.ms_reserva.dto.UsuarioCompartidoDTO;
import cl.duoc.flanhotel.ms_reserva.entidad.Reserva;
import cl.duoc.flanhotel.ms_reserva.repository.ReservaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 🌟 IMPORTS REQUERIDOS PARA EL CHECKOUT Y FACTURACIÓN:
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private UsuarioFeignClient usuarioFeignClient;
    @Autowired
    private HabitacionFeignClient habitacionFeignClient;
    @Autowired
    private FacturaClient facturaClient; // 👈 Inyectamos el cliente para conectar con ms_facturacion

    public Reserva crearReserva(ReservaDTO dto) {
        log.info("Iniciando proceso para crear una reserva...");
        java.time.LocalDate hoy = java.time.LocalDate.now();

        if (dto.getNombreQuienReserva() == null || dto.getNombreQuienReserva().trim().isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "El nombre de quien reserva es obligatorio."
            );
        }
        if (dto.getFechaInicio().isBefore(hoy)) {
            log.warn("Error de fechas: Intento de reserva en el pasado ({})", dto.getFechaInicio());
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "No puedes realizar una reserva en una fecha que ya pasó."
            );
        }
        if (!dto.getFechaFin().isAfter(dto.getFechaInicio())) {
            log.warn("Error de fechas: Fecha fin ({}) no es posterior a la de inicio ({})", dto.getFechaFin(), dto.getFechaInicio());
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "La fecha de salida debe ser obligatoriamente posterior a la fecha de entrada."
            );
        }
        try {
            log.info("Consultando disponibilidad física de la habitación ID: {}", dto.getIdHabitacion());

            // Esto ahora llamará de forma interna a http://localhost:8082/api/habitaciones/buscar/{id}
            Map<String, Object> habitacionExistente = habitacionFeignClient.obtenerHabitacionPorId(dto.getIdHabitacion());

            if (habitacionExistente == null || habitacionExistente.isEmpty()) {
                throw new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.BAD_REQUEST,
                        "La habitación seleccionada no está registrada en el sistema."
                );
            }
        } catch (Exception e) {
            log.error("Error al buscar habitación en ms_habitacion: {}", e.getMessage());
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "No se puede realizar la reserva porque la habitación con ID " + dto.getIdHabitacion() + " no existe o fue eliminada."
            );
        }

        UsuarioCompartidoDTO usuarioReal;
        try {
            usuarioReal = usuarioFeignClient.obtenerUsuarioPorId(dto.getIdCliente());
        } catch (Exception e) {
            log.error("Error al buscar usuario en ms_auth: {}", e.getMessage());
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "El idCliente " + dto.getIdCliente() + " no existe en el sistema de usuarios."
            );
        }

        dto.setNombreCliente(usuarioReal.getUsername());

        List<Reserva> todasLasActivas = reservaRepository.findByEstado("ACTIVA");
        boolean hayTraslape = todasLasActivas.stream()
                .filter(r -> r.getIdHabitacion().equals(dto.getIdHabitacion()))
                .anyMatch(r -> dto.getFechaInicio().isBefore(r.getFechaFin()) &&
                        dto.getFechaFin().isAfter(r.getFechaInicio()));

        if (hayTraslape) {
            log.warn("Intento de reserva fallido: La habitación {} ya está ocupada.", dto.getIdHabitacion());
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "La habitación ya se encuentra reservada en esas fechas.");
        }

        Reserva reserva = new Reserva();
        reserva.setIdCliente(dto.getIdCliente());
        reserva.setIdHabitacion(dto.getIdHabitacion());
        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaFin(dto.getFechaFin());
        reserva.setEstado("ACTIVA");
        reserva.setNombreCliente(dto.getNombreCliente());
        reserva.setNombreQuienReserva(dto.getNombreQuienReserva());

        Reserva guardada = reservaRepository.save(reserva);
        log.info("Reserva guardada exitosamente con ID: {}", guardada.getId());

        try {
            habitacionFeignClient.actualizarEstadoHabitacion(dto.getIdHabitacion(), "OCUPADA");
            log.info("Estado de la habitación {} actualizado a 'OCUPADA' con éxito.", dto.getIdHabitacion());
        } catch (Exception e) {
            log.error("No se pudo actualizar el estado de la habitación en ms_habitacion: {}", e.getMessage());
        }

        return guardada;
    }

    public List<Reserva> listarTodas() {
        log.info("Consultando la lista de todas las reservas en la base de datos");
        return reservaRepository.findAll();
    }

    public void eliminarReserva(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Reserva no encontrada con el ID: " + id);
        }
        reservaRepository.deleteById(id);
        habitacionFeignClient.actualizarEstadoHabitacion(id, "DISPONIBLE");
    }

    public List<Reserva> listarPorHabitacionId(Long idHabitacion) {
        log.info("Servicio: Buscando reservas para la habitación ID: {}", idHabitacion);
        List<Reserva> todas = reservaRepository.findAll();
        return todas.stream()
                .filter(r -> r.getIdHabitacion().equals(idHabitacion))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Reserva> listarReservasActivas() {
        log.info("Buscando todas las reservas con estado ACTIVA en la base de datos");
        return reservaRepository.findByEstado("ACTIVA");
    }

    public Reserva cancelarReserva(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("No se encontró la reserva con ID: " + idReserva));

        reserva.setEstado("CANCELADA");
        log.info("La reserva ID {} ha sido CANCELADA", idReserva);
        return reservaRepository.save(reserva);
    }

    // 🌟 QUITAMOS EL @Transactional PARA QUE NO TRANQUE LA BASE DE DATOS DURANTE LA LLAMADA DE FEIGN
    public Reserva procesarCheckOut(Long idReserva) {
        log.info("Procesando Check-Out con nombres de parámetros corregidos para Reserva ID: {}", idReserva);

        // 1. Buscamos la reserva
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Error: No existe ninguna reserva registrada con el ID: " + idReserva));

        if (!"ACTIVA".equalsIgnoreCase(reserva.getEstado())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "La reserva no está ACTIVA.");
        }

        // 2. Pedimos los datos de la habitación (para la matemática)
        Double precioNoche = 50000.0;
        try {
            Map<String, Object> hab = habitacionFeignClient.obtenerHabitacionPorId(reserva.getIdHabitacion());
            if (hab != null && hab.get("precioNoche") != null) {
                precioNoche = Double.valueOf(hab.get("precioNoche").toString());
            }
        } catch (Exception e) {
            log.error("No se pudo obtener el precio de ms_habitacion, usando por defecto: {}", precioNoche);
        }

        // 3. Calculamos noches y monto total
        long noches = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
        if (noches <= 0) noches = 1;
        Double montoTotalCalculado = noches * precioNoche;

        // 4. LIBERAMOS LA HABITACIÓN
        try {
            habitacionFeignClient.actualizarEstadoHabitacion(reserva.getIdHabitacion(), "DISPONIBLE");
            log.info("🚀 ÉXITO: Orden enviada a ms_habitacion para cambiar ID {} a DISPONIBLE.", reserva.getIdHabitacion());
        } catch (Exception e) {
            log.error("❌ ERROR CRÍTICO al liberar habitación: {}", e.getMessage());
        }

        // 5. ARCHIVAMOS LA RESERVA
        // Al no haber @Transactional a nivel de método, el .saveAndFlush() impacta de inmediato y libera el registro de forma permanente en MySQL
        reserva.setEstado("COMPLETADA");
        Reserva reservaGuardada = reservaRepository.saveAndFlush(reserva);
        log.info("🚀 ÉXITO: Reserva ID {} guardada y liberada en la base de datos.", idReserva);


        return reservaGuardada;
    }
}