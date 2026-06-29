package cl.duoc.flanhotel.ms_facturacion.service;

import cl.duoc.flanhotel.ms_facturacion.config.ReservaClient;
import cl.duoc.flanhotel.ms_facturacion.entity.Factura;
import cl.duoc.flanhotel.ms_facturacion.exception.FacturaNotFoundException;
import cl.duoc.flanhotel.ms_facturacion.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ReservaClient reservaClient;

    @Override
    public Factura crearFactura(Factura factura) {
        try {
            reservaClient.obtenerReservaPorId(factura.getIdReserva());
        } catch (Exception e) {
            throw new FacturaNotFoundException(
                    "Error: No se puede generar la factura porque la Reserva ID "
                            + factura.getIdReserva() + " no existe."
            );
        }
        if (factura.getEstadoPago() == null) {
            factura.setEstadoPago("PENDIENTE");
        }
        return facturaRepository.save(factura);
    }

    @Override
    public List<Factura> obtenerTodas() {
        return facturaRepository.findAll();
    }

    @Override
    public Factura obtenerPorId(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new FacturaNotFoundException(
                        "Factura no encontrada con ID: " + id));
    }

    @Override
    public Factura obtenerPorIdReserva(Long idReserva) {
        return facturaRepository.findByIdReserva(idReserva)
                .orElseThrow(() -> new FacturaNotFoundException(
                        "No hay factura asociada a la reserva con ID: " + idReserva));
    }

    @Override
    public Factura cambiarEstadoPago(Long id, String nuevoEstado) {
        Factura factura = obtenerPorId(id);
        factura.setEstadoPago(nuevoEstado.toUpperCase());
        return facturaRepository.save(factura);
    }
}