package com.example.finalprojectapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import androidx.appcompat.app.AppCompatActivity;

/*Custom Alert Dialog Box */

public class AlertDialogBox {

    private Activity activity;
    private AlertDialog dialogBox;

    AlertDialogBox(Activity activity){
        this.activity = activity;
    }

    void startDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.alert_dialog_box,null));
        builder.setCancelable(false);

        dialogBox = builder.create();
        dialogBox.show();
    }

    void dismissDialog(){
        dialogBox.dismiss();
    }
}
