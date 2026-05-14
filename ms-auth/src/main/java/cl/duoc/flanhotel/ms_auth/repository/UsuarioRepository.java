package cl.duoc.flanhotel.ms_auth.repository;

import cl.duoc.flanhotel.ms_auth.entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring Boot es tan inteligente que con solo escribir este nombre,
    // él sabrá cómo buscar un usuario por su username en la base de datos.
    Optional<Usuario> findByUsername(String username);
}
