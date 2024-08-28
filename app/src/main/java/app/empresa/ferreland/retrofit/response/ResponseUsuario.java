
package app.empresa.ferreland.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUsuario {

    @SerializedName("IdUsuario")
    @Expose
    private Integer idUsuario;
    @SerializedName("Nombres")
    @Expose
    private String nombres;
    @SerializedName("Apellidos")
    @Expose
    private String apellidos;
    @SerializedName("Correo")
    @Expose
    private String correo;
    @SerializedName("Clave")
    @Expose
    private String clave;
    @SerializedName("Reestablecer")
    @Expose
    private Boolean reestablecer;
    @SerializedName("Activo")
    @Expose
    private Boolean activo;
    @SerializedName("FechaRegistro")
    @Expose
    private String fechaRegistro;
    @SerializedName("Correo_inst")
    @Expose
    private Object correoInst;
    @SerializedName("idRol")
    @Expose
    private Integer idRol;
    @SerializedName("Rol")
    @Expose
    private ResponseRole rol;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Boolean getReestablecer() {
        return reestablecer;
    }

    public void setReestablecer(Boolean reestablecer) {
        this.reestablecer = reestablecer;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Object getCorreoInst() {
        return correoInst;
    }

    public void setCorreoInst(Object correoInst) {
        this.correoInst = correoInst;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public ResponseRole getRol() {
        return rol;
    }

    public void setRol(ResponseRole rol) {
        this.rol = rol;
    }


}
