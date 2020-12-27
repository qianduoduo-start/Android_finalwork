package com.example.finalwork;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.finalwork.adapter.SearchLeftAdapter;
import com.example.finalwork.adapter.SearchRightAdapter;
import com.example.finalwork.bean.PinBuBean;
import com.example.finalwork.bean.PinBuWordBean;
import com.example.finalwork.db.DBManager;
import com.example.finalwork.utils.AssetsUtils;
import com.example.finalwork.utils.CommonUtils;
import com.example.finalwork.utils.URLUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

public class BaseSearchActivity extends BaseActivity {
    ExpandableListView exLv;
    PullToRefreshGridView pullGv;
    TextView titleTv;
    List<String> groupDatas; //表示分组的列表 【A,B,C,D】
    List<List<PinBuBean.ResultBean>>childDatas; //将对应的子类列表放到这个集合
    private SearchLeftAdapter adapter;
    int selGroupPos = 0;//被点击的是哪组
    int selChildPos = 0;//被点击的是那一子组
    //右侧GridView的数据
    List<PinBuWordBean.ResultBean.ListBean> gridDatas;
    private SearchRightAdapter gridAdapter;

    int totalpage;//总页数
    int page = 1;//当前的页数
    int pagesize = 48;//一页的数据
    String word = ""; //点了哪个拼音或部首
    String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pinyin);
        exLv = findViewById(R.id.searchpy_elv);
        pullGv = findViewById(R.id.searchpy_gv);
        titleTv = findViewById(R.id.searchpy_tv);
        //初始化GridView数据
        initGridDatas();
    }


    //初始化GridView数据
    public void initGridDatas(){
        gridDatas = new ArrayList<>();
        //设置适配器
        gridAdapter = new SearchRightAdapter(this, gridDatas);
        pullGv.setAdapter(gridAdapter);
    }

    //GridView监听器
    public void setGVListener(final int type) {
        //上拉加载
        pullGv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pullGv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                //判断当前页数是否小于总页数
                if(page<totalpage){
                    page++;
                    if(type == CommonUtils.TYPE_PINYIN){
                        url = URLUtils.getPinyinurl(word,page,pagesize);
                    }else if(type == CommonUtils.TYPE_BUSHOU){
                        url = URLUtils.getBushouurl(word,page,pagesize);
                    }
                    loadData(url);
                }else {
                    pullGv.onRefreshComplete();
                }
            }
        });

        //跳转详细页面监听
        pullGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到详情页面

            }
        });
    }

    //加载数据成功时调用
    @Override
    public void onSuccess(String result){
        PinBuWordBean bean=new Gson().fromJson(result,PinBuWordBean.class);
        PinBuWordBean.ResultBean resultBean = bean.getResult();
        totalpage = resultBean.getTotalpage();
        List<PinBuWordBean.ResultBean.ListBean> list = resultBean.getList();
        //加载数据
        refreshDataByGV(list);
        //将数据写入数据库
        writeDBByThread(list);
    }

    //将数据写入数据库
    public void writeDBByThread(final List<PinBuWordBean.ResultBean.ListBean> list) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBManager.insertListToPywordtb(list);
            }
        }).start();
    }


    //跟新GridView中的数据
    public void refreshDataByGV(List<PinBuWordBean.ResultBean.ListBean> list){
        //加载了新的拼音或部首
        if(page == 1){
            gridDatas.clear();
            gridDatas.addAll(list);
            gridAdapter.notifyDataSetChanged();
        }else{//进行上拉加载
            gridDatas.addAll(list);
            gridAdapter.notifyDataSetChanged();
            pullGv.onRefreshComplete();
        }
    }

    //设置ExpandListView的监听
    public void setExLvListener(final int type) {
        exLv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                adapter.setSelectGroupPos(groupPosition);
                //获取被点击的位置
                selGroupPos = groupPosition;
                int groupSize = childDatas.get(selGroupPos).size();
                if(groupSize <= selChildPos){
                    selChildPos = groupSize-1;
                    adapter.setSelectChildPos(selChildPos);
                }
                //数据没有改变，布局改变了
                adapter.notifyDataSetInvalidated();
                //获取被点击的数据
                getDataAlterWord(type);
                return false;
            }
        });
        exLv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                adapter.setSelectGroupPos(groupPosition);
                adapter.setSelectChildPos(childPosition);
                adapter.notifyDataSetInvalidated();
                selGroupPos = groupPosition;
                selChildPos = childPosition;
                //网络加载右边的GridView内容
                getDataAlterWord(type);
                return false;
            }
        });
    }

    //改变左边选中，显示
    private void getDataAlterWord(int type) {
        List<PinBuBean.ResultBean> groupList = childDatas.get(selGroupPos);
            page = 1;
            PinBuBean.ResultBean bean = groupList.get(selChildPos);
            if (type == CommonUtils.TYPE_PINYIN) {
                word = bean.getPinyin();
                url = URLUtils.getPinyinurl(word,page,pagesize);
            }else if(type == CommonUtils.TYPE_BUSHOU){
                word = bean.getBushou();
                url = URLUtils.getBushouurl(word,page,pagesize);
            }
            loadData(url);
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
                if (type == CommonUtils.TYPE_PINYIN) {
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
                }else if(type == CommonUtils.TYPE_BUSHOU){
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

            adapter = new SearchLeftAdapter(this, groupDatas, childDatas, type);
            exLv.setAdapter(adapter);
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
