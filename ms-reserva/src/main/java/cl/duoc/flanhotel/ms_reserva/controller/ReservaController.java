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

    // Eliminar una reserva
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("Controlador: Petición para eliminar la reserva ID: {}", id);
        reservaService.eliminarReserva(id);
        return ResponseEntity.ok("Reserva eliminada exitosamente con el ID: " + id);
    }
    @GetMapping("/habitacion/{id}")
    public ResponseEntity<List<Reserva>> obtenerReservasPorHabitacion(@PathVariable Long id) {
        log.info("Controlador: Petición de ms-habitacion para buscar reservas de la habitación ID: {}", id);
        List<Reserva> lista = reservaService.listarPorHabitacionId(id);
        return ResponseEntity.ok(lista);
    }
    @GetMapping("/activas")
    public ResponseEntity<List<Reserva>> obtenerReservasActivas() {
        List<Reserva> activas = reservaService.listarReservasActivas();
        return ResponseEntity.ok(activas);
    }
    // En ReservaController.java
    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Reserva> cancelarReserva(@PathVariable Long id) {
        Reserva reservaCancelada = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(reservaCancelada);
    }
    // Agrega esto al final de tu ReservaController.java

    @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public org.springframework.http.ResponseEntity<java.util.Map<String, String>> manejarErrorTraslape(org.springframework.web.server.ResponseStatusException ex) {

        java.util.Map<String, String> respuestaPersonalizada = new java.util.HashMap<>();

        // Extraemos el mensaje ("La habitación ya se encuentra reservada..." o "No puedes realizar una reserva...")
        respuestaPersonalizada.put("mensaje", ex.getReason());

        // 🔥 Cambiamos "OCUPADA" por algo más genérico para que sirva para cualquier error
        respuestaPersonalizada.put("estado", "ERROR_DE_VALIDACION");

        return org.springframework.http.ResponseEntity.ok(respuestaPersonalizada);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public org.springframework.http.ResponseEntity<java.util.Map<String, String>> manejarErrorFormatoJson(org.springframework.http.converter.HttpMessageNotReadableException ex) {

        java.util.Map<String, String> respuestaPersonalizada = new java.util.HashMap<>();

        // Personalizamos el mensaje para indicarle al usuario que el tipo de dato es incorrecto
        respuestaPersonalizada.put("mensaje", "Los campos vacios deben ser rellenados con datos válidos.");
        respuestaPersonalizada.put("estado", "ERROR_DE_VALIDACION");

        return org.springframework.http.ResponseEntity.badRequest().body(respuestaPersonalizada);
    }
    @PutMapping("/checkout/{id}")
    public ResponseEntity<cl.duoc.flanhotel.ms_reserva.entidad.Reserva> realizarCheckOut(@PathVariable Long id) {
        log.info("Controlador: Recibiendo petición de Check-Out para la reserva ID: {}", id);
        cl.duoc.flanhotel.ms_reserva.entidad.Reserva reservaFinalizada = reservaService.procesarCheckOut(id);
        return ResponseEntity.ok(reservaFinalizada);
    }
}