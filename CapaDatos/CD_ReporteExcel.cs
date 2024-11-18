using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CapaEntidad;
using OfficeOpenXml;
using System.Data.SqlClient;

using System.Data;
using System.Globalization;
using System.Runtime.InteropServices.ComTypes;
using OfficeOpenXml.Drawing;
using System.Drawing;
using System.IO;
using static System.Net.Mime.MediaTypeNames;
using System.Web;
using OfficeOpenXml.Style;

namespace CapaDatos
{
    public class CD_ReporteExcel
    {
        public byte[] GenerarInformeEventosExcel()
        {
           
            string query = @"
        SELECT 
            U.Correo AS CorreoUsuario,
            R.nombre AS NombreRol,
            E.fechaRegistro AS FechaRegistro,
            CONVERT(VARCHAR(8), E.fechaRegistro, 108) AS Hora,
            E.descripcion AS Descripcion
        FROM 
            EVENTOS E
        INNER JOIN 
            USUARIO U ON E.idUsuario = U.IdUsuario
        INNER JOIN 
            ROL R ON U.idRol = R.id
        ORDER BY 
            E.fechaRegistro DESC"; 

            using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
            {
                using (SqlCommand command = new SqlCommand(query, oconexion))
                {
                    oconexion.Open();
                    DataTable dataTable = new DataTable();
                    dataTable.Load(command.ExecuteReader());
                    ExcelPackage.LicenseContext = LicenseContext.NonCommercial;
                    using (ExcelPackage package = new ExcelPackage())
                    {
                        ExcelWorksheet worksheet = package.Workbook.Worksheets.Add("Reporte de Eventos");
                        var imagePath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Content", "images", "SayerLogo.png");
                        var logo = worksheet.Drawings.AddPicture("Logo", new FileInfo(imagePath));

                        logo.SetPosition(0, 5, 0, 600);
                        logo.SetSize(50, 50);

                        DateTime currentDate = DateTime.Now;
                        string username = HttpContext.Current.User.Identity.Name;

                        int contentStartRow = 4;
                        int contentStartColumn = 1;

                        // Títulos
                        worksheet.Cells[1, contentStartColumn].Value = $"Reporte generado en: {currentDate:dd/MM/yyyy}";
                        worksheet.Cells[2, contentStartColumn].Value = $"Generado por: {username}";

                        worksheet.Cells[contentStartRow, contentStartColumn].Value = "Correo Usuario";
                        worksheet.Cells[contentStartRow, contentStartColumn + 1].Value = "Nombre Rol";
                        worksheet.Cells[contentStartRow, contentStartColumn + 2].Value = "Fecha Registro";
                        worksheet.Cells[contentStartRow, contentStartColumn + 3].Value = "Hora";
                        worksheet.Cells[contentStartRow, contentStartColumn + 4].Value = "Descripción";

                        using (var range = worksheet.Cells[contentStartRow, contentStartColumn, contentStartRow, contentStartColumn + 4])
                        {
                            range.Style.Font.Bold = true;
                            range.Style.Fill.PatternType = ExcelFillStyle.Solid;
                            range.Style.Fill.BackgroundColor.SetColor(System.Drawing.Color.LightGray);
                            range.Style.HorizontalAlignment = ExcelHorizontalAlignment.Center;
                        }

                        for (int row = 0; row < dataTable.Rows.Count; row++)
                        {
                            worksheet.Cells[row + contentStartRow + 1, contentStartColumn].Value = dataTable.Rows[row]["CorreoUsuario"].ToString();
                            worksheet.Cells[row + contentStartRow + 1, contentStartColumn + 1].Value = dataTable.Rows[row]["NombreRol"].ToString();
                            worksheet.Cells[row + contentStartRow + 1, contentStartColumn + 2].Value = Convert.ToDateTime(dataTable.Rows[row]["FechaRegistro"]).ToString("dd/MM/yyyy");
                            worksheet.Cells[row + contentStartRow + 1, contentStartColumn + 3].Value = dataTable.Rows[row]["Hora"].ToString();
                            worksheet.Cells[row + contentStartRow + 1, contentStartColumn + 4].Value = dataTable.Rows[row]["Descripcion"].ToString();
                        }

                        worksheet.Cells[worksheet.Dimension.Address].AutoFitColumns();
                        worksheet.Cells[contentStartRow, contentStartColumn, contentStartRow + dataTable.Rows.Count, contentStartColumn + 4].Style.Border.BorderAround(ExcelBorderStyle.Thin);

                        return package.GetAsByteArray();
                    }
                }
            }
        }

