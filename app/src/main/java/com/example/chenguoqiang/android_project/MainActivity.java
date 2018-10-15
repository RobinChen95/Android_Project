package com.example.chenguoqiang.android_project;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.pku.edu.ChenGuoqiang.util.NetUtil;

public class MainActivity extends Activity implements View.OnClickListener {

    private ImageView mUpdateBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        if (NetUtil.getNetWorkState(this)!= NetUtil.NETWORK_NONE){
            Log.d("myWeather","网络OK");
            Toast.makeText(MainActivity.this,"网络OK",Toast.LENGTH_LONG).show();
        }
        else {
            Log.d("myWeather","无网络");
            Toast.makeText(MainActivity.this,"无网络",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view){
        if (view.getId()==R.id.title_update_btn){
            SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code","101010100");
            Log.d("myWeather",cityCode);
        }
    }
}
