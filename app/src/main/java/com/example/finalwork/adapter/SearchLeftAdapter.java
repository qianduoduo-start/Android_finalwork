package com.example.finalwork.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finalwork.R;
import com.example.finalwork.bean.PinBuBean;
import com.example.finalwork.utils.CommonUtils;

import java.util.List;

public class SearchLeftAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> groupDatas; //表示分组的列表
    List<List<PinBuBean.ResultBean>> childDatas; //将每组对应的子列表放入集合
    LayoutInflater inflater;
    //确定是选择的拼音还是部首
    int type;
    //表示选中的组的位置和组下面的子列表的位置
    int selectGroupPos = 0,selectChildPos =0;
    public void setSelectGroupPos(int selectGroupPos){
        this.selectGroupPos = selectGroupPos;
    }

    public void setSelectChildPos(int selectChildPos){
        this.selectChildPos = selectChildPos;
    }

    public SearchLeftAdapter(Context context,List<String> groupDatas, List<List<PinBuBean.ResultBean>> childDatas,int type){
        this.context = context;
        this.groupDatas = groupDatas;
        this.childDatas = childDatas;
        this.type = type;
        //初始化布局加载器
        inflater = LayoutInflater.from(context);
    }

    //获取分组的数量
    @Override
    public int getGroupCount() {
        return groupDatas.size();
    }

    //获取指定分组中有几个item
    @Override
    public int getChildrenCount(int groupPosition) {
        return childDatas.get(groupPosition).size();
    }

    //获取分组指定位置的数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupDatas.get(groupPosition);
    }

    //给出第几组，第几个，求出指定位置的对象
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childDatas.get(groupPosition).get(childPosition);
    }

    //
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_exlv_group,null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (GroupViewHolder) convertView.getTag();
        }
        //获取指定位置的数据
        String word = groupDatas.get(groupPosition);
        if (type == CommonUtils.TYPE_PINYIN) {
            holder.groupTv.setText(word);
        }else {
            holder.groupTv.setText(word + "划");
        }

        //选中会改变颜色，所以判断是否被选中
        if (selectGroupPos == groupPosition) {
            convertView.setBackgroundColor(Color.BLACK);
            holder.groupTv.setTextColor(Color.RED);
        }else{
            convertView.setBackgroundResource(R.color.grey_f3f3f3);
            holder.groupTv.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_exlv_child,null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ChildViewHolder) convertView.getTag();
        }
        PinBuBean.ResultBean bean = childDatas.get(groupPosition).get(childPosition);
        if (type == CommonUtils.TYPE_PINYIN) {
            holder.childTv.setText(bean.getPinyin());
        }else {
            holder.childTv.setText(bean.getBushou());
        }
        //子类选中颜色
        if(selectGroupPos == groupPosition && selectChildPos == childPosition){
            convertView.setBackgroundColor(Color.WHITE);
            holder.childTv.setTextColor(Color.RED);
        }else{
            convertView.setBackgroundResource(R.color.grey_f3f3f3);
            holder.childTv.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder{
        TextView groupTv;
        public GroupViewHolder(View view){
            groupTv = view.findViewById(R.id.item_group_tv);
        }
    }

    class ChildViewHolder{
        TextView childTv;
        public ChildViewHolder(View view){
            childTv = view.findViewById(R.id.item_child_tv);
        }
    }
}
