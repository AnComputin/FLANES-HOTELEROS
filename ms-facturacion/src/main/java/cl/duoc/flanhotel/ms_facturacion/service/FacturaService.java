package cl.duoc.flanhotel.ms_facturacion.service;

import cl.duoc.flanhotel.ms_facturacion.entity.Factura;
import java.util.List;

public interface FacturaService {
    Factura crearFactura(Factura factura);
    List<Factura> obtenerTodas();
    Factura obtenerPorId(Long id);
    Factura obtenerPorIdReserva(Long idReserva);
    Factura cambiarEstadoPago(Long id, String nuevoEstado);
}