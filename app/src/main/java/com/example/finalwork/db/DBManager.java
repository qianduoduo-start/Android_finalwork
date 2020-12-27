package com.example.finalwork.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finalwork.bean.PinBuWordBean;
import com.example.finalwork.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static SQLiteDatabase db;
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    //插入数据
    public static void insertWordToPywordtb(PinBuWordBean.ResultBean.ListBean bean){
       ContentValues values = new ContentValues();
       values.put("id",bean.getId());
       values.put("zi",bean.getZi());
       values.put("py",bean.getPy());
       values.put("wubi",bean.getWubi());
       values.put("pinyin",bean.getPinyin());
       values.put("bushou",bean.getBushou());
       values.put("bihua",bean.getBihua());
       db.insert(CommonUtils.TABLE_PYWORDTB,null,values);
    }

    //插入集合数据
    public static void insertListToPywordtb(List<PinBuWordBean.ResultBean.ListBean>list){
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                PinBuWordBean.ResultBean.ListBean bean = list.get(i);
                try {
                    insertWordToPywordtb(bean);
                }catch (Exception e){

                }
            }
        }
    }

    //查询拼音数据
    public static List<PinBuWordBean.ResultBean.ListBean>queryPyWordFromPywordtb(String py,int page,int pagesize){
        List<PinBuWordBean.ResultBean.ListBean>list = new ArrayList<>();
        String sql = "select * from pywordtb where py=? limit ?,?";
        int start = (page-1)*pagesize;
        int end = page*pagesize;
        Cursor cursor = db.rawQuery(sql,new String[]{py,start+"",end+""});
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String zi = cursor.getString(cursor.getColumnIndex("zi"));
            String py1 = cursor.getString(cursor.getColumnIndex("py"));
            String wubi = cursor.getString(cursor.getColumnIndex("wubi"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            String bushou = cursor.getString(cursor.getColumnIndex("bushou"));
            String bihua = cursor.getString(cursor.getColumnIndex("bihua"));
            PinBuWordBean.ResultBean.ListBean bean = new PinBuWordBean.ResultBean.ListBean(id, zi, py, wubi, pinyin, bushou, bihua);
            list.add(bean);
        }
        return list;
    }

    public static List<PinBuWordBean.ResultBean.ListBean>queryBsWordFromPywordtb(String bs,int page,int pagesize){
        List<PinBuWordBean.ResultBean.ListBean>list = new ArrayList<>();
        String sql = "select * from pywordtb where bushou=? limit ?,?";
        int start = (page-1)*pagesize;
        int end = page*pagesize;
        Cursor cursor = db.rawQuery(sql, new String[]{bs, start + "", end + ""});
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String zi = cursor.getString(cursor.getColumnIndex("zi"));
            String py1 = cursor.getString(cursor.getColumnIndex("py"));
            String wubi = cursor.getString(cursor.getColumnIndex("wubi"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            String bushou = cursor.getString(cursor.getColumnIndex("bushou"));
            String bihua = cursor.getString(cursor.getColumnIndex("bihua"));
            PinBuWordBean.ResultBean.ListBean bean = new PinBuWordBean.ResultBean.ListBean(id, zi, py1, wubi, pinyin, bushou, bihua);
            list.add(bean);
        }
        return list;
    }

}
