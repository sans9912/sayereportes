using CapaEntidad;
using CapaNegocio;
using CapaPresentacionAdmin.Permisos;
using ClosedXML.Excel;
using System;
using System.Collections.Generic;
using System.Data;
using System.IO;
using System.Web.Mvc;

namespace CapaPresentacionAdmin.Controllers
{

    [Authorize]
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult Dashboard()
        {
            return View();
        }

        [PermisosRol(CapaEntidad.Rol.Administrador)]
        public ActionResult Usuarios()
        {
            return View();
        }
        [PermisosRol(CapaEntidad.Rol.Administrador)]
        public ActionResult Clientes()
        {
            return View();
        }
        public ActionResult CrearUsuario()
        {
            return View();
        }
        public ActionResult SinPermiso()
        {
            ViewBag.Message = "Usted no cuenta con permisos para ver esta página";
            return View();
        }

        [HttpGet]
        public JsonResult ListarUsuarios()
        {


            List<Usuario> oLista = new List<Usuario>();

            oLista = new CN_Usuarios().Listar();


            return Json(new { data = oLista }, JsonRequestBehavior.AllowGet);

        }
        [HttpGet]
        public JsonResult ListarUsuariosE()
        {


            List<Usuario> oLista = new List<Usuario>();

            oLista = new CN_Usuarios().ListarE();


            return Json(new { data = oLista }, JsonRequestBehavior.AllowGet);

        }
        [HttpGet]
        public JsonResult ListarClientes()
        {


            List<Cliente> oLista = new List<Cliente>();

            oLista = new CN_Cliente().Listar();


            return Json(new { data = oLista }, JsonRequestBehavior.AllowGet);

        }

        public JsonResult ListarCompras()
        {

            List<Compra> oLista = new List<Compra>();

            oLista = new CN_Compra().Listar();


            //return Json(new { data = oLista }, JsonRequestBehavior.AllowGet);
            return Json(oLista, JsonRequestBehavior.AllowGet);

        }

        [HttpPost]
        public JsonResult GuardarUsuario(Usuario objeto)
        {
            object resultado;
            string mensaje = string.Empty;

            if (objeto.IdUsuario == 0)
            {

                resultado = new CN_Usuarios().Registrar(objeto, out mensaje);
            }
            else
            {
                resultado = new CN_Usuarios().Editar(objeto, out mensaje);

            }

            return Json(new { resultado = resultado, mensaje = mensaje }, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        public JsonResult GuardarCliente(Cliente objeto)
        {
            object resultado;
            string mensaje = string.Empty;

            if (objeto.IdCliente == 0)
            {

                resultado = new CN_Cliente().Registrar(objeto, out mensaje);
            }
            else
            {
                resultado = new CN_Cliente().Editar(objeto, out mensaje);

            }

            return Json(new { resultado = resultado, mensaje = mensaje }, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public JsonResult EliminarUsuario(int id)
        {
            bool respuesta = false;
            string mensaje = string.Empty;

            respuesta = new CN_Usuarios().Eliminar(id, out mensaje);

            return Json(new { resultado = respuesta, mensaje = mensaje }, JsonRequestBehavior.AllowGet);
        }





        [HttpGet]
        public JsonResult ListaReporte(string fechainicio, string fechafin, string idtransaccion)
        {
            List<Reporte> oLista = new List<Reporte>();

            oLista = new CN_Reporte().Ventas(fechainicio, fechafin, idtransaccion);

            return Json(new { data = oLista }, JsonRequestBehavior.AllowGet);
        }

        [HttpGet]
        public JsonResult ListarTopProductos()
        {

            List<Top5Productos> oLista = new List<Top5Productos>();
            oLista = new CN_Reporte().top5Productos();
            return Json(new { data = oLista }, JsonRequestBehavior.AllowGet);

        }



        [HttpGet]
        public JsonResult VistaDashBoard()
        {
            DashBoard objeto = new CN_Reporte().VerDashBoard();

            return Json(new { resultado = objeto }, JsonRequestBehavior.AllowGet);
        }

        [HttpGet]
        public JsonResult VistaIndicador(string fechainicio, string fechafin)
        {
            decimal objeto = new CN_Reporte().VerIndicador(fechainicio, fechafin);

            return Json(new { resultado = objeto }, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public FileResult ExportarVenta(string fechainicio, string fechafin, string idtransaccion)
        {

            List<Reporte> oLista = new List<Reporte>();
            oLista = new CN_Reporte().Ventas(fechainicio, fechafin, idtransaccion);

            DataTable dt = new DataTable();

            dt.Locale = new System.Globalization.CultureInfo("es-PE");
            dt.Columns.Add("Fecha Venta", typeof(string));
            dt.Columns.Add("Cliente", typeof(string));
            dt.Columns.Add("Producto", typeof(string));
            dt.Columns.Add("Precio", typeof(decimal));
            dt.Columns.Add("Cantidad", typeof(int));
            dt.Columns.Add("Total", typeof(decimal));
            dt.Columns.Add("IdTransaccion", typeof(string));


            foreach (Reporte rp in oLista)
            {
                dt.Rows.Add(new object[] {
                    rp.FechaVenta,
                    rp.Cliente,
                    rp.Producto,
                    rp.Precio,
                    rp.Cantidad,
                    rp.Total,
                    rp.IdTransaccion
                });

            }

            dt.TableName = "Datos";

            using (XLWorkbook wb = new XLWorkbook())
            {

                wb.Worksheets.Add(dt);
                using (MemoryStream stream = new MemoryStream())
                {
                    wb.SaveAs(stream);
                    return File(stream.ToArray(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "ReporteVenta" + DateTime.Now.ToString() + ".xlsx");

                }
            }



        }

        public ActionResult DescargarInformeExcel()
        {
            byte[] informeBytes = new CN_Reporte().GenerarInformeUsuariosExcel();


            Response.Clear();
            Response.ContentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            Response.AddHeader("content-disposition", "attachment;  filename=Usuarios.xlsx");
            Response.BinaryWrite(informeBytes);
            Response.End();

            return new EmptyResult();
        }

        public ActionResult DescargarInformeExcelClientes()
        {
            byte[] informeBytes = new CN_Reporte().GenerarInformeClientesExcel();


            Response.Clear();
            Response.ContentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            Response.AddHeader("content-disposition", "attachment;  filename=Clientes.xlsx");
            Response.BinaryWrite(informeBytes);
            Response.End();

            return new EmptyResult();
        }


    }
}