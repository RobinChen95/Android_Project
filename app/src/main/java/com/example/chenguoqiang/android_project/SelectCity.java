package com.example.chenguoqiang.android_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pku.edu.ChenGuoqiang.app.MyApplication;
import com.pku.edu.ChenGuoqiang.bean.City;
import com.pku.edu.ChenGuoqiang.util.ClearEditText;
import com.pku.edu.ChenGuoqiang.util.Myadapter;

import java.util.ArrayList;
import java.util.List;

public class SelectCity extends Activity implements View.OnClickListener {

    private ImageView mBackBtn;
    private ListView mList;
    private List<City> cityList;
    private ArrayList<City> filtedDataList;
    private String citycode;
    private TextView select_city_Tv;
    private Myadapter myadapter;
    private ClearEditText mClearEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_city);
        select_city_Tv = findViewById(R.id.city_name);
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        initViews();
        //设置标题栏的城市
        select_city_Tv.setText("当前城市：" + MyApplication.cityname_global);
    }

    private void initViews() {
        mList = (ListView) findViewById(R.id.city_list);
        MyApplication myApplication = (MyApplication) getApplication();
        cityList = myApplication.getmCityList();
        filtedDataList = new ArrayList<>(cityList);
        mClearEditText = (ClearEditText) findViewById(R.id.search_bar);
        myadapter = new Myadapter(SelectCity.this,cityList);
        mList.setAdapter(myadapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                citycode = filtedDataList.get(position).getNumber();
                Intent i = new Intent();
                i.putExtra("citycode", citycode);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterData(s);
            }
        });
    }


    private void filterData(Editable filterStr) {
        filtedDataList = new ArrayList<City>();
        Log.d("Filter", filterStr.toString());

        if (TextUtils.isEmpty(filterStr)) {
            for (City city : cityList) {
                filtedDataList.add(city);
            }
        } else {
            filtedDataList.clear();
            for (City city : cityList)
                if (city.getCity().indexOf(filterStr.toString()) != -1) {
                    filtedDataList.add(city);
                }
        }
        myadapter.updateListView(filtedDataList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            default:
                break;
        }
    }

}
