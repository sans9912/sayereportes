package app.empresa.ferreland.utidades;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class NetworkViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> mConnected = new MutableLiveData<>();

    public NetworkViewModel(Application app) {
        super(app);

        ConnectivityManager manager = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build();

            manager.registerNetworkCallback(networkRequest, new ConnectivityManager.NetworkCallback() {
                public void onAvailable(@NonNull Network network) {
                    mConnected.postValue(true);
                }

                public void onLost(@NonNull Network network) {
                    mConnected.postValue(false);
                }

                public void onUnavailable() {
                    mConnected.postValue(false);
                }
            });
        } else {
            mConnected.setValue(true);
        }
    }

    @NonNull
    public MutableLiveData<Boolean> getConnected() {
        return mConnected;
    }
}