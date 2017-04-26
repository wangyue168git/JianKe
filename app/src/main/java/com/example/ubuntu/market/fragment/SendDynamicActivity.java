package com.example.ubuntu.market.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.ubuntu.market.DynamicModel;
import com.example.ubuntu.market.R;
import com.example.ubuntu.market.dialog.DialogBuilder;
import com.example.ubuntu.market.entity.DynamicItem;
import com.example.ubuntu.market.entity.DynamicPhotoItem;
import com.example.ubuntu.market.entity.User;
import com.example.ubuntu.market.impl.JKModelImpl;
import com.example.ubuntu.market.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * 作者： wangyue on 17-3-29 15:38
 * 邮箱：973459080@qq.com
 */
public class  SendDynamicActivity extends Activity{

    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.send)
    TextView send;
    @Bind(R.id.edit_content)
    EditText editContent;
    @Bind(R.id.gridView)
    GridView gridView;
    private DynamicPhotoChooseAdapter mDynamicPhotoChooseAdapter;
    private final String LOGINUSER = "loginuser";
    private User mUser;
    private Dialog mLoadingDialog;
    private Dialog mLoadingFinishDialog;
    private final int REQUEST_CODE = 0x01;
    @Override
    protected void  onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.senddynamic_activity_main);
        mUser = (User) getIntent().getSerializableExtra("User");
        ButterKnife.bind(this);
        init();
    }
    public void init(){
        final PhotoPickerIntent intent = new PhotoPickerIntent(SendDynamicActivity.this);
        intent.setPhotoCount(6);
        intent.setShowCamera(true);
        mDynamicPhotoChooseAdapter = new DynamicPhotoChooseAdapter(SendDynamicActivity.this);
        gridView.setAdapter(mDynamicPhotoChooseAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mDynamicPhotoChooseAdapter.getCount() - 1) {
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });
        mLoadingDialog = DialogBuilder.createLoadingDialog(SendDynamicActivity.this, "正在上传");
        mLoadingFinishDialog = DialogBuilder.createLoadingfinishDialog(SendDynamicActivity.this, "上传完成");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选择结果回调
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            List<DynamicPhotoItem> list = new ArrayList<>();
            if (pathList.size() != 0) {
                for (String path : pathList) {
                    list.add(new DynamicPhotoItem(path, false));
                }
            }
            mDynamicPhotoChooseAdapter.addData(list);
        }
    }
     int x = 0;
    @OnClick({R.id.cancel, R.id.send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.send:
                mLoadingDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(x ==0) {
                            mLoadingDialog.dismiss();
                            ToastUtil.showLong(getApplicationContext(), "网络问题，请稍后再试");
                        }
                        x = 0;
                    }
                }, 10000);
                DynamicItem dynamicItem = new DynamicItem();
                dynamicItem.setWriter(mUser);
                dynamicItem.setType("动态");
                List<BmobFile> fileList = new ArrayList<>();
                ArrayList<DynamicPhotoItem> photoItems = (ArrayList<DynamicPhotoItem>) mDynamicPhotoChooseAdapter.getData();
                for (int i = 0; i < photoItems.size() - 1; i++) {
                    fileList.add(new BmobFile(new File(photoItems.get(i).getFilePath())));
                }
                dynamicItem.setDynamicContent(editContent.getText().toString());
                dynamicItem.setPhotoList(fileList);
                new DynamicModel().sendDynamicItem(dynamicItem, new JKModelImpl.BaseListener() {
                    @Override
                    public void getSuccess(Object o) {
                        mLoadingDialog.dismiss();
                        mLoadingFinishDialog.show();
                        Intent intent = new Intent();
                        intent.setAction("MSG_DY1");
                        sendBroadcast(intent);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mLoadingFinishDialog.dismiss();
                                finish();
                            }
                        }, 500);
                        x = 1;
                    }
                    @Override
                    public void getFailure() {

                    }
                });
                break;
        }
    }
}


