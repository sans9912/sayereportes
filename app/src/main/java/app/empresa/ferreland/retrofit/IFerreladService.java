package app.empresa.ferreland.retrofit;

import app.empresa.ferreland.retrofit.request.RequestLogin;
import app.empresa.ferreland.retrofit.request.RequestRegistrarUsuario;
import app.empresa.ferreland.retrofit.response.ResponseUsuario;
import app.empresa.ferreland.retrofit.response.ResponseVenta;
import app.empresa.ferreland.retrofit.response.ResponseProducto;
import app.empresa.ferreland.retrofit.response.ResponseToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IFerreladService {

    //TODO: AUTH
    @POST("/api/auth/login")
    Call<ResponseToken> doLogin(@Body RequestLogin requestLogin);

    //TODO: ARTICULO
    @GET("/api/producto/read")
    Call<List<ResponseProducto>> doProducto();

    //TODO: VENTA
    @GET("/api/venta/read")
    Call<List<ResponseVenta>> doVenta();

    //TODO: USUARIO
    @GET("/api/usuario/read")
    Call<List<ResponseUsuario>> doUsuario();

    @POST("/api/auth/signup")
    Call<ResponseToken> doUsuarioRegistrar(@Body RequestRegistrarUsuario requestRegistrarUsuario);
}



//    @GET("/api/auth/renew")
//    Call<ResponseAuth> doRenew();

//    @DELETE("/api/categoria/delete/{id}")
//    Call<ResponseSuccess> toCategoryDelete(@Path("id") String idProduct);

//    @POST("/api/producto/create")
//    Call<ResponseSuccess> toProduct(@Body RequestProduct requestProduct);

//    @DELETE("/api/producto/delete/{id}")
//    Call<ResponseSuccess> toProductDelete(@Path("id") String idProduct);

//    @GET("/api/proveedor/read")
//    Call<List<ResponseSupplier>> doSupplier();

//    @POST("/api/usuario/create")
//    Call<ResponseSuccess> toUser(@Body RequestUser requestUser);

//    @DELETE("/api/usuario/delete/{id}")
//    Call<ResponseSuccess> toUserDelete(@Path("id") String idProduct);

