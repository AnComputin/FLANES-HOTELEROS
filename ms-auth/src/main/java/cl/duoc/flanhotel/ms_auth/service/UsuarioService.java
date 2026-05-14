package cl.duoc.flanhotel.ms_auth.service;

import cl.duoc.flanhotel.ms_auth.entidad.Usuario;
import cl.duoc.flanhotel.ms_auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Le dice a Spring Boot: "Este es el cerebro de la aplicación"
public class UsuarioService {

    @Autowired // Inyecta el repositorio que creamos antes para poder usarlo
    private UsuarioRepository usuarioRepository;

    // Metodo 1: GUARDAR USUARIO EN BASE DE DATOS
    public Usuario registrarUsuario(Usuario nuevoUsuario) {
        // (Más adelante aquí agregaremos la encriptación de la contraseña)
        return usuarioRepository.save(nuevoUsuario);
    }

    // Metodo 2: LISTAR USUARIOS
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Metodo 3: BUSCAR USUARIO POR NOMBRE DE USUARIO
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Metodo 4: ELIMINAR USUARIO
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}