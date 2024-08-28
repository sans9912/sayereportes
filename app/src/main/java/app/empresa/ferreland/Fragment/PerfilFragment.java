package app.empresa.ferreland.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import app.empresa.ferreland.R;
import app.empresa.ferreland.common.Constantes;
import app.empresa.ferreland.common.SharedPreferencesManager;
import app.empresa.ferreland.retrofit.FerrelandClient;
import app.empresa.ferreland.retrofit.IFerreladService;


public class PerfilFragment extends Fragment {

    TextView tvNombreProfile, tvCorreoProfile, tvRoleProfile;
    public FerrelandClient ferrelandClient = null;
    public IFerreladService icreativeService;
    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        findviews(view);
        loadData();
        return view;

    }

    private void findviews(View view) {
        tvNombreProfile = (TextView) view.findViewById(R.id.tvNombreProfile);
        tvCorreoProfile = (TextView) view.findViewById(R.id.tvCorreoProfile);
        tvRoleProfile = (TextView) view.findViewById(R.id.tvRoleProfile);
    }

    private void loadData() {
        String nombre = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USER_NOMBRE);
        String correo = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USER_EMAIL);
        String rol = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USER_ROLE);

        tvNombreProfile.setText(nombre.toString());
        tvCorreoProfile.setText(correo.toString());
        tvRoleProfile.setText(rol.toString());

    }

}