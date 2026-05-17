package cl.duoc.flanhotel.ms_facturacion.service;

import cl.duoc.flanhotel.ms_facturacion.entity.Factura;
import cl.duoc.flanhotel.ms_facturacion.exception.FacturaNotFoundException;
import cl.duoc.flanhotel.ms_facturacion.repository.FacturaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;

    public FacturaServiceImpl(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    @Override
    public Factura crearFactura(Factura factura) {
        if (factura.getEstadoPago() == null) {
            factura.setEstadoPago("PENDIENTE");
        }
        return facturaRepository.save(factura);
    }

    @Override
    public List<Factura> obtenerTodas() {
        return facturaRepository.findAll();
    }

    // @Override: Anotación del compilador de Java. Asegura que estamos implementando de forma correcta
    // un método que ya fue declarado en nuestro contrato base (la interfaz FacturaService).
    @Override
    public Factura obtenerPorId(Long id) {
        // Busca la factura en la tabla usando Spring Data JPA por su Clave Primaria (ID)
        return facturaRepository.findById(id)
                // .orElseThrow: Si findById no halla registros (retorna un Optional vacío),
                // detiene el proceso e instancia de inmediato nuestra excepción personalizada.
                .orElseThrow(() -> new FacturaNotFoundException("Factura no encontrada con ID: " + id));
    }

    @Override
    public Factura obtenerPorIdReserva(Long idReserva) {
        // Busca en la tabla filtrando por la columna personalizada de la reserva
        return facturaRepository.findByIdReserva(idReserva)
                // Si la consulta no trae datos, gatilla el error especializado enviando el mensaje dinámico
                .orElseThrow(() -> new FacturaNotFoundException("No hay factura asociada a la reserva con ID: " + idReserva));
    }

    @Override
    public Factura cambiarEstadoPago(Long id, String nuevoEstado) {
        Factura factura = obtenerPorId(id);
        factura.setEstadoPago(nuevoEstado.toUpperCase());
        return facturaRepository.save(factura);
    }
}