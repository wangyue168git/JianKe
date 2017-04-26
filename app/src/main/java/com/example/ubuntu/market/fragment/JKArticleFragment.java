package com.example.ubuntu.market.fragment;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.ubuntu.market.activity.JKArticleDetailActivity;
import com.example.ubuntu.market.JKModel;
import com.example.ubuntu.market.activity.JKSearchActivity;
import com.example.ubuntu.market.R;
import com.example.ubuntu.market.RoundImageView;
import com.example.ubuntu.market.SlideShowView;
import com.example.ubuntu.market.entity.ArticleDetail;
import com.example.ubuntu.market.entity.JKArticle;
import com.example.ubuntu.market.impl.JKModelImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.maxwin.view.XListView;


public class JKArticleFragment extends Fragment implements JKArticleFragImpl,XListView.IXListViewListener{

    @Bind(R.id.xListView)
    XListView xListView;
    @Bind(R.id.search)
    EditText search;
    @Bind(R.id.jktip)
    LinearLayout tip;
    @Bind(R.id.jkloading)
    RelativeLayout loading;


    RoundImageView authorImg;
    ImageView imageView;

    private MyReceiver myReceiver;
    private JKAdapter jkAdapter ;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<JKArticle> list;
    private List<Map<String,Object>>  mDataList;
    final JKModel jkModel = new JKModel();
    private SlideShowView mSlideshowView;
    private LinearLayout mJKFragmentHead;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this, v);
        mJKFragmentHead = (LinearLayout)inflater.inflate(R.layout.jkhead,null);
        mSlideshowView = (SlideShowView) mJKFragmentHead.findViewById(R.id.slideshowView);
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(true);
        xListView.addHeaderView(mJKFragmentHead);
        jkModel.getGeneralJKsItem1(new JKModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                list = (ArrayList<JKArticle>) o;
                jkAdapter = new JKAdapter(getActivity(),R.layout.listview_item,list);
                xListView.setAdapter(jkAdapter);
                loading.setVisibility(View.GONE);
                tip.setVisibility(View.VISIBLE);
            }
            @Override
            public void getFailure() {
                Log.i("log","failue");
            }
        });
        xListView.setXListViewListener(this);
        onInitSliderShow();

//        EventBus eventBus = EventBus.getDefault();
//        eventBus.register(this);
        myReceiver = new MyReceiver();
        //创建intent刷选器
        IntentFilter filter = new IntentFilter();
        //制定监听的Action
        filter.addAction("MSG");
        //注册Brodecast,开启监听
        getContext().registerReceiver(myReceiver,filter);

        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<JKArticle> mDatas = jkAdapter.getmDatas();
                JKArticle jkArticle = mDatas.get(i-2);

                jkModel.getJKArticleDetailItem(getActivity(),jkArticle.getLink(), new JKModelImpl.BaseListener() {
                    @Override
                    public void getSuccess(Object o) {
                        Intent intent = new Intent(getActivity(),JKArticleDetailActivity.class);
                        intent.putExtra("article", (ArticleDetail) o);
                        startActivity(intent);
                    }
                    @Override
                    public void getFailure() {
                    }
                });
            }
        });
        return v;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(myReceiver);
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        jkModel.getGeneralJKsItem1(new JKModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                ArrayList<JKArticle> list = (ArrayList<JKArticle>) o;
                onRefresh(list);
            }
            @Override
            public void getFailure() {
            }
        });

    }

    @Override
    public void onLoadMore() {
        jkModel.getGeneralJKsItem2(new JKModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                ArrayList<JKArticle> list = (ArrayList<JKArticle>) o;
                onLoadMore(list);
            }
            @Override
            public void getFailure() {
            }
        });
    }

    @Override
    public void onLoadMore(List<JKArticle> list) {
        jkAdapter.addAll(list);
        jkAdapter.notifyDataSetChanged();
        xListView.stopLoadMore();
    }

    @Override
    public void onRefresh(List<JKArticle> list) {
        xListView.setVisibility(View.VISIBLE);
        jkAdapter.setDatas(list);
        jkAdapter.notifyDataSetChanged();
        xListView.stopRefresh();
    }
    public void onInitSliderShow(){
        jkModel.getSliderShowJKArticle(new JKModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<SlideShowView.SliderShowViewItem> list = (List<SlideShowView.SliderShowViewItem>) o;
                mSlideshowView.initUI(getActivity(), list);
            }
            @Override
            public void getFailure() {

            }
        });
    }
    @OnClick(R.id.search)
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.search:
                startActivity(new Intent(getActivity(), JKSearchActivity.class));
                break;
        }
    }

    public  class  MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("MSG")) {
                int position = intent.getIntExtra("msg", 0);
                Log.i("position",position+"");
                list.remove(position);
                jkAdapter.notifyDataSetChanged();
            }
        }
    }

}
