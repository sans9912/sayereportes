package app.empresa.ferreland.ui;

import static app.empresa.ferreland.R.id.nav_power;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.empresa.ferreland.R;

import app.empresa.ferreland.common.Constantes;
import app.empresa.ferreland.common.SharedPreferencesManager;
import app.empresa.ferreland.databinding.ActivityMainBinding;
import app.empresa.ferreland.retrofit.ActivityResultBus;
import app.empresa.ferreland.retrofit.ActivityResultEvent;
import app.empresa.ferreland.retrofit.FerrelandClient;
import app.empresa.ferreland.retrofit.IFerreladService;
import app.empresa.ferreland.utidades.CustomAlertDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public FerrelandClient ferrelandClient = null;
    public IFerreladService icreativeService;
    public CustomAlertDialog customAlertDialog;
    public Activity activity;
    public NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;

//        Tools.isOnlineShowDialog(activity);

        customAlertDialog = new CustomAlertDialog();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_inicio,
                R.id.navigation_producto,
                R.id.navigation_usuario,
                R.id.navigation_venta,
                R.id.navigation_perfil)
                .build();
        navController = Navigation.findNavController(this, R.id.fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        retrofitInit();

//        if (Tools.isOnline(this))
//            doToRenew();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case nav_power:
                CustomAlertDialogWarning();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CustomAlertDialogWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Theme_Creative_AlertDialogCustom);
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_warning_dialog, (ConstraintLayout) activity.findViewById(R.id.layoutDialogContainerWarning));
        builder.setCancelable(false);
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitleWarning))
                .setText(activity.getResources().getString(R.string.warning_title));
        ((TextView) view.findViewById(R.id.textMessageWarning))
                .setText("Estas seguro de Cerrar Session.?");
        ((Button) view.findViewById(R.id.btnyesWarnning))
                .setText(activity.getResources().getString(R.string.acept));
        ((Button) view.findViewById(R.id.btnNoWarning))
                .setText(activity.getResources().getString(R.string.no));
        ((ImageView) view.findViewById(R.id.imageIconWarning))
                .setImageResource(R.drawable.ic_warning);
        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btnyesWarnning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                //delete token
                SharedPreferencesManager.removeSomeLabel(Constantes.PREF_TOKEN);
                SharedPreferencesManager.removeAll();
                //finish activity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        view.findViewById(R.id.btnNoWarning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

//    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_activity_main, fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }

    private void retrofitInit() {
        this.ferrelandClient = FerrelandClient.getInstance();
        this.icreativeService = ferrelandClient.getCreativeService();
    }

    private void doToRenew() {
//        Call<ResponseAuth> call = icreativeService.doRenew();
//        call.enqueue(new Callback<ResponseAuth>() {
//            @Override
//            public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
//                if (response.isSuccessful()) {
//                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_TOKEN, response.body().getToken());
//                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_ID, response.body().getData().getId());
//                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_USER, response.body().getData().getNombre());
//                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_EMAIL, response.body().getData().getEmail());
//                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_IMAGE, response.body().getData().getImg());
//                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_ROLE, response.body().getData().getRole().get(0).getNombre());
//                } else {
//                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                    finish();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseAuth> call, Throwable t) {
//                customAlertDialog.showErrorDialog(MainActivity.this, "Error de conexion");
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultBus.getInstance().postQueue(new ActivityResultEvent(requestCode, resultCode, data));
    }

}