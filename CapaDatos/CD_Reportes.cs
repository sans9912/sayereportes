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

        public List<dynamic> ObtenerTendenciasMensuales()
        {
            List<dynamic> tendencias = new List<dynamic>();
            try
            {
                using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
                {
                    string query = @"
                    SELECT Mes, 
                           SUM(Venta) AS TotalVentas, 
                           SUM(Utilidad) AS TotalUtilidad, 
                           SUM(Unidades) AS TotalUnidades
                    FROM reportes
                    GROUP BY Mes
                    ORDER BY MIN(Fecha) ASC;";

                    SqlCommand cmd = new SqlCommand(query, oconexion);
                    oconexion.Open();
                    using (SqlDataReader dr = cmd.ExecuteReader())
                    {
                        while (dr.Read())
                        {
                            tendencias.Add(new
                            {
                                Mes = dr["Mes"].ToString(),
                                Ventas = Convert.ToDecimal(dr["TotalVentas"]),
                                Utilidad = Convert.ToDecimal(dr["TotalUtilidad"]),
                                Unidades = Convert.ToInt32(dr["TotalUnidades"])
                            });
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                throw new Exception("Error al obtener tendencias mensuales: " + ex.Message);
            }

            return tendencias;
        }

        public string ObtenerProximoMes()
        {
            try
            {
                using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
                {
                    string query = @"
                SELECT TOP 1 
                       DATEADD(MONTH, 1, MAX(Fecha)) AS ProximoMes
                FROM reportes;";

                    SqlCommand cmd = new SqlCommand(query, oconexion);
                    oconexion.Open();

                    object resultado = cmd.ExecuteScalar();
                    if (resultado != null && resultado != DBNull.Value)
                    {
                        DateTime proximoMes = Convert.ToDateTime(resultado);
                        return proximoMes.ToString("MMMM yyyy", new System.Globalization.CultureInfo("es-ES"));
                    }
                }
            }
            catch (Exception ex)
            {
                throw new Exception("Error al obtener el próximo mes: " + ex.Message);
            }

            return string.Empty;
        }

        public List<dynamic> ObtenerTopProductosVendidos()
        {
            List<dynamic> topProductos = new List<dynamic>();
            try
            {
                using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
                {
                    string query = @"
                    SELECT TOP 5 Articulo, SUM(Unidades) AS TotalUnidades
                    FROM reportes
                    GROUP BY Articulo
                    ORDER BY SUM(Unidades) DESC;";

                    SqlCommand cmd = new SqlCommand(query, oconexion);
                    oconexion.Open();
                    using (SqlDataReader dr = cmd.ExecuteReader())
                    {
                        while (dr.Read())
                        {
                            topProductos.Add(new
                            {
                                Articulo = dr["Articulo"].ToString(),
                                Unidades = Convert.ToInt32(dr["TotalUnidades"])
                            });
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                throw new Exception("Error al obtener los productos más vendidos: " + ex.Message);
            }

            return topProductos;
        }

     
        public List<dynamic> ObtenerProductosMayorUtilidad()
        {
            List<dynamic> topUtilidad = new List<dynamic>();
            try
            {
                using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
                {
                    string query = @"
                    SELECT Articulo, SUM(Utilidad) AS TotalUtilidad
                    FROM reportes
                    GROUP BY Articulo
                    ORDER BY SUM(Utilidad) DESC;";

                    SqlCommand cmd = new SqlCommand(query, oconexion);
                    oconexion.Open();
                    using (SqlDataReader dr = cmd.ExecuteReader())
                    {
                        while (dr.Read())
                        {
                            topUtilidad.Add(new
                            {
                                Articulo = dr["Articulo"].ToString(),
                                Utilidad = Convert.ToDecimal(dr["TotalUtilidad"])
                            });
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                throw new Exception("Error al obtener los productos con mayor utilidad: " + ex.Message);
            }

            return topUtilidad;
        }

       
        public List<dynamic> ObtenerMapaCalorVentas()
        {
            List<dynamic> mapaCalor = new List<dynamic>();
            try
            {
                using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
                {
                    string query = @"
                    SELECT Mes, SUM(Venta) AS TotalVentas
                    FROM reportes
                    GROUP BY Mes
                    ORDER BY MIN(Fecha) ASC;";

                    SqlCommand cmd = new SqlCommand(query, oconexion);
                    oconexion.Open();
                    using (SqlDataReader dr = cmd.ExecuteReader())
                    {
                        while (dr.Read())
                        {
                            mapaCalor.Add(new
                            {
                                Mes = dr["Mes"].ToString(),
                                Ventas = Convert.ToDecimal(dr["TotalVentas"])
                            });
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                throw new Exception("Error al generar el mapa de calor: " + ex.Message);
            }

            return mapaCalor;
        }

        public int GuardarReportePDF(string nombreArchivo, byte[] archivoData, out string mensaje)
        {
            int idAutogenerado = 0;
            mensaje = string.Empty;

            try
            {
                using (SqlConnection conexion = new SqlConnection(Conexion.cn))
                {
                    string query = "INSERT INTO ReportesArchivos (NombreArchivo, TipoArchivo, Archivo, FechaRegistro) " +
                                   "VALUES (@NombreArchivo, @TipoArchivo, @Archivo, @FechaRegistro); " +
                                   "SELECT SCOPE_IDENTITY();";

                    SqlCommand cmd = new SqlCommand(query, conexion);
                    cmd.CommandType = CommandType.Text;

                    cmd.Parameters.AddWithValue("@NombreArchivo", nombreArchivo);
                    cmd.Parameters.AddWithValue("@TipoArchivo", "application/pdf");
                    cmd.Parameters.AddWithValue("@Archivo", archivoData);
                    cmd.Parameters.AddWithValue("@FechaRegistro", DateTime.Now);

                    conexion.Open();

                    idAutogenerado = Convert.ToInt32(cmd.ExecuteScalar());
                }
            }
            catch (Exception ex)
            {
                idAutogenerado = 0;
                mensaje = ex.Message;
            }

            return idAutogenerado;
        }


        public int ActualizarReportePDF(string nombreArchivo, byte[] archivoData, out string mensaje)
        {
            int filasAfectadas = 0;
            mensaje = string.Empty;

            try
            {
                using (SqlConnection conexion = new SqlConnection(Conexion.cn))
                {
                    string query = "UPDATE ReportesArchivos SET Archivo = @Archivo, FechaRegistro = @FechaRegistro " +
                                   "WHERE NombreArchivo = @NombreArchivo";

                    SqlCommand cmd = new SqlCommand(query, conexion);
                    cmd.CommandType = CommandType.Text;

                    cmd.Parameters.AddWithValue("@NombreArchivo", nombreArchivo);
                    cmd.Parameters.AddWithValue("@Archivo", archivoData);
                    cmd.Parameters.AddWithValue("@FechaRegistro", DateTime.Now);

                    conexion.Open();

                    filasAfectadas = cmd.ExecuteNonQuery();
                }
            }
            catch (Exception ex)
            {
                mensaje = ex.Message;
            }

            return filasAfectadas;
        }

 
        public bool ExisteArchivo(string nombreArchivo)
        {
            bool existe = false;
            try
            {
                using (SqlConnection conexion = new SqlConnection(Conexion.cn))
                {
                    string query = "SELECT COUNT(*) FROM ReportesArchivos WHERE NombreArchivo = @NombreArchivo";

                    SqlCommand cmd = new SqlCommand(query, conexion);
                    cmd.Parameters.AddWithValue("@NombreArchivo", nombreArchivo);

                    conexion.Open();
                    int count = Convert.ToInt32(cmd.ExecuteScalar());

                    existe = count > 0;
                }
            }
            catch (Exception)
            {
                existe = false;
            }

            return existe;
        }

        public List<ReportePDF> ObtenerReportes()
        {
            List<ReportePDF> reportes = new List<ReportePDF>();

            try
            {
                using (SqlConnection conexion = new SqlConnection(Conexion.cn))  
                {
                    string query = "SELECT Id, NombreArchivo, FechaRegistro FROM ReportesArchivos";
                    SqlCommand cmd = new SqlCommand(query, conexion);

                    conexion.Open();
                    SqlDataReader reader = cmd.ExecuteReader();

                    while (reader.Read())
                    {
                        reportes.Add(new ReportePDF
                        {
                            Id = reader.GetInt32(0),
                            NombreArchivo = reader.GetString(1),
                            FechaRegistro = reader.GetDateTime(2)
                        });
                    }
                }
            }
            catch (Exception ex)
            {
                // Manejo de excepción
            }

            return reportes;
        }


        public ReportePDF ObtenerReportePorId(int id)
        {
            ReportePDF reporte = null;

            try
            {
                using (SqlConnection conexion = new SqlConnection(Conexion.cn))  
                {
                    string query = "SELECT Id, NombreArchivo, Archivo, FechaRegistro FROM ReportesArchivos WHERE Id = @Id";
                    SqlCommand cmd = new SqlCommand(query, conexion);
                    cmd.Parameters.AddWithValue("@Id", id);

                    conexion.Open();
                    SqlDataReader reader = cmd.ExecuteReader();

                    if (reader.Read())
                    {
                        reporte = new ReportePDF
                        {
                            Id = reader.GetInt32(0),
                            NombreArchivo = reader.GetString(1),
                            Archivo = reader["Archivo"] as byte[],  
                            FechaRegistro = reader.GetDateTime(3)
                        };
                    }
                }
            }
            catch (Exception ex)
            {
                
            }

            return reporte;
        }

    }
}