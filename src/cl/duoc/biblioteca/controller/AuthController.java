package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.dao.UsuarioDAO;
import cl.duoc.biblioteca.model.Usuario;

public class AuthController {
    private UsuarioDAO usuarioDAO;

    public AuthController() {
        usuarioDAO = new UsuarioDAO();
    }

    public Usuario iniciarSesion(String correo, String contrasenia) {
        if (correo == null || correo.trim().isEmpty() || contrasenia == null || contrasenia.trim().isEmpty()) {
            return null;
        }
        return usuarioDAO.autenticar(correo.trim(), contrasenia.trim());
    }
}
