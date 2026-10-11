
package cl.duoc.biblioteca.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    private static DataBaseConnection instance;

    private static final String URL =
            "jdbc:mysql://localhost:3306/biblioteca_escolar?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private DataBaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "No se encontró el conector de MySQL.", e);
        }
    }

    public static synchronized DataBaseConnection getInstance() {
        if (instance == null) {
            instance = new DataBaseConnection();
        }

        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
