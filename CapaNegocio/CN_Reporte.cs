using CapaDatos;
using CapaEntidad;
using System.Collections.Generic;


namespace CapaNegocio
{
    public class CN_Reporte
    {
        private CD_Reporte objCapaDato = new CD_Reporte();

        public List<Reporte> Ventas(string fechainicio, string fechafin, string idtransaccion)
        {
            return objCapaDato.Ventas(fechainicio, fechafin, idtransaccion);
        }

        public byte[] GenerarInformeUsuariosExcel()
        {
            return objCapaDato.GenerarInformeUsuariosExcel();
        }
        public byte[] GenerarInformeClientesExcel()
        {
            return objCapaDato.GenerarInformeClientesExcel();
        }
        public byte[] GenerarInformeMarcasExcel()
        {
            return objCapaDato.GenerarInformeMarcasExcel();
        }
        public byte[] GenerarInformeCategoriasExcel()
        {
            return objCapaDato.GenerarInformeCategoriasExcel();
        }

        public byte[] GenerarInformeProductosExcel()
        {
            return objCapaDato.GenerarInformeProductosExcel();
        }
        public byte[] GenerarInformeComprasExcel()
        {
            return objCapaDato.GenerarInformeComprasExcel();
        }

        public DashBoard VerDashBoard()
        {
            return objCapaDato.VerDashBoard();
        }
        public List<Top5Productos> top5Productos()
        {
            List<Top5Productos> topProductos = objCapaDato.ObtenerTopProductos();
            return topProductos;
        }
        public decimal VerIndicador(string fechainicio, string fechafin)
        {
            return objCapaDato.Indicador(fechainicio, fechafin);
        }

    }
}
