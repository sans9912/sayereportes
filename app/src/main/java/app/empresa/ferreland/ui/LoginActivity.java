package app.empresa.ferreland.ui;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import app.empresa.ferreland.R;

import app.empresa.ferreland.common.Constantes;
import app.empresa.ferreland.common.SharedPreferencesManager;
import app.empresa.ferreland.retrofit.FerrelandClient;
import app.empresa.ferreland.retrofit.IFerreladService;
import app.empresa.ferreland.retrofit.request.RequestLogin;
import app.empresa.ferreland.retrofit.response.ResponseError;
import app.empresa.ferreland.retrofit.response.ResponseToken;
import app.empresa.ferreland.utidades.CustomAlertDialog;
import app.empresa.ferreland.utidades.ValidationData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, InternetConnectivityListener {

    public TextInputEditText etCorreo, etContrasenia;
    public TextInputLayout tlyCorreo, tlyPassword;
    public TextView tvOlvidaste, tvRegistrarse;
    public MaterialButton btnIniciar;
    public FerrelandClient ferrelandClient;
    public IFerreladService icreativeService;
    public CustomAlertDialog customAlertDialog;
    public CircularProgressIndicator indicatorButton;
    public InternetAvailabilityChecker mInternetAvailabilityChecker;
    private static final int REQUEST_CODE = 101001;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    public TextView textoOHuella;

    ImageView ivHuella;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        etCorreo = (TextInputEditText) findViewById(R.id.etCorreo);
        etContrasenia = (TextInputEditText) findViewById(R.id.etPassword);
        tlyCorreo = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        tlyPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        btnIniciar = (MaterialButton) findViewById(R.id.btnIniciar);
        tvOlvidaste = (TextView) findViewById(R.id.tvOlvidaste);
        tvRegistrarse = (TextView) findViewById(R.id.tvRegistrarse);
        indicatorButton = (CircularProgressIndicator) findViewById(R.id.indicatorButton);
        ivHuella = findViewById(R.id.ivHuella);
        textoOHuella =  (TextView) findViewById(R.id.textO);
    }

    private void events() {
        btnIniciar.setOnClickListener(this);
        tvRegistrarse.setOnClickListener(this);
        tvOlvidaste.setOnClickListener(this);

        String token = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_TOKEN);

        if (token != null) {
            ivHuella.setVisibility(View.VISIBLE);
            textoOHuella.setVisibility(View.VISIBLE);
        }else{
            ivHuella.setVisibility(View.GONE);
            textoOHuella.setVisibility(View.GONE);
        }

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                //Toast.makeText(this, "No hay funciones biométricas disponibles en este dispositivo.", Toast.LENGTH_SHORT).show();
                ivHuella.setVisibility(View.GONE);
                textoOHuella.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "Las funciones biométricas no están disponibles actualmente.", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
        }


        executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                Toast.makeText(getApplicationContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Inicio de sesión biométrico").setSubtitle("Inicie sesión con su credencial biométrica").setNegativeButtonText("Ingrese su usuario y contraseña").build();

        ivHuella.setOnClickListener(view -> biometricPrompt.authenticate(promptInfo));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnIniciar) {
            show();
            doToLogin();
        }
        if (id == R.id.tvRegistrarse) {
            doToRegistrarse();
        }
        if (id == R.id.tvOlvidaste) {
            doToRecoverPass();
        }
    }

    private void doToRecoverPass() {
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
            tlyPassword.setError(getString(R.string.title_error_password));
            return false;
        } else {
            tlyPassword.setErrorEnabled(false);
        }
        return true;
    }

    private void doToLogin() {
        if (validateEmail() && validatePassword()) {
            RequestLogin requestLogin = new RequestLogin(etCorreo.getText().toString().trim(), etContrasenia.getText().toString().trim());
            Call<ResponseToken> call = icreativeService.doLogin(requestLogin);
            call.enqueue(new Callback<ResponseToken>() {
                @Override
                public void onResponse(@NonNull Call<ResponseToken> call, @NonNull Response<ResponseToken> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_TOKEN, response.body().getToken());

                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_USER_NOMBRE, response.body().getUsuario().getNombres());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_USER_EMAIL, response.body().getUsuario().getCorreo());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_USER_ROLE, response.body().getUsuario().getRol().getNombre());

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        ResponseError message = new Gson().fromJson(response.errorBody().charStream(), ResponseError.class);
                        customAlertDialog.showErrorDialog(LoginActivity.this, message.getMessage());
                    }
                    hide();
                }

                @Override
                public void onFailure(@NonNull Call<ResponseToken> call, @NonNull Throwable t) {
                    customAlertDialog.showErrorDialog(LoginActivity.this, t.getMessage());
                    hide();
                }
            });
        } else {
            hide();
        }
    }

    private void doToRegistrarse() {
        startActivity(new Intent(LoginActivity.this, RegistrarseActivity.class));
    }

    private void hide() {
        btnIniciar.setEnabled(true);
        indicatorButton.setVisibility(View.GONE);
    }

    private void show() {
        btnIniciar.setEnabled(false);
        indicatorButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
//        if (isConnected) {
//            textSinConexion.setText(getString(R.string.title_connected));
//            textSinConexion.setTextColor(getResources().getColor(R.color.green));
//        } else {
//            textSinConexion.setText(R.string.title_noconnected);
//            textSinConexion.setTextColor(getResources().getColor(R.color.red));
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInternetAvailabilityChecker.removeInternetConnectivityChangeListener(this);
    }

}


//        setTheme(R.style.Theme_Creative);
//        SplashScreen splashScreem = SplashScreen.installSplashScreen(this);
//        try {
//            Thread.sleep(0000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }