package cl.duoc.flanhotel.ms_reserva.entidad;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idCliente;
    private Long idHabitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
}