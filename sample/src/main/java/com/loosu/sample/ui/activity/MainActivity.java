package com.loosu.sample.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loosu.sample.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //findViewById(R.id.btn).setOnClickListener(this);
    }

//    public void onClick(View view) {
//        Intent service = new Intent();
//        startService(service);
//    }


}
