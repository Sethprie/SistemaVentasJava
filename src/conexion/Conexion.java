package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ediso
 */
public class Conexion {

    private static String ultimoErrorConexion = null;

    /**
     * Mensaje de la última SQLException al conectar, o null si la última conexión fue exitosa.
     */
    public static String getUltimoErrorConexion() {
        return ultimoErrorConexion;
    }

    //conexion local
    public static Connection conectar() {
        ultimoErrorConexion = null;
        try {
            // Timeouts en ms: evita esperas muy largas si el servidor no responde
            String url = "jdbc:mysql://localhost/bd_sistema_ventas"
                    + "?connectTimeout=8000"
                    + "&socketTimeout=15000";
            Connection cn = DriverManager.getConnection(url, "root", "");
            return cn;
        } catch (SQLException e) {
            ultimoErrorConexion = e.getMessage();
            System.out.println("Error en la conexion local " + e);
        }
        return null;
    }
}
