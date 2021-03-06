package com.pku.edu.ChenGuoqiang.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import com.pku.edu.ChenGuoqiang.bean.City;
import com.pku.edu.ChenGuoqiang.db.CityDB;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    public static String cityname_global;

    public static String province_global = null;

    private static final String TAG = "Myapp";

    private static MyApplication mApplication;
    private CityDB mCityDB;

    private List<City> mCityList;


    private void initCityList(){
        mCityList = new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                prepareCityList();
            }
        }).start();
    }

    private boolean prepareCityList(){
        mCityList = mCityDB.getAllCity();
        int i = 0;
        for (City city : mCityList){
            i++;
            String cityName = city.getCity();
            String cityCode = city.getNumber();
        }
        Log.d(TAG,"i="+i);
        return true;
    }

    private CityDB openCityDB() {

        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "database1"
                + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        Log.d(TAG, path);
        if (!db.exists()) {

            String pathfolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + getPackageName()
                    + File.separator + "database1"
                    + File.separator;
            File dirFisrtFolder = new File(pathfolder);
            if (!dirFisrtFolder.exists()) {
                dirFisrtFolder.mkdir();
                Log.i("MyAPP","mkdirs");
            }
            Log.i("MyApp","db is not exists");
            try{
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len=is.read(buffer))!=-1){
                    fos.write(buffer,0,len);
                    fos.flush();
                }
                fos.close();
                is.close();
            }catch (IOException e){
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this,path);
    }

    public List<City> getmCityList(){
        return mCityList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyApplication->Oncreate");

        mApplication = this;
        mCityDB = openCityDB();
        initCityList();
    }

    public static MyApplication getInstance() {
        return mApplication;
    }

}
