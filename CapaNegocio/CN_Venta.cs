using CapaDatos;
using CapaEntidad;
using System.Collections.Generic;
using System.Data;
namespace CapaNegocio
{
    public class CN_Venta
    {
        private CD_Venta objCapaDato = new CD_Venta();
        public bool Registrar(Venta obj, DataTable DetalleVenta, out string Mensaje)
        {

            return objCapaDato.Registrar(obj, DetalleVenta, out Mensaje);
        }

        public List<DetalleVenta> ListarCompras(int idcliente)
        {
            return objCapaDato.ListarCompras(idcliente);
        }
    }
}
