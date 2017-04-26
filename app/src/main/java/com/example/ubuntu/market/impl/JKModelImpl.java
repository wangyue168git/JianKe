package com.example.ubuntu.market.impl;

import android.content.Context;

/**
 * 作者： wangyue on 17-3-30 13:55
 * 邮箱：973459080@qq.com
 */
public interface JKModelImpl {

    //void getGeneralJKsItem(BaseListener listener);

    void getGeneralJKsItem1(BaseListener listener);

    void getGeneralJKsItem2( BaseListener listener);

    void getJKArticleDetailItem(Context context,String link, BaseListener listener);

    void  getSliderShowJKArticle(BaseListener listener);


    interface BaseListener<T>{
        void getSuccess(T t);
        void getFailure();
    }

}
