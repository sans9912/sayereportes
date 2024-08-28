package app.empresa.ferreland.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import app.empresa.ferreland.R;


import app.empresa.ferreland.adapter.AdapterProducto;
import app.empresa.ferreland.retrofit.FerrelandClient;
import app.empresa.ferreland.retrofit.IFerreladService;
import app.empresa.ferreland.retrofit.response.ResponseProducto;
import app.empresa.ferreland.ui.MainActivity;
import app.empresa.ferreland.utidades.CustomAlertDialog;
import app.empresa.ferreland.utidades.Tools;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoFragment extends Fragment {

    public AVLoadingIndicatorView avLoadingIndicatorView;
    public List<ResponseProducto> itemsProductos;
    public AdapterProducto adapterProducto;
    public RecyclerView rvProducto;
    public MainActivity mainActivity;
    public RelativeLayout rlOnlineNetwork;
    public FerrelandClient ferrelandClient = null;
    public IFerreladService icreativeService;
    public CustomAlertDialog customAlertDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    public ProductoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customAlertDialog = new CustomAlertDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producto, container, false);
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
        rlOnlineNetwork = (RelativeLayout) view.findViewById(R.id.rlOnlineNetwork);
        rvProducto = (RecyclerView) view.findViewById(R.id.rvProducto);
        avLoadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.load_indicator_view_cont);
    }

    private void initialize() {
        rvProducto.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvProducto.setHasFixedSize(true);
    }

    private void events() {
    }

    private void ocultar(View v) {
        v.clearFocus(); //*Agregar!
        InputMethodManager input = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        input.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void loadData() {
        if (Tools.isOnline(getContext())) {
            rlOnlineNetwork.setVisibility(View.GONE);
            rvProducto.setVisibility(View.VISIBLE);
            doToProducto();
        } else {
            rlOnlineNetwork.setVisibility(View.VISIBLE);
            rvProducto.setVisibility(View.GONE);
            avLoadingIndicatorView.hide();
        }
    }

    public void doToProducto() {
        Call<List<ResponseProducto>> call = icreativeService.doProducto();
        call.enqueue(new Callback<List<ResponseProducto>>() {
            @Override
            public void onResponse(@NonNull Call<List<ResponseProducto>> call, @NonNull Response<List<ResponseProducto>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    itemsProductos = response.body();
                    adapterProducto = new AdapterProducto(getContext(), itemsProductos);
                    rvProducto.setAdapter(adapterProducto);
                } else {
                    customAlertDialog.showWarningDialog(getActivity(), "Algo salio mal, vuelva a intentarlo");
                }
                avLoadingIndicatorView.hide();
            }

            @Override
            public void onFailure(@NonNull Call<List<ResponseProducto>> call, Throwable t) {
                customAlertDialog.showErrorDialog(getActivity(), t.getMessage());
                avLoadingIndicatorView.hide();
            }
        });
    }

}