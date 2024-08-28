package app.empresa.ferreland.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.empresa.ferreland.R;

import app.empresa.ferreland.adapter.AdapterVenta;
import app.empresa.ferreland.retrofit.FerrelandClient;
import app.empresa.ferreland.retrofit.IFerreladService;
import app.empresa.ferreland.retrofit.response.ResponseVenta;
import app.empresa.ferreland.ui.MainActivity;
import app.empresa.ferreland.utidades.CustomAlertDialog;
import app.empresa.ferreland.utidades.Tools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

public class VentaFragment extends Fragment {

    public AVLoadingIndicatorView avLoadingIndicatorView;
    public List<ResponseVenta> itemsVentas;
    public AdapterVenta adapterVenta;
    public RecyclerView rvVenta;
    public MainActivity mainActivity;
    public RelativeLayout rlOnlineNetwork;
    public ItemTouchHelper.Callback callback;
    public FerrelandClient ferrelandClient = null;
    public IFerreladService icreativeService;
    public CustomAlertDialog customAlertDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    public VentaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customAlertDialog = new CustomAlertDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ventas, container, false);
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
        rvVenta = (RecyclerView) view.findViewById(R.id.rvVenta);
        avLoadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.load_indicator_view_cont);
    }

    private void initialize() {
        rvVenta.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvVenta.setHasFixedSize(true);
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
            rvVenta.setVisibility(View.VISIBLE);
            doToAVenta();
        } else {
            rlOnlineNetwork.setVisibility(View.VISIBLE);
            avLoadingIndicatorView.hide();
            rvVenta.setVisibility(View.GONE);
        }
    }

    public void doToAVenta() {
        Call<List<ResponseVenta>> call = icreativeService.doVenta();
        call.enqueue(new Callback<List<ResponseVenta>>() {
            @Override
            public void onResponse(Call<List<ResponseVenta>> call, Response<List<ResponseVenta>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    itemsVentas = response.body();
                    adapterVenta = new AdapterVenta(getContext(), itemsVentas);
                    rvVenta.setAdapter(adapterVenta);
                } else {
                    customAlertDialog.showWarningDialog(getActivity(), "Algo salio mal, vuelva a intentarlo");
                }
                avLoadingIndicatorView.hide();
            }

            @Override
            public void onFailure(Call<List<ResponseVenta>> call, Throwable t) {
                Log.d("node", t.getMessage());
                customAlertDialog.showErrorDialog(getActivity(), t.getMessage());
                avLoadingIndicatorView.hide();
                //Error de conexion
            }
        });
    }

}