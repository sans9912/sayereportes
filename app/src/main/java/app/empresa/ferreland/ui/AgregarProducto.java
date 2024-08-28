package app.empresa.ferreland.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.empresa.ferreland.R;
import app.empresa.ferreland.entidades.Elemento;

public class AgregarProducto extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 2;
    Button btnAgregar;
    ImageButton Escanear,AbrirImagen;
    EditText CodigoB, Nombre, Descripcion, Precio, Stock;
    Spinner SMarcas, SCategorias;
    int pMarca=0, pCategoria=0;
    ImageView Mostrar;
    String ruta="";
    String rutaimagensubida="";
    List<Elemento> listaCategorias = new ArrayList<>();
    List<Elemento> listaMarcas = new ArrayList<>();
    String nomI;
    StorageReference storageReference;
    String storage_path="producto/*";
    String photo="img";
    String idd="p1";
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;
    Uri URIIMAGENSTORAGE;
    Boolean s=false;
    String nombref="",descripcionf="",CodigoBarrasf="";
    int IdMarcaf=0, IdCategoriaf=0;
    Boolean estadof;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);
        IniciarComponentes();
        LeerWs();
        LeerMa();
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        Escanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator=new IntentIntegrator(AgregarProducto.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Lector - CDB");
                integrator.setCameraId(0);//Camara trasera
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });
        AbrirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSelector();
            }
        });



        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtenerMarcayCategoria();
                nombref=Nombre.getText()+"";
                descripcionf=Descripcion.getText()+"";
                IdMarcaf=pMarca;
                IdCategoriaf=pCategoria;
                CodigoBarrasf=CodigoB.getText()+"";
                estadof=true;
                if(nombref!="" && descripcionf!=""&&CodigoBarrasf!=""&&Precio.getText().length()!=0&&Stock.getText().length()!=0&&ruta.length()!=0){
                    nomI=nombref;
                    idd=CodigoBarrasf;
                    subirPhoto(URIIMAGENSTORAGE);
                    Toast.makeText(AgregarProducto.this,"Cargando Datos...",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(AgregarProducto.this,"Complete todos los campos",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void subirPhoto(Uri image_url) {
        String rute_storage_photo = storage_path + "" + photo + "" + nomI +""+ idd;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                            //ruta=uriTask.toString();
                            rutaimagensubida=uri.toString();
                            Toast.makeText(AgregarProducto.this,"Datos Guardados",Toast.LENGTH_LONG).show();
                            s=true;
                            try {
                                Thread.sleep(500);
                                if(s){
                                    int precio=Integer.parseInt(Precio.getText().toString());
                                    int stock=Integer.parseInt(Stock.getText().toString());

                                    if (ruta!=""){
                                        AgregarPro(nombref,descripcionf,IdMarcaf,IdCategoriaf,precio,stock,rutaimagensubida,CodigoBarrasf,estadof);
                                    }

                                    //Toast.makeText(AgregarProducto.this,"Ruta: "+rutaimagensubida,Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(AgregarProducto.this,"Error al cargar datos",Toast.LENGTH_LONG).show();
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
                Toast.makeText(AgregarProducto.this, "Error"+e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ObtenerMarcayCategoria() {
        Elemento elementoEncontrado = null;
        String M=SMarcas.getSelectedItem().toString();
        for (Elemento elemento : listaMarcas) {
            if (elemento.getCadena().equals(M)) {
                elementoEncontrado = elemento;
                break;
            }
        }
        pMarca=elementoEncontrado.getEntero();

        Elemento elementoEncontradoC = null;
        String C=SCategorias.getSelectedItem().toString();
        for (Elemento elemento : listaCategorias) {
            if (elemento.getCadena().equals(C)) {
                elementoEncontradoC = elemento;
                break;
            }
        }
        pCategoria=elementoEncontradoC.getEntero();
    }


    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
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
                    List<String> listaDescripcionesC = new ArrayList<>();

                    for (Elemento elemento : listaCategorias) {
                        listaDescripcionesC.add(elemento.getCadena());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AgregarProducto.this, android.R.layout.simple_spinner_item, listaDescripcionesC);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    SCategorias.setAdapter(adapter);
                    SCategorias.setOnItemSelectedListener(AgregarProducto.this);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(AgregarProducto.this).add(postRequest);
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
                    List<String> listaDescripcionesM = new ArrayList<>();

                    for (Elemento elemento : listaMarcas) {
                        listaDescripcionesM.add(elemento.getCadena());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AgregarProducto.this, android.R.layout.simple_spinner_item, listaDescripcionesM);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    SMarcas.setAdapter(adapter);
                    SMarcas.setOnItemSelectedListener(AgregarProducto.this);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(AgregarProducto.this).add(postRequest);
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
    private void limpiar() {
        CodigoB.setText("");
        Nombre.setText("");
        Descripcion.setText("");
        Precio.setText("");
        Stock.setText("");
        Mostrar.setImageResource(R.drawable.herramientas);
    }

    private void IniciarComponentes() {
        btnAgregar=findViewById(R.id.btnAgregarProducto);
        CodigoB=findViewById(R.id.txtcodB);
        Nombre=findViewById(R.id.txtNombreM);
        Descripcion=findViewById(R.id.txtDescripcionM);
        Precio=findViewById(R.id.txtPrecioM);
        Stock=findViewById(R.id.txtStockM);
        SMarcas=findViewById(R.id.SmarcaM);
        SCategorias=findViewById(R.id.SCategoriaM);
        Escanear=findViewById(R.id.btnEscanear);
        AbrirImagen=findViewById(R.id.btnImagenA);
        Mostrar=findViewById(R.id.imagenVer);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this,"Lectura Cancelada",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
                CodigoB.setText(result.getContents());
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            openImage(selectedImageUri);
        } else if (requestCode == REQUEST_CODE_OPEN_DOCUMENT && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            insertImagePath(imageUri);
            URIIMAGENSTORAGE=imageUri;
        }
        super.onActivityResult(requestCode, resultCode, data);




    }
    private void AgregarPro(String n, String d, int idm, int idc, int p, int s, String rutai, String cd, boolean estado) {
        String url="http://www.ferreland.somee.com/api/Productos/addProducto";
        JSONObject js=new JSONObject();
        try{
            js.put("idProducto", 0);
            js.put("nombre",n);
            js.put("descripcion",d);
            js.put("idMarca", idm);
            js.put("idCategoria", idc);
            js.put("precio",p);
            js.put("stock", s);
            js.put("rutaImagen",rutai);
            js.put("codigo",cd);
            js.put("activo", estado);

        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject objC= response.getJSONObject("data");
                    String objM= response.getString("mensaje");
                    Toast.makeText(AgregarProducto.this, objM, Toast.LENGTH_LONG).show();
                    ruta="";
                    limpiar();
                } catch (JSONException e) {
                    Toast.makeText(AgregarProducto.this, "Error Producto no agregado", Toast.LENGTH_LONG).show();
                    throw new RuntimeException(e);
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AgregarProducto.this, "Error "+error.getMessage(), Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(AgregarProducto.this).add(jsonObjectRequest);
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
