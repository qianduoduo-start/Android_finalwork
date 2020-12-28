package com.example.finalwork.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.finalwork.bean.PinBuWordBean;
import com.example.finalwork.bean.WordBean;
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
        long loc =db.insert(CommonUtils.TABLE_PYWORDTB,null,values);
    }

    //插入集合数据
    public static void insertListToPywordtb(List<PinBuWordBean.ResultBean.ListBean>list){
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                PinBuWordBean.ResultBean.ListBean bean = list.get(i);
                try {
                    insertWordToPywordtb(bean);
                }catch (Exception e){
                    Log.i("qyh", "insertListToPywordtb: "+bean.getZi()+"已存在！");
                }
            }
        }
    }

    //查询拼音数据
    public static List<PinBuWordBean.ResultBean.ListBean>queryPyWordFromPywordtb(String py,int page,int pagesize){
        List<PinBuWordBean.ResultBean.ListBean>list = new ArrayList<>();
        String sql = "select * from pywordtb where py=? or py like ? or py like ? or py like ? limit ?,?";
        int start = (page-1)*pagesize;
        int end = page*pagesize;
        String type1 = py+",%";
        String type2 = "%,"+py+",%";
        String type3 = "%,"+py;
        Cursor cursor = db.rawQuery(sql, new String[]{py,type1,type2,type3, start + "", end + ""});
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

    public static void insertWordToWordtb(WordBean.ResultBean bean){
        ContentValues values = new ContentValues();
        values.put("id",bean.getId());
        values.put("zi",bean.getZi());
        values.put("py",bean.getPy());
        values.put("wubi",bean.getWubi());
        values.put("pinyin",bean.getPinyin());
        values.put("bushou",bean.getBushou());
        values.put("bihua",bean.getBihua());
        //将集合转化成字符串类型进行插入
        String jijie = listToString(bean.getJijie());
        values.put("jijie",jijie);
        String xiangjie = listToString(bean.getXiangjie());
        values.put("xiangjie",xiangjie);
        db.insert(CommonUtils.TABLE_WORDTB,null,values);
    }

    public static WordBean.ResultBean queryWordFromWordtb(String word){
        String sql = "select * from wordtb where zi=?";
        Cursor cursor = db.rawQuery(sql, new String[]{word});
        if (cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String zi = cursor.getString(cursor.getColumnIndex("zi"));
            String py1 = cursor.getString(cursor.getColumnIndex("py"));
            String wubi = cursor.getString(cursor.getColumnIndex("wubi"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
            String bushou = cursor.getString(cursor.getColumnIndex("bushou"));
            String bihua = cursor.getString(cursor.getColumnIndex("bihua"));
            String jijie = cursor.getString(cursor.getColumnIndex("jijie"));
            String xiangjie = cursor.getString(cursor.getColumnIndex("xiangjie"));
            List<String> jijielist = stringToList(jijie);
            List<String> xiangxilist = stringToList(xiangjie);
            WordBean.ResultBean bean = new WordBean.ResultBean(id, zi, py1, wubi, pinyin, bushou, bihua, jijielist, xiangxilist);
            return bean;
        }
        return null;
    }

    /* 将List集合转化成字符串的方法*/
    public static String listToString(List<String>list){
        StringBuilder sb = new StringBuilder();
        if (list!=null&&!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                String msg = list.get(i);
                msg+="|";
                sb.append(msg);
            }
        }
        return sb.toString();
    }

    /** 将字符串转换成List集合的方法*/
    public static List<String>stringToList(String msg){
        List<String>list = new ArrayList<>();
        if (!TextUtils.isEmpty(msg)) {
            String[] arr = msg.split("\\|");
            for (int i = 0; i < arr.length; i++) {
                String s = arr[i].trim();
                if (!TextUtils.isEmpty(s)) {
                    list.add(s);
                }
            }
        }
        return list;
    }
}
