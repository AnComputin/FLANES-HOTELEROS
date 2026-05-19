package cl.duoc.flanhotel.ms_reserva.repository;

import cl.duoc.flanhotel.ms_reserva.entidad.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Cambiar a este nombre exacto:
    List<Reserva> findByIdHabitacion(Long idHabitacion);

    List<Reserva> findByEstado(String estado); // Buscar reservas con estado "ACTIVA"
}