package cl.duoc.flanhotel.ms_habitacion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HabitacionDTO {

    @NotBlank(message = "El número de habitación es obligatorio")
    private String numero;

    @NotBlank(message = "El tipo (Simple, Doble, etc.) es obligatorio")
    private String tipo;

    @NotNull(message = "El precio no puede estar vacío")
    @Min(value = 1, message = "El precio debe ser mayor a 0")
    private Integer precioNoche;

    private String estado = "Disponible"; // Valor por defecto

}