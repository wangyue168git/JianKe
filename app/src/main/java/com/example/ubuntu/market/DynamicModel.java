package com.example.ubuntu.market;

import android.util.Log;
import android.widget.Toast;

import com.example.ubuntu.market.application.BaseApplication;
import com.example.ubuntu.market.entity.Comment;
import com.example.ubuntu.market.entity.DynamicItem;
import com.example.ubuntu.market.entity.FeedBack;
import com.example.ubuntu.market.entity.User;
import com.example.ubuntu.market.impl.JKModelImpl;
import com.example.ubuntu.market.impl.makemodelimpl;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * 作者： wangyue on 17-4-14 14:23
 * 邮箱：973459080@qq.com
 */
public class DynamicModel {


    public void sendDynamicItem(final DynamicItem dynamicItem, final JKModelImpl.BaseListener listener) {
        if(dynamicItem.getPhotoList().size()!=0){
            final String[] array = new String[dynamicItem.getPhotoList().size()];
            for (int i = 0; i < dynamicItem.getPhotoList().size(); i++) {
                array[i] = dynamicItem.getPhotoList().get(i).getLocalFile().getAbsolutePath();
                //Log.i("path", "sendDynamicItem: " + array[i] + " " + dynamicItem.getPhotoList().size());
            }
            //Bmob批量上传图片
            BmobFile.uploadBatch(array,new UploadBatchListener(){

                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    if(list1.size() == array.length){
                        dynamicItem.setPhotoList(list);
                        dynamicItem.save(new SaveListener() {
                            @Override
                            public void done(Object o, Object o2) {
                                listener.getSuccess(null);
                                Toast.makeText(BaseApplication.getmyContext(), "上传成功", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void done(Object o, BmobException e) {
                                Toast.makeText(BaseApplication.getmyContext(), "上传失败", Toast.LENGTH_LONG).show();
                                listener.getFailure();
                            }

                        });
                    }
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {

                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }else {
            dynamicItem.save(new SaveListener() {
                @Override
                public void done(Object o, Object o2) {
                    listener.getSuccess(null);
                    Toast.makeText(BaseApplication.getmyContext(), "上传成功", Toast.LENGTH_LONG).show();
                }

                @Override
                public void done(Object o, BmobException e) {
                    Toast.makeText(BaseApplication.getmyContext(), "上传失败", Toast.LENGTH_LONG).show();
                    listener.getFailure();
                }
            });
        }
    }

    /**
     * 获取所有的朋友圈消息
     *
     * @param listener
     */
    public void getDynamicItem(int pageNumber,final makemodelimpl.BaseListener listener) {

        BmobQuery<DynamicItem> query = new BmobQuery<DynamicItem>();
        query.order("-createdAt");
        query.setLimit(10);
        query.setSkip(10*pageNumber);
        query.findObjects(new FindListener<DynamicItem>() {
            @Override
            public void done(List<DynamicItem> object, BmobException e) {
                if (object != null && object.size() != 0) {
                    listener.getSuccess(object);
                }else {
                    listener.getFailure();
                }
            }
        });
    }

    /**
     * 获取当前用户的所有动态
     *
     * @param user
     * @param listener
     */

    public void getDynamicItemByUser(User user, final makemodelimpl.BaseListener listener) {
        BmobQuery<DynamicItem> query = new BmobQuery<DynamicItem>();
        query.addWhereEqualTo("writer", user);
        query.order("-createdAt");
        query.findObjects(new FindListener<DynamicItem>() {
            @Override
            public void done(List<DynamicItem> object, BmobException e) {
                if (object != null && object.size() != 0) {
                    listener.getSuccess(object);
                }
            }

        });
    }

    /**
     * 获得动态的评论
     * @param dynamicItem
     * @param listener
     */
    public void getComment(DynamicItem dynamicItem, final makemodelimpl.BaseListener listener){
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("dynamicItem",dynamicItem);
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> object, BmobException e) {
                if (object != null && object.size() != 0) {
                    listener.getSuccess(object);
                }
            }
        });
    }

    public void sendComment(Comment comment, final makemodelimpl.BaseListener listener){
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    listener.getSuccess(s);
                }
            }
        });
    }
    public void deleteComment(Comment comment, final makemodelimpl.BaseListener listener){
        comment.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    listener.getSuccess(e);
                }
            }
        });
    }
    public void deleteComments(DynamicItem dynamicItem){
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("dynamicItem",dynamicItem);
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if(list.size()!=0) {
                    List<BmobObject> commentList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        Comment comment = new Comment();
                        comment.setObjectId(list.get(i).getObjectId());
                        commentList.add(comment);
                    }
                    new BmobBatch().deleteBatch(commentList).doBatch(new QueryListListener<BatchResult>() {
                        @Override
                        public void done(List<BatchResult> o, BmobException e) {
                            if (e == null) {
                               Log.i("","删除成功");
                            }
                        }
                    });
                }
            }
        });

    }
    /**
     * 删除个人动态
     * @param dynamicItem
     * @param listener
     */
    public void deleteDynamic(final DynamicItem dynamicItem, final makemodelimpl.BaseListener listener){
          dynamicItem.delete(new UpdateListener() {
              @Override
              public void done(BmobException e) {
                  if(e==null){
                      deleteComments(dynamicItem);
                      listener.getSuccess(e);
                  }
              }
          });

    }

    public void saveFeedBack(FeedBack feedBack, final makemodelimpl.BaseListener listener){
        feedBack.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null) {
                    listener.getSuccess(s);
                }
            }
        });
    }

}
