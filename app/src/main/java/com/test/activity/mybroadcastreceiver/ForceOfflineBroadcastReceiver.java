package com.test.activity.mybroadcastreceiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.WindowManager;

import com.test.activity.LoginActivity;
import com.test.activity.activity_manager.ActivityManager;

/**
 * Created by hasse on 2020/1/8.
 */

public class ForceOfflineBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("你将被强制下线");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int p) {
                ActivityManager.finishAllActivity();
                Intent i = new Intent(context, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        AlertDialog alertDialog = builder.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
//            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);

        } else {
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        alertDialog.show();
    }
}
