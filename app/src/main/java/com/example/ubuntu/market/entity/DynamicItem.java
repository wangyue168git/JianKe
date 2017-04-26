package com.example.ubuntu.market.entity;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 作者： wangyue on 17-4-10 15:37
 * 邮箱：973459080@qq.com
 */
public class DynamicItem extends BmobObject implements Serializable {



    public User writer;

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public String getDynamicContent() {
        return dynamicContent;
    }

    public void setDynamicContent(String dynamicContent) {
        this.dynamicContent = dynamicContent;
    }

    public List<BmobFile> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<BmobFile> photoList) {
        this.photoList = photoList;
    }

    public String dynamicContent;
    public List<BmobFile> photoList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String type;

}
