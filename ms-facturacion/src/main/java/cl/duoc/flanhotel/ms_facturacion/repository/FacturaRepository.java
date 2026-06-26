package cl.duoc.flanhotel.ms_facturacion.repository;

import cl.duoc.flanhotel.ms_facturacion.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    // Un metodo personalizado por si alguna vez quieres buscar la factura de una reserva específica
    Optional<Factura> findByIdReserva(Long idReserva);
}