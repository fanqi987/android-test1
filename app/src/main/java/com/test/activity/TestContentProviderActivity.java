package com.test.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.test.activity.mybroadcastreceiver.R;

public class TestContentProviderActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_test);


    }
}
