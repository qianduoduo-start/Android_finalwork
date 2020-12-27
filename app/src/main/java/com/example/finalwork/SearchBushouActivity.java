package com.example.finalwork;

import android.os.Bundle;

import com.example.finalwork.bean.PinBuWordBean;
import com.example.finalwork.db.DBManager;
import com.example.finalwork.utils.CommonUtils;
import com.example.finalwork.utils.URLUtils;

import java.util.List;

public class SearchBushouActivity extends BaseSearchActivity {
    //获取部首的url
    String url;
    @Override
   protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        titleTv.setText(R.string.main_tv_bushou);
        initData(CommonUtils.FILE_BUSHOU,CommonUtils.TYPE_BUSHOU);
        setExLvListener(CommonUtils.TYPE_BUSHOU);
        exLv.expandGroup(0);
        word = "丨";
        url  = URLUtils.getBushouurl(word,page,pagesize);
        loadData(url);
        setGVListener(CommonUtils.TYPE_BUSHOU);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        List<PinBuWordBean.ResultBean.ListBean> list = DBManager.queryBsWordFromPywordtb(word, page, pagesize);
        refreshDataByGV(list);
    }
}