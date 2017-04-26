package com.example.ubuntu.market.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ubuntu.market.R;
import com.example.ubuntu.market.UserModel;
import com.example.ubuntu.market.entity.User;
import com.example.ubuntu.market.impl.makemodelimpl;
import com.example.ubuntu.market.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.register_back)
    ImageView registerBack;
    @Bind(R.id.register_name)
    EditText registerName;
    @Bind(R.id.register_password)
    EditText registerPassword;
    @Bind(R.id.register_password_again)
    EditText registerPassword_again;
    @Bind(R.id.register_btn)
    Button registerBtn;
    @Bind(R.id.register_info)
    CheckBox checkBox;



    private String mPhone;
    private UserModel mUserModel = new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mPhone = getIntent().getStringExtra("phone");
        registerBtn.setEnabled(false);
    }
    @OnClick({R.id.register_back, R.id.register_btn})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_back:
                finish();
                break;
            case R.id.register_btn:
                if (!TextUtils.isEmpty(registerName.getText().toString()) && !TextUtils.isEmpty(registerPassword.getText().toString())) {
                    User user = new User();
                    user.setName(registerName.getText().toString());
                    user.setPassword(registerPassword.getText().toString());
                    user.setNumber(mPhone);
                    mUserModel.onRegister(user, new makemodelimpl.BaseListener() {
                        @Override
                        public void getSuccess(Object o) {
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }

                        @Override
                        public void getFailure() {

                        }
                    });
        }else{
                    ToastUtil.showLong(RegisterActivity.this, "请填写完整信息");
                }
                break;
        }
    }
    @OnTextChanged({R.id.register_password_again,R.id.register_password})
    public void onTextChange(){
        if(registerPassword_again.getText().toString()!="" && registerPassword.getText().toString()!=""
                && registerPassword_again.getText().toString().equals(registerPassword.getText().toString())){
            Drawable nav_up=getResources().getDrawable(R.drawable.yes);
            Drawable nav_left=getResources().getDrawable(R.drawable.password);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            nav_left.setBounds(0, 0, nav_left.getMinimumWidth(), nav_left.getMinimumHeight());
            registerPassword_again.setCompoundDrawables(nav_left,null,nav_up,null);
        }else{
            Drawable nav_up=getResources().getDrawable(R.drawable.no);
            Drawable nav_left=getResources().getDrawable(R.drawable.password);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            nav_left.setBounds(0, 0, nav_left.getMinimumWidth(), nav_left.getMinimumHeight());
            registerPassword_again.setCompoundDrawables(nav_left,null,nav_up,null);
        }
    }
    @OnCheckedChanged (R.id.register_info)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        if(isChecked){
            registerBtn.setEnabled(true);
        }else{
            registerBtn.setEnabled(false);
        }
    }



}
