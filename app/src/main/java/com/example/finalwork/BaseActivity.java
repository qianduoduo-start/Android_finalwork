package com.example.finalwork;

import androidx.appcompat.app.AppCompatActivity;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class BaseActivity extends AppCompatActivity implements Callback.CommonCallback<String> {
    //加载网络数据
    public void loadData(String path){
        //创建请求参数体
        RequestParams params = new RequestParams(path);
        x.http().get(params,this);
    }
    @Override
    public void onSuccess(String result) {
        //网络请求成功调用，result获取到的Json数据
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        //网络请求取消时调用

    }

    @Override
    public void onCancelled(CancelledException cex) {
        //网络请求取消时调用
    }

    @Override
    public void onFinished() {
        //网络请求完成时调用
    }
}
