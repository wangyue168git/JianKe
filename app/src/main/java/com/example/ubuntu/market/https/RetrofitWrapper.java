package com.example.ubuntu.market.https;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 作者： wangyue on 17-3-30 09:35
 * 邮箱：973459080@qq.com
 */
public class RetrofitWrapper {
    //单例
    public static RetrofitWrapper instance;
    public static Retrofit retrofit;
    public static final String BASE_URL = "http://www.jianshu.com/";

    private RetrofitWrapper(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为Observable<T>的支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
    public static RetrofitWrapper getInstance(){
        if(instance == null){
            //给当前类加锁，实现单例模式
            synchronized (RetrofitWrapper.class){
                instance = new RetrofitWrapper();
            }
        }
        return instance;
    }
    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }
}
