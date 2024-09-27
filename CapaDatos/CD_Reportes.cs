using CapaEntidad;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CapaDatos
{
    public class CD_Reportes
    {
        public int Registrar(Reportes obj, out string Mensaje)
        {
            int idautogenerado = 1;

            Mensaje = string.Empty;
            try
            {

                using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
                {
                  
                   string query = "INSERT INTO reportes(Codigo,Articulo,Medida,Unidades,Venta,Costo,Utilidad) VALUES(@Codigo,@Articulo,@Medida,@Unidades,@Venta,@Costo,@Utilidad); SELECT SCOPE_IDENTITY();";

                    SqlCommand cmd = new SqlCommand(query, oconexion);
                    cmd.CommandType = CommandType.Text;

                    cmd.Parameters.AddWithValue("@Codigo", obj.Codigo);
                    cmd.Parameters.AddWithValue("@Articulo", obj.Articulo);
                    cmd.Parameters.AddWithValue("@Medida", obj.Medida);
                    cmd.Parameters.AddWithValue("@Unidades", obj.Unidades);
                    cmd.Parameters.AddWithValue("@Venta", obj.Venta);
                    cmd.Parameters.AddWithValue("@Costo", obj.Costo);
                    cmd.Parameters.AddWithValue("@Utilidad", obj.Utilidad);

                    oconexion.Open();

                    idautogenerado = Convert.ToInt32(cmd.ExecuteScalar());  
                }
            }
            catch (Exception ex)
            {
                idautogenerado = 0;
                Mensaje = ex.Message;
            }
            return idautogenerado;
        }

    }
}
