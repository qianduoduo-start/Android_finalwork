package com.example.finalwork;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.finalwork.bean.PinBuBean;
import com.example.finalwork.utils.AssetsUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

public class SearchPinyinActivity extends AppCompatActivity {
    final int TYPE_PINYIN = 0,TYPE_BUSHOU = 1;
    final String FILE_PINYIN = "pinyin.txt";
    final String FILE_BUSHOU= "bushou.txt";
    ExpandableListView exLv;
    PullToRefreshGridView pullGv;
    List<String> groupDatas; //表示分组的列表 【A,B,C,D】
    List<List<PinBuBean.ResultBean>>childDatas; //将对应的子类列表放到这个集合
    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pinyin);
        exLv = findViewById(R.id.searchpy_elv);
        pullGv = findViewById(R.id.searchpy_gv);
        initData(FILE_PINYIN,TYPE_PINYIN);
    }

    public void initData(String assetsName,int type){
        /**
         * 读取Assets文件中的数据，使用Gson解析，将数据存储到二位数组中
         * @Param assetsName 文件名称
         * @param type 文件类型 pinyin--0  bushou--1
         * */
        groupDatas = new ArrayList<>();
        childDatas = new ArrayList<>();
        String json = AssetsUtils.getAssetsContent(this,assetsName);
        if (!TextUtils.isEmpty(json)) {
           PinBuBean pinBuBean = new Gson().fromJson(json,PinBuBean.class);
           List<PinBuBean.ResultBean> list=pinBuBean.getResult();
           //整理数据
            List<PinBuBean.ResultBean> grouplist = new ArrayList<>();//声明每组包含的元素集合
            for(int i=0;i< list.size();i++){
                PinBuBean.ResultBean bean = list.get(i);
                if (type == TYPE_PINYIN) {
                   String pinyin_key =  bean.getPinyin_key(); //获取大写字母
                    if (!groupDatas.contains(pinyin_key)) {  // 判断是否存在于分组列表中
                        groupDatas.add(pinyin_key);
                        //说明上一拼音已经全部录入到grouplist当中了，可以将上一个拼音的集合到childDatas
                        if (grouplist.size()>0) {
                            childDatas.add(grouplist);
                        }
                        //给新的一组创建一个对应的子列表
                        grouplist = new ArrayList<>();
                        grouplist.add(bean);
                    }else{
                        //大写字母已经存在，直接添加
                        grouplist.add(bean);
                    }
                }else if(type == TYPE_BUSHOU){
                    String bihua = bean.getBihua();
                    if (!groupDatas.contains(bihua)) {
                        groupDatas.add(bihua);
                        //新的一组，把上一组进行添加
                        if (grouplist.size()>0) {
                            childDatas.add(grouplist);
                        }
                        //新的一组，创建子列表
                        grouplist = new ArrayList<>();
                        grouplist.add(bean);
                    }else{
                        //当前笔划存在，不用添加
                        grouplist.add(bean);
                    }
                }
            }
            //循环结束后最后一组没有添加进去，手动添加
               childDatas.add(grouplist);

        }
    }

    public void onClick(View view){
         switch ( view.getId()){
             case R.id.searchpy_iv_back:
                 finish();
                 break;
         }
    }
}