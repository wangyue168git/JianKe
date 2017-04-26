package com.example.ubuntu.market.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 作者： wangyue on 17-4-18 16:14
 * 邮箱：973459080@qq.com
 */
public class FeedBack extends BmobObject implements Serializable {

    private User user;

    public String getFeedBackText() {
        return feedBackText;
    }

    public void setFeedBackText(String feedBackText) {
        this.feedBackText = feedBackText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private String feedBackText;
}
