using System;

namespace CapaEntidad
{
    public class Compra
    {
        public int IdCompra { get; set; }
        public decimal precioCompra { get; set; }
        public string precioCompraTexto { get; set; }
        public decimal total { get; set; }
        public string totalTexto { get; set; }
        public Producto oProducto { get; set; }
        public Usuario oUsuario { get; set; }
        public int cantidad { get; set; }
        public bool Activo { get; set; }
        public DateTime FechaRegistro { get; set; }
        public string FechaRegistrotxt { get; set; }
        //public string Base64 { get; set; }
    }
}
