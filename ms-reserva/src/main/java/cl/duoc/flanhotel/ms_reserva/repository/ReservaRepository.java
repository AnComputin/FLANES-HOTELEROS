package cl.duoc.flanhotel.ms_reserva.repository;

import cl.duoc.flanhotel.ms_reserva.entidad.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}