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
import com.pku.edu.ChenGuoqiang.bean.City;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MyExpandableListView extends BaseExpandableListAdapter {
    private class ViewHolder{
        TextView provinceTv;
        TextView cityTv;
        ImageView provinceIcon;
    }

    private LayoutInflater mInflater;
    private List<City> mDatas;
    //使用哈希映射存储省份,每个省份下又包含各自城市
    private HashMap<Integer,ArrayList<City>> provinces ;
    //使用哈希映射存储省份对应的数字
    private HashMap<String,Integer> province_integer_map;
    public MyExpandableListView(Context context, List<City> datas){
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        provinces = new HashMap<>();
        province_integer_map = new HashMap<>();
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
                provinces.put(province_integer_map.get(city.getProvince()),new ArrayList<City>());
                Log.d("添加了省份：",city.getProvince());
            }
            else {
                //此处可能不能添加
                ArrayList<City> temp = provinces.get(province_integer_map.get(city.getProvince()));
                temp.add(city);
                provinces.remove(province_integer_map.get(city.getProvince()));
                provinces.put(province_integer_map.get(city.getProvince()),temp);
            }
        }
        //打印
        for (int i = 0; i < mDatas.size(); i++) {
            Log.d("原始数据：",mDatas.get(i).getCity());
        }
        for (int i = 0; i < provinces.size(); i++) {
            for (int j = 0; j < provinces.get(i).size(); j++) {
                Log.d("省份","内容："+provinces.get(i).get(j).getCity());
            }
        }
    }

    public void updateListView(List<City> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }
    /**
     * Gets the number of groups.
     *
     * @return the number of groups
     */
    @Override
    public int getGroupCount() {
        return provinces.size();
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
        return mDatas.size();
    }

    /**
     * Gets the data associated with the given group.
     *
     * @param groupPosition the position of the group
     * @return the data child for the specified group
     */
    @Override
    public Object getGroup(int groupPosition) {
        return provinces.get(groupPosition);
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
        return provinces.get(groupPosition).get(childPosition);
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
     * @see Adapter#hasStableIds()
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
        ViewHolder holder;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_first, parent, false);
            holder.provinceTv=(TextView)convertView.findViewById(R.id.first_group);
            holder.provinceIcon=(ImageView)convertView.findViewById(R.id.province_icon);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        City city = mDatas.get(groupPosition);
        holder.provinceTv.setText(city.getProvince());

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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_second, parent, false);
            holder.cityTv=(TextView)convertView.findViewById(R.id.second_group);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        City city = mDatas.get(groupPosition);
        holder.cityTv.setText(city.getProvince());

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
