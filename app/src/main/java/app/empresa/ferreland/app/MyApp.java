package app.empresa.ferreland.app;

import android.app.Application;
import android.content.Context;

import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;

public class MyApp extends Application {

    private static MyApp instance;


    public static MyApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        InternetAvailabilityChecker.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        InternetAvailabilityChecker.getInstance().removeAllInternetConnectivityChangeListeners();
    }

}
