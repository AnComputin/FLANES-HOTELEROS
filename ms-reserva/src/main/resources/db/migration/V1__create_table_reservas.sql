CREATE TABLE reservas (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          id_cliente BIGINT NOT NULL,
                          id_habitacion BIGINT NOT NULL,
                          fecha_inicio DATE NOT NULL,
                          fecha_fin DATE NOT NULL,
                          nombre_cliente VARCHAR(150),
                          nombre_quien_reserva VARCHAR(150) NOT NULL,
                          estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVA'
);