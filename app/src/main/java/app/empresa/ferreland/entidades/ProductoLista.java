package app.empresa.ferreland.entidades;

public class ProductoLista {
    private int Stock;
    private int Precio;
    private int id;
    private String nombre;

    public ProductoLista(int stock, int precio, int id, String nombre) {
        Stock = stock;
        Precio = precio;
        this.id = id;
        this.nombre = nombre;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    public int getPrecio() {
        return Precio;
    }

    public void setPrecio(int precio) {
        Precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
