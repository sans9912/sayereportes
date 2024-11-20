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

        public List<dynamic> ObtenerTendenciasMensuales(out string mensaje)
        {
            mensaje = string.Empty;
            try
            {
                var tendencias = objCapaDato.ObtenerTendenciasMensuales();
                if (tendencias == null || tendencias.Count == 0)
                {
                    mensaje = "No se encontraron datos de tendencias mensuales.";
                    return null;
                }
                return tendencias;
            }
            catch (Exception ex)
            {
                mensaje = "Ocurrió un error al obtener las tendencias mensuales: " + ex.Message;
                return null;
            }
        }

       
        public List<dynamic> ObtenerTopProductosVendidos(out string mensaje)
        {
            mensaje = string.Empty;
            try
            {
                var topProductos = objCapaDato.ObtenerTopProductosVendidos();
                if (topProductos == null || topProductos.Count == 0)
                {
                    mensaje = "No se encontraron datos de los productos más vendidos.";
                    return null;
                }
                return topProductos;
            }
            catch (Exception ex)
            {
                mensaje = "Ocurrió un error al obtener los productos más vendidos: " + ex.Message;
                return null;
            }
        }

      
        public List<dynamic> ObtenerProductosMayorUtilidad(out string mensaje)
        {
            mensaje = string.Empty;
            try
            {
                var topUtilidad = objCapaDato.ObtenerProductosMayorUtilidad();
                if (topUtilidad == null || topUtilidad.Count == 0)
                {
                    mensaje = "No se encontraron datos de los productos con mayor utilidad.";
                    return null;
                }
                return topUtilidad;
            }
            catch (Exception ex)
            {
                mensaje = "Ocurrió un error al obtener los productos con mayor utilidad: " + ex.Message;
                return null;
            }
        }

    
        public List<dynamic> ObtenerMapaCalorVentas(out string mensaje)
        {
            mensaje = string.Empty;
            try
            {
                var mapaCalor = objCapaDato.ObtenerMapaCalorVentas();
                if (mapaCalor == null || mapaCalor.Count == 0)
                {
                    mensaje = "No se encontraron datos para generar el mapa de calor.";
                    return null;
                }
                return mapaCalor;
            }
            catch (Exception ex)
            {
                mensaje = "Ocurrió un error al obtener el mapa de calor de ventas: " + ex.Message;
                return null;
            }
        }


    }

}

