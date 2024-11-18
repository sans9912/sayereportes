using CapaEntidad;
using CapaNegocio;
using Newtonsoft.Json.Linq;
using System;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Mvc;

namespace CapaPresentacionAdmin.Controllers
{
    public class FechaController : Controller
    {
        public async Task<DateTime> ObtenerFechaHoraAsync()
        {
            using (HttpClient client = new HttpClient())
            {
                string url = "http://worldtimeapi.org/api/timezone/America/Mexico_City";

                try
                {
                    HttpResponseMessage response = await client.GetAsync(url);

                    if (response.IsSuccessStatusCode)
                    {
                        string jsonResponse = await response.Content.ReadAsStringAsync();
                        JObject data = JObject.Parse(jsonResponse);

                        string dateTimeString = data["datetime"].ToString();
                        DateTimeOffset fechaHoraOffset = DateTimeOffset.Parse(dateTimeString);

                        string utcOffsetString = data["utc_offset"].ToString();
                        TimeSpan utcOffset = TimeSpan.Parse(utcOffsetString);

                        DateTime fechaHoraMexico = fechaHoraOffset.UtcDateTime + utcOffset;

                        return fechaHoraMexico;
                    }
                    else
                    {
                        throw new Exception("No se pudo obtener la hora de Puerto Escondido");
                    }
                }
                catch (HttpRequestException httpRequestException)
                {
                    return DateTime.Now; 
                }
                catch (Exception ex)
                {
                    return DateTime.Now;
                }
            }
        }
    }
}