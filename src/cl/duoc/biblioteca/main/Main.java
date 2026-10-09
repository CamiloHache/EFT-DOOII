package cl.duoc.biblioteca.main;

import cl.duoc.biblioteca.controller.AuthController;
import cl.duoc.biblioteca.dao.LibroDAO;
import cl.duoc.biblioteca.model.Libro;
import cl.duoc.biblioteca.model.Usuario;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== COMPROBANDO BLOQUE 1 Y BLOQUE 2 =====");

        AuthController auth = new AuthController();
        Usuario user = auth.iniciarSesion("admin@biblioteca.cl", "admin123");

        if(user != null) {
            System.out.println("Usuario " + user.getNombre() + " autenticado correctamente, (" + user.getRol() + ")");
        } else {
            System.out.println("ERROR: Usuario no ha sido autenticado");
        }

        LibroDAO libroDAO = new LibroDAO();
        System.out.println("===== LISTA DE LIBROS EN BD =====");
        for (Libro l : libroDAO.listarTodos()) {
            System.out.println("· " + l.getTitulo() + " - Stock: " + l.getStock());
        }
    }
}
