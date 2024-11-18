using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data.SqlClient;
using CapaEntidad;
using System.Data;

namespace CapaDatos
{
    public class CD_Eventos
    {

        public List<Eventos> Listar()
        {

            List<Eventos> lista = new List<Eventos>();

            try
            {
                using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
                {

                    string query = "SELECT fechaRegistro,descripcion,idUsuario FROM EVENTOS";

                    SqlCommand cmd = new SqlCommand(query, oconexion);
                    cmd.CommandType = CommandType.Text;

                    oconexion.Open();

                    using (SqlDataReader dr = cmd.ExecuteReader())
                    {
                        while (dr.Read())
                        {
                            lista.Add(new Eventos()
                            {
                                fechaRegistro = Convert.ToDateTime(dr["fechaRegistro"]),
                                descripcion = dr["descripcion"].ToString(),
                                idUsuario = Convert.ToInt32(dr["idUsuario"])
                            });
                        }
                    }
                }
            }
            catch
            {
                lista = new List<Eventos>();

            }
            return lista;
        }


        public int Registrar(Eventos obj, out string Mensaje)
        {
            int idautogenerado = 1;
            Mensaje = string.Empty;

            try
            {
                using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
                {

                    string query = "INSERT INTO EVENTOS(fechaRegistro, descripcion, idUsuario) " +
                                   "VALUES(@fechaRegistro, @descripcion, @idUsuario); " +
                                   "SELECT SCOPE_IDENTITY();";

                    SqlCommand cmd = new SqlCommand(query, oconexion);
                    cmd.CommandType = CommandType.Text;

                    cmd.Parameters.AddWithValue("@fechaRegistro", obj.fechaRegistro);
                    cmd.Parameters.AddWithValue("@descripcion", obj.descripcion);
                    cmd.Parameters.AddWithValue("@idUsuario", obj.idUsuario);

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

        public List<EventoDetalle> ListarDetalle()
        {
            List<EventoDetalle> lista = new List<EventoDetalle>();

            try
            {
                using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
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
                    ROL R ON U.idRol = R.id";

                    SqlCommand cmd = new SqlCommand(query, oconexion);
                    cmd.CommandType = CommandType.Text;

                    oconexion.Open();

                    using (SqlDataReader dr = cmd.ExecuteReader())
                    {
                        while (dr.Read())
                        {
                            lista.Add(new EventoDetalle()
                            {
                                CorreoUsuario = dr["CorreoUsuario"].ToString(),
                                NombreRol = dr["NombreRol"].ToString(),
                                FechaRegistro = Convert.ToDateTime(dr["FechaRegistro"]),
                                Hora = dr["Hora"].ToString(),
                                Descripcion = dr["Descripcion"].ToString(),
                            });
                        }
                    }
                }
            }
            catch
            {
                lista = new List<EventoDetalle>();
            }
            return lista;
        }
    }
}
