
package app.empresa.ferreland.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestProduct {

    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("partnumber")
    @Expose
    private String partnumber;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("stock")
    @Expose
    private Integer stock;
    @SerializedName("precio")
    @Expose
    private Integer precio;
    @SerializedName("lote")
    @Expose
    private String lote;
    @SerializedName("marca")
    @Expose
    private String marca;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("categoria")
    @Expose
    private String categoria;
    @SerializedName("proveedor")
    @Expose
    private String proveedor;
    @SerializedName("usuario")
    @Expose
    private String usuario;

    /**
     * No args constructor for use in serialization
     *
     */
    public RequestProduct() {
    }

    /**
     *
     * @param descripcion
     * @param marca
     * @param precio
     * @param img
     * @param lote
     * @param categoria
     * @param proveedor
     * @param usuario
     * @param stock
     * @param nombre
     * @param partnumber
     */
    public RequestProduct(String nombre, String partnumber, String descripcion, Integer stock, Integer precio, String lote, String marca, String img, String categoria, String proveedor, String usuario) {
        super();
        this.nombre = nombre;
        this.partnumber = partnumber;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.lote = lote;
        this.marca = marca;
        this.img = img;
        this.categoria = categoria;
        this.proveedor = proveedor;
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public void setPartnumber(String partnumber) {
        this.partnumber = partnumber;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
