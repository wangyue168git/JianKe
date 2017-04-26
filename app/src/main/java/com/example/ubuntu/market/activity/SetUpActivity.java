package com.example.ubuntu.market.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ubuntu.market.R;
import com.example.ubuntu.market.UserModel;
import com.example.ubuntu.market.entity.UserLocal;
import com.example.ubuntu.market.impl.makemodelimpl;
import com.example.ubuntu.market.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.update.BmobUpdateAgent;

public class SetUpActivity extends AppCompatActivity {


    @Bind(R.id.setname)
    TextView setname;
    @Bind(R.id.set_logout)
    Button logout;
    @Bind(R.id.setonlywifi)
    TextView setwifi;

    private UserModel userModel = new UserModel();
    final UserLocal userl = userModel.getUserLocal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set_up);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModel.deleteLocal();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        setwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSingleChoiceDialog();
            }
        });
    }

    @OnClick(R.id.set_back)
    public void onClick() {
        finish();
    }

    int yourChoice = 0;
    private void showSingleChoiceDialog(){
        final String[] items = { "仅wifi下提醒","3G/4G/wifi下提醒",};
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(this);
        singleChoiceDialog.setTitle("更新提醒情景");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, yourChoice,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         yourChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoice == 0) {
                            BmobUpdateAgent.setUpdateOnlyWifi(true);

                        }else{
                            BmobUpdateAgent.setUpdateOnlyWifi(false);
                        }
                        ToastUtil.showShort(getApplicationContext(),"修改成功！");
                    }
                });
        singleChoiceDialog.show();
    }

    private void showInputDialog() {
    /*@setView 装入一个EditView
     */
        final EditText editText = new EditText(this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this);
        inputDialog.setTitle("修改昵称").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!TextUtils.isEmpty(editText.getText())){
                            userModel.updateUserName(editText.getText().toString(), userl.getObjectId(),
                                    new makemodelimpl.BaseListener() {
                                        @Override
                                        public void getSuccess(Object o) {
                                            userModel.updateUserName(editText.getText().toString());
                                            ToastUtil.showShort(getApplicationContext(),"修改成功！");
                                        }

                                        @Override
                                        public void getFailure() {
                                        }
                                    });
                        }
                    }
                }).show();

    }
}
