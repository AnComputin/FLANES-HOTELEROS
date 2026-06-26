package cl.duoc.flanhotel.ms_reserva.dto;

import lombok.Data;

@Data
public class UsuarioCompartidoDTO {
    private Long id; // o idUsuario, según cómo se llame allá

    // 🔥 CAMBIA ESTO por el nombre exacto que usas en tu entidad de ms_auth
    private String username;

    // Ajusta tus getters y setters para esa variable
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}

