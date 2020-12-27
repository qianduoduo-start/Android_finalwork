package com.example.finalwork;

import android.app.Application;

import com.example.finalwork.db.DBManager;

import org.xutils.x;

public class UniteApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        //初始化xUtils数据
        x.Ext.init(this);
        //初始化数据库对象
        DBManager.initDB(this);
    }
}
