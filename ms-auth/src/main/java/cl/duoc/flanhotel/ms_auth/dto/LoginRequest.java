package cl.duoc.flanhotel.ms_auth.dto;

import lombok.Data; // <--- Import necesario

@Data // <--- Esta anotación crea los métodos getUsername y getPassword por ti
public class LoginRequest {
    private String username;
    private String password;
}