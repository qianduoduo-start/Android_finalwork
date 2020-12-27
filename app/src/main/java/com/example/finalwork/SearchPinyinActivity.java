package com.example.finalwork;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.finalwork.adapter.SearchLeftAdapter;
import com.example.finalwork.bean.PinBuBean;
import com.example.finalwork.bean.PinBuWordBean;
import com.example.finalwork.db.DBManager;
import com.example.finalwork.utils.AssetsUtils;
import com.example.finalwork.utils.CommonUtils;
import com.example.finalwork.utils.URLUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

public class SearchPinyinActivity extends BaseSearchActivity {

    //获取拼音数据的网址
    String url;
    @Override
   protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initData(CommonUtils.FILE_PINYIN,CommonUtils.TYPE_PINYIN);
        setExLvListener(CommonUtils.TYPE_PINYIN);
        exLv.expandGroup(0);
        word = "a"; //默认获取a
        url  = URLUtils.getPinyinurl(word,page,pagesize);
        loadData(url);
        setGVListener(CommonUtils.TYPE_PINYIN);
    }

    @Override
    public void onError(Throwable ex,boolean isOnCallback){
        List<PinBuWordBean.ResultBean.ListBean> list = DBManager.queryPyWordFromPywordtb(word, page, pagesize);
        refreshDataByGV(list);
    }
}