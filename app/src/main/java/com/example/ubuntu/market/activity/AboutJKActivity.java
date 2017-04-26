package com.example.ubuntu.market.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ubuntu.market.DynamicModel;
import com.example.ubuntu.market.R;
import com.example.ubuntu.market.UserModel;
import com.example.ubuntu.market.entity.FeedBack;
import com.example.ubuntu.market.entity.User;
import com.example.ubuntu.market.entity.UserLocal;
import com.example.ubuntu.market.impl.makemodelimpl;
import com.example.ubuntu.market.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutJKActivity extends AppCompatActivity {

    @Bind(R.id.about_back)
    ImageView back;
    @Bind(R.id.about_button)
    Button bt;
    @Bind(R.id.about_edit)
    EditText about_edit;
    @Bind(R.id.aboutjk)
    TextView aboutJK;
    @Bind(R.id.about_title)
    TextView title;

    private DynamicModel dynamicModel = new DynamicModel();
    UserModel mUserModel = new UserModel();
    private FeedBack feedBack;
    private UserLocal mUserLocal;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about_jk);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        aboutJK.setText("本App由个人开发，旨在促进用户之间的交流沟通，更有丰富的文章信息可供查阅，在很多方面多有借鉴，也有很多不足的部分，后续将继续完善。" +'\n'+
                "使用过程中有什么问题和建议，欢迎反馈，将反馈信息填写在下方表中，提交即可。" +'\n'+
                "后续有更新将通过版本更新提醒" +'\n'+
                "谢谢使用");
        feedBack = new FeedBack();
        mUserLocal = mUserModel.getUserLocal();
        mUserModel.getUser(mUserLocal.getObjectId(), new makemodelimpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                mUser = (User) o;
                feedBack.setUser(mUser);
            }
            @Override
            public void getFailure() {
            }
        });


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(about_edit.getText())){
                    feedBack.setFeedBackText(about_edit.getText().toString());
                    dynamicModel.saveFeedBack(feedBack, new makemodelimpl.BaseListener() {
                        @Override
                        public void getSuccess(Object o) {
                            ToastUtil.showShort(AboutJKActivity.this,"反馈成功，感谢你的意见");
                        }
                        @Override
                        public void getFailure() {
                        }
                    });
                }
            }
        });
    }
    @OnClick(R.id.about_back)
    public void onClick() {
        finish();
    }
}
