package com.pku.edu.ChenGuoqiang.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenguoqiang.android_project.R;
import com.pku.edu.ChenGuoqiang.app.MyApplication;
import com.pku.edu.ChenGuoqiang.bean.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyExpandableListView extends BaseExpandableListAdapter {

    private LayoutInflater mInflater;
    private List<City> mDatas;

    //使用哈希映射存储省份,每个省份下又包含各自城市
    private HashMap<Integer,ArrayList<City>> provinces ;
    //使用哈希映射存储省份对应的数字
    private HashMap<String,Integer> province_integer_map;
    //最终存储的省份列表
    private ArrayList<String> provinces_result ;
    //最终存储的城市列表
    private ArrayList<ArrayList<City>> city_result;
    public MyExpandableListView(Context context, List<City> datas){
        mInflater = LayoutInflater.from(context);
        formatData(datas);
    }

    //自定义的格式化数据的函数
    public  ArrayList<ArrayList<City>> formatData(List<City> datas){
        mDatas = datas;
        provinces = new HashMap<>();
        province_integer_map = new HashMap<>();
        provinces_result = new ArrayList<>();
        city_result = new ArrayList<>();
        int provinces_count = 0;
        //将省份与数字建立映射关系
        for (City city: mDatas) {
            if (!province_integer_map.containsKey(city.getProvince())){
                province_integer_map.put(city.getProvince(),provinces_count);
                provinces_count++;
            }
        }
        for (City city : mDatas){
            if (!provinces.containsKey(province_integer_map.get(city.getProvince()))){
                //没有该省份时就添加省份的城市列表，可能处理的比较繁琐
                provinces.put(province_integer_map.get(city.getProvince()),new ArrayList<City>());
                provinces_result.add(city.getProvince());
                ArrayList<City> temp = provinces.get(province_integer_map.get(city.getProvince()));
                temp.add(city);
                provinces.remove(province_integer_map.get(city.getProvince()));
                provinces.put(province_integer_map.get(city.getProvince()),temp);
                Log.d("添加了省份",city.getProvince()+"\t编号："+province_integer_map.get(city.getProvince()));
            }
            else {
                //如果有了该城市所在的省份，就在对应的省份列表中添加该城市，可能处理的比较繁琐
                ArrayList<City> temp = provinces.get(province_integer_map.get(city.getProvince()));
                temp.add(city);
                provinces.remove(province_integer_map.get(city.getProvince()));
                provinces.put(province_integer_map.get(city.getProvince()),temp);
            }
        }
        //将结果添加进结果集
        for (int i = 0; i < provinces.size(); i++) {
            city_result.add(provinces.get(i));
        }
        return city_result;
    }

    public void updateListView(List<City> datas){
        formatData(datas);
        notifyDataSetChanged();
    }
    /**
     * Gets the number of groups.
     *
     * @return the number of groups
     */
    @Override
    public int getGroupCount() {
        return provinces_result.size();
    }

    /**
     * Gets the number of children in a specified group.
     *
     * @param groupPosition the position of the group for which the children
     *                      count should be returned
     * @return the children count in the specified group
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return city_result.get(groupPosition).size();
    }

    /**
     * Gets the data associated with the given group.
     *
     * @param groupPosition the position of the group
     * @return the data child for the specified group
     */
    @Override
    public Object getGroup(int groupPosition) {
        return provinces_result.get(groupPosition);
    }

    /**
     * Gets the data associated with the given child within the given group.
     *
     * @param groupPosition the position of the group that the child resides in
     * @param childPosition the position of the child with respect to other
     *                      children in the group
     * @return the data of the child
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return city_result.get(groupPosition).get(childPosition);
    }

    /**
     * Gets the ID for the group at the given position. This group ID must be
     * unique across groups. The combined ID (see
     * {@link #getCombinedGroupId(long)}) must be unique across ALL items
     * (groups and all children).
     *
     * @param groupPosition the position of the group for which the ID is wanted
     * @return the ID associated with the group
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * Gets the ID for the given child within the given group. This ID must be
     * unique across all children within the group. The combined ID (see
     * {@link #getCombinedChildId(long, long)}) must be unique across ALL items
     * (groups and all children).
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child within the group for which
     *                      the ID is wanted
     * @return the ID associated with the child
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Indicates whether the child and group IDs are stable across changes to the
     * underlying data.
     *
     * @return whether or not the same ID always refers to the same object
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * Gets a View that displays the given group. This View is only for the
     * group--the Views for the group's children will be fetched using
     * {@link #getChildView(int, int, boolean, View, ViewGroup)}.
     *
     * @param groupPosition the position of the group for which the View is
     *                      returned
     * @param isExpanded    whether the group is expanded or collapsed
     * @param convertView   the old view to reuse, if possible. You should check
     *                      that this view is non-null and of an appropriate type before
     *                      using. If it is not possible to convert this view to display
     *                      the correct data, this method can create a new view. It is not
     *                      guaranteed that the convertView will have been previously
     *                      created by
     *                      {@link #getGroupView(int, boolean, View, ViewGroup)}.
     * @param parent        the parent that this view will eventually be attached to
     * @return the View corresponding to the group at the specified position
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_first,parent,false);
        }
        TextView tvGroup = (TextView) convertView.findViewById(R.id.first_group);
        ImageView imgGroup = (ImageView) convertView.findViewById(R.id.province_icon);
        // 设置分组组名
        tvGroup.setText(provinces_result.get(groupPosition));
        MyApplication.province_global = provinces_result.get(groupPosition);
        // 设置图片
        switch (provinces_result.get(groupPosition)){
            case "北京":
                imgGroup.setImageResource(R.drawable.bj);
                break;
            case "上海":
                imgGroup.setImageResource(R.drawable.sh);
                break;
            case "天津":
                imgGroup.setImageResource(R.drawable.tj);
                break;
            case "重庆":
                imgGroup.setImageResource(R.drawable.cq);
                break;
            case "黑龙江":
                imgGroup.setImageResource(R.drawable.hlj);
                break;
            case "吉林":
                imgGroup.setImageResource(R.drawable.jl);
                break;
            case "辽宁":
                imgGroup.setImageResource(R.drawable.ln);
                break;
            case "内蒙古":
                imgGroup.setImageResource(R.drawable.nmg);
                break;
            case "河北":
                imgGroup.setImageResource(R.drawable.hebei);
                break;
            case "山西":
                imgGroup.setImageResource(R.drawable.shanxi);
                break;
            case "陕西":
                imgGroup.setImageResource(R.drawable.shaanxi);
                break;
            case "山东":
                imgGroup.setImageResource(R.drawable.sd);
                break;
            case "新疆":
                imgGroup.setImageResource(R.drawable.xj);
                break;
            case "西藏":
                imgGroup.setImageResource(R.drawable.xz);
                break;
            case "青海":
                imgGroup.setImageResource(R.drawable.qh);
                break;
            case "甘肃":
                imgGroup.setImageResource(R.drawable.gs);
                break;
            case "宁夏":
                imgGroup.setImageResource(R.drawable.nx);
                break;
            case "河南":
                imgGroup.setImageResource(R.drawable.henan);
                break;
            case "江苏":
                imgGroup.setImageResource(R.drawable.js);
                break;
            case "湖北":
                imgGroup.setImageResource(R.drawable.hubei);
                break;
            case "浙江":
                imgGroup.setImageResource(R.drawable.zj);
                break;
            case "安徽":
                imgGroup.setImageResource(R.drawable.ah);
                break;
            case "福建":
                imgGroup.setImageResource(R.drawable.fj);
                break;
            case "江西":
                imgGroup.setImageResource(R.drawable.jx);
                break;
            case "湖南":
                imgGroup.setImageResource(R.drawable.hunan);
                break;
            case "贵州":
                imgGroup.setImageResource(R.drawable.gz);
                break;
            case "四川":
                imgGroup.setImageResource(R.drawable.sc);
                break;
            case "广东":
                imgGroup.setImageResource(R.drawable.gd);
                break;
            case "云南":
                imgGroup.setImageResource(R.drawable.yn);
                break;
            case "广西":
                imgGroup.setImageResource(R.drawable.gx);
                break;
            case "海南":
                imgGroup.setImageResource(R.drawable.hainan);
                break;
            case "香港":
                imgGroup.setImageResource(R.drawable.xg);
                break;
            case "澳门":
                imgGroup.setImageResource(R.drawable.am);
                break;
            case "台湾":
                imgGroup.setImageResource(R.drawable.tw);
                break;
                default:
                    break;
        }
        return convertView;
    }

    /**
     * Gets a View that displays the data for the given child within the given
     * group.
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child (for which the View is
     *                      returned) within the group
     * @param isLastChild   Whether the child is the last child within the group
     * @param convertView   the old view to reuse, if possible. You should check
     *                      that this view is non-null and of an appropriate type before
     *                      using. If it is not possible to convert this view to display
     *                      the correct data, this method can create a new view. It is not
     *                      guaranteed that the convertView will have been previously
     *                      created by
     *                      {@link #getChildView(int, int, boolean, View, ViewGroup)}.
     * @param parent        the parent that this view will eventually be attached to
     * @return the View corresponding to the child at the specified position
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild , View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_second,parent,false);
        }
        TextView cityTV = (TextView)convertView.findViewById(R.id.second_group);
        cityTV.setText(city_result.get(groupPosition).get(childPosition).getCity());
        return convertView;
    }

    /**
     * Whether the child at the specified position is selectable.
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child within the group
     * @return whether the child is selectable.
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
