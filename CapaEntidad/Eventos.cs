using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CapaEntidad
{
    public class Eventos
    {
        public DateTime fechaRegistro { get; set; }
        public string descripcion { get; set; }
        public int idUsuario { get; set; }

    }
    public class EventoDetalle
    {
        public string CorreoUsuario { get; set; }
        public string NombreRol { get; set; }
        public DateTime FechaRegistro { get; set; }
        public string Hora { get; set; }
        public string Descripcion { get; set; }
    }
}
