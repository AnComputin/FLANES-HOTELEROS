package cl.duoc.flanhotel.ms_habitacion.entidad;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "habitaciones")
public class Habitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private String tipo;
    private Integer precioNoche;
    private String estado;
}
