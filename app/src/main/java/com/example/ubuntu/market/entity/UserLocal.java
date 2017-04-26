package com.example.ubuntu.market.entity;

import java.io.Serializable;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 作者： wangyue on 17-3-28 14:51
 * 邮箱：973459080@qq.com
 */
@Table(name = "UserLocal")
public class UserLocal extends Model implements Serializable{

    //对象Id
    @Column(name = "objectId")
    String objectId;
    //姓名
    @Column(name = "Name")
    String Name;
    //头像
    @Column(name = "Photo")
    String Photo;
    //密码
    @Column(name = "Password")
    String Password;
    //手机号码
    @Column(name = "Number")
    String Number;


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }
}
