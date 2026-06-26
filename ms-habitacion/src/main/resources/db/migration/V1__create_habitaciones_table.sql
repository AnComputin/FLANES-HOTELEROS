CREATE TABLE habitaciones (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              numero VARCHAR(50) NOT NULL UNIQUE,
                              tipo VARCHAR(50) NOT NULL, -- Valores: SIMPLE, DELUXE, SUITE
                              precio_noche DECIMAL(10,2) NOT NULL,
                              estado VARCHAR(30) NOT NULL DEFAULT 'DISPONIBLE' -- Valores: DISPONIBLE, OCUPADA
);