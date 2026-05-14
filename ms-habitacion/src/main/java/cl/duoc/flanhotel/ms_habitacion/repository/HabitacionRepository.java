package cl.duoc.flanhotel.ms_habitacion.repository;

import cl.duoc.flanhotel.ms_habitacion.entidad.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    // Aquí podrías agregar búsquedas personalizadas después, como buscar por número
}