package app.empresa.ferreland.retrofit;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import app.empresa.ferreland.app.MyApp;
import app.empresa.ferreland.common.Constantes;
import app.empresa.ferreland.common.SharedPreferencesManager;
import app.empresa.ferreland.ui.LoginActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {

        String token = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_TOKEN);

        Request request = chain
                .request()
                .newBuilder()
                .header("Content-Type", "application/json")
                .addHeader("authorization", "Bearer " + token)
                .build();

        Response response = chain.proceed(request);
        Log.d("node", "Code : " + response.code());
        Log.d("node", "Code : " + response.body());


        if (response.code() == HttpStatus.BadRequest) {
            //Servidor no procesa la peticion por error del cliente
            return response;
        }

        if (response.code() == HttpStatus.Unauthorized) {
            //No esta autorizado
            Intent intent = new Intent(MyApp.getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApp.getContext().startActivity(intent);
            return response;
        }

//        if (response.code() == HttpStatus.UnprocessableEntity){
//            Log.i("Response message: ", response.message());
//            return response;
//        }

        return response;
    }

}
