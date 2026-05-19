-- ============================================================
--  PROYECTO 3 - E-Commerce UTS
--  Base de Datos: ecommerce_db
--  Autor: Jaider Javier Jaimes | ID: 1097095697
--  Motor: MySQL 8.0
-- ============================================================

CREATE DATABASE IF NOT EXISTS ecommerce_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE ecommerce_db;

-- ------------------------------------------------------------
-- TABLA: roles
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS roles (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- ------------------------------------------------------------
-- TABLA: usuarios
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    activo     TINYINT(1) DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
-- TABLA: usuario_roles  (N:N)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario_roles (
    usuario_id BIGINT NOT NULL,
    rol_id     BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    CONSTRAINT fk_ur_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_rol     FOREIGN KEY (rol_id)     REFERENCES roles(id)    ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- TABLA: productos
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS productos (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio      DECIMAL(10,2) NOT NULL,
    stock       INT DEFAULT 0,
    imagen_url  VARCHAR(300),
    activo      TINYINT(1) DEFAULT 1,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
-- TABLA: pedidos
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pedidos (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id  BIGINT NOT NULL,
    fecha       DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado      ENUM('PENDIENTE','ENVIADO','ENTREGADO','CANCELADO') DEFAULT 'PENDIENTE',
    total       DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- ------------------------------------------------------------
-- TABLA: detalle_pedido
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS detalle_pedido (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id       BIGINT NOT NULL,
    producto_id     BIGINT NOT NULL,
    cantidad        INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_dp_pedido   FOREIGN KEY (pedido_id)   REFERENCES pedidos(id)   ON DELETE CASCADE,
    CONSTRAINT fk_dp_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- ------------------------------------------------------------
-- TABLA: carritos
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS carritos (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_carrito_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- TABLA: items_carrito
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS items_carrito (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrito_id  BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad    INT NOT NULL DEFAULT 1,
    CONSTRAINT fk_ic_carrito  FOREIGN KEY (carrito_id)  REFERENCES carritos(id)  ON DELETE CASCADE,
    CONSTRAINT fk_ic_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- ============================================================
--  DATOS DE PRUEBA
-- ============================================================

-- Roles
INSERT INTO roles (nombre) VALUES
  ('ROLE_ADMIN'),
  ('ROLE_CLIENTE');

-- Usuarios (passwords cifradas con BCrypt strength=10)
-- admin@uts.edu.co -> password: admin123
-- cliente@uts.edu.co -> password: cliente123
INSERT INTO usuarios (nombre, email, password) VALUES
  ('Administrador UTS',  'admin@uts.edu.co',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
  ('Jaider Jaimes',      'jaider@uts.edu.co',  '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW'),
  ('Maria Lopez',        'maria@gmail.com',    '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW'),
  ('Carlos Ramirez',     'carlos@gmail.com',   '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW');

-- Asignar roles
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES
  (1, 1), -- admin -> ROLE_ADMIN
  (1, 2), -- admin -> ROLE_CLIENTE (doble rol)
  (2, 2), -- jaider -> ROLE_CLIENTE
  (3, 2), -- maria -> ROLE_CLIENTE
  (4, 2); -- carlos -> ROLE_CLIENTE

-- Productos de prueba
INSERT INTO productos (nombre, descripcion, precio, stock, imagen_url) VALUES
  ('Laptop HP 14',        'Laptop HP 14 pulgadas, Intel Core i5, 8GB RAM, 256GB SSD',     2500000.00, 15, '/img/laptop.jpg'),
  ('Mouse Inalambrico',   'Mouse optico inalambrico USB receptor nano, 1600 DPI',           45000.00, 50, '/img/mouse.jpg'),
  ('Teclado Mecanico',    'Teclado mecanico RGB switches azules, USB',                      180000.00, 30, '/img/teclado.jpg'),
  ('Monitor 24 FHD',      'Monitor Full HD 24 pulgadas IPS, 75Hz, HDMI+VGA',               750000.00, 10, '/img/monitor.jpg'),
  ('Auriculares Gaming',  'Auriculares gaming con microfono, sonido 7.1 virtual',           120000.00, 25, '/img/auriculares.jpg'),
  ('Webcam HD 1080p',     'Camara web HD 1080p con microfono integrado, USB',               95000.00, 20, '/img/webcam.jpg');

-- Pedidos de prueba
INSERT INTO pedidos (usuario_id, estado, total) VALUES
  (2, 'ENTREGADO', 2545000.00),
  (3, 'ENVIADO',   300000.00),
  (4, 'PENDIENTE', 45000.00);

-- Detalles de pedido
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario) VALUES
  (1, 1, 1, 2500000.00),
  (1, 2, 1,   45000.00),
  (2, 3, 1,  180000.00),
  (2, 5, 1,  120000.00),
  (3, 2, 1,   45000.00);

-- Carritos
INSERT INTO carritos (usuario_id) VALUES (2), (3), (4);

-- Items en carrito
INSERT INTO items_carrito (carrito_id, producto_id, cantidad) VALUES
  (1, 4, 1),
  (2, 6, 2),
  (3, 3, 1);

-- ============================================================
-- FIN DEL SCRIPT
-- ============================================================
