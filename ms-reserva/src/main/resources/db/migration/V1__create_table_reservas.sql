CREATE TABLE reservas (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          estado VARCHAR(255),
                          fecha_fin DATE,
                          fecha_inicio DATE,
                          id_cliente BIGINT,
                          id_habitacion BIGINT
);