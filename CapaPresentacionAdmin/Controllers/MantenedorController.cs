using CapaEntidad;
using CapaNegocio;
using Newtonsoft.Json;
using System.Collections.Generic;
using System.Data;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;


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
        public ActionResult Compra()
        {
            return View();
        }
        public ActionResult DescargarInformeExcelMarcas()
        {
            byte[] informeBytes = new CN_Reporte().GenerarInformeMarcasExcel();


            Response.Clear();
            Response.ContentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            Response.AddHeader("content-disposition", "attachment;  filename=Marcas.xlsx");
            Response.BinaryWrite(informeBytes);
            Response.End();

            return new EmptyResult();
        }
        public ActionResult DescargarInformeExcelCategorias()
        {
            byte[] informeBytes = new CN_Reporte().GenerarInformeCategoriasExcel();


            Response.Clear();
            Response.ContentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            Response.AddHeader("content-disposition", "attachment;  filename=Categorias.xlsx");
            Response.BinaryWrite(informeBytes);
            Response.End();

            return new EmptyResult();
        }
        public ActionResult DescargarInformeExcelProductos()
        {
            byte[] informeBytes = new CN_Reporte().GenerarInformeProductosExcel();


            Response.Clear();
            Response.ContentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            Response.AddHeader("content-disposition", "attachment;  filename=Productos.xlsx");
            Response.BinaryWrite(informeBytes);
            Response.End();

            return new EmptyResult();
        }
        public ActionResult DescargarInformeExcelCompras()
        {
            byte[] informeBytes = new CN_Reporte().GenerarInformeComprasExcel();


            Response.Clear();
            Response.ContentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            Response.AddHeader("content-disposition", "attachment;  filename=Compras.xlsx");
            Response.BinaryWrite(informeBytes);
            Response.End();

            return new EmptyResult();
        }

        public ActionResult SinPermiso()
        {
            ViewBag.Message = "Usted no cuenta con permisos para ver esta página";
            return View();
        }
        // ++++++++++++++++ CATEGORIA ++++++++++++++++++++

        #region CATEGORIA
        [HttpGet]
        public JsonResult ListarCategorias()
        {

            List<Categoria> oLista = new List<Categoria>();
            oLista = new CN_Categoria().Listar();
            return Json(new { data = oLista }, JsonRequestBehavior.AllowGet);

        }


        [HttpPost]
        public JsonResult GuardarCategoria(Categoria objeto)
        {
            object resultado;
            string mensaje = string.Empty;

            if (objeto.IdCategoria == 0)
            {

                resultado = new CN_Categoria().Registrar(objeto, out mensaje);
            }
            else
            {
                resultado = new CN_Categoria().Editar(objeto, out mensaje);

            }

            return Json(new { resultado = resultado, mensaje = mensaje }, JsonRequestBehavior.AllowGet);
        }



        [HttpPost]
        public JsonResult EliminarCategoria(int id)
        {
            bool respuesta = false;
            string mensaje = string.Empty;

            respuesta = new CN_Categoria().Eliminar(id, out mensaje);

            return Json(new { resultado = respuesta, mensaje = mensaje }, JsonRequestBehavior.AllowGet);
        }

        #endregion

        // ++++++++++++++++ MARCA ++++++++++++++++++++

        #region MARCA
        [HttpGet]
        public JsonResult ListarMarca()
        {
            List<Marca> oLista = new List<Marca>();
            oLista = new CN_Marca().Listar();
            return Json(new { data = oLista }, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public JsonResult GuardarMarca(Marca objeto)
        {
            object resultado;
            string mensaje = string.Empty;

            if (objeto.IdMarca == 0)
            {
                resultado = new CN_Marca().Registrar(objeto, out mensaje);
            }
            else
            {
                resultado = new CN_Marca().Editar(objeto, out mensaje);
            }

            return Json(new { resultado = resultado, mensaje = mensaje }, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public JsonResult EliminarMarca(int id)
        {
            bool respuesta = false;
            string mensaje = string.Empty;

            respuesta = new CN_Marca().Eliminar(id, out mensaje);

            return Json(new { resultado = respuesta, mensaje = mensaje }, JsonRequestBehavior.AllowGet);
        }
        #endregion


        // ++++++++++++++++ PRODUCTO ++++++++++++++++++++
        #region PRODUCTO
        [HttpGet]
        public JsonResult ListarProducto()
        {
            List<Producto> oLista = new List<Producto>();
            oLista = new CN_Producto().Listar();
            return Json(new { data = oLista }, JsonRequestBehavior.AllowGet);
        }



        [HttpPost]
        public async Task<JsonResult> GuardarProducto(string objeto, HttpPostedFileBase archivoImagen)
        //public JsonResult GuardarProducto(string objeto, HttpPostedFileBase archivoImagen)
        {

            string mensaje = string.Empty;
            bool operacion_exitosa = true;
            bool guardar_imagen_exito = true;

            Producto oProducto = new Producto();
            oProducto = JsonConvert.DeserializeObject<Producto>(objeto);

            decimal precio;

            if (decimal.TryParse(oProducto.PrecioTexto, NumberStyles.AllowDecimalPoint, new CultureInfo("es-PE"), out precio))
            {

                oProducto.Precio = precio;
            }
            else
            {

                return Json(new { operacionExitosa = false, mensaje = "El formato del precio debe ser ##.##" }, JsonRequestBehavior.AllowGet);
            }


            if (oProducto.IdProducto == 0)
            {
                int idProductoGenerado = new CN_Producto().Registrar(oProducto, out mensaje);

                if (idProductoGenerado != 0)
                {
                    oProducto.IdProducto = idProductoGenerado;
                }
                else
                {
                    operacion_exitosa = false;
                }
            }
            else
            {
                operacion_exitosa = new CN_Producto().Editar(oProducto, out mensaje);
            }


            if (operacion_exitosa)
            {

                if (archivoImagen != null)
                {

                    //string ruta_guardar = ConfigurationManager.AppSettings["ServidorFotos"];

                    string extension = Path.GetExtension(archivoImagen.FileName);
                    string nombre_imagen = string.Concat(oProducto.IdProducto.ToString(), extension);

                    string ruta_guardar = await cnfirebase.SubirStorage(archivoImagen.InputStream, nombre_imagen);

                    //try
                    //{
                    //    archivoImagen.SaveAs(Path.Combine(ruta_guardar, nombre_imagen));

                    //}
                    //catch (Exception ex)
                    //{
                    //    string msg = ex.Message;
                    //    guardar_imagen_exito = false;
                    //}

                    guardar_imagen_exito = ruta_guardar != "" ? true : false;

                    if (guardar_imagen_exito)
                    {

                        oProducto.RutaImagen = ruta_guardar;
                        oProducto.NombreImagen = nombre_imagen;
                        bool rspta = new CN_Producto().GuardarDatosImagen(oProducto, out mensaje);
                    }
                    else
                    {

                        mensaje = "Se guardaro el producto pero hubo problemas con la imagen";
                    }


                }
            }




            return Json(new { operacionExitosa = operacion_exitosa, idGenerado = oProducto.IdProducto, mensaje = mensaje }, JsonRequestBehavior.AllowGet);
        }


        [HttpPost]
        public JsonResult ImagenProducto(int id)
        {

            //bool conversion;
            Producto oproducto = new CN_Producto().Listar().Where(p => p.IdProducto == id).FirstOrDefault();

            //string textoBase64 = CN_Recursos.ConvertirBase64(Path.Combine(oproducto.RutaImagen,oproducto.NombreImagen), out conversion );


            //return Json(new
            //{
            //    conversion = conversion,
            //    textobase64 = textoBase64,
            //    extension = Path.GetExtension(oproducto.NombreImagen)

            //},
            // JsonRequestBehavior.AllowGet
            //);

            return Json(new { ruta = oproducto.RutaImagen }, JsonRequestBehavior.AllowGet);

        }


        [HttpPost]
        public JsonResult EliminarProducto(int id)
        {
            bool respuesta = false;
            string mensaje = string.Empty;

            respuesta = new CN_Producto().Eliminar(id, out mensaje);

            return Json(new { resultado = respuesta, mensaje = mensaje }, JsonRequestBehavior.AllowGet);
        }

        #endregion
        #region Compra
        // ++++++++++++++++ Compra ++++++++++++++++++++
        [HttpGet]
        public JsonResult ListarCompra()
        {
            List<Compra> oLista = new List<Compra>();
            oLista = new CN_Compra().Listar();
            return Json(new { data = oLista }, JsonRequestBehavior.AllowGet);
        }
        [HttpPost]
        public JsonResult GuardarCompra(string objeto)
        {

            string mensaje = string.Empty;
            bool operacion_exitosa = true;
            //bool guardar_imagen_exito = true;

            Compra oCompra = new Compra();
            oCompra = JsonConvert.DeserializeObject<Compra>(objeto);

            decimal precio;

            if (decimal.TryParse(oCompra.precioCompraTexto, NumberStyles.AllowDecimalPoint, new CultureInfo("es-PE"), out precio))
            {

                oCompra.precioCompra = precio;
            }
            else
            {

                return Json(new { operacionExitosa = false, mensaje = "El formato del precio debe ser ##.##" }, JsonRequestBehavior.AllowGet);
            }


            if (oCompra.IdCompra == 0)
            {
                int idCompraGenerado = new CN_Compra().Registrar(oCompra, out mensaje);

                if (idCompraGenerado != 0)
                {
                    oCompra.IdCompra = idCompraGenerado;
                }
                else
                {
                    operacion_exitosa = false;
                }
            }
            else
            {
                //operacion_exitosa = new CN_Compra().Editar(oCompra, out mensaje);

            }



            return Json(new { operacionExitosa = operacion_exitosa, idGenerado = oCompra.IdCompra, mensaje = mensaje }, JsonRequestBehavior.AllowGet);
        }
        //[HttpPost]
        //public JsonResult GuardarCompra(string objeto)
        //{

        //    string mensaje = string.Empty;
        //    bool operacion_exitosa = true;
        //    //bool guardar_imagen_exito = true;

        //    Compra oCompra = new Compra();
        //    oCompra = JsonConvert.DeserializeObject<Compra>(objeto);

        //    decimal precio;

        //    if (decimal.TryParse(oCompra.precioCompraTexto, NumberStyles.AllowDecimalPoint, new CultureInfo("es-PE"), out precio))
        //    {

        //        oCompra.precioCompra = precio;
        //    }
        //    else
        //    {

        //        return Json(new { operacionExitosa = false, mensaje = "El formato del precio debe ser ##.##" }, JsonRequestBehavior.AllowGet);
        //    }


        //    if (oCompra.idCompra == 0)
        //    {
        //        int idCompraGenerado = new CN_Compra().Registrar(oCompra, out mensaje);

        //        if (idCompraGenerado != 0)
        //        {
        //            oCompra.idCompra = idCompraGenerado;
        //        }
        //        else
        //        {
        //            operacion_exitosa = false;
        //        }
        //    }
        //    else
        //    {
        //        //operacion_exitosa = new CN_Compra().Editar(oCompra, out mensaje);

        //    }



        //    return Json(new { operacionExitosa = operacion_exitosa, idGenerado = oCompra.idCompra, mensaje = mensaje }, JsonRequestBehavior.AllowGet);
        //}


        //if (operacion_exitosa)
        //{

        //    if (archivoImagen != null)
        //    {

        //        string ruta_guardar = ConfigurationManager.AppSettings["ServidorFotos"];

        //        string extension = Path.GetExtension(archivoImagen.FileName);
        //        string nombre_imagen = string.Concat(oCompra.IdCompra.ToString(), extension);

        //        //string ruta_guardar = await cnfirebase.SubirStorage(archivoImagen.InputStream, nombre_imagen);

        //        try
        //        {
        //            archivoImagen.SaveAs(Path.Combine(ruta_guardar, nombre_imagen));

        //        }
        //        catch (Exception ex)
        //        {
        //            string msg = ex.Message;
        //            guardar_imagen_exito = false;
        //        }

        //        //guardar_imagen_exito = ruta_guardar != "" ? true : false;

        //        if (guardar_imagen_exito)
        //        {

        //            oCompra.RutaImagen = ruta_guardar;
        //            oCompra.NombreImagen = nombre_imagen;
        //            bool rspta = new CN_Compra().GuardarDatosImagen(oCompra, out mensaje);
        //        }
        //        else
        //        {

        //            mensaje = "Se guardaro el Compra pero hubo problemas con la imagen";
        //        }


        //    }
        //}

    }
    #endregion


}