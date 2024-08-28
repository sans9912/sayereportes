package app.empresa.ferreland.utidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import app.empresa.ferreland.R;


public class CustomAlertDialog {

    public void showErrorDialog(final Activity context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Creative_AlertDialogCustom);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_error_dialog, (ConstraintLayout) context.findViewById(R.id.layoutDialogContainerError));
        builder.setCancelable(false);
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitleError))
                .setText(context.getResources().getString(R.string.error_title));
        ((TextView) view.findViewById(R.id.textMessageError))
                .setText(message);
        ((Button) view.findViewById(R.id.buttonActionError))
                .setText(context.getResources().getString(R.string.acept));
        ((ImageView) view.findViewById(R.id.imageIconError))
                .setImageResource(R.drawable.ic_error);
        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.buttonActionError).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    public void showSuccessDialog(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Theme_Creative_AlertDialogCustom);
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_success_dialog, (ConstraintLayout)
                activity.findViewById(R.id.layoutDialogContainerSuccess));
        builder.setCancelable(false);
        builder.setView(view);

        ((TextView) view.findViewById(R.id.textTitleSuccess))
                .setText(activity.getResources().getString(R.string.success_title));
        ((TextView) view.findViewById(R.id.textMessageSuccess))
                .setText(message);
        ((Button) view.findViewById(R.id.buttonActionSuccess))
                .setText(activity.getResources().getString(R.string.acept));
        ((ImageView) view.findViewById(R.id.imageIconSuccess))
                .setImageResource(R.drawable.ic_done);
        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.buttonActionSuccess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    public void showWarningDialog(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Theme_Creative_AlertDialogCustom);
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_warning_dialog, (ConstraintLayout) activity.findViewById(R.id.layoutDialogContainerWarning));
        builder.setCancelable(false);
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitleWarning))
                .setText(activity.getResources().getString(R.string.warning_title));
        ((TextView) view.findViewById(R.id.textMessageWarning))
                .setText(message);
        ((Button) view.findViewById(R.id.btnyesWarnning))
                .setText(activity.getResources().getString(R.string.acept));
        ((Button) view.findViewById(R.id.btnNoWarning))
                .setText(activity.getResources().getString(R.string.no));
        ((ImageView) view.findViewById(R.id.imageIconWarning))
                .setImageResource(R.drawable.ic_warning);
        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btnyesWarnning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.btnNoWarning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}
