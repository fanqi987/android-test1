package com.test.activity.mybroadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by hasse on 2020/1/8.
 */

public class AnotherMyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "这是广播接收器2", Toast.LENGTH_SHORT).show();
                abortBroadcast();

    }
}
