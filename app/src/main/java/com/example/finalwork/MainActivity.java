package com.example.finalwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
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
        Intent intent =  new Intent();
        switch (view.getId()){
            case R.id.main_iv_setting:

                break;
            case R.id.main_iv_search:
                String text = ziEt.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    intent.setClass(this,WordInfoActivity.class);
                    intent.putExtra("zi",text);
                    startActivity(intent);
                }
                break;
            case R.id.main_tv_pinyin:
                intent.setClass(this,SearchPinyinActivity.class);
                startActivity(intent);
                break;
            case R.id.main_tv_bushou:
                intent.setClass(this,SearchBushouActivity.class);
                startActivity(intent);
                break;
            case R.id.main_tv_chengyu:

                break;
            case R.id.main_tv_tuwen:

                break;
        }
    }
}
