package com.example.ubuntu.market.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ubuntu.market.R;
import com.example.ubuntu.market.UserModel;
import com.example.ubuntu.market.fragment.DynamicFragment;
import com.example.ubuntu.market.fragment.JKArticleFragment;
import com.example.ubuntu.market.fragment.UserFragment;
import com.example.ubuntu.market.utils.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * 作者： wangyue on 17-3-28 17:00
 * 邮箱：973459080@qq.com
 */
public class HomeActivity extends FragmentActivity {
    @Bind(R.id.content)
    FrameLayout content;
    @Bind(R.id.homeIv)
    ImageView homeIv;
    @Bind(R.id.homeTv)
    TextView homeTv;
    @Bind(R.id.homePage)
    RelativeLayout foodPage;
    @Bind(R.id.controlIv)
    ImageView controlIv;
    @Bind(R.id.controlTv)
    TextView controlTv;
    @Bind(R.id.controlPage)
    RelativeLayout controlPage;
    @Bind(R.id.personIv)
    ImageView personIv;
    @Bind(R.id.personTv)
    TextView personTv;
    @Bind(R.id.personPage)
    RelativeLayout personPage;

    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;
    public static boolean isLogin = new UserModel().isLogin();
    private UserFragment mUserFragment;
    //private DynamicFragment mDynamicFragment;
    private JKArticleFragment jkArticleFragment;
    private DynamicFragment mDynamicFragment;

    private com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 表示设置当前的Activity 无Title并且全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        BmobUpdateAgent.update(this);
        init();
    }
    public void init() {
        homeIv.setImageResource(R.drawable.main_recipe_red);
        homeTv.setTextColor(Color.parseColor("#FD7575"));
        imageLoader.init(ImageLoaderConfiguration.createDefault(HomeActivity.this));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.xiaolian)
                .showImageForEmptyUri(R.drawable.xiaolian)
                .showImageOnFail(R.drawable.xiaolian).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        jkArticleFragment = new JKArticleFragment();
        fragmentTransaction.replace(R.id.content, jkArticleFragment);
        fragmentTransaction.commit();
    }
    //用于表示动态界面更新符
    int i = 0;
    @OnClick({R.id.homePage, R.id.controlPage, R.id.personPage})
    public void onClick(View view) {
        FragmentTransaction fragmenttransaction = fragmentManager.beginTransaction();
        hideFragments(fragmenttransaction);
        switch (view.getId()) {
            case R.id.homePage:
                i = 0;
                clearView();
                homeIv.setImageResource(R.drawable.main_recipe_red);
                homeTv.setTextColor(Color.parseColor("#FD7575"));
                if (jkArticleFragment != null) {
                    fragmenttransaction.show(jkArticleFragment);
                }
                break;
            case R.id.controlPage:
                if(isLogin==true) {
                    clearView();
                    controlIv.setImageResource(R.drawable.main_home_red);
                    controlTv.setTextColor(Color.parseColor("#FD7575"));
                    if (mDynamicFragment != null) {
                        fragmenttransaction.show(mDynamicFragment);
                        if (++i > 1) {
                            Intent intent = new Intent();
                            intent.setAction("MSG_DY1");
                            sendBroadcast(intent);
                        }
                    } else {
                        i++;
                        mDynamicFragment = new DynamicFragment();
                        fragmenttransaction.add(R.id.content, mDynamicFragment);
                    }
                }else{
                    ToastUtil.showLong(getApplicationContext(),"请先登陆，方可使用朋友圈：）");
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    fragmenttransaction.show(jkArticleFragment);
                }
                break;
            case R.id.personPage:
                i = 0;
                clearView();
                personIv.setImageResource(R.drawable.main_user_red);
                personTv.setTextColor(Color.parseColor("#FD7575"));
                if (mUserFragment != null) {
                    fragmenttransaction.show(mUserFragment);
                } else {
                    mUserFragment = new UserFragment();
                    fragmenttransaction.add(R.id.content, mUserFragment);
                }
                break;
        }
        fragmenttransaction.commit();
    }

    public void clearView() {
        homeIv.setImageResource(R.drawable.main_recipe_gray);
        homeTv.setTextColor(Color.parseColor("#828383"));
        controlIv.setImageResource(R.drawable.main_home_gray);
        controlTv.setTextColor(Color.parseColor("#828383"));
        personIv.setImageResource(R.drawable.main_user_gray);
        personTv.setTextColor(Color.parseColor("#828383"));
    }


    private void hideFragments(
            FragmentTransaction fragment) {

        if(jkArticleFragment !=null){
            fragment.hide(jkArticleFragment);
        }
        if (mUserFragment != null) {
            fragment.hide(mUserFragment);
        }
        if (mDynamicFragment != null) {
            fragment.hide(mDynamicFragment);
        }
    }

}
