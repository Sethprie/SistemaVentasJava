package controlador;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import modelo.LoginResultado;
import modelo.Usuario;

/**
 *
 * @author Edison Zambrano - © Programador Fantasma
 */
public class Ctrl_Usuario {

    /** Mensaje del último error SQL en login (consulta), para mostrarlo en la vista. */
    private static String ultimoErrorLoginConsulta = null;

    public static String getUltimoErrorLoginConsulta() {
        return ultimoErrorLoginConsulta;
    }

    /**
     * **************************************************
     * metodo para guardar un nuevo usuario
     * **************************************************
     */
    public boolean guardar(Usuario objeto) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        if (cn == null) {
            return false;
        }
        try {
            PreparedStatement consulta = cn.prepareStatement("insert into tb_usuario values(?,?,?,?,?,?,?,?)");
            consulta.setInt(1, 0);//id
            consulta.setString(2, objeto.getNombre());
            consulta.setString(3, objeto.getApellido());
            consulta.setString(4, objeto.getUsuario());
            consulta.setString(5, objeto.getPassword());
            consulta.setString(6, objeto.getTelefono());
            consulta.setInt(7, objeto.getEstado());
            consulta.setString(8, objeto.getRol());
            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al guardar usuario: " + e);
        }
        return respuesta;
    }

    /**
     * ********************************************************************
     * metodo para consultar si el usuario ya esta registrado en la BBDD
     * ********************************************************************
     */
    public boolean existeUsuario(String usuario) {
        boolean respuesta = false;
        String sql = "select usuario from tb_usuario where usuario = '" + usuario + "';";
        Connection cn = Conexion.conectar();
        if (cn == null) {
            return false;
        }
        try (Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                respuesta = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar usuario: " + e);
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e);
            }
        }
        return respuesta;
    }

    /**
     * **************************************************
     * metodo para Iniciar Sesion (sin diálogos; la vista muestra mensajes en el hilo EDT)
     * **************************************************
     */
    public LoginResultado loginUser(Usuario objeto) {
        ultimoErrorLoginConsulta = null;
        Connection cn = Conexion.conectar();
        if (cn == null) {
            return LoginResultado.ERROR_CONEXION;
        }
        String sql = "select usuario, password, rol from tb_usuario where usuario = '" + objeto.getUsuario() + "' and password = '" + objeto.getPassword() + "'";
        try (Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                objeto.setRol(rs.getString("rol"));
                return LoginResultado.EXITO;
            }
            return LoginResultado.CREDENCIALES_INCORRECTAS;
        } catch (SQLException e) {
            System.out.println("Error al Iniciar Sesion: " + e);
            ultimoErrorLoginConsulta = e.getMessage();
            return LoginResultado.ERROR_CONSULTA;
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión tras login: " + e);
            }
        }
    }

    /**
     * **************************************************
     * metodo para actualizar un usuario
     * **************************************************
     */
    public boolean actualizar(Usuario objeto, int idUsuario) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        if (cn == null) {
            return false;
        }
        try {
            PreparedStatement consulta = cn.prepareStatement("update tb_usuario set nombre=?, apellido = ?, usuario = ?, password= ?, telefono = ?, estado = ?, rol = ? where idUsuario ='" + idUsuario + "'");
            consulta.setString(1, objeto.getNombre());
            consulta.setString(2, objeto.getApellido());
            consulta.setString(3, objeto.getUsuario());
            consulta.setString(4, objeto.getPassword());
            consulta.setString(5, objeto.getTelefono());
            consulta.setInt(6, objeto.getEstado());
            consulta.setString(7, objeto.getRol());

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e);
        }
        return respuesta;
    }

    /**
     * **************************************************
     * metodo para eliminar un usuario
     * **************************************************
     */
    public boolean eliminar(int idUsuario) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        if (cn == null) {
            return false;
        }
        try {
            PreparedStatement consulta = cn.prepareStatement(
                    "delete from tb_usuario where idUsuario ='" + idUsuario + "'");

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e);
        }
        return respuesta;
    }
}