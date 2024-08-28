
package app.empresa.ferreland.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseToken {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("usuario")
    @Expose
    private ResponseUsuario usuario;
    @SerializedName("token")
    @Expose
    private String token;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResponseUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(ResponseUsuario usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
