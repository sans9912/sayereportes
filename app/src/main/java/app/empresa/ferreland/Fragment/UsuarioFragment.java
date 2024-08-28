package app.empresa.ferreland.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import app.empresa.ferreland.R;


import app.empresa.ferreland.adapter.AdapterUsuario;
import app.empresa.ferreland.retrofit.FerrelandClient;
import app.empresa.ferreland.retrofit.IFerreladService;
import app.empresa.ferreland.retrofit.response.ResponseUsuario;
import app.empresa.ferreland.ui.MainActivity;
import app.empresa.ferreland.utidades.CustomAlertDialog;
import app.empresa.ferreland.utidades.Tools;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioFragment extends Fragment {

    public List<ResponseUsuario> itemsUsuario;
    public RecyclerView rvUsuario;
    public MainActivity mainActivity;
    public RelativeLayout rlOnlineNetwork;
    public AVLoadingIndicatorView avLoadingIndicatorView;
    public CustomAlertDialog customAlertDialog;
    public ItemTouchHelper.Callback callback;
    public FerrelandClient ferrelandClient = null;
    public IFerreladService icreativeService;
    public AdapterUsuario adapterUsuario;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    public UsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customAlertDialog = new CustomAlertDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuario, container, false);
        retrofitInit();
        findviews(view);
        initialize();
        events();
        loadData();
        return view;
    }

    private void retrofitInit() {
        this.ferrelandClient = FerrelandClient.getInstance();
        this.icreativeService = ferrelandClient.getCreativeService();
    }

    private void findviews(View view) {
        rvUsuario = (RecyclerView) view.findViewById(R.id.rvUsuario);
        avLoadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.load_indicator_view_cont);
        rlOnlineNetwork = (RelativeLayout) view.findViewById(R.id.rlOnlineNetwork);
    }

    private void initialize() {
        rvUsuario.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvUsuario.setHasFixedSize(true);
    }
    private void events() {    }

    private void ocultar(View v) {
        v.clearFocus(); //*Agregar!
        InputMethodManager input = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        input.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void loadData() {
        if (Tools.isOnline(getContext())) {
            rlOnlineNetwork.setVisibility(View.GONE);
            rvUsuario.setVisibility(View.VISIBLE);
            doToUsuario();
        } else {
            rlOnlineNetwork.setVisibility(View.VISIBLE);
            avLoadingIndicatorView.hide();
            rvUsuario.setVisibility(View.GONE);
        }
    }

    private void doToUsuario() {
        Call<List<ResponseUsuario>> call = icreativeService.doUsuario();
        call.enqueue(new Callback<List<ResponseUsuario>>() {
            @Override
            public void onResponse(Call<List<ResponseUsuario>> call, Response<List<ResponseUsuario>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    itemsUsuario = response.body();
                    adapterUsuario = new AdapterUsuario(getContext(), itemsUsuario);
                    rvUsuario.setAdapter(adapterUsuario);
                } else {
                    customAlertDialog.showWarningDialog(getActivity(), "Algo salio mal, vuelva a intentarlo");
                }
                avLoadingIndicatorView.hide();
            }

            @Override
            public void onFailure(Call<List<ResponseUsuario>> call, Throwable t) {
                customAlertDialog.showErrorDialog(getActivity(), "Error de conexion");
                avLoadingIndicatorView.hide();
            }
        });
    }
}