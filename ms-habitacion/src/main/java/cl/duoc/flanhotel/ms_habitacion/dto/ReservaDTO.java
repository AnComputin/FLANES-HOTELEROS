package cl.duoc.flanhotel.ms_habitacion.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservaDTO {

    // 🔥 AGREGA ESTA ANOTACIÓN AQUÍ TAMBIÉN:
    @com.fasterxml.jackson.annotation.JsonProperty("idReserva")
    private Long id;

    private Long idCliente;
    private Long idHabitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private String nombreCliente;
    private String nombreQuienReserva;
}