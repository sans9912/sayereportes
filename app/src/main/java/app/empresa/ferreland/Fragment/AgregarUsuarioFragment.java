package app.empresa.ferreland.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;

import app.empresa.ferreland.R;

import app.empresa.ferreland.retrofit.FerrelandClient;
import app.empresa.ferreland.retrofit.IFerreladService;
import app.empresa.ferreland.ui.MainActivity;
import app.empresa.ferreland.utidades.CustomAlertDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AgregarUsuarioFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    public FerrelandClient ferrelandClient = null;
    public IFerreladService icreativeService;
    public MaterialButton btnSaveUsuario;
    public TextInputLayout cpdTypeLayoutRole;
    public AppCompatAutoCompleteTextView cpdTypeRole;
    public TextInputEditText etNombreU, etApellidoU, etCedulaU, etFechaU, etEmailU, etPasswordU;
    public TextInputLayout tlNombreU, tlApellidoU, tlCedulaU, tlFechaU, tlEmailU, tlPasswordU;

    public String idRole = "", idUsuario = "", imagen = "";
    public CustomAlertDialog customAlertDialog;
    public MainActivity mainActivity;
    DatePickerDialog dialogDatePicker;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    public AgregarUsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customAlertDialog = new CustomAlertDialog();
        dialogDatePicker = new DatePickerDialog(getActivity(), this, 2022, 06, 10);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);
        retrofitInit();
        findViews(view);
        events();
        goToRole();
        return view;