        public byte[] GenerarInformeUsuariosExcel()
        {

            string query = "SELECT Nombres, Apellidos, Correo, Activo, FechaRegistro FROM USUARIO ORDER BY CONVERT(DATE, FechaRegistro, 105) DESC";

            using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
            {
                using (SqlCommand command = new SqlCommand(query, oconexion))
                {
                    oconexion.Open();
                    DataTable dataTable = new DataTable();
                    dataTable.Load(command.ExecuteReader());
                    ExcelPackage.LicenseContext = LicenseContext.NonCommercial;
                    using (ExcelPackage package = new ExcelPackage())
                    {
                        ExcelWorksheet worksheet = package.Workbook.Worksheets.Add("Reporte de usuarios");
                        var imagePath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Content", "images", "SayerLogo.png");
                        var logo = worksheet.Drawings.AddPicture("Logo", new FileInfo(imagePath));

                       
                        logo.SetPosition(0, 5, 0, 600);  
                        logo.SetSize(50, 50);  

                        DateTime currentDate = DateTime.Now;
                        string username = HttpContext.Current.User.Identity.Name;

                        int contentStartRow = 4;
                        int contentStartColumn = 1;

                        // Títulos
                        worksheet.Cells[1, contentStartColumn].Value = $"Reporte generado en: {currentDate:dd/MM/yyyy}";
                        worksheet.Cells[2, contentStartColumn].Value = $"Generado por: {username}";

                        worksheet.Cells[contentStartRow, contentStartColumn].Value = "Nombres";
                        worksheet.Cells[contentStartRow, contentStartColumn + 1].Value = "Apellidos";
                        worksheet.Cells[contentStartRow, contentStartColumn + 2].Value = "Correo";
                        worksheet.Cells[contentStartRow, contentStartColumn + 3].Value = "Activo";
                        worksheet.Cells[contentStartRow, contentStartColumn + 4].Value = "Fecha de registro";

                        using (var range = worksheet.Cells[contentStartRow, contentStartColumn, contentStartRow, contentStartColumn + 4])
                        {
                            range.Style.Font.Bold = true;
                            range.Style.Fill.PatternType = ExcelFillStyle.Solid;
                            range.Style.Fill.BackgroundColor.SetColor(System.Drawing.Color.LightGray);
                            range.Style.HorizontalAlignment = ExcelHorizontalAlignment.Center;
                        }

                        for (int row = 0; row < dataTable.Rows.Count; row++)
                        {
                            worksheet.Cells[row + contentStartRow + 1, contentStartColumn].Value = dataTable.Rows[row]["Nombres"].ToString();
                            worksheet.Cells[row + contentStartRow + 1, contentStartColumn + 1].Value = dataTable.Rows[row]["Apellidos"].ToString();
                            worksheet.Cells[row + contentStartRow + 1, contentStartColumn + 2].Value = dataTable.Rows[row]["Correo"].ToString();
                            worksheet.Cells[row + contentStartRow + 1, contentStartColumn + 3].Value = dataTable.Rows[row]["Activo"].ToString();
                            worksheet.Cells[row + contentStartRow + 1, contentStartColumn + 4].Value = dataTable.Rows[row]["FechaRegistro"].ToString();
                        }

                        worksheet.Cells[worksheet.Dimension.Address].AutoFitColumns();
                        worksheet.Cells[contentStartRow, contentStartColumn, contentStartRow + dataTable.Rows.Count, contentStartColumn + 4].Style.Border.BorderAround(ExcelBorderStyle.Thin);

                        return package.GetAsByteArray();
                    }
                }
            }
        }

       
       
    }
}
