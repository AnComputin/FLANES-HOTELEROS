package cl.duoc.flanhotel.ms_habitacion.service;

import cl.duoc.flanhotel.ms_habitacion.client.ReservaClient;
import cl.duoc.flanhotel.ms_habitacion.dto.HabitacionDTO;
import cl.duoc.flanhotel.ms_habitacion.dto.ReservaDTO;
import cl.duoc.flanhotel.ms_habitacion.entidad.Habitacion;
import cl.duoc.flanhotel.ms_habitacion.repository.HabitacionRepository;
import lombok.extern.slf4j.Slf4j; // <-- Importante
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j // <-- Esta anotación activa los logs
@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private ReservaClient reservaClient;

    public Habitacion crearHabitacion(HabitacionDTO dto) {
        Habitacion habitacion = new Habitacion();
        habitacion.setNumero(dto.getNumero());
        habitacion.setTipo(dto.getTipo());
        habitacion.setPrecioNoche(dto.getPrecioNoche());
        habitacion.setEstado(dto.getEstado());
        if (habitacionRepository.existsByNumero(habitacion.getNumero())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Ya existe una habitación con el número " + habitacion.getNumero() + ". El número debe ser distinto."
            );
        }
        log.info("Iniciando proceso para crear la habitación número: {}", dto.getNumero()); // <-- Log de info


        Habitacion guardada = habitacionRepository.save(habitacion);
        log.info("Habitación guardada exitosamente con ID: {}", guardada.getId()); // <-- Log de éxito

        return guardada;
    }

    public List<Habitacion> listarTodas() {
        log.info("Consultando la lista de todas las habitaciones");
        return habitacionRepository.findAll();
    }
    public Map<String, Object> obtenerHabitacionConReservas(Long idHabitacion) {

        // 1. Buscas la habitación en TU base de datos
        Habitacion habitacion = habitacionRepository.findById(idHabitacion).orElse(null);

        // 2. Usas OpenFeign para ir a buscar las reservas al microservicio de tu compañera
        Object reservasDeLaCompanera = reservaClient.obtenerReservasPorHabitacion(idHabitacion);

        // 3. Juntas todo en una sola respuesta
        Map<String, Object> respuestaFinal = new HashMap<>();
        respuestaFinal.put("Habitacion", habitacion);
        respuestaFinal.put("Reservas", reservasDeLaCompanera);

        return respuestaFinal;
    }
    public List<Habitacion> listarHabitacionesDisponibles() {
        // 1. Buscas todas tus habitaciones en la BD
        List<Habitacion> todas = habitacionRepository.findAll();

        // 2. Le pides las reservas al servicio de tu compañera
        List<ReservaDTO> reservas = reservaClient.listarTodasLasReservas();

        // 3. 🔥 FILTRO ULTRA SEGURO: Sacamos los IDs de habitaciones realmente ocupadas hoy
        List<Long> idsOcupados = reservas.stream()
                .filter(r -> r != null && r.getIdHabitacion() != null && "ACTIVA".equalsIgnoreCase(r.getEstado()))
                .map(ReservaDTO::getIdHabitacion)
                .toList();

        // 4. Filtras tu lista: te quedas solo con las que NO están ocupadas
        List<Habitacion> disponibles = todas.stream()
                .filter(h -> !idsOcupados.contains(h.getId()))
                .toList();

        // 🔥 TRUCO DE ORO: Nos aseguramos de que el JSON que va a Postman
        // cambie el string del atributo a "DISPONIBLE" para que no muestre el dato viejo de la BD
        for (Habitacion hab : disponibles) {
            hab.setEstado("DISPONIBLE");
        }

        return disponibles;
    }

    public List<Habitacion> listarHabitacionesOcupadas() {
        List<ReservaDTO> reservas = reservaClient.listarTodasLasReservas();

        // 🔥 Filtrar solo las activas
        List<Long> idsOcupados = reservas.stream()
                .filter(r -> "ACTIVA".equalsIgnoreCase(r.getEstado()))
                .map(ReservaDTO::getIdHabitacion)
                .distinct()
                .toList();

        if (idsOcupados.isEmpty()) {
            return List.of();
        }

        List<Habitacion> ocupadas = habitacionRepository.findAllById(idsOcupados);
        for (Habitacion hab : ocupadas) {
            hab.setEstado("OCUPADA");
        }
        return ocupadas;
    }

    public List<Habitacion> listarTodasConEstadoReal() {
        // 1. Traemos todas las habitaciones de nuestra base de datos
        List<Habitacion> habitaciones = habitacionRepository.findAll();

        // 2. Traemos todas las reservas históricas
        List<ReservaDTO> reservas = reservaClient.listarTodasLasReservas();

        // 3. 🔥 CORRECCIÓN: Filtramos SÓLO los IDs de habitaciones que tengan reservas realmente "ACTIVAS"
        List<Long> idsRealmenteOcupados = reservas.stream()
                .filter(r -> "ACTIVA".equalsIgnoreCase(r.getEstado())) // 👈 Ignoramos COMPLETADAS y CANCELADAS
                .map(ReservaDTO::getIdHabitacion)
                .toList();

        // 4. Recorremos tus habitaciones y mutamos el atributo según la realidad actual
        for (Habitacion hab : habitaciones) {
            if (idsRealmenteOcupados.contains(hab.getId())) {
                hab.setEstado("OCUPADA");
            } else {
                hab.setEstado("DISPONIBLE");
            }
        }

        return habitaciones;
    }
    public List<Map<String, Object>> listarTodasConReservas() {
        // 1. Traemos todas las habitaciones de la Base de Datos
        List<Habitacion> habitaciones = habitacionRepository.findAll();

        // 2. Traemos todas las reservas desde ms-reserva vía OpenFeign
        List<ReservaDTO> todasLasReservas = reservaClient.listarTodasLasReservas();

        // 3. Cruzamos los datos usando estructuras nativas (Map) para evitar errores de DTOs
        List<Map<String, Object>> resultado = new java.util.ArrayList<>();

        for (Habitacion hab : habitaciones) {
            // Filtramos las reservas que le pertenecen a esta habitación en específico
            List<ReservaDTO> reservasDeEstaHab = todasLasReservas.stream()
                    .filter(r -> r.getIdHabitacion().equals(hab.getId()))
                    .toList();

// 🔥 EL ARREGLO: Evaluamos si hay al menos UNA reserva que esté "ACTIVA"
            boolean tieneReservaActiva = reservasDeEstaHab.stream()
                    .anyMatch(r -> "ACTIVA".equalsIgnoreCase(r.getEstado()));

// 2. Si tiene una reserva activa, se ocupa. Si está cancelada (o vacía), queda DISPONIBLE.
            if (tieneReservaActiva) {
                hab.setEstado("OCUPADA");
            } else {
                hab.setEstado("DISPONIBLE");
            }

            // Armamos el bloque de estructura idéntico al que ya usas
            Map<String, Object> bloque = new java.util.HashMap<>();
            bloque.put("Habitacion", hab);
            bloque.put("Reservas", reservasDeEstaHab);

            resultado.add(bloque);
        }

        return resultado;
    }
    public void eliminarHabitacion(Long idHabitacion) {
        log.info("Intentando eliminar la habitación con ID: {}", idHabitacion);

        // 1. Validamos si existe antes de borrar
        if (!habitacionRepository.existsById(idHabitacion)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "No se puede eliminar: La habitación con ID " + idHabitacion + " no existe."
            );
        }

        habitacionRepository.deleteById(idHabitacion);
    }

}