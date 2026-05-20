package cl.duoc.flanhotel.ms_reserva.entidad;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "reservas") // Asegúrate de que en tu BD tenga la "s" al final
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @com.fasterxml.jackson.annotation.JsonProperty("idReserva")
    private Long id;

    // Te recomiendo mapear explícitamente los campos para que la BD los entienda siempre bien
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "id_habitacion")
    private Long idHabitacion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    private String estado;

    // 🔥 ESTA ES LA LÍNEA CLAVE: Le dice a JPA que "nombreCliente" de Java es "nombre_cliente" de MySQL
    @Column(name = "nombre_cliente")
    private String nombreCliente;
    private String nombreQuienReserva;
}