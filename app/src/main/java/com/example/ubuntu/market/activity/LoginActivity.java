package com.example.ubuntu.market.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ubuntu.market.*;
import com.example.ubuntu.market.entity.User;
import com.example.ubuntu.market.entity.UserLocal;
import com.example.ubuntu.market.eventbus.UserEvent;
import com.example.ubuntu.market.impl.makemodelimpl;
import com.example.ubuntu.market.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class LoginActivity extends AppCompatActivity {

    //绑定对应组件
    @Bind(R.id.login_back)
    ImageView loginBack;
    @Bind(R.id.login_register)
    TextView loginRegister;
    @Bind(R.id.login_btn)
    Button loginBtn;
    @Bind(R.id.login_uname)
    EditText loginName;
    @Bind(R.id.login_pass)
    EditText loginPass;

    private UserModel mUserModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mUserModel = new UserModel();
    }

    @OnClick({R.id.login_back, R.id.login_register, R.id.login_btn})
    public void onCLick(View view){
        switch (view.getId()){
            case R.id.login_back:
                finish();
                break;
            case R.id.login_register:
                startActivity(new Intent(LoginActivity.this,PhoneValidateActivity.class));
                finish();
                break;
            case R.id.login_btn:
                if(!TextUtils.isEmpty(loginName.getText().toString())
                        && !TextUtils.isEmpty(loginPass.getText().toString())){
                    mUserModel.getUser(loginName.getText().toString(), loginPass.getText().toString(), new makemodelimpl.BaseListener() {
                        @Override
                        public void getSuccess(Object o) {
                            ToastUtil.showLong(LoginActivity.this,"登陆成功");
                            HomeActivity.isLogin = true;
                            User user = (User) o;
                            UserLocal userLocal = new UserLocal();

                            userLocal.setName(user.getName());
                            userLocal.setObjectId(user.getObjectId());
                            userLocal.setNumber(user.getNumber());

                            if(user.getPhoto() != null){
                                //本地数据库对象userlocal的photo属性存的是在Bmob云端存储的user对象photo文件的url路径
                                userLocal.setPhoto(user.getPhoto().getUrl());
                            }
                            mUserModel.putUserLocal(userLocal);
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            EventBus.getDefault().post(new UserEvent(userLocal));
                            finish();
                        }

                        @Override
                        public void getFailure() {
                           ToastUtil.showLong(LoginActivity.this,"用户名或密码错误，请重新输入");
                        }
                    });
                }
        }

    }


}
