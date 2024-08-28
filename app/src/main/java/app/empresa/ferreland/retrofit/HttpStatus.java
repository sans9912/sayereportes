package app.empresa.ferreland.retrofit;

public class HttpStatus {

    // TODO - RESPUESTAS SATISFCTORIAS
    public static int Ok = 200; //La solicitud ha tenido éxito
    public static int Created = 201; // La solicitud ha tenido éxito y se ha creado un nuevo recurso
    public static int Accepted = 202; // La solicitud se ha recibido, pero aún no se ha actuado

    // TODO - ERRORES DE CLIENTE
    public static int BadRequest = 400; // Esta respuesta significa que el servidor no pudo interpretar la solicitud dada una sintaxis inválida.0
    public static int Unauthorized = 401; // Es necesario autenticar para obtener la respuesta solicitada
    public static int Forbidden = 403; // El cliente no posee los permisos necesarios para cierto contenido, por lo que el servidor está rechazando otorgar una respuesta apropiada.
    public static int NotFound = 404; // El servidor no pudo encontrar el contenido solicitado. Este código de respuesta es uno de los más famosos dada su alta ocurrencia en la web.
    public static int UnprocessableEntity = 422; // La petición estaba bien formada pero no se pudo seguir debido a errores de semántica.


    // TODO - ERRORES DE SERVIDOR
    public static int IntervalServerError = 500; // El servidor ha encontrado una situación que no sabe cómo manejarla.

}



