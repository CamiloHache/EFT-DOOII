package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DataBaseConnection;
import cl.duoc.biblioteca.model.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    public List<Libro> listarTodos() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros";

        try (Connection con = DataBaseConnection.getInstance().getConnection();
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                libros.add(new Libro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("isbn"),
                        rs.getString("editorial"),
                        rs.getInt("stock"),
                        rs.getInt("id_categoria")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar libros: " + e.getMessage());
        }
        return libros;
    }
}