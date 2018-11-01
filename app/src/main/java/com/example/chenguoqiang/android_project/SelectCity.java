package com.example.chenguoqiang.android_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pku.edu.ChenGuoqiang.app.MyApplication;
import com.pku.edu.ChenGuoqiang.bean.City;

import java.util.ArrayList;
import java.util.List;

public class SelectCity extends Activity implements View.OnClickListener {

    private ImageView mBackBtn;
    private ListView mList;
    private List<City> cityList;
    private ArrayAdapter<String> madpter;
    private ArrayList<String> city_name;
    private String citycode;
    private TextView select_city_Tv;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_city);
        select_city_Tv = findViewById(R.id.city_name);
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        initViews();
        //设置标题栏的城市
        select_city_Tv.setText("当前城市："+MyApplication.cityname_global);
    }

    private void initViews(){
        city_name = new ArrayList<>();
        mList = (ListView)findViewById(R.id.city_list);
        MyApplication myApplication = (MyApplication) getApplication();
        cityList = myApplication.getmCityList();
        for (City city : cityList){
            city_name.add(city.getCity());
        }
        madpter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,city_name);
        mList.setAdapter(madpter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
                citycode=cityList.get(position).getNumber();
                Intent i = new Intent();
                i.putExtra("citycode",citycode);
                setResult(RESULT_OK,i);
                finish();
            }
        });

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
                default:
                    break;
        }
    }

}
