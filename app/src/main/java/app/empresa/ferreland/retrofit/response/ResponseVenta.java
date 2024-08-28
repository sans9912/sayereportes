
package app.empresa.ferreland.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseVenta {
    @SerializedName("IdVenta")
    @Expose
    private Integer idVenta;
    @SerializedName("TotalProducto")
    @Expose
    private Integer totalProducto;
    @SerializedName("MontoTotal")
    @Expose
    private Double montoTotal;
    @SerializedName("Contacto")
    @Expose
    private String contacto;
    @SerializedName("IdDistrito")
    @Expose
    private String idDistrito;
    @SerializedName("Telefono")
    @Expose
    private String telefono;
    @SerializedName("Direccion")
    @Expose
    private String direccion;
    @SerializedName("IdTransaccion")
    @Expose
    private String idTransaccion;
    @SerializedName("FechaVenta")
    @Expose
    private String fechaVenta;
    @SerializedName("ConfirmarReserva")
    @Expose
    private Boolean confirmarReserva;
    @SerializedName("IdCliente")
    @Expose
    private Integer idCliente;
    @SerializedName("Cliente")
    @Expose
    private ResponseCliente cliente;
    @SerializedName("Detalle_Venta")
    @Expose
    private List<ResponseDetalleVenta> detalleVenta;

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public Integer getTotalProducto() {
        return totalProducto;
    }

    public void setTotalProducto(Integer totalProducto) {
        this.totalProducto = totalProducto;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getIdDistrito() {
        return idDistrito;
    }

    public void setIdDistrito(String idDistrito) {
        this.idDistrito = idDistrito;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Boolean getConfirmarReserva() {
        return confirmarReserva;
    }

    public void setConfirmarReserva(Boolean confirmarReserva) {
        this.confirmarReserva = confirmarReserva;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public ResponseCliente getCliente() {
        return cliente;
    }

    public void setCliente(ResponseCliente cliente) {
        this.cliente = cliente;
    }

    public List<ResponseDetalleVenta> getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(List<ResponseDetalleVenta> detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

}
