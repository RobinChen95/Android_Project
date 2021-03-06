package com.example.chenguoqiang.android_project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.pku.edu.ChenGuoqiang.bean.TodayWeather;
import com.pku.edu.ChenGuoqiang.util.NetUtil;
import com.pku.edu.ChenGuoqiang.app.MyApplication;
import com.pku.edu.ChenGuoqiang.util.pageAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener,ViewPager.OnPageChangeListener {

    private static final int UPDATE_TODAY_WEATHER = 1;

    private ImageView mUpdateBtn;

    private ImageView mcitySelect;

    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv,
            temperatureTv, climateTv, windTv, city_name_Tv;

    private TextView date1,temperature1,fengxiang1,climate1,
            date2,temperature2,fengxiang2,climate2,
            date3,temperature3,fengxiang3,climate3,
            date4,temperature4,fengxiang4,climate4;

    private ImageView weatherImg, pmImg;

    private ImageView weatherImg1,weatherImg2,weatherImg3,weatherImg4;

    private ImageView page1Img,page2Img;

    private LinearLayout backgroundImg;

    private SwipeRefreshLayout Refresh;

    private String updatedCityCode;

    private ViewPager viewPager;
    private List<View> views;
    private pageAdapter pageadapter;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    //初始化界面
    void initView() {
        updatedCityCode = "101010100";
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        backgroundImg = (LinearLayout) findViewById(R.id.backgroundImg);
        //------------------设置ViewPager页面------------------
        viewPager = (ViewPager)findViewById(R.id.pages);
        View page1 = LayoutInflater.from(this).inflate(R.layout.page1,null);
        View page2 = LayoutInflater.from(this).inflate(R.layout.page2,null);
        views = new ArrayList<>();
        //findViewById，注意要用R.id.XX
        date1 = (TextView) page1.findViewById(R.id.date1);
        temperature1 = (TextView)page1.findViewById(R.id.temperature1);
        fengxiang1 = (TextView)page1.findViewById(R.id.fengxiang1);
        climate1 = (TextView)page1.findViewById(R.id.climate1);
        weatherImg1 = (ImageView)page1.findViewById(R.id.weather_img1);
        date2 = (TextView) page1.findViewById(R.id.date2);
        temperature2 = (TextView)page1.findViewById(R.id.temperature2);
        fengxiang2 = (TextView)page1.findViewById(R.id.fengxiang2);
        climate2 = (TextView)page1.findViewById(R.id.climate2);
        weatherImg2 = (ImageView)page1.findViewById(R.id.weather_img2);
        date3 = (TextView) page2.findViewById(R.id.date1);
        temperature3 = (TextView)page2.findViewById(R.id.temperature1);
        fengxiang3 = (TextView)page2.findViewById(R.id.fengxiang1);
        climate3 = (TextView)page2.findViewById(R.id.climate1);
        weatherImg3 = (ImageView)page2.findViewById(R.id.weather_img1);
        date4 = (TextView) page2.findViewById(R.id.date2);
        temperature4 = (TextView)page2.findViewById(R.id.temperature2);
        fengxiang4 = (TextView)page2.findViewById(R.id.fengxiang2);
        climate4 = (TextView)page2.findViewById(R.id.climate2);
        weatherImg4 = (ImageView)page2.findViewById(R.id.weather_img2);
        //设置N/A
        date1.setText("N/A");
        temperature1.setText("N/A");
        fengxiang1.setText("N/A");
        climate1.setText("N/A");
        date2.setText("N/A");
        temperature2.setText("N/A");
        fengxiang2.setText("N/A");
        climate2.setText("N/A");
        date3.setText("N/A");
        temperature3.setText("N/A");
        fengxiang3.setText("N/A");
        climate3.setText("N/A");
        date4.setText("N/A");
        temperature4.setText("N/A");
        fengxiang4.setText("N/A");
        climate4.setText("N/A");
        //设置适配器
        views.add(page1);
        views.add(page2);
        pageadapter = new pageAdapter(this,views);
        viewPager.setAdapter(pageadapter);
        page1Img = findViewById(R.id.page1Img);
        page2Img = findViewById(R.id.page2Img);
        viewPager.setOnPageChangeListener(this);
        //------------------设置ViewPager页面------------------

        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
    }

    //实现下拉刷新的线程
    @SuppressLint("ResourceAsColor")
    void PulltoRefresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //根据ID找到控件
                Refresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
                //设置颜色
                Refresh.setColorSchemeColors(R.color.colorPrimaryDark);
                //创建监听器
                Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //查询之前展示动画
                        Refresh.setRefreshing(true);
                        queryWeatherCode(updatedCityCode);
                        //查询之后关闭动画
                        Refresh.setRefreshing(false);

                    }
                });
            }
        }).start();
    }

    //根据得到的数据更新界面
    void updateTodayWeather(TodayWeather todayWeather) {
        int pmIntdata = Integer.parseInt(todayWeather.getPm25());
        city_name_Tv.setText(todayWeather.getCity() + "天气");
        MyApplication.cityname_global = todayWeather.getCity();
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText("湿度:" + todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh() + "~" + todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:" + todayWeather.getFengli());

        //--------------------更新背景图片--------------------
        if (MyApplication.province_global!=null){
            switch (MyApplication.province_global){
                case "北京":
                    backgroundImg.setBackgroundResource(R.drawable.bj);
                    break;
                case "上海":
                    backgroundImg.setBackgroundResource(R.drawable.sh);
                    break;
                case "天津":
                    backgroundImg.setBackgroundResource(R.drawable.tj);
                    break;
                case "重庆":
                    backgroundImg.setBackgroundResource(R.drawable.cq);
                    break;
                case "黑龙江":
                    backgroundImg.setBackgroundResource(R.drawable.hlj);
                    break;
                case "吉林":
                    backgroundImg.setBackgroundResource(R.drawable.jl);
                    break;
                case "辽宁":
                    backgroundImg.setBackgroundResource(R.drawable.ln);
                    break;
                case "内蒙古":
                    backgroundImg.setBackgroundResource(R.drawable.nmg);
                    break;
                case "河北":
                    backgroundImg.setBackgroundResource(R.drawable.hebei);
                    break;
                case "山西":
                    backgroundImg.setBackgroundResource(R.drawable.shanxi);
                    break;
                case "陕西":
                    backgroundImg.setBackgroundResource(R.drawable.shaanxi);
                    break;
                case "山东":
                    backgroundImg.setBackgroundResource(R.drawable.sd);
                    break;
                case "新疆":
                    backgroundImg.setBackgroundResource(R.drawable.xj);
                    break;
                case "西藏":
                    backgroundImg.setBackgroundResource(R.drawable.xz);
                    break;
                case "青海":
                    backgroundImg.setBackgroundResource(R.drawable.qh);
                    break;
                case "甘肃":
                    backgroundImg.setBackgroundResource(R.drawable.gs);
                    break;
                case "宁夏":
                    backgroundImg.setBackgroundResource(R.drawable.nx);
                    break;
                case "河南":
                    backgroundImg.setBackgroundResource(R.drawable.henan);
                    break;
                case "江苏":
                    backgroundImg.setBackgroundResource(R.drawable.js);
                    break;
                case "湖北":
                    backgroundImg.setBackgroundResource(R.drawable.hubei);
                    break;
                case "浙江":
                    backgroundImg.setBackgroundResource(R.drawable.zj);
                    break;
                case "安徽":
                    backgroundImg.setBackgroundResource(R.drawable.ah);
                    break;
                case "福建":
                    backgroundImg.setBackgroundResource(R.drawable.fj);
                    break;
                case "江西":
                    backgroundImg.setBackgroundResource(R.drawable.jx);
                    break;
                case "湖南":
                    backgroundImg.setBackgroundResource(R.drawable.hunan);
                    break;
                case "贵州":
                    backgroundImg.setBackgroundResource(R.drawable.gz);
                    break;
                case "四川":
                    backgroundImg.setBackgroundResource(R.drawable.sc);
                    break;
                case "广东":
                    backgroundImg.setBackgroundResource(R.drawable.biz_plugin_weather_shenzhen_bg);
                    break;
                case "云南":
                    backgroundImg.setBackgroundResource(R.drawable.yn);
                    break;
                case "广西":
                    backgroundImg.setBackgroundResource(R.drawable.gx);
                    break;
                case "海南":
                    backgroundImg.setBackgroundResource(R.drawable.hainan);
                    break;
                case "香港":
                    backgroundImg.setBackgroundResource(R.drawable.xg);
                    break;
                case "澳门":
                    backgroundImg.setBackgroundResource(R.drawable.am);
                    break;
                case "台湾":
                    backgroundImg.setBackgroundResource(R.drawable.tw);
                    break;
                default:
                    break;
            }
        }
        //--------------------更新背景图片--------------------

        //--------------------更新ViewPager--------------------
        date1.setText(todayWeather.getTomorrow1_date());
        temperature1.setText(todayWeather.getTomorrow1_high()+"~"+todayWeather.getTomorrow1_low());
        fengxiang1.setText(todayWeather.getTomorrow1_fengxiang());
        climate1.setText(todayWeather.getTomorrow1_type());
        date2.setText(todayWeather.getTomorrow2_date());
        temperature2.setText(todayWeather.getTomorrow2_high()+"~"+todayWeather.getTomorrow2_low());
        fengxiang2.setText(todayWeather.getTomorrow2_fengxiang());
        climate2.setText(todayWeather.getTomorrow2_type());
        date3.setText(todayWeather.getTomorrow3_date());
        temperature3.setText(todayWeather.getTomorrow3_high()+"~"+todayWeather.getTomorrow3_low());
        fengxiang3.setText(todayWeather.getTomorrow3_fengxiang());
        climate3.setText(todayWeather.getTomorrow3_type());
        date4.setText(todayWeather.getTomorrow4_date());
        temperature4.setText(todayWeather.getTomorrow4_high()+"~"+todayWeather.getTomorrow4_low());
        fengxiang4.setText(todayWeather.getTomorrow4_fengxiang());
        climate4.setText(todayWeather.getTomorrow4_type());
        pageadapter.notifyDataSetChanged();
        //--------------------更新ViewPager--------------------

        //********************更新ViewPager图片********************
        if (todayWeather.getTomorrow1_type().equals("晴"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_qing);
        if (todayWeather.getTomorrow1_type().equals("暴雪"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if (todayWeather.getTomorrow1_type().equals("暴雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        if (todayWeather.getTomorrow1_type().equals("大暴雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if (todayWeather.getTomorrow1_type().equals("大雪"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if (todayWeather.getTomorrow1_type().equals("大雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if (todayWeather.getTomorrow1_type().equals("多云"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if (todayWeather.getTomorrow1_type().equals("雷阵雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if (todayWeather.getTomorrow1_type().equals("雷阵雨冰雹"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if (todayWeather.getTomorrow1_type().equals("沙尘暴"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        if (todayWeather.getTomorrow1_type().equals("特大暴雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if (todayWeather.getTomorrow1_type().equals("雾"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_wu);
        if (todayWeather.getTomorrow1_type().equals("小雪"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if (todayWeather.getTomorrow1_type().equals("小雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if (todayWeather.getTomorrow1_type().equals("阴"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_yin);
        if (todayWeather.getTomorrow1_type().equals("雨夹雪"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if (todayWeather.getTomorrow1_type().equals("阵雪"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if (todayWeather.getTomorrow1_type().equals("阵雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if (todayWeather.getTomorrow1_type().equals("中雪"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if (todayWeather.getTomorrow1_type().equals("中雨"))weatherImg1.setImageResource(R.drawable.biz_plugin_weather_zhongyu);

        if (todayWeather.getTomorrow2_type().equals("晴"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_qing);
        if (todayWeather.getTomorrow2_type().equals("暴雪"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if (todayWeather.getTomorrow2_type().equals("暴雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        if (todayWeather.getTomorrow2_type().equals("大暴雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if (todayWeather.getTomorrow2_type().equals("大雪"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if (todayWeather.getTomorrow2_type().equals("大雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if (todayWeather.getTomorrow2_type().equals("多云"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if (todayWeather.getTomorrow2_type().equals("雷阵雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if (todayWeather.getTomorrow2_type().equals("雷阵雨冰雹"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if (todayWeather.getTomorrow2_type().equals("沙尘暴"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        if (todayWeather.getTomorrow2_type().equals("特大暴雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if (todayWeather.getTomorrow2_type().equals("雾"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_wu);
        if (todayWeather.getTomorrow2_type().equals("小雪"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if (todayWeather.getTomorrow2_type().equals("小雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if (todayWeather.getTomorrow2_type().equals("阴"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_yin);
        if (todayWeather.getTomorrow2_type().equals("雨夹雪"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if (todayWeather.getTomorrow2_type().equals("阵雪"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if (todayWeather.getTomorrow2_type().equals("阵雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if (todayWeather.getTomorrow2_type().equals("中雪"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if (todayWeather.getTomorrow2_type().equals("中雨"))weatherImg2.setImageResource(R.drawable.biz_plugin_weather_zhongyu);

        if (todayWeather.getTomorrow3_type().equals("晴"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_qing);
        if (todayWeather.getTomorrow3_type().equals("暴雪"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if (todayWeather.getTomorrow3_type().equals("暴雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        if (todayWeather.getTomorrow3_type().equals("大暴雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if (todayWeather.getTomorrow3_type().equals("大雪"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if (todayWeather.getTomorrow3_type().equals("大雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if (todayWeather.getTomorrow3_type().equals("多云"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if (todayWeather.getTomorrow3_type().equals("雷阵雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if (todayWeather.getTomorrow3_type().equals("雷阵雨冰雹"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if (todayWeather.getTomorrow3_type().equals("沙尘暴"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        if (todayWeather.getTomorrow3_type().equals("特大暴雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if (todayWeather.getTomorrow3_type().equals("雾"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_wu);
        if (todayWeather.getTomorrow3_type().equals("小雪"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if (todayWeather.getTomorrow3_type().equals("小雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if (todayWeather.getTomorrow3_type().equals("阴"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_yin);
        if (todayWeather.getTomorrow3_type().equals("雨夹雪"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if (todayWeather.getTomorrow3_type().equals("阵雪"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if (todayWeather.getTomorrow3_type().equals("阵雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if (todayWeather.getTomorrow3_type().equals("中雪"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if (todayWeather.getTomorrow3_type().equals("中雨"))weatherImg3.setImageResource(R.drawable.biz_plugin_weather_zhongyu);

        if (todayWeather.getTomorrow4_type().equals("晴"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_qing);
        if (todayWeather.getTomorrow4_type().equals("暴雪"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if (todayWeather.getTomorrow4_type().equals("暴雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        if (todayWeather.getTomorrow4_type().equals("大暴雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if (todayWeather.getTomorrow4_type().equals("大雪"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if (todayWeather.getTomorrow4_type().equals("大雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if (todayWeather.getTomorrow4_type().equals("多云"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if (todayWeather.getTomorrow4_type().equals("雷阵雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if (todayWeather.getTomorrow4_type().equals("雷阵雨冰雹"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if (todayWeather.getTomorrow4_type().equals("沙尘暴"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        if (todayWeather.getTomorrow4_type().equals("特大暴雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if (todayWeather.getTomorrow4_type().equals("雾"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_wu);
        if (todayWeather.getTomorrow4_type().equals("小雪"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if (todayWeather.getTomorrow4_type().equals("小雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if (todayWeather.getTomorrow4_type().equals("阴"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_yin);
        if (todayWeather.getTomorrow4_type().equals("雨夹雪"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if (todayWeather.getTomorrow4_type().equals("阵雪"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if (todayWeather.getTomorrow4_type().equals("阵雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if (todayWeather.getTomorrow4_type().equals("中雪"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if (todayWeather.getTomorrow4_type().equals("中雨"))weatherImg4.setImageResource(R.drawable.biz_plugin_weather_zhongyu);


        //********************更新ViewPager图片********************

        //使用setImageResource方法更新Pm2.5图片与天气图片
        if (pmIntdata<=50)pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
        else if (pmIntdata<=100)pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
        else if (pmIntdata<=150)pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
        else if (pmIntdata<=200)pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
        else if (pmIntdata<=300)pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
        else pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
        //更新天气图片
        if (todayWeather.getType().equals("晴"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
        if (todayWeather.getType().equals("暴雪"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if (todayWeather.getType().equals("暴雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        if (todayWeather.getType().equals("大暴雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if (todayWeather.getType().equals("大雪"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if (todayWeather.getType().equals("大雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if (todayWeather.getType().equals("多云"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if (todayWeather.getType().equals("雷阵雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if (todayWeather.getType().equals("雷阵雨冰雹"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if (todayWeather.getType().equals("沙尘暴"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        if (todayWeather.getType().equals("特大暴雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if (todayWeather.getType().equals("雾"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
        if (todayWeather.getType().equals("小雪"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if (todayWeather.getType().equals("小雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if (todayWeather.getType().equals("阴"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
        if (todayWeather.getType().equals("雨夹雪"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if (todayWeather.getType().equals("阵雪"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if (todayWeather.getType().equals("阵雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if (todayWeather.getType().equals("中雪"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if (todayWeather.getType().equals("中雨"))weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);

        Toast.makeText(MainActivity.this, "更新成功!", Toast.LENGTH_SHORT).show();
    }


    //解析HTML
    private TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = null;
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }

                            //--------------------以下为更新解析第1-4天天气的代码--------------------
                            else if (xmlPullParser.getName().equals("date") && dateCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow1_date(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow1_high(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow1_low(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow1_type(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow1_fengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow2_date(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow2_high(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow2_low(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow2_type(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow2_fengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow3_date(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow3_high(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow3_low(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow3_type(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow3_fengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow4_date(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow4_high(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow4_low(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow4_type(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTomorrow4_fengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            }
                            //--------------------以上为更新解析第1-4天天气的代码--------------------
                        }

                        break;


                    // 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                // 进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

    //根据citycode查询网页并将返回的HTML调用自定义的解释器解析为todayWeather对象，并通过消息机制发送给主线程
    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    //输入流
                    InputStream in = con.getInputStream();
                    //BufferedReader作用是包装字符流，现将字符读入缓存，再读入内存以提高效率
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    //StringBuilder可以使用append方法在字符串末尾处添加字符
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("responseAppended", str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);
                    parseXML(responseStr);

                    todayWeather = parseXML(responseStr);
                    if (todayWeather != null) {Log.d("todayWeatherMessage", todayWeather.toString());
                        Message msg =new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) con.disconnect();
                }
            }
        }).start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        mUpdateBtn = findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        if (NetUtil.getNetWorkState(this) != NetUtil.NETWORK_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK", Toast.LENGTH_LONG).show();
        } else {
            Log.d("myWeather", "无网络");
            Toast.makeText(MainActivity.this, "无网络", Toast.LENGTH_LONG).show();
        }

        mcitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mcitySelect.setOnClickListener(this);

        initView();

        PulltoRefresh();
    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.title_city_manager){
            Intent i = new Intent(this,SelectCity.class);
            startActivityForResult(i,1);
        }

        if (view.getId() == R.id.title_update_btn) {

            if (NetUtil.getNetWorkState(this) != NetUtil.NETWORK_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(updatedCityCode);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1&&resultCode==RESULT_OK){
            updatedCityCode = data.getStringExtra("citycode");
            Log.d("myWeather","选择的城市代码为"+updatedCityCode);
            if (NetUtil.getNetWorkState(this)!=NetUtil.NETWORK_NONE){
                Log.d("mywather","网络OK");
                queryWeatherCode(updatedCityCode);
            }
            else {
                Log.d("myWeather","无网络");
                Toast.makeText(this, "无网络", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if (i==0){
            //如果是第一页，设置第一个小圆点为红，第二个为灰色
            page1Img.setImageResource(R.drawable.point_enable);
            page2Img.setImageResource(R.drawable.point_disable);
        }else {
            //如果是第二页，设置第一个小圆点为灰，第二个为红色
            page1Img.setImageResource(R.drawable.point_disable);
            page2Img.setImageResource(R.drawable.point_enable);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
