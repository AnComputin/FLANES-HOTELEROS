package cl.duoc.flanhotel.ms_habitacion.controller;

import cl.duoc.flanhotel.ms_habitacion.dto.HabitacionDTO;
import cl.duoc.flanhotel.ms_habitacion.entidad.Habitacion;
import cl.duoc.flanhotel.ms_habitacion.repository.HabitacionRepository;
import cl.duoc.flanhotel.ms_habitacion.service.HabitacionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api/habitaciones")
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;
    @Autowired
    private HabitacionRepository habitacionRepository;

    @PostMapping("/crear")
    public ResponseEntity<Habitacion> crear(@Valid @RequestBody HabitacionDTO habitacionDTO) {
        Habitacion nueva = habitacionService.crearHabitacion(habitacionDTO);
        return ResponseEntity.ok(nueva);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Habitacion>> listarHabitaciones() {
        log.info("Petición GET recibida para listar habitaciones con estado real...");

        // 🌟 EL CAMBIO CLAVE: Cambia .listarTodas() por .listarTodasConEstadoReal()
        List<Habitacion> listaConEstadoReal = habitacionService.listarTodasConEstadoReal();

        return ResponseEntity.ok(listaConEstadoReal);
    }


    // Cuando llames a esta ruta en Postman, se activará todo el flujo
    @GetMapping("/{id}/con-reservas")
    public ResponseEntity<Map<String, Object>> obtenerConReservas(@PathVariable Long id) {
        Map<String, Object> resultado = habitacionService.obtenerHabitacionConReservas(id);
        return ResponseEntity.ok(resultado);
    }
    @GetMapping("/disponibles")
    public ResponseEntity<List<Habitacion>> obtenerDisponibles() {
        return ResponseEntity.ok(habitacionService.listarHabitacionesDisponibles());
    }
    @GetMapping("/ocupadas")
    public ResponseEntity<List<Habitacion>> obtenerOcupadas() {
        log.info("Controlador: Petición recibida para listar todas las habitaciones ocupadas");
        List<Habitacion> ocupadas = habitacionService.listarHabitacionesOcupadas();
        return ResponseEntity.ok(ocupadas);
    }
    @GetMapping("/listar-con-estado")
    public ResponseEntity<List<Habitacion>> listarConEstado() {
        log.info("Controlador: Petición recibida para listar habitaciones con estado calculado en tiempo real");
        List<Habitacion> lista = habitacionService.listarTodasConEstadoReal();
        return ResponseEntity.ok(lista);
    }
    @GetMapping("/listar-con-reservas")
    public ResponseEntity<List<Map<String, Object>>> listarTodasConReservas() {
        return ResponseEntity.ok(habitacionService.listarTodasConReservas());
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public org.springframework.http.ResponseEntity<java.util.Map<String, String>> manejarErrorValidacion(org.springframework.web.server.ResponseStatusException ex) {

        java.util.Map<String, String> respuestaPersonalizada = new java.util.HashMap<>();

        respuestaPersonalizada.put("mensaje", ex.getReason());
        respuestaPersonalizada.put("estado", "ERROR_DE_VALIDACION");

        // Lo devolvemos como un 400 Bad Request
        return org.springframework.http.ResponseEntity.badRequest().body(respuestaPersonalizada);
    }
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<java.util.Map<String, String>> eliminar(@PathVariable Long id) {
        // 1. Ejecuta el borrado (si no existe, saltará el error que ya programamos)
        habitacionService.eliminarHabitacion(id);

        // 2. Creamos el JSON de éxito
        java.util.Map<String, String> respuesta = new java.util.HashMap<>();
        respuesta.put("mensaje", "La habitación con ID " + id + " fue eliminada exitosamente.");
        respuesta.put("estado", "EXITOSO");

        // Devolvemos un 200 OK con el mensajito
        return ResponseEntity.ok(respuesta);
    }
    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstado(
            @PathVariable("id") Long id,
            @RequestParam("nuevoEstado") String nuevoEstado) { // 👈 Asegúrate de que diga ("nuevoEstado")

        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Habitación no encontrada"));

        habitacion.setEstado(nuevoEstado);
        habitacionRepository.saveAndFlush(habitacion);

        return ResponseEntity.ok().build();
    }
}