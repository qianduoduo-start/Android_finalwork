package com.example.finalwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.finalwork.bean.WordBean;
import com.example.finalwork.db.DBManager;
import com.example.finalwork.utils.URLUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class WordInfoActivity extends BaseActivity {
    TextView ziTv,pyTv,wubiTv,bihuaTv,bushouTv,jsTv,xxjsTv;
    ListView jsLv;
    String zi;
    List<String> mDatas;
    private ArrayAdapter<String> stringArrayAdapter;
    private ArrayAdapter<String> adapter;
    private List<String> jijie;
    private List<String> xiangjie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_info);
        //接受上一个页面传递过来的对象
        Intent intent = getIntent();
        zi = intent.getStringExtra("zi");
        String url = URLUtils.getWordurl(zi);
        initView();
        //初始化ListView
        mDatas = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.item_word_jslv, R.id.item_wordlv_tv, mDatas);
        jsLv.setAdapter(adapter);
        loadData(url);
    }

    private void initView() {
        ziTv = findViewById(R.id.word_tv_zi);
        wubiTv = findViewById(R.id.word_tv_wubi);
        pyTv = findViewById(R.id.word_tv_pinyin);
        bihuaTv = findViewById(R.id.word_tv_bihua);
        bushouTv = findViewById(R.id.word_tv_bushou);
        jsTv = findViewById(R.id.word_tv_js);
        xxjsTv = findViewById(R.id.word_tv_xxjs);
        jsLv = findViewById(R.id.word_lv_js);
    }
    @Override
    public void onSuccess(String json) {
        WordBean wordBean = new Gson().fromJson(json, WordBean.class);
        WordBean.ResultBean resultBean = wordBean.getResult();
        // 插入数据库
        DBManager.insertWordToWordtb(resultBean);
        // 将数据显示
        notifyView(resultBean);
    }

    private void notifyView(WordBean.ResultBean resultBean) {
        ziTv.setText(resultBean.getZi());
        pyTv.setText(resultBean.getPinyin());
        wubiTv.setText("五笔 : "+resultBean.getWubi());
        bihuaTv.setText("笔画 : "+resultBean.getBihua());
        bushouTv.setText("部首 : "+resultBean.getBushou());
        jijie = resultBean.getJijie();
        xiangjie = resultBean.getXiangjie();
        // 默认一进去，就显示基本解释
        mDatas.clear();
        mDatas.addAll(jijie);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        WordBean.ResultBean bean = DBManager.queryWordFromWordtb(zi);
        if (bean!=null) {
            notifyView(bean);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wordinfo_iv_back:
                finish();
                break;
            case R.id.word_tv_js:
                jsTv.setTextColor(Color.RED);
                xxjsTv.setTextColor(Color.BLACK);
                //清空之前数据
                mDatas.clear();
                mDatas.addAll(jijie);
                adapter.notifyDataSetChanged();
                break;
            case R.id.word_tv_xxjs:
                xxjsTv.setTextColor(Color.RED);
                jsTv.setTextColor(Color.BLACK);
                mDatas.clear();
                mDatas.addAll(xiangjie);
                adapter.notifyDataSetChanged();
                break;
        }
    }
}