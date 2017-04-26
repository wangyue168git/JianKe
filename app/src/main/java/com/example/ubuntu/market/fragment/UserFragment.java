package com.example.ubuntu.market.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ubuntu.market.activity.AboutJKActivity;
import com.example.ubuntu.market.activity.JKLoveActivity;
import com.example.ubuntu.market.activity.LoginActivity;
import com.example.ubuntu.market.R;
import com.example.ubuntu.market.RoundImageView;
import com.example.ubuntu.market.UserModel;
import com.example.ubuntu.market.activity.MyDynamicsActivity;
import com.example.ubuntu.market.activity.SendArticleActivity;
import com.example.ubuntu.market.activity.SetUpActivity;
import com.example.ubuntu.market.dialog.DialogBuilder;
import com.example.ubuntu.market.entity.User;
import com.example.ubuntu.market.entity.UserLocal;

import com.example.ubuntu.market.eventbus.UserEvent;
import com.example.ubuntu.market.impl.makemodelimpl;
import com.example.ubuntu.market.utils.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.listener.BmobDialogButtonListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateStatus;
import de.greenrobot.event.EventBus;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;


public class UserFragment extends Fragment {
    @Bind(R.id.UserPhoto)
    RoundImageView UserPhoto;
    @Bind(R.id.loginText)
    TextView loginText;
    @Bind(R.id.sendFood)
    TextView sendFood;
    @Bind(R.id.sendDynamic)
    TextView sendDynamic;
    @Bind(R.id.love)
    Button love;
    @Bind(R.id.send)
    Button  send;
    @Bind(R.id.update)
    Button  update;
    @Bind(R.id.guanyu)
    Button  guanyu;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private final String LOGINUSER = "loginuser";
    private UserLocal mUserLocal;
    private final int REQUEST_CODE = 0x01;
    private final int REQUEST_CODE_1 = 0X02;

    private UserModel mUserModel = new UserModel();
    private Dialog mLoadingDialog;
    private Dialog mLoadingFinishDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUserLocal = mUserModel.getUserLocal();
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, v);
        EventBus.getDefault().register(this);


        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_empty_dish)
                .showImageForEmptyUri(R.drawable.ic_empty_dish)
                .showImageOnFail(R.drawable.ic_empty_dish).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();
        if (mUserLocal != null) {
            loginText.setText(mUserLocal.getName());
            imageLoader.displayImage(mUserLocal.getPhoto(), UserPhoto, options);
        }

        mLoadingDialog = DialogBuilder.createLoadingDialog(getActivity(), "正在上传图片");
        mLoadingFinishDialog = DialogBuilder.createLoadingfinishDialog(getActivity(), "上传完成");
        return v;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.UserPhoto, R.id.loginText, R.id.sendFood, R.id.sendDynamic, R.id.love, R.id.send, R.id.update, R.id.guanyu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.UserPhoto:
                if (mUserModel.isLogin()) {
                    final PhotoPickerIntent intent = new PhotoPickerIntent(getActivity());
                    intent.setPhotoCount(1);
                    intent.setShowCamera(true);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.loginText:
                if(mUserModel.isLogin()){
                    Intent intent = new Intent(getActivity(),SetUpActivity.class);
                    startActivityForResult(intent,REQUEST_CODE_1);
                }else {
                    onLogin(getActivity());
                }
                break;
            case R.id.sendFood:
                startActivity(new Intent(getActivity(), SendArticleActivity.class));
                break;
            case R.id.sendDynamic:
                if (mUserModel.isLogin()) {
                    mUserModel.getUser(mUserLocal.getObjectId(), new makemodelimpl.BaseListener() {
                        @Override
                        public void getSuccess(Object o) {
                            onSendDynamic(getActivity(), (User) o);
                        }
                        @Override
                        public void getFailure() {
                        }
                    });
                } else {
                    ToastUtil.showLong(getContext(),"请先登陆，方可发表动态：）");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.love:
                startActivity(new Intent(getActivity(), JKLoveActivity.class));
                break;
            case R.id.send:
                if (mUserModel.isLogin()) {
                   startActivity(new Intent(getActivity(), MyDynamicsActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.update:
               // BmobUpdateAgent.initAppVersion();
//                BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
//                    @Override
//                    public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
//                        if (updateStatus == UpdateStatus.No) {
//                            Toast.makeText(getActivity(), "版本无更新", Toast.LENGTH_SHORT).show();
//                        } else if (updateStatus == UpdateStatus.Yes) {
//                            BmobUpdateAgent.forceUpdate(getActivity());
//                        }
//                    }
//                });
                BmobUpdateAgent.update(getActivity());
                break;
            case R.id.guanyu:
                startActivity(new Intent(getActivity(),AboutJKActivity.class));
                break;
        }
    }

    /**
     * Eventbus的处理函数
     *接收消息
     * @param event
     */
    public void onEventMainThread(UserEvent event) {
        mUserLocal = event.getUserLocal();
        if (mUserLocal != null) {
            if (event.getUserLocal().getPhoto() != null) {
                imageLoader.displayImage(event.getUserLocal().getPhoto(), UserPhoto, options);
            }
            loginText.setText(event.getUserLocal().getName());
        }
    }


    int x = 0; //表示是否上传成功
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选择结果回调
        if(requestCode == REQUEST_CODE_1){
            mUserLocal = mUserModel.getUserLocal();
            loginText.setText(mUserLocal.getName());

        }
        if (requestCode == REQUEST_CODE && data != null) {
            mLoadingDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(x ==0) {
                        mLoadingDialog.dismiss();
                        ToastUtil.showLong(getActivity(), "网络问题，请稍后再试");
                    }
                    x = 0;
                }
            }, 7000);
            List<String> pathList = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            mUserModel.updateUserPhoto(pathList.get(0), mUserLocal.getObjectId(), new makemodelimpl.BaseListener() {

                public void getSuccess(Object o) {
                    mLoadingDialog.dismiss();
                    mLoadingFinishDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLoadingFinishDialog.dismiss();
                        }
                    }, 500);
                    ToastUtil.showLong(getActivity(), "头像修改成功");
                    x = 1 ;
                    User user = (User) o;
                    imageLoader.displayImage(user.getPhoto().getUrl(), UserPhoto, options);

                    if (user.getPhoto() != null) {
                        mUserModel.updateUserLocalPhoto(user.getPhoto().getUrl());
                    }

                }
                public void getFailure() {

                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 登录
     *
     * @param context
     */
    public void onLogin(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    /**
     * 发说说
     *
     * @param context
     */
    public void onSendDynamic(Context context, User user) {
        if (mUserModel.isLogin()) {
            Intent intent = new Intent(context, SendDynamicActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("User", user);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } else {
            onLogin(context);
        }
    }

}
