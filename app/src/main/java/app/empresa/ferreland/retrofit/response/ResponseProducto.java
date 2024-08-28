
package app.empresa.ferreland.retrofit.response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseProducto {

    @SerializedName("IdProducto")
    @Expose
    private Integer idProducto;
    @SerializedName("Nombre")
    @Expose
    private String nombre;
    @SerializedName("Descripcion")
    @Expose
    private String descripcion;
    @SerializedName("Precio")
    @Expose
    private Double precio;
    @SerializedName("Stock")
    @Expose
    private Integer stock;
    @SerializedName("RutaImagen")
    @Expose
    private String rutaImagen;
    @SerializedName("NombreImagen")
    @Expose
    private String nombreImagen;
    @SerializedName("Activo")
    @Expose
    private Boolean activo;
    @SerializedName("FechaRegistro")
    @Expose
    private String fechaRegistro;
    @SerializedName("C\u00f3digo")
    @Expose
    private String cDigo;

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
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

    public String getCDigo() {
        return cDigo;
    }

    public void setCDigo(String cDigo) {
        this.cDigo = cDigo;
    }


}
