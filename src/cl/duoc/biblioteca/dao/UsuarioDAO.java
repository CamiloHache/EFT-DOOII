package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DataBaseConnection;
import cl.duoc.biblioteca.model.Usuario;
import java.sql.*;

public class UsuarioDAO {
    public Usuario autenticar(String correo, String contraseña) {
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND contraseña = ?";
        try (Connection con = DataBaseConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, correo);
            stm.setString(2, contraseña);
            ResultSet rs = stm.executeQuery();

            if(rs.next()){
                return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("rut"), rs.getString("correo"), rs.getString("contraseña"), rs.getString("rol"));
            }
        } catch (SQLException e) {
            System.out.println("Error al autenticar usuario: " + e.getMessage());
        }
        return null;
    }
}
