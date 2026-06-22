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

// IMPORTS DE HATEOAS
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        List<Habitacion> listaConEstadoReal = habitacionService.listarTodasConEstadoReal();
        return ResponseEntity.ok(listaConEstadoReal);
    }

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

    // 🌟 MÉTODO ACTUALIZADO CON HATEOAS
    @GetMapping("/buscar/{id}")
    public ResponseEntity<EntityModel<Habitacion>> buscarPorId(@PathVariable Long id) {
        log.info("Controlador Habitaciones: Buscando habitación con ID: {}", id);

        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Habitación no encontrada con ID: " + id));

        // Envolvemos la entidad en el modelo HATEOAS
        EntityModel<Habitacion> recurso = EntityModel.of(habitacion);

        // Agregamos el enlace hacia sí mismo (self)
        recurso.add(linkTo(methodOn(HabitacionController.class).buscarPorId(id)).withSelfRel());

        // Agregamos el enlace hacia la lista de todas las habitaciones
        recurso.add(linkTo(methodOn(HabitacionController.class).listarHabitaciones()).withRel("listar-todas"));

        // Agregamos el enlace hacia la actualización de estado (requiere un parámetro, ponemos un valor por defecto ilustrativo)
        recurso.add(linkTo(methodOn(HabitacionController.class).actualizarEstado(id, "OCUPADA")).withRel("cambiar-estado"));

        // Agregamos el enlace hacia la eliminación de la habitación
        recurso.add(linkTo(methodOn(HabitacionController.class).eliminar(id)).withRel("eliminar-habitacion"));

        return ResponseEntity.ok(recurso);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public org.springframework.http.ResponseEntity<java.util.Map<String, String>> manejarErrorValidacion(org.springframework.web.server.ResponseStatusException ex) {
        java.util.Map<String, String> respuestaPersonalizada = new java.util.HashMap<>();
        respuestaPersonalizada.put("mensaje", ex.getReason());
        respuestaPersonalizada.put("estado", "ERROR_DE_VALIDACION");
        return org.springframework.http.ResponseEntity.badRequest().body(respuestaPersonalizada);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<java.util.Map<String, String>> eliminar(@PathVariable Long id) {
        habitacionService.eliminarHabitacion(id);
        java.util.Map<String, String> respuesta = new java.util.HashMap<>();
        respuesta.put("mensaje", "La habitación con ID " + id + " fue eliminada exitosamente.");
        respuesta.put("estado", "EXITOSO");
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstado(
            @PathVariable("id") Long id,
            @RequestParam("nuevoEstado") String nuevoEstado) {

        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Habitación no encontrada"));

        habitacion.setEstado(nuevoEstado);
        habitacionRepository.saveAndFlush(habitacion);

        return ResponseEntity.ok().build();
    }
}