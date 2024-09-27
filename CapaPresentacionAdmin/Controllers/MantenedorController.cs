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


namespace CapaPresentacionAdmin.Controllers
{
    [Authorize]
    public class MantenedorController : Controller
    {
        private CN_FireBase cnfirebase = new CN_FireBase();

        // GET: Mantenedor
        public ActionResult Categoria()
        {
            return View();
        }
        public ActionResult Marca()
        {
            return View();
        }
        //[PermisosRol(CapaEntidad.Rol.JefeDeVentas)]
        public ActionResult Producto()
        {
            return View();
        }
        public ActionResult Subida()
        {
            return View();
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

        public ActionResult SubirReporte(HttpPostedFileBase fileExcel)
        {
            List<Reportes> lista = new List<Reportes>();
            string mensajeError = string.Empty;
            string mensaje = string.Empty;

            if (fileExcel != null)
            {
                string extension = Path.GetExtension(fileExcel.FileName);
                if (extension == ".xls" || extension == ".xlsx")
                {
                    try
                    {
                        DataTable dt = LeerExcel(fileExcel);

                        if (dt != null)
                        {
                            foreach (DataRow dr in dt.Rows)
                            {
                                if (dr["Código"] == DBNull.Value || dr["Artículo"] == DBNull.Value ||
                                    dr["U. med."] == DBNull.Value || dr["Unidades"] == DBNull.Value ||
                                    dr["Venta"] == DBNull.Value || dr["Costo"] == DBNull.Value ||
                                    dr["Utilidad"] == DBNull.Value)
                                {
                                    mensajeError = "Formato de filas incorrecto: una o más filas tienen campos vacíos o incorrectos.";
                                    return Json(new { resultado = false, mensaje = mensajeError });
                                }

                                try
                                {
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

                                    lista.Add(reporte);
                                }
                                catch (Exception ex)
                                {
                                    mensajeError = "Formato de filas incorrecto: error al convertir los datos.";
                                    return Json(new { resultado = false, mensaje = mensajeError });
                                }
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        mensajeError = "Error al leer el archivo: asegúrate de que el formato del archivo es correcto.";
                        return Json(new { resultado = false, mensaje = mensajeError });
                    }



                    return Json(new { resultado = true, mensaje = $"Se guardaron los registros correctamente." });

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


        public ActionResult SinPermiso()
        {
            ViewBag.Message = "Usted no cuenta con permisos para ver esta página";
            return View();
        }
       
       


    }

}