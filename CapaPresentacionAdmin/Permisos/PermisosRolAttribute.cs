using CapaEntidad;
using System.Web;
using System.Web.Mvc;

namespace CapaPresentacionAdmin.Permisos
{
    public class PermisosRolAttribute : ActionFilterAttribute
    {
        private Rol idrol;

        public PermisosRolAttribute(Rol _idrol)
        {
            idrol = _idrol;
        }

        public static int ObtenerIdRol()
        {
            if (HttpContext.Current.Session["Usuario"] != null)
            {
                Usuario usuario = HttpContext.Current.Session["Usuario"] as Usuario;
                return (int)usuario.idRol;
            }
            return 0; // O cualquier valor predeterminado en caso de que la sesión no tenga un usuario o el usuario no tenga un idRol válido.
        }
        public static string ObtenerNombApel()
        {
            string nombreapel = "";
            if (HttpContext.Current.Session["Usuario"] != null)
            {
                Usuario usuario = HttpContext.Current.Session["Usuario"] as Usuario;
                nombreapel = usuario.Nombres + " " + usuario.Apellidos;
                return nombreapel;
            }
            return "Error"; // O cualquier valor predeterminado en caso de que la sesión no tenga un usuario o el usuario no tenga un idRol válido.
        }
        public override void OnActionExecuting(ActionExecutingContext filterContext)
        {
            if (HttpContext.Current.Session["Usuario"] != null)
            {
                Usuario usuario = HttpContext.Current.Session["Usuario"] as Usuario;
                if (usuario.idRol != this.idrol)
                {
                    filterContext.Result = new RedirectResult("~/Home/SinPermiso");
                }
            }
            base.OnActionExecuting(filterContext);
        }
    }
}