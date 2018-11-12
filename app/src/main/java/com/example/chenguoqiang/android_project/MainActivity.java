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

public class MainActivity extends Activity implements View.OnClickListener {

    private static final int UPDATE_TODAY_WEATHER = 1;

    private ImageView mUpdateBtn;

    private ImageView mcitySelect;

    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv,
            temperatureTv, climateTv, windTv, city_name_Tv;

    private ImageView weatherImg, pmImg;

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
                Refresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
                Refresh.setColorSchemeColors(R.color.colorPrimaryDark);
                Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        queryWeatherCode(updatedCityCode);
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

        viewPager = (ViewPager)findViewById(R.id.pages);

        views = new ArrayList<>();
        views.add(LayoutInflater.from(this).inflate(R.layout.page1,null));
        views.add(LayoutInflater.from(this).inflate(R.layout.page2,null));

        pageadapter = new pageAdapter(this,views);

        viewPager.setAdapter(pageadapter);

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
}
