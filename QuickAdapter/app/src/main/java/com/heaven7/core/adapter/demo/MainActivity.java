package com.heaven7.core.adapter.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vida.android.util.Reflection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(new Reflection().hello());
    }
}