//        DatePickerDialog dpd = new DatePickerDialog(this,
//                AlertDialog.THEME_HOLO_LIGHT,null, 1960, 1, 1);
//        dpd.getDatePicker().setCalendarViewShown(false);
//        dpd.show();
    }

    private void retrofitInit() {
        this.ferrelandClient = FerrelandClient.getInstance();
        this.icreativeService = ferrelandClient.getCreativeService();
    }

    private void findViews(View v) {
        cpdTypeLayoutRole = v.findViewById(R.id.cpdTypeLayoutRole);
        cpdTypeRole = v.findViewById(R.id.cpdTypeRole);
        btnSaveUsuario = v.findViewById(R.id.btnSaveUsuario);

        //Form Layout
        tlNombreU = v.findViewById(R.id.tlNombreU);
        tlApellidoU = v.findViewById(R.id.tlApellidoU);
        tlCedulaU = v.findViewById(R.id.tlCedulaU);
        tlFechaU = v.findViewById(R.id.tlFechaU);
        tlEmailU = v.findViewById(R.id.tlEmailU);
        tlPasswordU = v.findViewById(R.id.tlPasswordU);

        //Form
        etNombreU = v.findViewById(R.id.etNombreU);
        etApellidoU = v.findViewById(R.id.etApellidosU);
        etCedulaU = v.findViewById(R.id.etCedulaU);
        etFechaU = v.findViewById(R.id.etFechaU);
        etEmailU = v.findViewById(R.id.etEmailU);
        etPasswordU = v.findViewById(R.id.etPasswordU);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void events() {
        btnSaveUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String nombre = String.valueOf(etNombreU.getText());
//                String apellido = String.valueOf(etApellidoU.getText());
//                String cedula = String.valueOf(etCedulaU.getText());
//                String fecha = String.valueOf(etFechaU.getText());
//                String email = String.valueOf(etEmailU.getText());
//                String password = String.valueOf(etPasswordU.getText());
//
//                setError(tlNombreU, nombre, "El Nombre es obligatorio");
//                setError(tlApellidoU, apellido, "El Apellido es obligatorio");
//                setError(tlCedulaU, cedula, "La Cedula es obligatorio");
//                setError(tlFechaU, fecha, "La Fecha es obligatorio");
//                setError(tlEmailU, email, "El Email es obligatorio");
//                setError(tlPasswordU, password, "El Password es obligatorio");
//                setError(cpdTypeLayoutRole, idRole, "El Rol es obligatorio");
//
//                Log.d("node", "" + nombre);
//                Log.d("node", "" + apellido);
//                Log.d("node", "" + cedula);
//                Log.d("node", "" + fecha);
//                Log.d("node", "" + email);
//                Log.d("node", "" + password);
//                Log.d("node", idRole);
//
//
//                if (nombre.isEmpty() ||
//                        apellido.isEmpty() ||
//                        cedula.isEmpty() ||
//                        fecha.isEmpty() ||
//                        email.isEmpty() ||
//                        password.isEmpty() ||
//                        idRole.isEmpty()) {
//                } else {
//                    RequestUser requestUser = new RequestUser(nombre, apellido, Integer.parseInt(cedula), fecha, email, password, "", idRole);
//                    Call<ResponseSuccess> call = icreativeService.toUser(requestUser);
//                    call.enqueue(new Callback<ResponseSuccess>() {
//                        @Override
//                        public void onResponse(Call<ResponseSuccess> call, Response<ResponseSuccess> response) {
//
//                            if (response.isSuccessful()) {
//                                customAlertDialog.showSuccessDialog(getActivity(), response.body().getMessage());
////                                Navigation.findNavController(getActivity(), R.id.fragment_activity_main).navigate(R.id.navigation_user);
//                            } else {
//
//
//                                ResponseError message = new Gson().fromJson(response.errorBody().charStream(), ResponseError.class);
//                                customAlertDialog.showErrorDialog(getActivity(), message.getMessage());
//
////                                JsonParser parser = new JsonParser();
////                                JsonElement mJson = null;
////                                try {
////                                    mJson = parser.parse(response.errorBody().string());
////                                    Gson gson = new Gson();
////                                    ResponseErrors errorResponse = gson.fromJson(mJson, ResponseErrors.class);
////
////                                } catch (IOException ex) {
////                                    ex.printStackTrace();
////                                }
//
//
////                                String error = "";
////                                ResponseErrors message = new Gson().fromJson(response.errorBody().charStream(), ResponseErrors.class);
////                                for (Error responseError : message.getErrors()) {
////                                    error = error + responseError.getMsg() + "\n";
////                                }
////                                customAlertDialog.showErrorDialog(getActivity(), error);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseSuccess> call, Throwable t) {
//                            customAlertDialog.showErrorDialog(getActivity(), "Error de conexion");
//                        }
//                    });
//
//                }
            }
        });

        etFechaU.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialogDatePicker.getDatePicker().setCalendarViewShown(false);
                dialogDatePicker.show();
                ocultar(v);
                return true;
            }
        });
    }

    public boolean setError(TextInputLayout textInputLayout, String data, String errorMessage) {
        if (data.isEmpty()) {
            textInputLayout.setError(errorMessage);
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }

    private void goToRole() {
//        Call<List<ResponseRole>> call = icreativeService.doRole();
//        call.enqueue(new Callback<List<ResponseRole>>() {
//            @Override
//            public void onResponse(Call<List<ResponseRole>> call, Response<List<ResponseRole>> response) {
//                if (response.isSuccessful()) {
//                    ArrayList<ResponseRole> responseRoles = new ArrayList<>();
//                    for (ResponseRole responseRole : response.body()) {
//                        responseRoles.add(new ResponseRole(responseRole.getTipo().toLowerCase(), responseRole.getId()));
//                    }
//                    ArrayAdapter<ResponseRole> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item_role, responseRoles);
//                    cpdTypeRole.setAdapter(arrayAdapter);
//                    cpdTypeRole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            idRole = responseRoles.get(position).getId();
//                        }
//                    });
//                } else {
//                    Toast.makeText(getActivity(), "Algo salio mal", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ResponseRole>> call, Throwable t) {
//
//            }
//        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d("node", "" + year);
        Log.d("node", "" + month);
        Log.d("node", "" + dayOfMonth);
        etFechaU.setText(new StringBuilder().append(dayOfMonth).append("/").append(month + 1).append("/").append(year));
    }

    private void ocultar(View v) {
        v.clearFocus(); //*Agregar!
        InputMethodManager input = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        input.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}