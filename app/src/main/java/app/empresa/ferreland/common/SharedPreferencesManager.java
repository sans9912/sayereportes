package app.empresa.ferreland.common;

import android.content.Context;
import android.content.SharedPreferences;

import app.empresa.ferreland.app.MyApp;

public class SharedPreferencesManager {

    private static final String APP_SETTINGS_FILE = "APP_SETTINGS";

    private SharedPreferencesManager() {
    }

    private static SharedPreferences getSharedPreferences() {
        return MyApp.getContext().getSharedPreferences(APP_SETTINGS_FILE, Context.MODE_PRIVATE);
    }

    //String
    public static void setSomeStringValue(String dataLabel, String dataValue) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(dataLabel, dataValue);
        editor.apply();
    }

    //Bool
    public static void setSomeBoolValue(String dataLabel, Boolean dataValue) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(dataLabel, dataValue);
        editor.apply();
    }

    //String
    public static String getSomeStringValue(String dataLabel) {
        return getSharedPreferences().getString(dataLabel, null);
    }

    //String
    public static Boolean getSomeBoolValue(String dataLabel) {
        return getSharedPreferences().getBoolean(dataLabel, false);
    }

    //Remove
    public static void removeSomeLabel(String dataLabel) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(dataLabel);
        editor.apply();
    }

    //Remove All
    public static void removeAll() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        editor.apply();

    }

}
