using CapaDatos;
using CapaEntidad;
using System.Collections.Generic;


namespace CapaNegocio
{
    public class CN_ReporteExcel
    {
        private CD_ReporteExcel objCapaDato = new CD_ReporteExcel();

        

        public byte[] GenerarInformeUsuariosExcel()
        {
            return objCapaDato.GenerarInformeUsuariosExcel();
        }
        public byte[] GenerarInformeEventosExcel()
        {
            return objCapaDato.GenerarInformeEventosExcel();
        }


    }
}
