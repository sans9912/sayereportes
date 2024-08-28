using CapaEntidad;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;

namespace CapaDatos
{
    public class CD_Compra
    {
        public List<Compra> Listar()
        {

            List<Compra> lista = new List<Compra>();

            try
            {
                using (SqlConnection connection = new SqlConnection(Conexion.cn))
                {

                    //StringBuilder sb = new StringBuilder();


                    string query = @"
                    SELECT c.idCompra,
                    p.IdProducto, p.Nombre AS NomProduct,
                    u.IdUsuario, u.Nombres AS NomUser, u.Apellidos AS ApeUser,
                    c.cantidad, c.precioCompra, c.Activo, c.FechaRegistro, c.Total
                    FROM Compra c
                    INNER JOIN PRODUCTO p ON p.IdProducto = c.idProducto
                    INNER JOIN USUARIO u ON u.IdUsuario = c.idEmpleado";

                    using (SqlCommand command = new SqlCommand(query, connection))
                    {
                        command.CommandType = CommandType.Text;

                        connection.Open();

                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                Compra compra = new Compra()
                                {
                                    IdCompra = reader.GetInt32(reader.GetOrdinal("idCompra")),
                                    oProducto = new Producto()
                                    {
                                        IdProducto = reader.GetInt32(reader.GetOrdinal("IdProducto")),
                                        Nombre = reader.GetString(reader.GetOrdinal("NomProduct"))
                                    },

                                    oUsuario = new Usuario()
                                    {
                                        IdUsuario = reader.GetInt32(reader.GetOrdinal("IdUsuario")),
                                        Nombres = reader.GetString(reader.GetOrdinal("NomUser")),
                                        Apellidos = reader.GetString(reader.GetOrdinal("ApeUser"))
                                    },
                                    cantidad = reader.GetInt32(reader.GetOrdinal("cantidad")),
                                    precioCompra = reader.GetDecimal(reader.GetOrdinal("precioCompra")),

                                    Activo = reader.GetBoolean(reader.GetOrdinal("Activo")),
                                    FechaRegistro = reader.GetDateTime(reader.GetOrdinal("FechaRegistro")).Date,
                                    total = reader.GetDecimal(reader.GetOrdinal("Total"))
                                };

                                lista.Add(compra);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                // Manejar la excepción de forma adecuada
                Console.WriteLine("Error: " + ex.Message);
                lista = new List<Compra>();
            }

            return lista;
        }
        //       @precioCompra decimal (18,2),
        //@idProducto varchar(100),
        //@idEmpleado varchar(100),
        //@cantidad int,
        //@Activo bit,
        //   @Mensaje varchar(500) output,
        //@Resultado int output,
        //   @cantidadMandar int output
        //public int Registrar(Compra obj, out string Mensaje)
        //{
        //    int idautogenerado = 0;

        //    Mensaje = string.Empty;
        //    try
        //    {


        //        using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
        //        {
        //            SqlCommand cmd = new SqlCommand("sp_RegistrarCompra", oconexion);
        //            cmd.Parameters.AddWithValue("precioCompra", obj.precioCompra);
        //            cmd.Parameters.AddWithValue("idProducto", obj.oProducto.IdProducto);
        //            cmd.Parameters.AddWithValue("idEmpleado", obj.oUsuario.IdUsuario);
        //            cmd.Parameters.AddWithValue("cantidad", obj.cantidad);
        //            cmd.Parameters.AddWithValue("Activo", obj.Activo);
        //            cmd.Parameters.Add("Resultado", SqlDbType.Int).Direction = ParameterDirection.Output;
        //            cmd.Parameters.Add("cantidadMandar", SqlDbType.Int).Direction = ParameterDirection.Output;
        //            cmd.Parameters.Add("Mensaje", SqlDbType.VarChar, 500).Direction = ParameterDirection.Output;
        //            cmd.CommandType = CommandType.StoredProcedure;

        //            oconexion.Open();

        //            cmd.ExecuteNonQuery();

        //            idautogenerado = Convert.ToInt32(cmd.Parameters["Resultado"].Value);
        //            Mensaje = cmd.Parameters["Mensaje"].Value.ToString();
        //        }
        //    }
        //    catch (Exception ex)
        //    {
        //        idautogenerado = 0;
        //        Mensaje = ex.Message;
        //    }
        //    return idautogenerado;
        //}
        public int Registrar(Compra obj, out string Mensaje)
        {
            int idautogenerado = 0;

            Mensaje = string.Empty;
            try
            {


                using (SqlConnection oconexion = new SqlConnection(Conexion.cn))
                {
                    SqlCommand cmd = new SqlCommand("sp_RegistrarCompraW", oconexion);
                    cmd.Parameters.AddWithValue("precioCompra", obj.precioCompra);
                    cmd.Parameters.AddWithValue("idProducto", obj.oProducto.IdProducto);
                    cmd.Parameters.AddWithValue("idEmpleado", obj.oUsuario.IdUsuario);
                    cmd.Parameters.AddWithValue("cantidad", obj.cantidad);
                    cmd.Parameters.AddWithValue("Activo", obj.Activo);
                    cmd.Parameters.AddWithValue("total", obj.total);
                    cmd.Parameters.Add("Resultado", SqlDbType.Int).Direction = ParameterDirection.Output;
                    cmd.Parameters.Add("cantidadMandar", SqlDbType.Int).Direction = ParameterDirection.Output;
                    cmd.Parameters.Add("Mensaje", SqlDbType.VarChar, 500).Direction = ParameterDirection.Output;
                    cmd.CommandType = CommandType.StoredProcedure;

                    oconexion.Open();

                    cmd.ExecuteNonQuery();

                    idautogenerado = Convert.ToInt32(cmd.Parameters["Resultado"].Value);
                    Mensaje = cmd.Parameters["Mensaje"].Value.ToString();
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
