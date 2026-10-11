-- ============================================================
-- Biblioteca Escolar
-- Script 01: creación de base de datos y tablas
-- Motor: MySQL 8.x
-- IMPORTANTE: ejecutar en una base nueva o respaldar antes.
-- Este script no elimina ni sobrescribe tablas existentes.
-- ============================================================

CREATE DATABASE IF NOT EXISTS biblioteca_escolar
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE biblioteca_escolar;

-- 1. Categorías (tabla sin dependencias)
CREATE TABLE categorias (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_categorias_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 2. Estudiantes
CREATE TABLE estudiantes (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    rut VARCHAR(12) NOT NULL,
    curso VARCHAR(50) NOT NULL,
    correo VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_estudiantes_rut (rut)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 3. Usuarios del sistema
CREATE TABLE usuarios (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    rut VARCHAR(12) NOT NULL,
    correo VARCHAR(100) NOT NULL,
    `contraseña` VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_usuarios_rut (rut),
    UNIQUE KEY uq_usuarios_correo (correo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 4. Libros (depende de categorías)
CREATE TABLE libros (
    id INT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) NOT NULL,
    editorial VARCHAR(100) NOT NULL,
    stock INT NOT NULL DEFAULT 1,
    id_categoria INT DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_libros_isbn (isbn),
    KEY idx_libros_categoria (id_categoria),
    CONSTRAINT fk_libros_categoria
        FOREIGN KEY (id_categoria) REFERENCES categorias(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    CONSTRAINT chk_libros_stock CHECK (stock >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 5. Préstamos (depende de estudiantes y libros)
CREATE TABLE prestamos (
    id INT NOT NULL AUTO_INCREMENT,
    id_estudiante INT NOT NULL,
    id_libro INT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion DATE NOT NULL,
    devuelto TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_prestamos_estudiante (id_estudiante),
    KEY idx_prestamos_libro (id_libro),
    KEY idx_prestamos_estado_fecha (devuelto, fecha_devolucion),
    CONSTRAINT fk_prestamos_estudiante
        FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_prestamos_libro
        FOREIGN KEY (id_libro) REFERENCES libros(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT chk_prestamos_devuelto CHECK (devuelto IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
