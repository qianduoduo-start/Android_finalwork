package com.example.finalwork;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActitvty extends AppCompatActivity {
    TextView pyTv,bsTv,cyuTv,twenTv,juziTv;
    EditText ziEt;
    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    protected void initView(){
        pyTv = findViewById(R.id.main_tv_pinyin);
        bsTv = findViewById(R.id.main_tv_bushou);
        cyuTv = findViewById(R.id.main_tv_chengyu);
        twenTv = findViewById(R.id.main_tv_tuwen);
        juziTv = findViewById(R.id.mian_tv_juzi);
        ziEt = findViewById(R.id.mian_et);
    }

    public void onClick(View view){
       switch (view.getId()){
           case R.id.main_iv_setting:

               break;
           case R.id.main_iv_search:

               break;
           case R.id.main_tv_pinyin:

               break;
           case R.id.main_tv_bushou:

               break;
           case R.id.main_tv_chengyu:

               break;
           case R.id.main_tv_tuwen:

               break;
       }
    }
}
