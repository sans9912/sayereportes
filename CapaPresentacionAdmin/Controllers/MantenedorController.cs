using CapaEntidad;
using CapaNegocio;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Data;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using OfficeOpenXml;
using System.Web.Services.Description;
using System.Data.SqlClient;
using System.Text.RegularExpressions;
using System.Configuration;
using System.Net.Http.Headers;
using System.Net.Http;
using DocumentFormat.OpenXml.Drawing;




namespace CapaPresentacionAdmin.Controllers
{
    
    [Authorize]
    public class MantenedorController : Controller
    {
       
       
        //[PermisosRol(CapaEntidad.Rol.JefeDeVentas)]
       
        public ActionResult Subida()
        {
            return View();
        }

        public ActionResult Dashboard()
        {
            return View();
        }

        public async Task<string> ObtenerPredicciones()
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://127.0.0.1:5000/");
                client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("ApiKey", "Lf7nh6iJTYbAqeFChWKYSsM3msyMcaF5aZDQUq67c4jtJcScaQaUnBDvvflh4F6kZK91yFRY5YECjfq4NVf2dJotoobrhjgC8jmy9EkqjXvDYRPeS57s5Vql45LxSZLa");

                HttpResponseMessage response = await client.GetAsync("predicciones");

                if (response.IsSuccessStatusCode)
                {
                    string resultado = await response.Content.ReadAsStringAsync();
                    Console.WriteLine("Respuesta de la API externa:", resultado);  // Mostrar la respuesta aquí
                    return resultado;
                }
                else
                {
                    throw new Exception($"Error: {response.StatusCode} - {response.ReasonPhrase}");
                }
            }
        }

        [HttpGet]
        public async Task<ActionResult> ObtenerPrediccionesDashboard()
        {
            try
            {
                string resultado = await ObtenerPredicciones();
                var predicciones = Newtonsoft.Json.JsonConvert.DeserializeObject<dynamic>(resultado);


                return Json(new
                {
                    Ventas = (double)predicciones.ventas,
                    Utilidad = (double)predicciones.utilidad,
                    Unidades = (double)predicciones.unidades
                }, JsonRequestBehavior.AllowGet);
            }
            catch (Exception ex)
            {
                return new HttpStatusCodeResult(400, ex.Message);
            }
        }

        public DataTable LeerExcel(HttpPostedFileBase archivo)
        {
            var dt = new DataTable();


            ExcelPackage.LicenseContext = LicenseContext.NonCommercial;

            using (var pck = new ExcelPackage())
            {
                using (var stream = archivo.InputStream)
                {
                    pck.Load(stream);
                }

                var ws = pck.Workbook.Worksheets.First();
                bool hasHeader = true;


                foreach (var firstRowCell in ws.Cells[1, 1, 1, ws.Dimension.End.Column])
                {
                    dt.Columns.Add(hasHeader ? firstRowCell.Text : $"Column {firstRowCell.Start.Column}");
                }


                int startRow = hasHeader ? 2 : 1;
                for (int rowNum = startRow; rowNum <= ws.Dimension.End.Row; rowNum++)
                {
                    var wsRow = ws.Cells[rowNum, 1, rowNum, ws.Dimension.End.Column];
                    DataRow row = dt.Rows.Add();
                    foreach (var cell in wsRow)
                    {
                        row[cell.Start.Column - 1] = cell.Text;
                    }
                }
            }

            return dt;
        }

        public async Task<ActionResult> SubirReporte(HttpPostedFileBase fileExcel)
        {
            List<Reportes> lista = new List<Reportes>();
            string mensajeError = string.Empty;
            string mensaje = string.Empty;

            bool validacionRealizada = false;

            if (fileExcel != null)
            {
                string extension = System.IO.Path.GetExtension(fileExcel.FileName);
                if (extension == ".xls" || extension == ".xlsx")
                {
                    string nombreArchivo = System.IO.Path.GetFileNameWithoutExtension(fileExcel.FileName).ToUpper();

                    if (!System.Text.RegularExpressions.Regex.IsMatch(nombreArchivo, @"^[A-Z]+[0-9]{4}$"))
                    {
                        mensajeError = "Nombre de archivo incorrecto. El nombre debe seguir el formato 'MESYYYY'.";
                        return Json(new { resultado = false, mensaje = mensajeError });
                    }

                    string mesTexto = nombreArchivo.Substring(0, nombreArchivo.Length - 4);
                    string anioTexto = nombreArchivo.Substring(nombreArchivo.Length - 4, 4);

                    DateTime fechaMes;
                    try
                    {
                        fechaMes = DateTime.ParseExact($"{mesTexto} {anioTexto}", "MMMM yyyy", new System.Globalization.CultureInfo("es-ES"));
                    }
                    catch
                    {
                        mensajeError = "Nombre de archivo incorrecto. Asegúrate de que el nombre del archivo contiene un mes válido.";
                        return Json(new { resultado = false, mensaje = mensajeError });
                    }

                    string mesAnioTexto = fechaMes.ToString("MMMM yyyy");

                    CN_Reportes capaNegocio = new CN_Reportes();

                    if (!validacionRealizada)
                    {
                        if (capaNegocio.ExisteReporteParaMes(mesAnioTexto, out mensajeError))
                        {
                            return Json(new { resultado = false, mensaje = "Ya existe un reporte de esta fecha" });
                        }

                        if (!string.IsNullOrEmpty(mensajeError))
                        {
                            return Json(new { resultado = false, mensaje = mensajeError });
                        }

                        validacionRealizada = true; 
                    }

                    try
                    {
                        DataTable dt = LeerExcel(fileExcel);

                        if (dt != null)
                        {
                            foreach (DataRow dr in dt.Rows)
                            {
                                if (dr.IsNull("Código") || dr.IsNull("Artículo") ||
                                    string.IsNullOrWhiteSpace(dr["Código"].ToString()) ||
                                    string.IsNullOrWhiteSpace(dr["Artículo"].ToString()) ||
                                    dr["Artículo"].ToString().ToLower().Contains("total"))
                                {
                                    continue;
                                }

                                Reportes reporte = new Reportes();

                                string ventaStr = dr["Venta"].ToString().Replace(",", "");
                                string costoStr = dr["Costo"].ToString().Replace(",", "");
                                string utilidadStr = dr["Utilidad"].ToString().Replace(",", "");

                                reporte.Codigo = dr["Código"].ToString();
                                reporte.Articulo = dr["Artículo"].ToString();
                                reporte.Medida = dr["U. med."].ToString();
                                reporte.Unidades = Convert.ToInt32(dr["Unidades"]);
                                reporte.Venta = Convert.ToDecimal(ventaStr);
                                reporte.Costo = Convert.ToDecimal(costoStr);
                                reporte.Utilidad = Convert.ToDecimal(utilidadStr);

                                reporte.Mes = mesAnioTexto;
                                reporte.Fecha = fechaMes;

                                lista.Add(reporte);
                            }
                        }
                    }
                    catch (Exception)
                    {
                        mensajeError = "Error al leer el archivo: asegúrate de que el formato del archivo es correcto.";
                        return Json(new { resultado = false, mensaje = mensajeError });
                    }

                    if (lista.Count == 0)
                    {
                        mensajeError = "El archivo no contiene datos válidos para subir.";
                        return Json(new { resultado = false, mensaje = mensajeError });
                    }

                    int registrosGuardados = capaNegocio.Registrar(lista, out mensaje);

                    if (registrosGuardados > 0)
                    {
                        FechaController fechaController = new FechaController();
                        DateTime horaLocal = await fechaController.ObtenerFechaHoraAsync();

                        Eventos oEvento = new Eventos
                        {
                            descripcion = $"Subida de reporte ({mesAnioTexto})",
                            idUsuario = (int)(Session["UsuarioId"] as int?),
                            fechaRegistro = horaLocal
                        };

                        string mensajeEvento;
                        new CN_Eventos().Registrar(oEvento, out mensajeEvento);

                        return Json(new { resultado = true, mensaje = $"Se guardaron los registros correctamente." });
                    }
                    else
                    {
                        return Json(new { resultado = false, mensaje = $"Error al guardar los registros: {mensaje}" });
                    }
                }
                else
                {
                    mensajeError = "Formato de archivo no válido. Solo se permiten archivos .xls o .xlsx.";
                    return Json(new { resultado = false, mensaje = mensajeError });
                }
            }
            else
            {
                mensajeError = "No se ha proporcionado ningún archivo.";
                return Json(new { resultado = false, mensaje = mensajeError });
            }
        }

        private int MesANumero(string mes)
        {
            Dictionary<string, int> meses = new Dictionary<string, int>(StringComparer.InvariantCultureIgnoreCase)
    {
        { "ENERO", 1 },
        { "FEBRERO", 2 },
        { "MARZO", 3 },
        { "ABRIL", 4 },
        { "MAYO", 5 },
        { "JUNIO", 6 },
        { "JULIO", 7 },
        { "AGOSTO", 8 },
        { "SEPTIEMBRE", 9 },
        { "OCTUBRE", 10 },
        { "NOVIEMBRE", 11 },
        { "DICIEMBRE", 12 }
    };

            if (meses.ContainsKey(mes))
            {
                return meses[mes];
            }
            throw new ArgumentException("Mes no válido.");
        }

        public ActionResult SinPermiso()
        {
            ViewBag.Message = "Usted no cuenta con permisos para ver esta página";
            return View();
        }

        [HttpPost]
        public ActionResult RevertirReporte()
        {
            try
            {
                CN_Reportes capaNegocio = new CN_Reportes();
                string mensaje;

                bool resultado = capaNegocio.RevertirUltimoMes(out mensaje);

                if (resultado)
                {
                    return Json(new { resultado = true, mensaje });
                }
                else
                {
                    return Json(new { resultado = false, mensaje });
                }
            }
            catch (Exception ex)
            {
                return Json(new { resultado = false, mensaje = $"Ocurrió un error: {ex.Message}" });
            }
        }

        [HttpGet]
        public ActionResult ObtenerReportesSubidos()
        {
            try
            {
                CN_Reportes capaNegocio = new CN_Reportes();
                string mensaje;
                var listaReportes = capaNegocio.ObtenerReportesConReversion(out mensaje);

               
                if (listaReportes != null && listaReportes.Count > 0)
                {
                    return Json(new { resultado = true, listaReportes }, JsonRequestBehavior.AllowGet);
                }
                else
                {
                    return Json(new { resultado = false, mensaje = "No se encontraron reportes." });
                }
            }
            catch (Exception ex)
            {
                return Json(new { resultado = false, mensaje = $"Ocurrió un error: {ex.Message}" });
            }
        }

    }

}