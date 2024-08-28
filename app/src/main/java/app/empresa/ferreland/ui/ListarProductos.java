package app.empresa.ferreland.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.empresa.ferreland.Adaptadores.ListaProductosAdapter;
import app.empresa.ferreland.R;
import app.empresa.ferreland.entidades.Productos;

public class ListarProductos extends AppCompatActivity implements SearchView.OnQueryTextListener {
    SearchView txtBuscar;
    RecyclerView listaProductos;
    ArrayList<Productos> lArrayProductos;
    ListaProductosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_productos);
        txtBuscar=findViewById(R.id.txtBuscar);
        listaProductos=findViewById(R.id.listaProductos);
        lArrayProductos=new ArrayList<>();
        listaProductos.setLayoutManager(new LinearLayoutManager(ListarProductos.this));
        leerDatos();
        txtBuscar.setOnQueryTextListener(this);


    }
    List<Productos> listaP= new ArrayList<>();
    private void leerDatos() {
        String url="http://www.ferreland.somee.com/api/Productos/getAll";
        StringRequest postRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray objC= jsonObject.getJSONArray("data");


                    for (int i = 0; i < objC.length(); i++) {
                        JSONObject dataObject = objC.getJSONObject(i);
                        int id=dataObject.getInt("idProducto");
                        String nombre= dataObject.getString("nombre");
                        int stock=dataObject.getInt("stock");
                        int precio=dataObject.getInt("precio");
                        String rutaimagen= dataObject.getString("rutaImagen");

                        Productos producto = new Productos();
                        producto=new Productos();
                        producto.setId(id);
                        producto.setNombre(nombre);
                        producto.setStock(stock);
                        producto.setPrecio(precio);
                        producto.setRutaimagen(rutaimagen);
                        lArrayProductos.add(producto);

                    }

                    adapter=new ListaProductosAdapter(lArrayProductos);
                    listaProductos.setAdapter(adapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(ListarProductos.this).add(postRequest);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filtrado(newText);
        return false;
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