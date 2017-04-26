package com.example.ubuntu.market.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ubuntu.market.JKModel;
import com.example.ubuntu.market.R;
import com.example.ubuntu.market.entity.ArticleDetail;
import com.example.ubuntu.market.entity.JKArticle;
import com.example.ubuntu.market.fragment.JKArticleFragImpl;
import com.example.ubuntu.market.fragment.JKSearchAdapter;
import com.example.ubuntu.market.https.HtmlParser;
import com.example.ubuntu.market.impl.JKModelImpl;
import com.example.ubuntu.market.impl.makemodelimpl;
import com.example.ubuntu.market.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.maxwin.view.XListView;

public class JKSearchResultActivity extends AppCompatActivity implements JKArticleFragImpl,XListView.IXListViewListener{

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.search_xListView)
    XListView xListView;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.search_loading)
    RelativeLayout loading;
    @Bind(R.id.search_tip)
    LinearLayout tip;

    SearchReceiver myReceiver;
    JKSearchAdapter jkSearchAdapter;
    private final String SEARCHRESULT = "searchresult";
    private List<JKArticle> list;
    JKModel jkModel = new JKModel();
    int page=1;
    String search_q;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_jksearch_result);
        ButterKnife.bind(this);
        search_q = getIntent().getStringExtra("search_q");
        title.setText("关于 "+search_q+" 的搜索结果");
        list = new ArrayList<>();

        myReceiver = new SearchReceiver();
        //创建intent刷选器
        IntentFilter filter = new IntentFilter();
        //制定监听的Action
        filter.addAction("MSG_NO");
        //注册Brodecast,开启监听
        registerReceiver(myReceiver,filter);

        list = (List<JKArticle>)getIntent().getSerializableExtra(SEARCHRESULT);
        xListView.setPullRefreshEnable(false);
        xListView.setPullLoadEnable(true);
        jkSearchAdapter = new JKSearchAdapter(JKSearchResultActivity.this,R.layout.search_list_item,list);
        xListView.setAdapter(jkSearchAdapter);
        loading.setVisibility(View.GONE);
        tip.setVisibility(View.VISIBLE);
        xListView.setXListViewListener(this);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JKArticle jkArticle = list.get(i-1);
                jkModel.getJKArticleDetailItem(getApplicationContext(),jkArticle.getLink(), new JKModelImpl.BaseListener() {
                    @Override
                    public void getSuccess(Object o) {
                        Intent intent = new Intent(JKSearchResultActivity.this,JKArticleDetailActivity.class);
                        intent.putExtra("article", (ArticleDetail) o);
                        startActivity(intent);
                    }
                    @Override
                    public void getFailure() {
                    }
                });

            }
        });



    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadMore() {
        if(++page<=HtmlParser.total_page) {
            jkModel.onSearchJK(search_q, page, "note", new makemodelimpl.BaseListener() {
                @Override
                public void getSuccess(Object o) {
                    List<JKArticle> list = (List<JKArticle>) o;
                    onLoadMore(list);
                }
                @Override
                public void getFailure() {
                }
            });
        }
    }

    @Override
    public void onLoadMore(List<JKArticle> list) {
        jkSearchAdapter.addAll(list);
        jkSearchAdapter.notifyDataSetChanged();
        xListView.stopLoadMore();
    }

    @Override
    public void onRefresh(List<JKArticle> list) {
    }
    public  class  SearchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("MSG_NO")) {
                ToastUtil.showLong(getApplicationContext(), "该文章被偷走了,你懂的,嘿嘿：）");
            }
        }
    }
}
