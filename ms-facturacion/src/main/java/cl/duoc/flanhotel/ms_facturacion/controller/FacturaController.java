package cl.duoc.flanhotel.ms_facturacion.controller;

import cl.duoc.flanhotel.ms_facturacion.entity.Factura;
import cl.duoc.flanhotel.ms_facturacion.service.FacturaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    // Inyección de dependencias por constructor
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    // 1. Crear una nueva factura
    @PostMapping
    public ResponseEntity<Factura> crear(@RequestBody Factura factura) {
        Factura nuevaFactura = facturaService.crearFactura(factura);
        return new ResponseEntity<>(nuevaFactura, HttpStatus.CREATED);
    }

    // 2. Obtener todas las facturas
    @GetMapping
    public ResponseEntity<List<Factura>> obtenerTodas() {
        List<Factura> facturas = facturaService.obtenerTodas();
        return ResponseEntity.ok(facturas);
    }

    // 3. Buscar factura por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Factura> obtenerPorId(@PathVariable Long id) {
        Factura factura = facturaService.obtenerPorId(id);
        return ResponseEntity.ok(factura);
    }

    // 4. Buscar factura asociada a una reserva específica
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<Factura> obtenerPorIdReserva(@PathVariable Long idReserva) {
        Factura factura = facturaService.obtenerPorIdReserva(idReserva);
        return ResponseEntity.ok(factura);
    }

    // 5. Cambiar el estado de pago (Ej: PENDIENTE -> PAGADO)
    @PutMapping("/{id}/estado")
    public ResponseEntity<Factura> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {
        Factura facturaActualizada = facturaService.cambiarEstadoPago(id, nuevoEstado);
        return ResponseEntity.ok(facturaActualizada);
    }
}