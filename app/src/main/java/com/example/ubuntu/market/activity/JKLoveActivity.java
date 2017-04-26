package com.example.ubuntu.market.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ubuntu.market.MyLoveArticleModel;
import com.example.ubuntu.market.R;
import com.example.ubuntu.market.entity.ArticleDetail;
import com.example.ubuntu.market.entity.JKArticle;
import com.example.ubuntu.market.fragment.JKSearchAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JKLoveActivity extends AppCompatActivity {
    @Bind(R.id.jklove_back)
    ImageView back;
    @Bind(R.id.jklove_title)
    TextView title;
    @Bind(R.id.jklove_listview)
    ListView listview;
    @Bind(R.id.loading)
    RelativeLayout loading;

    private MyLoveArticleModel articleModel = new MyLoveArticleModel();
    private List<ArticleDetail> articleDetailList;
    private List<JKArticle> jkArticleList;
    private JKArticle jkArticle;
    private ArticleDetail articleDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_jklove);
        ButterKnife.bind(this);
        init();
    }
    public void init(){
        articleDetailList = new ArrayList<>();
        articleDetailList = articleModel.onQuerylist();
        jkArticleList = new ArrayList<>();
        for(ArticleDetail item : articleDetailList){
            jkArticle = new JKArticle();
            jkArticle.setAuthorImg("http:"+item.getAuthorImage());
            jkArticle.setTitle(item.getTitle());
            jkArticle.setAuthorName(item.getAuthorName());
            jkArticle.setArticleAbstract(item.getContent().replaceAll("<[^>]+>","").replaceAll("\n","").substring(0,130)+"...");
            jkArticleList.add(jkArticle);
        }
        if(articleDetailList.size()==0){
            loading.setVisibility(View.VISIBLE);
        }

        listview.setAdapter(new JKSearchAdapter(JKLoveActivity.this,R.layout.search_list_item,jkArticleList));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                articleDetail = articleDetailList.get(i);
                Intent intent = new Intent(JKLoveActivity.this,JKArticleDetailActivity.class);
                intent.putExtra("article",articleDetail);
                startActivity(intent);
            }
        });
    }
    @OnClick(R.id.jklove_back)
    public void onClick() {
        finish();
    }
}
