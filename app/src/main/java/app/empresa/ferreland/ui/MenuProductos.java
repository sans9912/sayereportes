package app.empresa.ferreland.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import app.empresa.ferreland.R;

public class MenuProductos extends AppCompatActivity {
    public Button ListarProductos;
    public Button AgregarProductos;
    public Button CompraProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_productos);
        inicializarComponentes();

        AgregarProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProductos.this, AgregarProducto.class);
                startActivity(intent);
            }
        });
        ListarProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProductos.this, ListarProductos.class);
                startActivity(intent);
            }
        });
        CompraProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProductos.this, CompraProducto.class);
                startActivity(intent);
            }
        });
    }

    private void inicializarComponentes() {

        ListarProductos=findViewById(R.id.btnListarProductos);
        AgregarProductos=findViewById(R.id.btnAgregarProducto);
        CompraProductos=findViewById(R.id.btnCompraProducto);
    }

}