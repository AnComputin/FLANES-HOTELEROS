package cl.duoc.flanhotel.ms_auth.controller;

import cl.duoc.flanhotel.ms_auth.dto.LoginRequest;
import cl.duoc.flanhotel.ms_auth.dto.OnAdminRegister;
import cl.duoc.flanhotel.ms_auth.dto.UsuarioDTO;
import cl.duoc.flanhotel.ms_auth.entidad.Usuario;
import cl.duoc.flanhotel.ms_auth.security.JwtUtil;
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

    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint 1: Crear un nuevo usuario (POST) con DTO y Validaciones
    // 1. REGISTRO PÚBLICO (Para Clientes)
    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario nuevoCliente = usuarioService.registrarCliente(usuarioDTO);
        return ResponseEntity.ok(nuevoCliente);
    }
    //2. REGISTRO PARA ADMINS
    @PostMapping("/registrar-admin")
    public ResponseEntity<Usuario> registrarAdmin(
            @org.springframework.validation.annotation.Validated(OnAdminRegister.class) @RequestBody UsuarioDTO usuarioDTO
    ) {
        Usuario nuevoAdmin = usuarioService.registrarAdmin(usuarioDTO);
        return ResponseEntity.ok(nuevoAdmin);
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 1. Validamos las credenciales contra la DB (MySQL)
        boolean esValido = usuarioService.validarUsuario(loginRequest.getUsername(), loginRequest.getPassword());

        if (esValido) {
            // 2. Si es real, le entregamos su llave (Token)
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok(token);
        } else {
            // 3. Si miente, le negamos el acceso
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id); // Asegúrate de que tu servicio tenga este método
        return ResponseEntity.ok(usuario);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<java.util.Map<String, String>> manejarDatosDuplicados(org.springframework.dao.DataIntegrityViolationException ex) {
        java.util.Map<String, String> respuesta = new java.util.HashMap<>();
        respuesta.put("estado", "ERROR_DE_VALIDACION");

        // Obtenemos la causa raíz que viene directo de los motores de Hibernate / MySQL
        String mensajeError = ex.getMostSpecificCause().getMessage();

        // Si el error es efectivamente una entrada duplicada (Código 1062 de MySQL)
        if (mensajeError != null && mensajeError.contains("Duplicate entry")) {

            // Evaluamos de forma inteligente la palabra clave del campo que falló en la BD
            if (mensajeError.contains("correo") || mensajeError.contains("email")) {
                respuesta.put("mensaje", "El correo electrónico ya se encuentra registrado por otro usuario.");
            } else if (mensajeError.contains("username") || mensajeError.contains("usuario") || mensajeError.contains("nombre")) {
                respuesta.put("mensaje", "El nombre de usuario ya está en uso. Por favor, elige otro.");
            } else if (mensajeError.contains("rut") || mensajeError.contains("dni")) {
                respuesta.put("mensaje", "El documento de identidad ingresado ya pertenece a una cuenta.");
            } else {
                respuesta.put("mensaje", "No se pudo completar el registro: Uno de los campos ingresados ya existe.");
            }

        } else {
            respuesta.put("mensaje", "Error de consistencia de datos al intentar procesar el usuario.");
        }

        // Devolvemos un código de estado 400 (Bad Request) con el JSON informativo
        return ResponseEntity.badRequest().body(respuesta);
    }
}