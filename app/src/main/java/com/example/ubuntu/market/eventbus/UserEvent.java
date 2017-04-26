package com.example.ubuntu.market.eventbus;

import com.example.ubuntu.market.entity.UserLocal;

/**
 * 作者： wangyue on 17-3-29 17:26
 * 邮箱：973459080@qq.com
 * 利用eventbus传递用户对象信息
 */
public class UserEvent {

    private UserLocal userLocal;
    public UserEvent(UserLocal userLocal){
        this.userLocal = userLocal;
    }

    public UserLocal getUserLocal() {
        return userLocal;
    }

    public void setUserLocal(UserLocal userLocal) {
        this.userLocal = userLocal;
    }

}
