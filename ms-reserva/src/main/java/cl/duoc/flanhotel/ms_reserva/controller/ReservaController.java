package cl.duoc.flanhotel.ms_reserva.controller;

import cl.duoc.flanhotel.ms_reserva.dto.ReservaDTO;
import cl.duoc.flanhotel.ms_reserva.entidad.Reserva;
import cl.duoc.flanhotel.ms_reserva.service.ReservaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/reservas") // Mapeo base unificado en plural para evitar confusiones
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // Crear reserva: El @Valid activa el filtro de errores para el DTO
    @PostMapping("/crear")
    public ResponseEntity<Reserva> crear(@Valid @RequestBody ReservaDTO dto) {
        log.info("Controlador: Petición para crear reserva recibida");
        Reserva nuevaReserva = reservaService.crearReserva(dto);
        // Devuelve un código 201 (Created) que es el estándar correcto para creaciones
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    // Listar reservas
    @GetMapping("/listar")
    public ResponseEntity<List<Reserva>> listar() {
        log.info("Controlador: Petición para listar reservas recibida");
        List<Reserva> lista = reservaService.listarTodas();
        return ResponseEntity.ok(lista); // Equivalente a un 200 OK
    }

    // Actualizar estado de una reserva
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Reserva> actualizar(@PathVariable Long id, @RequestParam String nuevoEstado) {
        log.info("Controlador: Petición para actualizar estado de la reserva ID: {}", id);
        Reserva actualizada = reservaService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(actualizada);
    }

    // Eliminar una reserva
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("Controlador: Petición para eliminar la reserva ID: {}", id);
        reservaService.eliminarReserva(id);
        return ResponseEntity.ok("Reserva eliminada exitosamente con el ID: " + id);
    }
}