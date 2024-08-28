
package app.empresa.ferreland.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDetalleVenta {

    @SerializedName("IdCliente")
    @Expose
    private Integer idCliente;
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
    @SerializedName("FechaRegistro")
    @Expose
    private String fechaRegistro;

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
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

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

}
