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

        // 2. Le pides las reservas al servicio de tu compañera usando Feign
        List<ReservaDTO> reservas = reservaClient.listarTodasLasReservas();

        // 3. Sacas la lista de IDs de habitaciones que están ocupadas hoy
        List<Long> idsOcupados = reservas.stream()
                .map(ReservaDTO::getIdHabitacion)
                .toList();

        // 4. Filtras tu lista: te quedas solo con las que NO están en la lista de ocupadas
        return todas.stream()
                .filter(h -> !idsOcupados.contains(h.getId()))
                .toList();
    }
    public List<Habitacion> listarHabitacionesOcupadas() {
        // 1. Le pedimos todas las reservas al servicio de tu compañera mediante OpenFeign
        List<ReservaDTO> reservas = reservaClient.listarTodasLasReservas();

        // 2. Extraemos una lista limpia con solo los IDs de las habitaciones que están ocupadas
        List<Long> idsOcupados = reservas.stream()
                .map(ReservaDTO::getIdHabitacion)
                .distinct() // Evita duplicados si una habitación tiene varias reservas
                .toList();

        // 3. Si no hay ninguna reserva en todo el hotel, devolvemos una lista vacía de inmediato
        if (idsOcupados.isEmpty()) {
            return List.of();
        }

        // 1. Buscamos las habitaciones en la BD y las guardamos en la lista "ocupadas"
        List<Habitacion> ocupadas = habitacionRepository.findAllById(idsOcupados);

        // 2. Recorremos esa lista para cambiar el estado "en el aire"
        for (Habitacion hab : ocupadas) {
            hab.setEstado("OCUPADA");
        }

        // 3. RETORNAMOS la lista ya modificada (Y NADA MÁS ABAJO)
        return ocupadas;
    }

    public List<Habitacion> listarTodasConEstadoReal() {
        // 1. Traemos todas las habitaciones de nuestra base de datos
        List<Habitacion> habitaciones = habitacionRepository.findAll();

        // 2. Traemos todas las reservas desde el microservicio de la compañera vía Feign
        List<ReservaDTO> reservas = reservaClient.listarTodasLasReservas();

        // 3. Filtramos los IDs de las habitaciones que tienen reservas registradas
        List<Long> idsOcupados = reservas.stream()
                .map(ReservaDTO::getIdHabitacion)
                .toList();

        // 4. Recorremos tus habitaciones y mutamos el atributo estado en el aire
        for (Habitacion hab : habitaciones) {
            if (idsOcupados.contains(hab.getId())) {
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

            // 🔥 LÓGICA DE ESTADO: Si tiene reservas pasa a OCUPADA, si no, DISPONIBLE
            if (!reservasDeEstaHab.isEmpty()) {
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

}