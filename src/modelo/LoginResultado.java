package modelo;

/**
 * Resultado del intento de inicio de sesión (evita confundir fallo de red con credenciales).
 */
public enum LoginResultado {
    EXITO,
    CREDENCIALES_INCORRECTAS,
    ERROR_CONEXION,
    ERROR_CONSULTA
}
