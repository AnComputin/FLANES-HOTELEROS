package cl.duoc.flanhotel.ms_habitacion.controller;

import cl.duoc.flanhotel.ms_habitacion.dto.HabitacionDTO;
import cl.duoc.flanhotel.ms_habitacion.entidad.Habitacion;
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

    @PostMapping("/crear")
    public ResponseEntity<Habitacion> crear(@Valid @RequestBody HabitacionDTO habitacionDTO) {
        Habitacion nueva = habitacionService.crearHabitacion(habitacionDTO);
        return ResponseEntity.ok(nueva);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Habitacion>> listar() {
        return ResponseEntity.ok(habitacionService.listarTodas());
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
}