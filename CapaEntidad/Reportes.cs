using System;

namespace CapaEntidad
{
    public class Reportes
    {
        public string Codigo { get; set; }
        public string Articulo { get; set; }
        public string Medida { get; set; }
        public int Unidades { get; set; }
        public decimal Venta { get; set; }
        public decimal Costo { get; set; }
        public decimal Utilidad { get; set; }
        public DateTime Fecha { get; set; } 
        public string Mes { get; set; } 
    }

    public class ReporteConReversion
    {
        public DateTime Fecha { get; set; }
        public string Mes { get; set; }
        public bool PermitirReversion { get; set; } 
    }
}
