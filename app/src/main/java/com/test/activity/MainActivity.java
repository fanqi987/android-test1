package com.test.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.test.activity.mybroadcastreceiver.ForceOfflineBroadcastReceiver;
import com.test.activity.mybroadcastreceiver.R;

public class MainActivity extends BaseActivity {

    private Button offlineBtn;
    private ForceOfflineBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //todo 忘了是静态注册，多写了一遍动态。
//        receiver = new ForceOfflineBroadcastReceiver();
//        IntentFilter iff = new IntentFilter("com.test.activity.broadcast.OFFLINE_BROADCAST");
//        registerReceiver(receiver, iff);

        offlineBtn = (Button) findViewById(R.id.send_offline_broadcast_btn);
        offlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("com.test.activity.broadcast.OFFLINE_BROADCAST");
                sendBroadcast(i);
            }
        });
    }
}
