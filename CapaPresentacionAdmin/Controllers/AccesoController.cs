using CapaEntidad;
using CapaNegocio;
using System;
using System.Linq;
using System.Threading.Tasks;
using System.Web.Mvc;
using System.Web.Security;

namespace CapaPresentacionAdmin.Controllers
{
    public class AccesoController : Controller
    {

        // GET: Acceso
        

        public ActionResult Index()
        {
            return View();
        }
        public ActionResult CambiarClave()
        {
            return View();
        }
        public ActionResult Reestablecer()
        {
            return View();
        }

        public ActionResult Bloqueado()
        {
            return View();
        }

        [HttpPost]
        public async Task<ActionResult> Index(string correo, string clave)
        {
            Usuario oUsuario = new Usuario();
            Usuario oUsuario1 = new Usuario();

            string correoLower = correo.ToLower();

            oUsuario = new CN_Usuarios()
                .Listar()
                .Where(u => u.Correo.ToLower() == correoLower && u.Clave == CN_Recursos.ConvertirSha256(clave))
                .FirstOrDefault();

            oUsuario1 = new CN_Usuarios()
                .Listar()
                .Where(us => us.Correo.ToLower() == correoLower)
                .FirstOrDefault();

            FechaController fechaController = new FechaController();

            if (oUsuario1 != null && oUsuario1.Clave != CN_Recursos.ConvertirSha256(clave))
            {
                DateTime horaLocal = await fechaController.ObtenerFechaHoraAsync();
                string mensajeEvento = string.Empty;
                Eventos oEventoIncorrecto = new Eventos
                {
                    descripcion = "Contraseña incorrecta",
                    idUsuario = oUsuario1.IdUsuario,
                    fechaRegistro = horaLocal
                };
                new CN_Eventos().Registrar(oEventoIncorrecto, out mensajeEvento);
            }

            if (oUsuario == null)
            {
                ViewBag.Error = "Correo o contraseña no correcta";
                return View();
            }
            else
            {
                if (oUsuario.Reestablecer)
                {
                    TempData["IdUsuario"] = oUsuario.IdUsuario;
                    return RedirectToAction("CambiarClave");
                }

                if (!oUsuario.Activo)
                {
                    ViewBag.Error = "Usuario inactivo";
                    return View();
                }

                DateTime horaLocal = await fechaController.ObtenerFechaHoraAsync();
                string mensajeEvento = string.Empty;
                Eventos oEventoInicioSesion = new Eventos
                {
                    descripcion = "Inicio de sesión",
                    idUsuario = oUsuario.IdUsuario,
                    fechaRegistro = horaLocal
                };
                new CN_Eventos().Registrar(oEventoInicioSesion, out mensajeEvento);

                FormsAuthentication.SetAuthCookie(oUsuario.Correo, false);
                Session["Usuario"] = oUsuario;
                Session["UsuarioId"] = oUsuario.IdUsuario;
                ViewBag.Error = null;
                return RedirectToAction("Index", "Home");
            }
        }


        [HttpPost]
        public async Task<ActionResult> CambiarClave(string idusuario, string claveactual, string nuevaclave, string confirmarclave)
        {

            Usuario oUsuario = new Usuario();

            oUsuario = new CN_Usuarios().Listar().Where(u => u.IdUsuario == int.Parse(idusuario)).FirstOrDefault();

            if (oUsuario.Clave != CN_Recursos.ConvertirSha256(claveactual))
            {
                TempData["IdUsuario"] = idusuario;
                ViewData["vclave"] = "";
                ViewBag.Error = "La contraseña actual no es correcta";
                return View();
            }
            else if (nuevaclave != confirmarclave)
            {

                TempData["IdUsuario"] = idusuario;
                ViewData["vclave"] = claveactual;
                ViewBag.Error = "Las contraseñas no coinciden";
                return View();
            }
            
            else if (nuevaclave.Length < 8)
            {
                TempData["IdUsuario"] = idusuario;
                ViewData["vclave"] = claveactual;
                ViewBag.Error = "La contraseña debe tener al menos 8 caracteres";
                return View();
            }
            else if (!nuevaclave.Any(char.IsUpper))
            {
                TempData["IdUsuario"] = idusuario;
                ViewData["vclave"] = claveactual;
                ViewBag.Error = "La contraseña debe tener al menos una mayúscula";
                return View();
            }
            else if (!nuevaclave.Any(char.IsLower))
            {
                TempData["IdUsuario"] = idusuario;
                ViewData["vclave"] = claveactual;
                ViewBag.Error = "La contraseña debe tener al menos una minúscula";
                return View();
            }
            else if (!nuevaclave.Any(char.IsDigit))
            {
                TempData["IdUsuario"] = idusuario;
                ViewData["vclave"] = claveactual;
                ViewBag.Error = "La contraseña debe tener al menos un número";
                return View();
            }
            else if (nuevaclave == claveactual)
            {
                TempData["IdUsuario"] = idusuario;
                ViewData["vclave"] = claveactual;
                ViewBag.Error = "La nueva contraseña no puede ser igual a la actual";
                return View();
            }
            ViewData["vclave"] = "";


            nuevaclave = CN_Recursos.ConvertirSha256(nuevaclave);


            string mensaje = string.Empty;

            bool respuesta = new CN_Usuarios().CambiarClave(int.Parse(idusuario), nuevaclave, out mensaje);

            if (respuesta)
            {
                FechaController fechaController = new FechaController();
                DateTime horaLocal = await fechaController.ObtenerFechaHoraAsync();
                Eventos oEvento = new Eventos();
                oEvento.descripcion = "Cambio de contraseña";
                oEvento.idUsuario = int.Parse(idusuario);
                oEvento.fechaRegistro = horaLocal;
                new CN_Eventos().Registrar(oEvento, out mensaje);
                return RedirectToAction("Index");
            }
            else
            {

                TempData["IdUsuario"] = idusuario;

                ViewBag.Error = mensaje;
                return View();
            }

        }


        [HttpPost]
        public ActionResult Reestablecer(string correo)
        {
            string correoLower = correo.ToLower();

            Usuario ousurio = new Usuario();

            ousurio = new CN_Usuarios().Listar().Where(item => item.Correo.ToLower() == correoLower).FirstOrDefault();

            if (ousurio == null)
            {
                ViewBag.Error = "No se encontró un usuario relacionado a ese correo";
                return View();
            }

            string mensaje = string.Empty;
            bool respuesta = new CN_Usuarios().ReestablecerClave(ousurio.IdUsuario, correo, out mensaje);

            if (respuesta)
            {
                ViewBag.Error = null;
                return RedirectToAction("Index", "Acceso");
            }
            else
            {
                ViewBag.Error = mensaje;
                return View();
            }
        }

        public ActionResult CerrarSesion()
        {

            FormsAuthentication.SignOut();
            Session["Usuario"] = null;
            Session["UsuarioId"] = null;
            return RedirectToAction("Index", "Acceso");

        }



    }
}