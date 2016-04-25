package it.csbeng.androidbase.androidbase.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.csbeng.androidbase.androidbase.core.BaseCore;
import it.csbeng.androidbase.androidbase.tools.BaseFuture;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseFuture<Integer> f = (BaseFuture)new BaseCore<Void, Void, Void>(this) {
            @Override
            protected void execute(Void aVoid) {

            }
        }.getResult();


    }
}
