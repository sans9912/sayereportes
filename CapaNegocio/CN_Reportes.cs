using CapaDatos;
using CapaEntidad;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CapaNegocio
{
    public class CN_Reportes
    {
        private CD_Reportes objCapaDato = new CD_Reportes();

        public bool ExisteReporteParaMes(string mes, out string mensaje)
        {
            return objCapaDato.ValidarMesReporte(mes, out mensaje);
        }

        public int Registrar(List<Reportes> lista, out string Mensaje)
        {
            Mensaje = string.Empty;
            int registrosExitosos = 0; 

            foreach (var obj in lista) 
            {
                var resultado = objCapaDato.Registrar(obj, out Mensaje);
                if (resultado > 0)
                {
                    registrosExitosos++;
                }
                else
                {
                    continue;
                }
            }

            return registrosExitosos; 
        }
    }
}
