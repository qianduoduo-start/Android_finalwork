package com.example.finalwork.utils;
public class URLUtils {

    public static String pinyinurl = "http://v.juhe.cn/xhzd/querypy?key=";
    public static String bushourul = "http://v.juhe.cn/xhzd/querybs?key=";
    public static final String DICTKEY = "f1b42e6f842757a6e734f5f3c4c7bc13";
    public static String wordurl = "http://v.juhe.cn/xhzd/query?key=";


    public static String getWordurl(String word){
        String url = wordurl+DICTKEY+"&word="+word;
        return url;
    }

    public static String getPinyinurl(String word,int page,int pagesize){
        String url = pinyinurl+DICTKEY+"&word="+word+"&page="+page+"&pagesize="+pagesize;
        return url;
    }

    public static String getBushouurl(String bs,int page,int pagesize){
        String url = bushourul+DICTKEY+"&word="+bs+"&page="+page+"&pagesize="+pagesize;
        return url;
    }
}
