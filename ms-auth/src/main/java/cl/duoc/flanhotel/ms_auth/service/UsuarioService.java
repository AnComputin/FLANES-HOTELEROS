package cl.duoc.flanhotel.ms_auth.service;

import cl.duoc.flanhotel.ms_auth.dto.UsuarioDTO;
import cl.duoc.flanhotel.ms_auth.entidad.Usuario;
import cl.duoc.flanhotel.ms_auth.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Metodo 1: REGISTRAR (Usando tus campos reales)
    public Usuario registrarCliente(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        // Recuerda usar tu encriptor de claves si lo tienes inyectado (ej: passwordEncoder.encode)
        usuario.setPassword(dto.getPassword());

        // 🔥 Forzamos que en la BD se guarde siempre como CLIENTE
        usuario.setRol("CLIENTE");

        return usuarioRepository.save(usuario);
    }
    public Usuario registrarAdmin(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());

        // 💼 Aquí sí respetamos el rol que manden desde Postman (ADMIN, RECEPCIONISTA, etc.)
        usuario.setRol(dto.getRol());

        return usuarioRepository.save(usuario);
    }

    // Metodo 2: LISTAR
    public List<Usuario> listarTodos() {
        log.info("Obteniendo lista de usuarios");
        return usuarioRepository.findAll();
    }

    // Metodo 3: BUSCAR POR USERNAME (Tu método original)
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Metodo 4: ELIMINAR
    public void eliminarUsuario(Long id) {
        // 1. Validar si el usuario NO existe en la base de datos
        if (!usuarioRepository.existsById(id)) {
            // Si no existe, frenamos el flujo de inmediato y lanzamos un error 404
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Error: No se puede eliminar porque el Usuario con ID " + id + " no existe."
            );
        }

        // 2. Si pasa la validación, recién ahí lo borramos
        usuarioRepository.deleteById(id);
    }

    public boolean validarUsuario(String username, String password) {
        // Buscamos al usuario por su username
        return usuarioRepository.findByUsername(username)
                .map(user -> user.getPassword().equals(password)) // Comparamos clave
                .orElse(false); // Si no existe, devolvemos falso
    }
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));
    }
}