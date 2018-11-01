package com.example.chenguoqiang.android_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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
    private String cityname;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_city);

        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        initViews();
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
                cityname=cityList.get(position).getCity();
                citycode=cityList.get(position).getNumber();
                Toast.makeText(SelectCity.this,cityname+":"+citycode,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
                Intent i = new Intent();
                i.putExtra("citycode","101160101");
                setResult(RESULT_OK,i);
                finish();
                break;
                default:
                    break;
        }
    }

}
