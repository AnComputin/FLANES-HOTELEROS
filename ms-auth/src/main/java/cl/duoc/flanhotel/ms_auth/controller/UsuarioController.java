package cl.duoc.flanhotel.ms_auth.controller;

import cl.duoc.flanhotel.ms_auth.dto.UsuarioDTO;
import cl.duoc.flanhotel.ms_auth.entidad.Usuario;
import cl.duoc.flanhotel.ms_auth.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint 1: Crear un nuevo usuario (POST) con DTO y Validaciones
    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        // Fíjate que aquí adentro ahora le pasamos el usuarioDTO al service (ya no está gris)
        Usuario nuevoUsuario = usuarioService.registrarUsuario(usuarioDTO);
        return ResponseEntity.ok(nuevoUsuario);
    }

    // Endpoint 2: Listar usuarios (GET)
    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // Endpoint 3: Eliminar un usuario (DELETE)
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
}