using Firebase.Auth;
using Firebase.Storage;
using System.IO;
using System.Threading;
using System.Threading.Tasks;

namespace CapaNegocio
{
    public class CN_FireBase
    {


        public async Task<string> SubirStorage(Stream archivo, string nombre)
        {
            string UrlImagen = string.Empty;

            string email = "ferrelandbolivia@gmail.com";
            string clave = "frrlndbol123";
            string ruta = "imgferreland-1f943.appspot.com";
            string api_key = "AIzaSyA8cS-pvE3oSbWxrhrgSQrL2FveYoqhFz8";

            try
            {

                var auth = new FirebaseAuthProvider(new FirebaseConfig(api_key));
                var a = await auth.SignInWithEmailAndPasswordAsync(email, clave);

                var cancellation = new CancellationTokenSource();

                var task = new FirebaseStorage(
                    ruta,
                    new FirebaseStorageOptions
                    {
                        AuthTokenAsyncFactory = () => Task.FromResult(a.FirebaseToken),
                        ThrowOnCancel = true
                    })
                    .Child("IMAGENES_PRODUCTO")
                    .Child(nombre)
                    .PutAsync(archivo, cancellation.Token);


                UrlImagen = await task;
            }
            catch
            {
                UrlImagen = "";
            }


            return UrlImagen;


        }
    }
}
