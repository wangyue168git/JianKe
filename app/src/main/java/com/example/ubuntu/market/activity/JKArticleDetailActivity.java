package com.example.ubuntu.market.activity;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ubuntu.market.R;
import com.example.ubuntu.market.RoundImageView;
import com.example.ubuntu.market.entity.ArticleDetail;
import com.example.ubuntu.market.MyLoveArticleModel;
import com.example.ubuntu.market.utils.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


import java.net.URL;


public class JKArticleDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ArticleDetail articleDetail;
    TextView textVIew;
    ImageView favorite;
    RelativeLayout loading;
    LinearLayout tip;
    RoundImageView article_authorImg;
    TextView article_authorName;
    TextView article_date;
    TextView article_wordage;
    TextView article_title;
    ImageView back;


    final Spanned[] text = new Spanned[1];
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    protected static final int SUCCESS_GET_CONTACT = 0;
    private Drawable drawable;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0x111) {
                textVIew.setText(text[0]);
                article_authorName.setText(articleDetail.getAuthorName());
                article_date.setText(articleDetail.getDate());
                article_wordage.setText(articleDetail.getWordAge());
                article_title.setText(articleDetail.getTitle());
                loading.setVisibility(View.GONE);
                tip.setVisibility(View.VISIBLE);
                imageLoader.displayImage("http:" + articleDetail.getAuthorImage(), article_authorImg, options);
                if (MyLoveArticleModel.onQuery(articleDetail)) {
                    favorite.setBackgroundResource(R.drawable.gray_star_enabled);
                } else {
                    favorite.setBackgroundResource(R.drawable.star);
                }
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_jkarticle_detail);
        init();

    }

    private void init() {
        articleDetail = (ArticleDetail) getIntent().getSerializableExtra("article");
        textVIew = (TextView) findViewById(R.id.articleDetail);
        article_authorName = (TextView) findViewById(R.id.article_authorName);
        article_title = (TextView) findViewById(R.id.title_article);
        article_date = (TextView) findViewById(R.id.article_date);
        article_authorImg = (RoundImageView) findViewById(R.id.article_authorImg);
        article_wordage = (TextView) findViewById(R.id.article_wordage);
        loading = (RelativeLayout)findViewById(R.id.loading);
        tip = (LinearLayout) findViewById(R.id.tip);
        favorite = (ImageView)findViewById(R.id.favorite);
        back = (ImageView) findViewById(R.id.article_back);
        back.setOnClickListener(this);
        favorite.setOnClickListener(this);

        imageLoader.init(ImageLoaderConfiguration.createDefault(JKArticleDetailActivity.this));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.xiaolian)
                .showImageForEmptyUri(R.drawable.xiaolian)
                .showImageOnFail(R.drawable.xiaolian).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();
//        Log.i("authorname", articleDetail.getAuthorName());

        new Thread() {
            @Override
            public void run() {
                text[0] = Html.fromHtml(articleDetail.getContent(), new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String s) {
                        Drawable drawable;
                        String imglink;
                        if (s.substring(0, 5).equals("http:")) {
                            imglink = s;
                        } else {
                            imglink = "http:" + s;
                        }
                        //Log.i("das",s);
                        URL url;
                        try {
                            url = new URL(imglink);
                            drawable = Drawable.createFromStream(url.openStream(), "");  //获取网路图片
                        } catch (Exception e) {
                            return null;
                        }
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth() * 3, drawable.getIntrinsicHeight() * 3);
                        return drawable;
                    }
                }, null);
                mHandler.sendEmptyMessage(0x111);
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.article_back:
                finish();
                break;
            case R.id.favorite:
                if (articleDetail != null) {
                    if (MyLoveArticleModel.onQuery(articleDetail)) {
                        favorite.setBackgroundResource(R.drawable.star);
                        MyLoveArticleModel.onDelete(articleDetail);
                        ToastUtil.showShort(JKArticleDetailActivity.this, "取消成功");
                    } else {
                        favorite.setBackgroundResource(R.drawable.gray_star_enabled);
                        Log.i("String",articleDetail.toString());
                        MyLoveArticleModel.onInsert(articleDetail);
                        ToastUtil.showShort(JKArticleDetailActivity.this, "收藏成功");
                    }
                }
                break;
        }

    }

}
//    class DownTask extends AsyncTask<URL, Integer, String> {
//        Context context;
//
//        public DownTask(Context ctx) {
//            context = ctx;
//        }
//
//        @Override
//        protected String doInBackground(URL... urls) {
//            return null;
//        }
//    }
// textVIew.setMovementMethod(ScrollingMovementMethod.getInstance());
//        DownTask task = new DownTask(this);
//        task.execute();
//textVIew.setMovementMethod(ScrollingMovementMethod.getInstance());
//        webView = (WebView)findViewById(R.id.webView);
//        webView.getSettings().setJavaScriptEnabled(true);
//        String encoding = "UTF-8";
//        String mimeType = "text/html";
//  webView.loadDataWithBaseURL("http://www.jianshu.com",articleLink,mimeType, encoding, "about:blank");