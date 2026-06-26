package cl.duoc.flanhotel.ms_reserva.controller;

import cl.duoc.flanhotel.ms_reserva.config.HabitacionFeignClient;
import cl.duoc.flanhotel.ms_reserva.dto.ReservaDTO;
import cl.duoc.flanhotel.ms_reserva.entidad.Reserva;
import cl.duoc.flanhotel.ms_reserva.service.ReservaService;
import cl.duoc.flanhotel.ms_reserva.config.FacturaClient;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@RequestMapping("/api/reservas")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private FacturaClient facturaClient;

    @Autowired
    private HabitacionFeignClient habitacionFeignClient;

    @PostMapping("/crear")
    public ResponseEntity<Reserva> crear(@Valid @RequestBody ReservaDTO dto) {
        log.info("Controlador: Petición para crear reserva recibida");
        Reserva nuevaReserva = reservaService.crearReserva(dto);
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Reserva>> listar() {
        log.info("Controlador: Petición para listar reservas recibida");
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        return ResponseEntity.ok("Reserva eliminada correctamente");
    }

    @GetMapping("/habitacion/{idHabitacion}")
    public ResponseEntity<List<Reserva>> obtenerPorHabitacion(@PathVariable Long idHabitacion) {
        return ResponseEntity.ok(reservaService.listarPorHabitacionId(idHabitacion));
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Reserva>> obtenerReservasActivas() {
        return ResponseEntity.ok(reservaService.listarReservasActivas());
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Reserva> cancelarReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.cancelarReserva(id));
    }

    @PutMapping("/checkout/{id}")
    public ResponseEntity<Reserva> realizarCheckOut(@PathVariable Long id) {
        log.info("Controlador: Recibiendo petición de Check-Out para la reserva ID: {}", id);

        Reserva reservaFinalizada = reservaService.procesarCheckOut(id);

        long noches = ChronoUnit.DAYS.between(reservaFinalizada.getFechaInicio(), reservaFinalizada.getFechaFin());
        if (noches <= 0) noches = 1;
        Double montoTotal = noches * 50000.0;

        try {
            Map<String, Object> facturaBody = new HashMap<>();
            facturaBody.put("idReserva", reservaFinalizada.getId());
            facturaBody.put("montoTotal", montoTotal);
            facturaBody.put("estadoPago", "PAGADO");
            facturaBody.put("fechaEmision", null);
            facturaClient.enviarFacturaANueva(facturaBody);
            log.info("Factura creada exitosamente vía Feign para reserva ID: {}", reservaFinalizada.getId());
        } catch (Exception e) {
            log.error("No se pudo crear la factura vía Feign: {}", e.getMessage());
        }

        return ResponseEntity.ok(reservaFinalizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Long id) {
        Reserva reserva = reservaService.listarTodas().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Reserva no encontrada con ID: " + id));
        return ResponseEntity.ok(reserva);
    }

    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> manejarError(org.springframework.web.server.ResponseStatusException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", ex.getReason());
        respuesta.put("estado", "ERROR_DE_VALIDACION");
        return ResponseEntity.ok(respuesta);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> manejarErrorJson(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Los campos vacíos deben ser rellenados con datos válidos.");
        respuesta.put("estado", "ERROR_DE_VALIDACION");
        return ResponseEntity.badRequest().body(respuesta);
    }
}