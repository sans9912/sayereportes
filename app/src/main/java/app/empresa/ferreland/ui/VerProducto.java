package app.empresa.ferreland.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.empresa.ferreland.R;
import app.empresa.ferreland.entidades.Elemento;

public class VerProducto extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText Nombre, Descripcion, Precio, Stock;
    ImageButton AbrirImagen;
    Button Modificar;
    FloatingActionButton fabEditar;
    Spinner Estado,Marca,Catgoria;

    ImageView Mostrar;
    int id=0;
    String ruta="";
    List<Elemento> listaCategorias = new ArrayList<>();
    List<Elemento> listaMarcas = new ArrayList<>();
    List<String>listaDescripcionesC = new ArrayList<>();
    List<String> listaDescripcionesM = new ArrayList<>();
    int ma=0,cat=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_producto);
        inicializarComponentes();

        if(savedInstanceState==null){
            Bundle extras=getIntent().getExtras();
            if(extras==null){
                id=Integer.parseInt(null);
            }else{
                id=extras.getInt("ID");

            }
        }else{
            id=(int)savedInstanceState.getSerializable("ID");
        }
        LeerWs();
        LeerMa();
        String url="http://www.ferreland.somee.com/api/Productos/getById/"+id;
        StringRequest postRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject objC= jsonObject.getJSONObject("data");
                    if (objC==null){
                        String obj= jsonObject.getString("mensaje");
                        Toast.makeText(VerProducto.this,obj,Toast.LENGTH_LONG).show();
                    }else{
                        Nombre.setText(objC.getString("nombre"));
                        Descripcion.setText(objC.getString("descripcion"));
                        Precio.setText(objC.getString("precio"));
                        Stock.setText(objC.getString("stock"));
                        boolean estado=objC.getBoolean("activo");
                        cat=objC.getInt("idCategoria");
                        ma=objC.getInt("idMarca");
                        ruta=objC.getString("rutaImagen");

                        String[] itemsE = {"Inactivo", "Activo"};

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(VerProducto.this, android.R.layout.simple_spinner_item,itemsE);
                        Estado.setAdapter(adapter);
                        if(!estado){
                            Estado.setSelection(0);
                        }else{
                            Estado.setSelection(1);
                        }

                        for (int i = 0; i < listaCategorias.size(); i++) {
                            Elemento elemento = listaCategorias.get(i);
                            int id = elemento.getEntero();
                            String descripcion = elemento.getCadena();

                            if (id == cat) {
                                for (int j = 0; j < listaDescripcionesC.size(); j++) {
                                    String el = listaDescripcionesC.get(i);
                                    if ( el == descripcion) {
                                        Catgoria.setSelection(i);
                                        break;
                                    }
                                }
                                break;
                            }
                        }

                        for (int i = 0; i < listaMarcas.size(); i++) {
                            Elemento elemento = listaMarcas.get(i);
                            int id = elemento.getEntero();
                            String descripcion = elemento.getCadena();

                            if (id == ma) {
                                for (int j = 0; j < listaDescripcionesM.size(); j++) {
                                    String el = listaDescripcionesM.get(i);
                                    if ( el == descripcion) {
                                        Marca.setSelection(i);
                                        break;
                                    }
                                }
                                break;
                            }
                        }



                        Modificar.setVisibility(View.INVISIBLE);
                        Nombre.setInputType(InputType.TYPE_NULL);
                        Descripcion.setInputType(InputType.TYPE_NULL);
                        Precio.setInputType(InputType.TYPE_NULL);
                        Stock.setInputType(InputType.TYPE_NULL);
                        showImageFromPath(ruta);
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
        Volley.newRequestQueue(VerProducto.this).add(postRequest);




        fabEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VerProducto.this, EditarProducto.class );
                intent.putExtra("ID",id);
                startActivity(intent);
            }
        });

    }
    private void showImageFromPath(String imagePath) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.herramientas) // Imagen de relleno mientras se carga la imagen
                .error(R.drawable.ic_launcher_background); // Imagen de error si no se puede cargar la imagen

        Glide.with(this)
                .load(imagePath)
                .apply(options)
                .into(Mostrar);
    }
    private void inicializarComponentes() {
        Nombre=findViewById(R.id.txtNombreM);
        Descripcion=findViewById(R.id.txtDescripcionM);
        Precio=findViewById(R.id.txtPrecioM);
        Stock=findViewById(R.id.txtStockM);
        Modificar=findViewById(R.id.btnModificar);
        fabEditar=findViewById(R.id.fabEditar);
        Estado=findViewById(R.id.SEstadoM);
        Marca=findViewById(R.id.SmarcaM);
        Catgoria=findViewById(R.id.SCategoriaM);
        AbrirImagen=findViewById(R.id.btnImagenA);
        Mostrar=findViewById(R.id.imagenVer);
        Estado.setEnabled(false);
        Marca.setEnabled(false);
        Catgoria.setEnabled(false);
        AbrirImagen.setVisibility(View.INVISIBLE);


    }
    private void LeerWs(){
        String url="http://www.ferreland.somee.com/api/Categorias/getAll";
        StringRequest postRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray objC= jsonObject.getJSONArray("data");
                    for (int i = 0; i < objC.length(); i++) {
                        JSONObject dataObject = objC.getJSONObject(i);
                        String descripcion = dataObject.getString("descripcion");
                        int id=dataObject.getInt("idCategoria");
                        Elemento elemento = new Elemento(id, descripcion);
                        listaCategorias.add(elemento);
                    }


                    for (Elemento elemento : listaCategorias) {
                        listaDescripcionesC.add(elemento.getCadena());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(VerProducto.this, android.R.layout.simple_spinner_item, listaDescripcionesC);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Catgoria.setAdapter(adapter);
                    Catgoria.setOnItemSelectedListener(VerProducto.this);



                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(VerProducto.this).add(postRequest);
    }
    private void LeerMa(){
        String url="http://www.ferreland.somee.com/api/Marcas/getAll";
        StringRequest postRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray objC= jsonObject.getJSONArray("data");
                    for (int i = 0; i < objC.length(); i++) {
                        JSONObject dataObject = objC.getJSONObject(i);
                        String descripcion = dataObject.getString("descripcion");
                        int id=dataObject.getInt("idMarca");
                        Elemento elemento = new Elemento(id, descripcion);
                        listaMarcas.add(elemento);
                    }


                    for (Elemento elemento : listaMarcas) {
                        listaDescripcionesM.add(elemento.getCadena());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(VerProducto.this, android.R.layout.simple_spinner_item, listaDescripcionesM);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Marca.setAdapter(adapter);
                    Marca.setOnItemSelectedListener(VerProducto.this);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(VerProducto.this).add(postRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
}