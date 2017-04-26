package com.example.ubuntu.market.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.ubuntu.market.*;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.smssdk.SMSSDK;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /**全屏设置，隐藏窗口所有装饰**/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //设置BmobConfig：允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)
        BmobConfig config = new BmobConfig.Builder(this)
                //设置appkey(必填)
                .setApplicationId("0f3f0aa6d90b3371768beaaf80be9822")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(5500)
                .build();
        Bmob.initialize(config);
        SMSSDK.initSDK(this, "1c82239811d84", "77b49cf79ad2413d4a481dcf41e65031");

        //实现欢迎界面的2秒延迟，后跳转，等待后台数据处理
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this,HomeActivity.class));
                finish();
            }
        },2000);

    }
}
