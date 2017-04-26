package com.example.ubuntu.market.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ubuntu.market.DynamicModel;
import com.example.ubuntu.market.R;
import com.example.ubuntu.market.UserModel;
import com.example.ubuntu.market.entity.DynamicItem;
import com.example.ubuntu.market.entity.User;
import com.example.ubuntu.market.entity.UserLocal;
import com.example.ubuntu.market.fragment.DynamicAdapter;
import com.example.ubuntu.market.impl.makemodelimpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyDynamicsActivity extends AppCompatActivity {

    @Bind(R.id.dylove_back)
    ImageView back;
    @Bind(R.id.dylove_title)
    TextView title;
    @Bind(R.id.dylove_listview)
    ListView listview;
    @Bind(R.id.loading)
    RelativeLayout loading;

    private DynamicModel dynamicModel = new DynamicModel();
    private UserModel mUserModel = new UserModel();
    private List<DynamicItem> dynamicItemList;
    private UserLocal mUserLocal;
    private User mUser;
    private DynamicItem dynamicItem;
    DynamicAdapter dynamicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_dynamics);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        mUserLocal = mUserModel.getUserLocal();
        mUserModel.getUser(mUserLocal.getObjectId(), new makemodelimpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                mUser = (User) o;
                dynamicModel.getDynamicItemByUser(mUser, new makemodelimpl.BaseListener() {
                    @Override
                    public void getSuccess(Object o) {
                        dynamicItemList = (List<DynamicItem>) o;

                        if(dynamicItemList.size()==0){
                            loading.setVisibility(View.VISIBLE);
                        }
                        dynamicAdapter.setDatas(dynamicItemList);
                        dynamicAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void getFailure() {
                    }
                });
            }
            @Override
            public void getFailure() {
            }
        });
        dynamicItemList = new ArrayList<>();

        dynamicAdapter = new DynamicAdapter(this, R.layout.dynamic_listviewother_item, dynamicItemList);
        listview.setAdapter(dynamicAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dynamicItem = dynamicItemList.get(i);
                Intent intent = new Intent(MyDynamicsActivity.this,DynamicDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("DYNAMIC", dynamicItem);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    @OnClick(R.id.dylove_back)
    public void onClick() {
        finish();
    }
}
