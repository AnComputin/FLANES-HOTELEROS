package cl.duoc.flanhotel.ms_reserva.dto;

import cl.duoc.flanhotel.ms_reserva.entidad.Reserva;
import java.util.List;
import java.util.Map;

public class HabitacionConReservasDTO {
    private Map<String, Object> habitacion; // Aquí guardaremos los datos que vienen de ms-habitacion
    private List<Reserva> reservas;         // Aquí las reservas locales

    // Constructor, Getters y Setters (o ponle @Data de Lombok)
    public HabitacionConReservasDTO(Map<String, Object> habitacion, List<Reserva> reservas) {
        this.habitacion = habitacion;
        this.reservas = reservas;
    }

    public Map<String, Object> getHabitacion() { return habitacion; }
    public void setHabitacion(Map<String, Object> habitacion) { this.habitacion = habitacion; }
    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }
}
