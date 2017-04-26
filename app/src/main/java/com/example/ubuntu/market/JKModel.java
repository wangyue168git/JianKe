package com.example.ubuntu.market;

import android.content.Context;
import android.content.Intent;

import com.example.ubuntu.market.entity.ArticleDetail;
import com.example.ubuntu.market.entity.JKArticle;
import com.example.ubuntu.market.https.HtmlParser;
import com.example.ubuntu.market.https.JKService;
import com.example.ubuntu.market.https.RetrofitWrapper;
import com.example.ubuntu.market.impl.JKModelImpl;
import com.example.ubuntu.market.impl.makemodelimpl;

import java.util.List;


import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 作者： wangyue on 17-3-30 13:53
 * 邮箱：973459080@qq.com
 */
public class JKModel implements JKModelImpl {

    public static String presentArticleId="123";
    HtmlParser htmlParser = new HtmlParser();
    JKService service = RetrofitWrapper.getInstance().create(JKService.class);
    Context context;

    @Override
    public void getGeneralJKsItem1(final BaseListener listener) {
        service.getArticleList1().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, List<JKArticle>>() { //string->List<JKArticle>
                    @Override
                    //Func1通过call(String s)方法将String->List<JKArticle>
                    public List<JKArticle> call(String s) {
                        return htmlParser.parserHtml(s);
                    }
                }).subscribe(new Subscriber<List<JKArticle>>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(List<JKArticle> jkArticles) {
                listener.getSuccess(jkArticles);
            }
        });
    }

    @Override
    public void getGeneralJKsItem2(final BaseListener listener) {
        service.getArticleList2(HtmlParser.cate,htmlParser.getMaxIds()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, List<JKArticle>>() { //string->List<JKArticle>
                    @Override
                    public List<JKArticle> call(String s) {
                        //Log.i("html",HtmlParser.parserHtml(s).get(0).getAuthorName());
                        return htmlParser.parserHtml(s);
                    }
                }).subscribe(new Subscriber<List<JKArticle>>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(List<JKArticle> jkArticles) {
                listener.getSuccess(jkArticles);
            }
        });
    }

    @Override
    public void getJKArticleDetailItem(Context context1,final String link, final BaseListener listener) {
        this.context = context1;
        service.getArticleDetail(link).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, ArticleDetail>() { //string->List<JKArticle>
                    @Override
                    public ArticleDetail call(String s) {
                        //Log.i("html",HtmlParser.parserHtml(s).get(0).getAuthorName());
                        presentArticleId = link;
                        return htmlParser.parserHtmlToDetail(s);
                    }
                }).subscribe(new Subscriber<ArticleDetail>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                Intent intent = new Intent();
                intent.setAction("MSG_NO");
                context.sendBroadcast(intent);
            }
            @Override
            public void onNext(ArticleDetail articleDetail) {
                listener.getSuccess(articleDetail);
            }
        });
    }
    public void getSliderShowJKArticle(final BaseListener listener){
        service.getSliderShowJKArticle().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, List<SlideShowView.SliderShowViewItem>>() {

                    @Override
                    public List<SlideShowView.SliderShowViewItem> call(String s) {
                        return htmlParser.parserHtmlToSliderShow(s);
                    }
                })
                .subscribe(new Subscriber<List<SlideShowView.SliderShowViewItem>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<SlideShowView.SliderShowViewItem> sliderShowViewItems) {
                        listener.getSuccess(sliderShowViewItems);
                    }
                });
    }
    public void onSearchJK(String jkname,int page,String type, final makemodelimpl.BaseListener listener){
        service.getSearchArticle(jkname,type,page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String,List<JKArticle>>() {

                    @Override
                    public List<JKArticle> call(String s) {
                        return htmlParser.parserJson_search(s);
                    }
                })
                .subscribe(new Subscriber<List<JKArticle>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(List<JKArticle> list) {
                        listener.getSuccess(list);
                    }
                });
    }


}
