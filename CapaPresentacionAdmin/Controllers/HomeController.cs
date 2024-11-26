using CapaEntidad;
using CapaNegocio;
using CapaPresentacionAdmin.Permisos;
using ClosedXML.Excel;
using System;
using System.Collections.Generic;
using System.Data;
using System.IO;
using System.Linq;
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

        

        [PermisosRol(CapaEntidad.Rol.Administrador)]
        public ActionResult Usuarios()
        {
            return View();
        }

        [PermisosRol(CapaEntidad.Rol.Administrador)]
        public ActionResult Eventos()
        {
            return View();
        }

        [PermisosRol(CapaEntidad.Rol.Administrador)]
        public ActionResult Clientes()
        {
            return View();
        }

        [PermisosRol(CapaEntidad.Rol.Administrador)]
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
        public JsonResult ListarEventos(string fechainicio, string fechafin)
        {

            DateTime fechaInicio = DateTime.ParseExact(fechainicio, "dd/MM/yyyy", null);
            DateTime fechaFin = DateTime.ParseExact(fechafin, "dd/MM/yyyy", null);

            List<EventoDetalle> oLista = new CN_Eventos().ListarDetalle();

            var eventosFiltrados = oLista.Where(e => e.FechaRegistro >= fechaInicio && e.FechaRegistro <= fechaFin).ToList();

            return Json(new { data = eventosFiltrados }, JsonRequestBehavior.AllowGet);
        }

        [HttpGet]
        public JsonResult ListarUsuariosE()
        {


            List<Usuario> oLista = new List<Usuario>();

            oLista = new CN_Usuarios().ListarE();


            return Json(new { data = oLista }, JsonRequestBehavior.AllowGet);

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
        public JsonResult EliminarUsuario(int id)
        {
            bool respuesta = false;
            string mensaje = string.Empty;

            respuesta = new CN_Usuarios().Eliminar(id, out mensaje);

            return Json(new { resultado = respuesta, mensaje = mensaje }, JsonRequestBehavior.AllowGet);
        }

       

        public ActionResult DescargarInformeExcel()
        {
            byte[] informeBytes = new CN_ReporteExcel().GenerarInformeUsuariosExcel();


            Response.Clear();
            Response.ContentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            Response.AddHeader("content-disposition", "attachment;  filename=Usuarios.xlsx");
            Response.BinaryWrite(informeBytes);
            Response.End();

            return new EmptyResult();
        }

        public ActionResult ExportarReporte()
        {
            byte[] informeBytes = new CN_ReporteExcel().GenerarInformeEventosExcel();


            Response.Clear();
            Response.ContentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            Response.AddHeader("content-disposition", "attachment;  filename=Eventos.xlsx");
            Response.BinaryWrite(informeBytes);
            Response.End();

            return new EmptyResult();
        }


    }
}