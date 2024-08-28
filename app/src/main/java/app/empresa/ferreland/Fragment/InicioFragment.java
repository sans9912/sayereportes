package app.empresa.ferreland.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import app.empresa.ferreland.R;


import app.empresa.ferreland.common.Constantes;
import app.empresa.ferreland.common.SharedPreferencesManager;
import app.empresa.ferreland.retrofit.FerrelandClient;
import app.empresa.ferreland.retrofit.IFerreladService;
import app.empresa.ferreland.ui.MenuProductos;
import app.empresa.ferreland.utidades.CustomAlertDialog;
import app.empresa.ferreland.utidades.Tools;

import com.wang.avi.AVLoadingIndicatorView;

public class InicioFragment extends Fragment {
    ImageButton boton;

    public Activity activity;
    public CustomAlertDialog customAlertDialog;
    public AVLoadingIndicatorView avLoadingIndicatorView;
    public RelativeLayout rlOnlineNetwork;
    public FerrelandClient ferrelandClient = null;
    public IFerreladService icreativeService;

    public TextView tvToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) getActivity();
        customAlertDialog = new CustomAlertDialog();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        retrofitInit();
        findviews(view);
        initialize();
        events();
        loadData();
        /*Bundle bundle = getArguments();
        if (bundle != null) {
            String id = bundle.getString("ID");
            Toast.makeText(getContext(),"ID: "+id,Toast.LENGTH_LONG).show();
            // Haz lo que necesites con el ID recibido
        }*/
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MenuProductos.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void retrofitInit() {
        this.ferrelandClient = FerrelandClient.getInstance();
        this.icreativeService = ferrelandClient.getCreativeService();
    }

    private void findviews(View view) {
        boton=(ImageButton) view.findViewById(R.id.btnImage);
        tvToken = (TextView) view.findViewById(R.id.tvToken);
        rlOnlineNetwork = (RelativeLayout) view.findViewById(R.id.rlOnlineNetwork);
        avLoadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.load_indicator_view_cont);
    }

    private void initialize() {
        String token = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_TOKEN);
        tvToken.setText(token);
    }

    private void events() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        Tools.isOnlineShowDialog(activity);
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadData() {
        if (Tools.isOnline(getContext())) {
            rlOnlineNetwork.setVisibility(View.GONE);
        } else {
            rlOnlineNetwork.setVisibility(View.VISIBLE);
            avLoadingIndicatorView.hide();
        }
    }
}