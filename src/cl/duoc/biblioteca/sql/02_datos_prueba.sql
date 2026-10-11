-- ============================================================
-- Biblioteca Escolar
-- Script 02: datos de prueba
-- Ejecutar después de 01_crear_base_datos.sql
-- Diseñado para una base recién creada.
-- Las contraseñas son de demostración y se almacenan en texto
-- plano porque el DAO actual autentica de esa manera.
-- ============================================================

USE biblioteca_escolar;

INSERT INTO categorias (id, nombre) VALUES
(1, 'Novela'),
(2, 'Ciencia'),
(3, 'Historia');

INSERT INTO estudiantes (id, nombre, rut, curso, correo) VALUES
(1, 'Ana González', '11111111-1', '2° Medio A', 'ana.gonzalez@demo.cl'),
(2, 'Benjamín Soto', '22222222-2', '3° Medio B', 'benjamin.soto@demo.cl'),
(3, 'Camila Rojas', '33333333-3', '1° Medio A', 'camila.rojas@demo.cl');

INSERT INTO usuarios (id, nombre, rut, correo, `contraseña`, rol) VALUES
(1, 'Bibliotecario Demo', '44444444-4', 'bibliotecario@demo.cl', 'Biblio123!', 'bibliotecario'),
(2, 'Estudiante Demo', '55555555-5', 'estudiante@demo.cl', 'Estudiante123!', 'estudiante');

-- Los stocks consideran dos préstamos actualmente no devueltos:
-- el libro 1 tiene un préstamo activo y el libro 2 también.
INSERT INTO libros (id, titulo, autor, isbn, editorial, stock, id_categoria) VALUES
(1, 'Cien años de soledad', 'Gabriel García Márquez', '9780307474728', 'Sudamericana', 3, 1),
(2, 'Breve historia del tiempo', 'Stephen Hawking', '9780553380163', 'Bantam', 4, 2),
(3, 'La historia de Chile', 'Sergio Villalobos', '9789561112345', 'Universitaria', 5, 3);

-- Préstamo 1: vencido hace 7 días, todavía no devuelto.
-- Préstamo 2: vigente, todavía no devuelto.
-- Préstamo 3: historial de un préstamo ya devuelto.
INSERT INTO prestamos
(id, id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto)
VALUES
(1, 1, 1, DATE_SUB(CURDATE(), INTERVAL 14 DAY),
 DATE_SUB(CURDATE(), INTERVAL 7 DAY), 0),
(2, 2, 2, DATE_SUB(CURDATE(), INTERVAL 4 DAY),
 DATE_ADD(CURDATE(), INTERVAL 3 DAY), 0),
(3, 3, 3, DATE_SUB(CURDATE(), INTERVAL 30 DAY),
 DATE_SUB(CURDATE(), INTERVAL 23 DAY), 1);

-- Mantener los AUTO_INCREMENT por encima de los IDs de demostración.
ALTER TABLE categorias AUTO_INCREMENT = 4;
ALTER TABLE estudiantes AUTO_INCREMENT = 4;
ALTER TABLE usuarios AUTO_INCREMENT = 3;
ALTER TABLE libros AUTO_INCREMENT = 4;
ALTER TABLE prestamos AUTO_INCREMENT = 4;
