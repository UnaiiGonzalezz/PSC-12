CREATE TABLE movimientos_stock (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  id_medicamento BIGINT NOT NULL,
  tipo VARCHAR(20),
  cantidad INT,
  stock_antes INT,
  stock_despues INT,
  fecha TIMESTAMP,
  CONSTRAINT fk_mov_med FOREIGN KEY (id_medicamento)
        REFERENCES medicamentos(id)
); 