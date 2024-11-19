using CapaEntidad;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CapaDatos
{
    public class CD_Reportes
    {
        public bool ValidarMesReporte(string mes, out string mensaje)
        {
            mensaje = string.Empty;

            try
            {
                using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
                {
                    string query = "SELECT COUNT(*) FROM reportes WHERE Mes = @Mes";
                    using (SqlCommand cmd = new SqlCommand(query, oconexion))
                    {
                        cmd.Parameters.Add("@Mes", SqlDbType.NVarChar).Value = mes;

                        oconexion.Open();
                        int count = Convert.ToInt32(cmd.ExecuteScalar());
                        return count > 0;
                    }
                }
            }
            catch (SqlException sqlEx)
            {
                mensaje = $"Error de base de datos: {sqlEx.Message}";
                return false; 
            }
            catch (Exception ex)
            {
                mensaje = $"Error general: {ex.Message}";
                return false;
            }
        }



        public int Registrar(Reportes obj, out string Mensaje)
        {
            int idautogenerado = 1;
            Mensaje = string.Empty;

            try
            {
                using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
                {

                    string query = "INSERT INTO reportes(Codigo, Articulo, Medida, Unidades, Venta, Costo, Utilidad, Mes, Fecha) " +
                                   "VALUES(@Codigo, @Articulo, @Medida, @Unidades, @Venta, @Costo, @Utilidad, @Mes, @Fecha); " +
                                   "SELECT SCOPE_IDENTITY();";

                    SqlCommand cmd = new SqlCommand(query, oconexion);
                    cmd.CommandType = CommandType.Text;

                    cmd.Parameters.AddWithValue("@Codigo", obj.Codigo);
                    cmd.Parameters.AddWithValue("@Articulo", obj.Articulo);
                    cmd.Parameters.AddWithValue("@Medida", obj.Medida);
                    cmd.Parameters.AddWithValue("@Unidades", obj.Unidades);
                    cmd.Parameters.AddWithValue("@Venta", obj.Venta);
                    cmd.Parameters.AddWithValue("@Costo", obj.Costo);
                    cmd.Parameters.AddWithValue("@Utilidad", obj.Utilidad);
                    cmd.Parameters.AddWithValue("@Mes", obj.Mes);
                    cmd.Parameters.AddWithValue("@Fecha", obj.Fecha);

                    oconexion.Open();

                    idautogenerado = Convert.ToInt32(cmd.ExecuteScalar());
                }
            }
            catch (Exception ex)
            {
                idautogenerado = 0;
                Mensaje = ex.Message;
            }

            return idautogenerado;
        }

        public bool RevertirReporteMesAnterior(out string mensaje)
        {
            mensaje = string.Empty;
            bool resultado = false;

            try
            {
                using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
                {
                    DateTime mesActual = DateTime.Now;
                    DateTime mesAnterior = mesActual.AddMonths(-1);
                    string mesAnteriorFormato = mesAnterior.ToString("MMMM yyyy", new System.Globalization.CultureInfo("es-ES"));

                    string queryEliminar = @"
                DELETE FROM reportes
                WHERE Mes = @MesAnterior";

                    oconexion.Open();

                    using (SqlCommand cmd = new SqlCommand(queryEliminar, oconexion))
                    {
                        cmd.Parameters.AddWithValue("@MesAnterior", mesAnteriorFormato);
                        int filasAfectadas = cmd.ExecuteNonQuery();
                        resultado = filasAfectadas > 0;

                        if (!resultado)
                        {
                            mensaje = "No se encontró un reporte para el mes anterior.";
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                mensaje = ex.Message;
            }

            return resultado;
        }

        public List<ReporteConReversion> ObtenerReportesSubidos(out string mensaje)
        {
            mensaje = string.Empty;
            List<ReporteConReversion> listaReportesConReversion = new List<ReporteConReversion>();

            try
            {
                using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
                {
                    string query = @"
                SELECT DISTINCT Mes, MAX(Fecha) AS Fecha
                FROM reportes
                WHERE Mes IS NOT NULL  -- Asegurarse de que no se seleccionen valores nulos para el mes
                GROUP BY Mes
                ORDER BY MAX(Fecha) DESC";

                    SqlCommand cmd = new SqlCommand(query, oconexion);
                    cmd.CommandType = CommandType.Text;

                    oconexion.Open();
                    using (SqlDataReader dr = cmd.ExecuteReader())
                    {
                        while (dr.Read())
                        {
                            var reporte = new ReporteConReversion
                            {
                                Mes = dr["Mes"].ToString(),
                                Fecha = Convert.ToDateTime(dr["Fecha"]),
                                PermitirReversion = false
                            };

                            string mesActual = DateTime.Now.ToString("MMMM yyyy");
                            string mesAnterior = DateTime.Now.AddMonths(-1).ToString("MMMM yyyy");

                            if (reporte.Mes.Equals(mesAnterior, StringComparison.OrdinalIgnoreCase))
                            {
                                reporte.PermitirReversion = true;
                            }

                            listaReportesConReversion.Add(reporte);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                mensaje = ex.Message;
            }

            return listaReportesConReversion;
        }
    }
}