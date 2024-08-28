package app.empresa.ferreland.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.empresa.ferreland.R;
import app.empresa.ferreland.entidades.Elemento;

public class EditarProducto extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 2;
    EditText Nombre, Descripcion, Precio, Stock;
    FloatingActionButton fabEditar;
    Button Modificar;
    Spinner Estado,Marca,Catgoria;
    ImageButton AbrirImagen;
    int id=0;
    int pEstado=3;
    ImageView Mostrar;
    String ruta="",rutaanterior="";
    String rutaimagensubida="";
    List<Elemento> listaCategorias = new ArrayList<>();
    List<Elemento> listaMarcas = new ArrayList<>();
    List<String>listaDescripcionesC = new ArrayList<>();
    List<String> listaDescripcionesM = new ArrayList<>();
    int ma=0,cat=0;

    String nomI;
    StorageReference storageReference;
    String storage_path="producto/*";
    String photo="img";
    String idd="p1";
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;
    Uri URIIMAGENSTORAGE;
    Boolean se=false;
    int idProducto,idc,idm,s;
    String n="",d="",p="";
    boolean estado;
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

        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        String url="http://www.ferreland.somee.com/api/Productos/getById/"+id;
        StringRequest postRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject objC= jsonObject.getJSONObject("data");
                    if (objC==null){
                        String obj= jsonObject.getString("mensaje");
                        Toast.makeText(EditarProducto.this,obj,Toast.LENGTH_LONG).show();
                    }else{
                        Nombre.setText(objC.getString("nombre"));
                        Descripcion.setText(objC.getString("descripcion"));
                        Precio.setText(objC.getString("precio"));
                        Stock.setText(objC.getString("stock"));
                        boolean estado=objC.getBoolean("activo");
                        cat=objC.getInt("idCategoria");
                        ma=objC.getInt("idMarca");
                        ruta=objC.getString("rutaImagen");
                        rutaanterior=ruta;


                        String[] itemsE = {"Inactivo", "Activo"};

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditarProducto.this, android.R.layout.simple_spinner_item,itemsE);
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
        Volley.newRequestQueue(EditarProducto.this).add(postRequest);

        AbrirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSelector();
            }
        });
        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idProducto=id;
                n=Nombre.getText()+"";
                d=Descripcion.getText()+"";
                idm=0;
                idc=0;
                s=0;
                p=String.valueOf(Precio.getText());
                if(Stock.getText().length()!=0){
                    s=Integer.parseInt(String.valueOf(Stock.getText()));
                }


                pEstado=Estado.getSelectedItemPosition();
                if (pEstado==0){
                    estado=false;
                }else{
                    estado=true;
                }
                int pc=Catgoria.getSelectedItemPosition();
                int pm=Marca.getSelectedItemPosition();
                String nc=listaDescripcionesC.get(pc);
                String nm=listaDescripcionesM.get(pm);
                for (int i = 0; i < listaCategorias.size(); i++) {
                    Elemento elemento = listaCategorias.get(i);
                    int id = elemento.getEntero();
                    String descripcion = elemento.getCadena();

                    if (descripcion == nc) {
                      idc=id;
                    }
                }
                for (int i = 0; i < listaMarcas.size(); i++) {
                    Elemento elemento = listaMarcas.get(i);
                    int id = elemento.getEntero();
                    String descripcion = elemento.getCadena();

                    if (descripcion == nm) {
                        idm=id;
                    }
                }



                if(n!=""&&d!=""&&p.length()!=0&&s!=0&& ruta!=null&&idc!=0&& idm!=0){
                    if(rutaanterior!=ruta){
                        nomI=n;
                        idd=n;
                        subirPhoto(URIIMAGENSTORAGE);
                        Toast.makeText(EditarProducto.this,"Cargando Datos...",Toast.LENGTH_LONG).show();
                    }else{
                        modificarPro(idProducto,n,d,idm,idc,p,s,ruta,estado);
                    }

                }else{
                    Toast.makeText(EditarProducto.this, "DEBE LLENAR TODOS LOS CAMPOS",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void subirPhoto(Uri uriimagenstorage) {
        String rute_storage_photo = storage_path + "" + photo + "" + nomI +""+ idd;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(uriimagenstorage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if (uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo", download_uri);

                            mfirestore.collection("producto").document(idd).update(map);
                            rutaimagensubida=uri.toString();
                            Toast.makeText(EditarProducto.this,"Datos Guardados",Toast.LENGTH_LONG).show();
                            se=true;
                            try {
                                Thread.sleep(500);
                                if(se){


                                    if (ruta!=""){
                                        modificarPro(idProducto,n,d,idm,idc,p,s,rutaimagensubida,estado);
                                        //AgregarPro(nombref,descripcionf,IdMarcaf,IdCategoriaf,precio,stock,rutaimagensubida,CodigoBarrasf,estadof);
                                    }

                                }else {
                                    Toast.makeText(EditarProducto.this,"Error al cargar datos",Toast.LENGTH_LONG).show();
                                }
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarProducto.this, "Error"+e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void modificarPro(int idProducto, String n, String d, int idm, int idc, String p, int s, String cd, boolean estado) {
        String url="http://www.ferreland.somee.com/api/Productos/updateProducto";
        JSONObject js=new JSONObject();
        try{
            js.put("idProducto", idProducto);
            js.put("nombre",n);
            js.put("descripcion",d);
            js.put("idMarca", idm);
            js.put("idCategoria", idc);
            js.put("precio",p);
            js.put("stock", s);
            js.put("rutaImagen",cd);
            js.put("activo", estado);

        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.PUT, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject objC= response.getJSONObject("data");
                    int id=objC.getInt("idProducto");
                    String objM= response.getString("mensaje");
                    Toast.makeText(EditarProducto.this, objM, Toast.LENGTH_LONG).show();
                    VerRegistro();
                } catch (JSONException e) {
                    Toast.makeText(EditarProducto.this, "Error Producto no modificado", Toast.LENGTH_LONG).show();
                    throw new RuntimeException(e);
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditarProducto.this, "Error "+error.getMessage(), Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(EditarProducto.this).add(jsonObjectRequest);
    }

    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    private void openImage(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setData(uri);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_OPEN_DOCUMENT);
    }

    private void insertImagePath(Uri uri) {
        String imagePath = uri.toString();
        //Toast.makeText(this,"Imp: "+imagePath,Toast.LENGTH_LONG).show();
        ruta=imagePath;
        showImageFromPath(ruta);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            openImage(selectedImageUri);
        } else if (requestCode == REQUEST_CODE_OPEN_DOCUMENT && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            insertImagePath(imageUri);
            URIIMAGENSTORAGE=imageUri;
        }
    }

    private void VerRegistro(){
        Intent intent=new Intent(this, VerProducto.class);
        intent.putExtra("ID",id);
        startActivity(intent);
    }

    private void inicializarComponentes() {
        Nombre=findViewById(R.id.txtNombreM);
        Descripcion=findViewById(R.id.txtDescripcionM);
        Precio=findViewById(R.id.txtPrecioM);
        Stock=findViewById(R.id.txtStockM);
        Modificar=findViewById(R.id.btnModificar);
        Estado=findViewById(R.id.SEstadoM);
        Marca=findViewById(R.id.SmarcaM);
        Catgoria=findViewById(R.id.SCategoriaM);
        fabEditar=findViewById(R.id.fabEditar);
        AbrirImagen=findViewById(R.id.btnImagenA);
        Mostrar=findViewById(R.id.imagenVer);
        AbrirImagen.setVisibility(View.VISIBLE);
        fabEditar.setVisibility(View.INVISIBLE);
        Estado.setEnabled(true);
        Marca.setEnabled(true);
        Catgoria.setEnabled(true);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditarProducto.this, android.R.layout.simple_spinner_item, listaDescripcionesC);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Catgoria.setAdapter(adapter);
                    Catgoria.setOnItemSelectedListener(EditarProducto.this);



                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(EditarProducto.this).add(postRequest);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditarProducto.this, android.R.layout.simple_spinner_item, listaDescripcionesM);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Marca.setAdapter(adapter);
                    Marca.setOnItemSelectedListener(EditarProducto.this);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(EditarProducto.this).add(postRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       /* if(parent==Estado){
            pEstado=position;
        }
        if(parent==Marca){
            pMarca=position;
        }
        if(parent==Catgoria){
            pCategoria=position;
        }*/
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