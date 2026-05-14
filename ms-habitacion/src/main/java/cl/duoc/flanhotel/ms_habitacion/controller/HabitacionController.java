package cl.duoc.flanhotel.ms_habitacion.controller;

import cl.duoc.flanhotel.ms_habitacion.dto.HabitacionDTO;
import cl.duoc.flanhotel.ms_habitacion.entidad.Habitacion;
import cl.duoc.flanhotel.ms_habitacion.service.HabitacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}