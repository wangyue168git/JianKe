package com.example.ubuntu.market;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.activeandroid.util.Log;
import com.example.ubuntu.market.application.BaseApplication;
import com.example.ubuntu.market.entity.User;
import com.example.ubuntu.market.entity.UserLocal;
import com.example.ubuntu.market.impl.makemodelimpl;
import com.example.ubuntu.market.utils.ToastUtil;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by ubuntu on 17-3-28.
 */
public class UserModel {

    private final String LOGINUSER = "loginuser";

    public void getUser(String phone, String password, final makemodelimpl.BaseListener listener){
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("number",phone);
        query.addWhereEqualTo("password",password);
        query.setLimit(1);
        Log.i("tag",phone+password);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(list!=null && list.size()!=0){
                    listener.getSuccess(list.get(0));
                }else{
                    listener.getFailure();
                }
            }
        });
    }

    /**
     * 根据objectId获取User
     *
     * @param objectId
     * @param listener
     */
    public void getUser(String objectId, final makemodelimpl.BaseListener listener) {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("objectId", objectId);
        query.setLimit(1);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (list != null && list.size() != 0) {
                    listener.getSuccess(list.get(0));
                } else {
                    listener.getFailure();
                }
            }
        });
    }
    public void onRegister(User user, final makemodelimpl.BaseListener listener){
        user.save(new SaveListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    ToastUtil.showLong(BaseApplication.getmyContext(), "注册成功");
                    listener.getSuccess(null);
                } else {
                    ToastUtil.showLong(BaseApplication.getmyContext(), "注册失败");
                    listener.getFailure();
                }
            }
        });
    }

    /**
     * 获取当前登录的对象
     *
     * @return
     */
    public UserLocal getUserLocal() {
        return new Select().from(UserLocal.class).executeSingle();
    }

    /**
     * 将当前用户保存到本地数据库中
     * @param userLocal
     */
    public void putUserLocal(UserLocal userLocal) {
        new Delete().from(UserLocal.class).execute();
        userLocal.save();
    }
    public void updateUserName(String name){
        new Update(UserLocal.class).set("Name=?",name).execute();
    }
    public void updateUserLocalPhoto(String Photo){
        new Update(UserLocal.class).set("Photo=?",Photo).execute();
    }

    public void deleteLocal(){
        new Delete().from(UserLocal.class).execute();
    }

    public void  isPhoneRegister(String phone, final makemodelimpl.BaseListener listener){
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("number",phone);
        query.setLimit(1);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (list != null && list.size() != 0) {
                    listener.getSuccess(list.get(0));
                } else {
                    listener.getFailure();
                }
            }
        });
    }


    public boolean isLogin() {
        List<UserLocal> list = new Select().from(UserLocal.class).execute();
        if (list.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更换用户的头像
     *
     * @param path
     * @param listener
     */
    public void updateUserPhoto(String path, final String objectId, final makemodelimpl.BaseListener listener){
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    final User user = new User();
                    user.setPhoto(bmobFile);
                    user.update(objectId,new UpdateListener() {
                        @Override
                        public void done(BmobException e)
                        {
                            if(e == null){
                                listener.getSuccess(user);
                            }else{
                                listener.getFailure();
                            }
                        }
                    });
                }else{
                    listener.getFailure();
                }
            }
        });
    }

    public void updateUserName(String name,String objectId,final makemodelimpl.BaseListener listener){
        User user = new User();
        user.setName(name);
        user.update(objectId,new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    listener.getSuccess(e);
                }
            }
        });
    }
}
