package com.example.ubuntu.market.impl;

/**
 * Created by ubuntu on 17-3-28.
 */
public interface makemodelimpl {

    interface BaseListener<T>{
        void  getSuccess(T t);
        void  getFailure();
    }

}
