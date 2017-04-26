package com.example.ubuntu.market.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ubuntu.market.R;
import com.example.ubuntu.market.UserModel;
import com.example.ubuntu.market.impl.makemodelimpl;
import com.example.ubuntu.market.utils.CountDownButtonHelper;
import com.example.ubuntu.market.utils.StringUtils;
import com.example.ubuntu.market.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by ubuntu on 17-3-28.
 * 手机验证界面
 */
public class PhoneValidateActivity extends Activity{

    @Bind(R.id.register_back)
    ImageView registerBack;
    @Bind(R.id.register_get_check_pass)
    Button registerGetCheckPass;
    @Bind(R.id.register_checknum)
    EditText register_checknum;
    @Bind(R.id.register_layout)
    LinearLayout registerLayout;
    @Bind(R.id.register_btn)
    Button registerBtn;
    EventHandler eh;
    @Bind(R.id.register_phone)
    EditText registerPhone;

    private UserModel mUserModel = new UserModel();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.phonevalidate_activity_main);
        ButterKnife.bind(this);
        eh = new EventHandler(){
            @Override
            public void afterEvent(int event,int result,Object data){
                if(result == SMSSDK.RESULT_COMPLETE){

                    if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        Intent intent = new Intent(PhoneValidateActivity.this, RegisterActivity.class);
                        intent.putExtra("phone",registerPhone.getText().toString());
                        startActivity(intent);
                        finish();
                    }else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){

                    }else if(event == SMSSDK.RESULT_ERROR){
                        ToastUtil.showLong(PhoneValidateActivity.this, "验证码错误");

                    }
                }else{
                        ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }
    @OnClick({R.id.register_back, R.id.register_get_check_pass, R.id.register_btn})
    public void onCLick(View view){
        switch (view.getId()){
            case R.id.register_back:
                finish();
                break;
            case R.id.register_get_check_pass:
                if(StringUtils.isMobileNO(registerPhone.getText().toString())){
                    mUserModel.isPhoneRegister(registerPhone.getText().toString(), new makemodelimpl.BaseListener() {
                        @Override
                        public void getSuccess(Object o) {
                            ToastUtil.showLong(PhoneValidateActivity.this, "当前手机号码已注册，请直接登录");
                            startActivity(new Intent(PhoneValidateActivity.this, LoginActivity.class));
                        }

                        @Override
                        public void getFailure() {
                            SMSSDK.getVerificationCode("86", registerPhone.getText().toString());
                            CountDownButtonHelper countDownButtonHelper = new CountDownButtonHelper(registerGetCheckPass, "获取验证码", 60, 1);
                            countDownButtonHelper.start();
                            countDownButtonHelper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {

                                @Override
                                public void finish() {
                                    registerGetCheckPass.setEnabled(true);
                                }
                            });
                        }
                    });
                }else{
                        ToastUtil.showLong(PhoneValidateActivity.this, "手机号码格式不正确");
                    }
                    break;
            case R.id.register_btn:
                if (!TextUtils.isEmpty(register_checknum.getText().toString()) && !TextUtils.isEmpty(registerPhone.getText().toString())) {
                    SMSSDK.submitVerificationCode("86", registerPhone.getText().toString(), register_checknum.getText().toString());
                }
                break;


        }

    }

}
