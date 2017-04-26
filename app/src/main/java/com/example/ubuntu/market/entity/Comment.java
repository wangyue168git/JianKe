package com.example.ubuntu.market.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 作者： wangyue on 17-4-17 13:20
 * 邮箱：973459080@qq.com
 */
public class Comment extends BmobObject implements Serializable {

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String commentUserName;

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String commentContent;

    public DynamicItem getDynamicItem() {
        return dynamicItem;
    }

    public void setDynamicItem(DynamicItem dynamicItem) {
        this.dynamicItem = dynamicItem;
    }

    public DynamicItem dynamicItem;

}
