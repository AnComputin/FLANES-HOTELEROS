package cl.duoc.flanhotel.ms_auth.repository;

import cl.duoc.flanhotel.ms_auth.entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Este nombre de metodo es el que el Service busca para que no de error
    Optional<Usuario> findByUsername(String username);
}
