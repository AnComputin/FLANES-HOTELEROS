package cl.duoc.flanhotel.ms_reserva.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservaDTO {
    private long id; // sin validacion pq mysql lo crea


    @NotNull(message = "El ID del cleinte ess obligatorio")
    private Long idCliente;

    @NotNull(message = "El ID de la habitacion es obligatorio")
    private Long idHabitacion;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent( message = "La fecha de inicio no puede estar en pasado")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    private String estado;
    private String nombreCliente;

}