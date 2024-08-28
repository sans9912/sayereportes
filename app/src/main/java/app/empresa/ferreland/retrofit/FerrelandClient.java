package app.empresa.ferreland.retrofit;

import app.empresa.ferreland.common.Constantes;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FerrelandClient {

    // Instance del Objeto CreativeClient
    private static FerrelandClient instance = null;

    // Service
    private final IFerreladService IFerreladService;

    public FerrelandClient() {

//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        httpClient.addInterceptor(logging);

        // OkHttpClient Interceptor
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new AuthInterceptor());
        OkHttpClient client = httpClient.build();

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_INVENTARIO_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IFerreladService = retrofit.create(IFerreladService.class);
    }

    // Patron Singleton
    public static FerrelandClient getInstance() {
        if (instance == null) {
            instance = new FerrelandClient();
        }
        return instance;
    }

    // Instance del Server
    public IFerreladService getCreativeService() {
        return IFerreladService;
    }

}
