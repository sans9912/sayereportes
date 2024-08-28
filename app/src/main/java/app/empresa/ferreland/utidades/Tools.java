package app.empresa.ferreland.utidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import app.empresa.ferreland.R;

public class Tools {

    private static boolean DISPLAY_DEBUG = true;
    private final Context _context;

    public Tools(Context context) {
        this._context = context;
    }

    public static void noConnection(final Activity context, String message) {
        AlertDialog.Builder alerBuilder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(R.layout.lyt_error, (ConstraintLayout) context.findViewById(R.id.layoutDialogContainerError));
        alerBuilder.setView(view);
        alerBuilder.setCancelable(false);

        AlertDialog alertDialog = alerBuilder.create();

        if (isOnline(context)) {
            String messageText = "";
            if (message != null && DISPLAY_DEBUG) {
                messageText = "\n\n" + message;
            }

            ((TextView) view.findViewById(R.id.textTitleError)).setText(context.getResources().getString(R.string.title_dialog_connection));
            ((TextView) view.findViewById(R.id.textContentError)).setText(context.getResources().getString(R.string.content_dialog_connection_description) + messageText);

        } else {

            ((TextView) view.findViewById(R.id.textTitleError)).setText(context.getResources().getString(R.string.title_dialog_internet_title));
            ((TextView) view.findViewById(R.id.textContentError)).setText(context.getResources().getString(R.string.content_dialog_internet_description));

        }
        ((Button) view.findViewById(R.id.buttonActionError)).setText(context.getResources().getString(R.string.acept));


        view.findViewById(R.id.buttonActionError).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    public static void noConnection(final Activity context) {
        noConnection(context, null);
    }

    public static boolean isOnline(Context context) {
        boolean connected = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            // Recupera todas las redes (tanto móviles como wifi)
            NetworkInfo[] redes = connectivity.getAllNetworkInfo();
            for (NetworkInfo rede : redes) {
                // Si alguna red tiene conexión, se devuelve true
                if (rede.getState() == NetworkInfo.State.CONNECTED) {
                    connected = true;
                    break;
                }
            }
        }
        return connected;
    }

    public static boolean isOnlineShowDialog(Activity mActivity) {
        if (isOnline(mActivity))
            return true;
        else
            noConnection(mActivity);
        return false;
    }


}
