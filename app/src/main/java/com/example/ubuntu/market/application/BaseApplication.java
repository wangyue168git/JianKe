package com.example.ubuntu.market.application;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.example.ubuntu.market.entity.ArticleDetail;
import com.example.ubuntu.market.entity.UserLocal;

/**
 * 作者： wangyue on 17-3-28 15:04
 * 邮箱：973459080@qq.com
 */
public class BaseApplication extends Application{

    static Context sContext;
    public static Context getmyContext(){
        return sContext;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        sContext = getApplicationContext();
        initializeDB();
    }
    @Override
    public void onTerminate(){
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    /**
     * android 5.0+ must configurationbuilder the ModelDB
     */
    protected void initializeDB() {
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        configurationBuilder.addModelClasses(ArticleDetail.class);
        configurationBuilder.addModelClasses(UserLocal.class);
        ActiveAndroid.initialize(configurationBuilder.create());
    }

}
