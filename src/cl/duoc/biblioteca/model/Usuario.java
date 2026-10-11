package cl.duoc.biblioteca.model;

public class Usuario extends Persona {

    private String contrasenia;
    private String rol;

    public Usuario() {
        super(0, "", "", "");
    }

    public Usuario(int id, String nombre, String rut, String correo,
                   String contrasenia, String rol) {
        super(id, nombre, rut, correo);
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String getTipoPersona() {
        return "Usuario";
    }
}