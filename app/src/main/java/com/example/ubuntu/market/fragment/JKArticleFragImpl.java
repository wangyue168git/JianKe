package com.example.ubuntu.market.fragment;

import com.example.ubuntu.market.entity.JKArticle;

import java.util.List;

/**
 * 作者： wangyue on 17-3-30 14:12
 * 邮箱：973459080@qq.com
 */
public interface JKArticleFragImpl {


    //加载更多
    void onLoadMore(List<JKArticle> list);
    //下拉刷新
    void onRefresh(List<JKArticle> list);
    //初始化sliderView
   // void onInitSliderShow(List<SlideShowView.SliderShowViewItem> list);
    //初始化最热动态
    //void onInitDynamic(DynamicItem item);
}
