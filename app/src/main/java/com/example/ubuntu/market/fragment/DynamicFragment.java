package com.example.ubuntu.market.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ubuntu.market.DynamicModel;
import com.example.ubuntu.market.R;
import com.example.ubuntu.market.UserModel;
import com.example.ubuntu.market.activity.ArticleByUserDetailActivity;
import com.example.ubuntu.market.activity.DynamicDetailActivity;
import com.example.ubuntu.market.activity.LoginActivity;
import com.example.ubuntu.market.entity.ArticleDetail;
import com.example.ubuntu.market.entity.DynamicItem;
import com.example.ubuntu.market.entity.User;
import com.example.ubuntu.market.entity.UserLocal;
import com.example.ubuntu.market.impl.makemodelimpl;
import com.example.ubuntu.market.utils.NetUtil;
import com.example.ubuntu.market.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.maxwin.view.XListView;

/**
 * 简友圈界面
 */

public class DynamicFragment extends Fragment implements XListView.IXListViewListener{

    @Bind(R.id.publish)
    ImageView publish;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.dynamic_xListView)
    XListView xListView;
    @Bind(R.id.dynamic_loading)
    RelativeLayout loading;
    @Bind(R.id.dynamic_tip)
    LinearLayout tip;



    private DynamicAdapter mAdapter;

    private User user;
    private UserLocal mUserLocal;
    private UserModel mUserModel = new UserModel();
    private DynamicModel mDynamicModel = new DynamicModel();
    private List<DynamicItem> mDynamicList = new ArrayList<>();;
    int page = 0;
    int dyNumber;
    private  MyReceiverRe myReceiver;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);
        ButterKnife.bind(this, view);

        myReceiver = new MyReceiverRe();
        //创建intent刷选器
        IntentFilter filter = new IntentFilter();
        //制定监听的Action
        filter.addAction("MSG_DY");
        filter.addAction("MSG_DY1");
        //注册Brodecast,开启监听
        getContext().registerReceiver(myReceiver,filter);


        mAdapter = new DynamicAdapter(getActivity(), R.layout.dynamic_listviewother_item, mDynamicList);
        xListView.setAdapter(mAdapter);
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);
        onRefresh();
        if (NetUtil.checkNet(getActivity())) {
            onRefresh();
        } else {
            loading.setVisibility(View.GONE);
            tip.setVisibility(View.VISIBLE);
            xListView.setVisibility(View.GONE);
        }
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dyNumber = position-1;
                DynamicItem item = mDynamicList.get(position-1);
                if(item.getType().equals("动态")) {
                    Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DYNAMIC", item);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), ArticleByUserDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DYNAMIC", item);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    //广播
    public  class  MyReceiverRe extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("MSG_DY")) {
                mDynamicList.remove(dyNumber);
                mAdapter.setDatas(mDynamicList);
                mAdapter.notifyDataSetChanged();
            }
            else if(action.equals("MSG_DY1")){
                onRefresh();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(myReceiver);
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.publish)
    public void onClick() {
        if (mUserModel.isLogin()) {
            mUserModel.getUser(mUserModel.getUserLocal().getObjectId(), new makemodelimpl.BaseListener() {
                @Override
                public void getSuccess(Object o) {
                    User user = (User) o;
                    Intent intent = new Intent(getActivity(), SendDynamicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("User", user);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }

                @Override
                public void getFailure() {

                }
            });
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    public void onRefresh(List<DynamicItem> list) {
        mDynamicList = list;
        loading.setVisibility(View.GONE);
        tip.setVisibility(View.GONE);
        xListView.setVisibility(View.VISIBLE);
        xListView.stopRefresh();
        mAdapter.setDatas(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        page = 0;
        mDynamicModel.getDynamicItem(page,new makemodelimpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<DynamicItem> list= (List<DynamicItem>) o;
                onRefresh(list);
            }

            @Override
            public void getFailure() {
            }
        });
    }
    public void onLoadMore(List<DynamicItem> list){
        mAdapter.addAll(list);
        mAdapter.notifyDataSetChanged();
        xListView.stopLoadMore();
    }

    @Override
    public void onLoadMore() {
        mDynamicModel.getDynamicItem(++page,new makemodelimpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<DynamicItem> list= (List<DynamicItem>) o;
                onLoadMore(list);
            }
            @Override
            public void getFailure() {
                ToastUtil.showShort(getActivity(),"没有更多内容了：）");
                xListView.stopLoadMore();
            }
        });
    }



}
