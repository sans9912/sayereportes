using CapaEntidad;
using CapaDatos;
using System.Collections.Generic;

namespace CapaNegocio
{
    public class CN_Eventos
    {
        private CD_Eventos objCapaDato = new CD_Eventos();
        public List<Eventos> Listar()
        {
            return objCapaDato.Listar();
        }
        public List<EventoDetalle> ListarDetalle()
        {
            return objCapaDato.ListarDetalle();
        }


        public int Registrar(Eventos obj, out string Mensaje)
        {

            Mensaje = string.Empty;


            if (string.IsNullOrEmpty(obj.descripcion) || string.IsNullOrWhiteSpace(obj.descripcion))
            {
                Mensaje = "La descripcion del evento no puede ser vacio";
            }



            if (string.IsNullOrEmpty(Mensaje))
            {

                return objCapaDato.Registrar(obj, out Mensaje);

            }
            else
            {

                return 0;
            }



        }
    }
}