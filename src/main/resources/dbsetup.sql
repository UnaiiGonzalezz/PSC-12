-- Usar la base de datos correcta
USE restapidb;

-- Tabla de Clientes
CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    telefono VARCHAR(50) NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL
);

-- Tabla de Medicamentos
CREATE TABLE IF NOT EXISTS medicamentos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    precio DOUBLE NOT NULL,
    stock INT NOT NULL,
    proveedor VARCHAR(50) NOT NULL,
    disponible BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla de Carritos
CREATE TABLE IF NOT EXISTS carritos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    creado DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id)
);

-- Tabla de Items del Carrito
CREATE TABLE IF NOT EXISTS carrito_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_carrito BIGINT NOT NULL,
    id_medicamento BIGINT NOT NULL,
    cantidad INT NOT NULL,
    FOREIGN KEY (id_carrito) REFERENCES carritos(id),
    FOREIGN KEY (id_medicamento) REFERENCES medicamentos(id)
);

-- Tabla de Compras
CREATE TABLE IF NOT EXISTS compras (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    idCliente BIGINT NOT NULL,
    fecha_compra DATE NOT NULL,
    cantidad INT NOT NULL,
    pago DOUBLE NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'Pendiente',
    metodo_pago VARCHAR(50) NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES clientes(id)
);

-- Tabla de Relación Compra - Medicamento (muchos a muchos)
CREATE TABLE IF NOT EXISTS compra_medicamento (
    idCompra BIGINT NOT NULL,
    idMedicamento BIGINT NOT NULL,
    PRIMARY KEY (idCompra, idMedicamento),
    FOREIGN KEY (idCompra) REFERENCES compras(id),
    FOREIGN KEY (idMedicamento) REFERENCES medicamentos(id)
);

-- Tabla de Movimientos de Stock
CREATE TABLE IF NOT EXISTS movimientos_stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_medicamento BIGINT NOT NULL,
    tipo ENUM('ENTRADA', 'AJUSTE', 'VENTA') NOT NULL,
    cantidad INT NOT NULL,
    stock_antes INT NOT NULL,
    stock_despues INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_medicamento) REFERENCES medicamentos(id)
);

-- Datos de prueba iniciales

-- Insertar clientes
INSERT INTO clientes (nombre, apellido, email, contrasena, telefono, metodo_pago) VALUES
('Domingo', 'Bermejo', 'domingo@bermejo.com', '123', '123456789', 'Tarjeta'),
('Ana', 'Gómez', 'ana@gomez.com', '456', '987654321', 'PayPal');

-- Insertar medicamentos
INSERT INTO medicamentos (nombre, categoria, precio, stock, proveedor) VALUES
('Paracetamol', 'Analgésico', 10.0, 100, 'Proveedor A'),
('Ibuprofeno', 'Antiinflamatorio', 12.5, 200, 'Proveedor B'),
('Amoxicilina', 'Antibiótico', 15.0, 150, 'Proveedor C');
