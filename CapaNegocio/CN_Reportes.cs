using CapaDatos;
using CapaEntidad;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CapaNegocio
{
    public class CN_Reportes
    {
        private CD_Reportes objCapaDato = new CD_Reportes();

        public bool ExisteReporteParaMes(string mes, out string mensaje)
        {
            return objCapaDato.ValidarMesReporte(mes, out mensaje);
        }

        public int Registrar(List<Reportes> lista, out string Mensaje)
        {
            Mensaje = string.Empty;
            int registrosExitosos = 0;

            foreach (var obj in lista)
            {
                var resultado = objCapaDato.Registrar(obj, out Mensaje);
                if (resultado > 0)
                {
                    registrosExitosos++;
                }
                else
                {
                    continue;
                }
            }

            return registrosExitosos;
        }

        public bool RevertirUltimoMes(out string mensaje)
        {
            mensaje = string.Empty;

            try
            {
                CD_Reportes objCapaDato = new CD_Reportes();
                bool exito = objCapaDato.RevertirReporteMesAnterior(out mensaje);

                if (exito)
                {
                    mensaje = "Los datos del mes más reciente fueron revertidos correctamente.";
                    return true;
                }
                else if (string.IsNullOrEmpty(mensaje))
                {
                    mensaje = "No se encontraron registros para revertir.";
                }

                return false;
            }
            catch (Exception ex)
            {
                mensaje = "Ocurrió un error: " + ex.Message;
                return false;
            }
        }

        public List<ReporteConReversion> ObtenerReportesConReversion(out string mensaje)
        {
            mensaje = string.Empty;
            List<ReporteConReversion> reportesConReversion = new List<ReporteConReversion>();

            try
            {

                List<ReporteConReversion> reportes = objCapaDato.ObtenerReportesSubidos(out mensaje);

     
                if (reportes == null || reportes.Count == 0)
                {
                    mensaje = "No se encontraron reportes.";
                    return null;
                }

                string mesActual = DateTime.Now.ToString("MMMM yyyy");
                string mesAnterior = DateTime.Now.AddMonths(-1).ToString("MMMM yyyy");

                foreach (var reporte in reportes)
                {
                    var reporteConReversion = new ReporteConReversion
                    {
                        Fecha = reporte.Fecha,
                        Mes = reporte.Mes,
                        PermitirReversion = reporte.Mes == mesAnterior 
                    };

                    reportesConReversion.Add(reporteConReversion);
                }
            }
            catch (Exception ex)
            {
                mensaje = "Ocurrió un error: " + ex.Message;
                return null;
            }

            return reportesConReversion;
        }
    }

}

