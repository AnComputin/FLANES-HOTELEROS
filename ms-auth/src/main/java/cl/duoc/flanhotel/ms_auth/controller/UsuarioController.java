package cl.duoc.flanhotel.ms_auth.controller;

import cl.duoc.flanhotel.ms_auth.entidad.Usuario;
import cl.duoc.flanhotel.ms_auth.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Le dice a Spring que esta es la puerta para recibir datos de internet
@RequestMapping("/api/usuarios") // La URL base será: http://localhost:8081/api/usuarios
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService; // Llamamos al cerebro que creamos antes

    // Endpoint 1: Crear un nuevo usuario (POST)
    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    // Endpoint 2: Ver todos los usuarios (GET)
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