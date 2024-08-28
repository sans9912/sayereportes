package app.empresa.ferreland.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.empresa.ferreland.R;
import app.empresa.ferreland.entidades.Productos;

public class CompraProducto extends AppCompatActivity {
    EditText CodB, NProducto,Cantidad,PrecioC,usuario;
    Button BuscarProducto, Aceptar;
    ImageButton Escanear;
    Productos producto;
    int idProducto=0,precio=0,cantidadA=0;
    String UsuarioN="sansypapyrus95";
    int IdU=1;
    String CodigoPProducto="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra_producto);
        inicializarComponentes();
        //CodB.setInputType(InputType.TYPE_NULL);

        usuario.setInputType(InputType.TYPE_NULL);
        NProducto.setInputType(InputType.TYPE_NULL);
        Escanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator=new IntentIntegrator(CompraProducto.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Lector - CDB");
                integrator.setCameraId(0);//Camara trasera
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });
        BuscarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo= String.valueOf(CodB.getText())+"";

                if(codigo!=""){
                    buscarp(codigo);
                    usuario.setText(UsuarioN);
                }else {
                    Toast.makeText(CompraProducto.this,"Ingrese el código de barras", Toast.LENGTH_LONG).show();
                }

            }
        });
        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idProducto!=0&&Cantidad.getText().toString().length()!=0&&PrecioC.getText().toString().length()!=0){
                    int cantidad=Integer.parseInt(Cantidad.getText().toString());
                    int Precio= Integer.parseInt(PrecioC.getText().toString());
                    AgregarCompra(Precio,idProducto,IdU,cantidad);

                }else{
                    Toast.makeText(CompraProducto.this, "Completar todos los campos",Toast.LENGTH_LONG).show();
                }



            }
        });
    }

    private void buscarp(String codigo) {
        String url="http://www.ferreland.somee.com/api/Productos/getByCode/"+codigo;
        StringRequest postRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int  obji= jsonObject.getInt("exito");

                    if (obji==0){
                        limpiar();
                        String obj= jsonObject.getString("mensaje");
                        Toast.makeText(CompraProducto.this,obj,Toast.LENGTH_LONG).show();

                    }else{
                        JSONObject objC= jsonObject.getJSONObject("data");
                        idProducto=objC.getInt("idProducto");
                        NProducto.setText(objC.getString("nombre"));
                        usuario.setText(UsuarioN);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(CompraProducto.this).add(postRequest);

    }
    private void AgregarCompra(int precio, int idProducto, int idU, int cantidad) {
        String url="http://www.ferreland.somee.com/api/Compra/addCompra";
        JSONObject js=new JSONObject();
        try{
            js.put("idCompra", 0);
            js.put("precioCompra",precio);
            js.put("idProducto",idProducto);
            js.put("idEmpleado", idU);
            js.put("cantidad", cantidad);

        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String objM= response.getString("mensaje");
                    if(objM!=null){
                        Toast.makeText(CompraProducto.this, objM, Toast.LENGTH_LONG).show();
                        limpiar();
                    }else{
                        Toast.makeText(CompraProducto.this, "Error al agregar la compra", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(CompraProducto.this, "Error Compra no agregada", Toast.LENGTH_LONG).show();
                    throw new RuntimeException(e);
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CompraProducto.this, "Error "+error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        Volley.newRequestQueue(CompraProducto.this).add(jsonObjectRequest);
    }

    private void limpiar() {
        CodB.setText("");
        usuario.setText("");
        PrecioC.setText("");
        Cantidad.setText("");
        NProducto.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this,"Lectura Cancelada",Toast.LENGTH_LONG).show();
            }else{
                //Toast.makeText(this,result.getContents(),Toast.LENGTH_SHORT).show();
                CodB.setText(result.getContents());
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menup,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.agregarp){
            agregarPro();
            return true;
        } else if (item.getItemId()==R.id.listarP) {
            listarPro();
            return true;
        }
        else if (item.getItemId()==R.id.compraP) {
            comprapro();
            return true;
        }
        else if (item.getItemId()==R.id.menui) {
            menui();
            return true;
        }

        else{
            return super.onOptionsItemSelected(item);
        }
    }
    public void menui(){
        Intent intent=new Intent(this,MenuProductos.class);
        startActivity(intent);
    }
    public void agregarPro(){
        Intent intent=new Intent(this,AgregarProducto.class);
        startActivity(intent);
    }
    public void listarPro(){
        Intent intent=new Intent(this,ListarProductos.class);
        startActivity(intent);
    }
    public void comprapro(){
        Intent intent=new Intent(this,CompraProducto.class);
        startActivity(intent);
    }
    private void inicializarComponentes() {
        CodB=findViewById(R.id.txtCodigoB);
        NProducto=findViewById(R.id.txtNombreP);
        Cantidad=findViewById(R.id.txtCantidadC);
        PrecioC=findViewById(R.id.txtPrecioCompra);
        usuario=findViewById(R.id.txtUsuario);
        BuscarProducto=findViewById(R.id.btnBuscar);
        Aceptar=findViewById(R.id.btnAceptar);
        Escanear=findViewById(R.id.btnEscanear);

    }
}