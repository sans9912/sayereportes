package app.empresa.ferreland.ui;

import static app.empresa.ferreland.app.MyApp.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import app.empresa.ferreland.R;
import app.empresa.ferreland.common.Constantes;
import app.empresa.ferreland.common.SharedPreferencesManager;
import app.empresa.ferreland.retrofit.FerrelandClient;
import app.empresa.ferreland.retrofit.IFerreladService;
import app.empresa.ferreland.retrofit.request.RequestRegistrarUsuario;
import app.empresa.ferreland.retrofit.response.ResponseError;
import app.empresa.ferreland.retrofit.response.ResponseToken;
import app.empresa.ferreland.utidades.CustomAlertDialog;
import app.empresa.ferreland.utidades.ValidationData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarseActivity extends AppCompatActivity {

    public FerrelandClient ferrelandClient;
    public IFerreladService icreativeService;
    public CustomAlertDialog customAlertDialog;
    public TextInputEditText etCorreo, etContrasenia, etNombre, etApellido;
    public TextInputLayout tlyNombre, tlyApellido, tlyCorreo, tlyContrasenia;
    public MaterialButton btnRegistrar;
    public CircularProgressIndicator indicatorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        retrofitInit();
        initialize();
        findViews();
        events();
    }

    private void retrofitInit() {
        this.ferrelandClient = FerrelandClient.getInstance();
        this.icreativeService = ferrelandClient.getCreativeService();
    }

    private void initialize() {
        this.customAlertDialog = new CustomAlertDialog();
    }

    private void findViews() {
        etNombre = (TextInputEditText) findViewById(R.id.etNombreUsuario);
        etApellido = (TextInputEditText) findViewById(R.id.etApellidosUsuario);
        etCorreo = (TextInputEditText) findViewById(R.id.etCorreoUsuario);
        etContrasenia = (TextInputEditText) findViewById(R.id.etPasswordUsuario);

        tlyNombre = (TextInputLayout) findViewById(R.id.textInputlNombre);
        tlyApellido = (TextInputLayout) findViewById(R.id.textInputlApellido);
        tlyCorreo = (TextInputLayout) findViewById(R.id.textInputlCorreo);
        tlyContrasenia = (TextInputLayout) findViewById(R.id.textInputlPassword);

        btnRegistrar = (MaterialButton) findViewById(R.id.btnRegistrarUsuario);
        indicatorButton = (CircularProgressIndicator) findViewById(R.id.indicatorButton);
    }

    private void events() {
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
                doToRegistrar();
            }
        });
    }

    private void show() {
        btnRegistrar.setEnabled(false);
        indicatorButton.setVisibility(View.VISIBLE);
    }

    private void hide() {
        btnRegistrar.setEnabled(true);
        indicatorButton.setVisibility(View.GONE);
    }

    private void doToRegistrar() {

        String nombre = String.valueOf(etNombre.getText());
        String apellido = String.valueOf(etApellido.getText());
        String email = String.valueOf(etCorreo.getText());
        String password = String.valueOf(etContrasenia.getText());

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Debe llenar todos los campos.!!!", Toast.LENGTH_SHORT).show();
        } else {
            if (validateEmail() && validatePassword()) {
                RequestRegistrarUsuario requestRegistrarUsuario = new RequestRegistrarUsuario(etCorreo.getText().toString().trim(), etContrasenia.getText().toString().trim(), etNombre.getText().toString().trim(), etApellido.getText().toString().trim());
                Call<ResponseToken> call = icreativeService.doUsuarioRegistrar(requestRegistrarUsuario);
                call.enqueue(new Callback<ResponseToken>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseToken> call, @NonNull Response<ResponseToken> response) {
                        if (response.isSuccessful()) {
                            SharedPreferencesManager.setSomeStringValue(Constantes.PREF_TOKEN, response.body().getToken());
                            startActivity(new Intent(RegistrarseActivity.this, AdminActivity.class));
                            finish();
                        } else {
                            ResponseError message = new Gson().fromJson(response.errorBody().charStream(), ResponseError.class);
                            customAlertDialog.showErrorDialog(RegistrarseActivity.this, message.getMessage());
                        }
                        hide();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseToken> call, @NonNull Throwable t) {
                        customAlertDialog.showErrorDialog(RegistrarseActivity.this, t.getMessage());
                        hide();
                    }
                });
            } else {
                hide();
            }
        }
    }

    private boolean validateCampoNombre() {
        String nombre = String.valueOf(etNombre.getText()).trim();
        if (nombre.isEmpty()) {
            tlyNombre.setError(getString(R.string.title_error_nombre));
            return false;
        } else {
            tlyNombre.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateCampoApellido() {
        String apellido = String.valueOf(etApellido.getText()).trim();
        if (apellido.isEmpty()) {
            tlyApellido.setError(getString(R.string.title_error_apellido));
            return false;
        } else {
            tlyApellido.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {
        String email = String.valueOf(etCorreo.getText()).trim();
        if (email.isEmpty()) {
            tlyCorreo.setError(getString(R.string.title_error_correo));
            return false;
        } else if (!ValidationData.isValidEmailAddress(email)) {
            tlyCorreo.setError(getString(R.string.title_correo_valid));
            return false;
        } else {
            tlyCorreo.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        String password = String.valueOf(etContrasenia.getText()).trim();
        if (password.isEmpty()) {
            tlyContrasenia.setError(getString(R.string.title_error_password));
            return false;
        } else {
            tlyContrasenia.setErrorEnabled(false);
        }
        return true;
    }
}