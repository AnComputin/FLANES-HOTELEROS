CREATE TABLE facturas (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          id_reserva BIGINT NOT NULL,
                          monto_total DOUBLE NOT NULL,
                          estado_pago VARCHAR(50) NOT NULL,
                          fecha_emision DATETIME NOT NULL
);