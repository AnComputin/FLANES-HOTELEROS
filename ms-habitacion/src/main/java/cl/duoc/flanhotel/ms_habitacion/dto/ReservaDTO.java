package cl.duoc.flanhotel.ms_habitacion.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservaDTO {
    private long id;
    private Long idCliente;
    private Long idHabitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private String nombreCliente;
}