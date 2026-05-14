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
    public Usuario registrarUsuario(UsuarioDTO dto) {
        log.info("Procesando registro para: {}", dto.getUsername());

        Usuario usuario = new Usuario();
        // Mapeamos solo lo que tú ya tienes definido
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol() != null ? dto.getRol() : "USER");

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
        usuarioRepository.deleteById(id);
    }
}