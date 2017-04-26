package com.example.ubuntu.market.https;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 作者： wangyue on 17-3-30 09:24
 * 邮箱：973459080@qq.com
 */
public interface JKService {

    @GET(" ")
    rx.Observable<String> getSliderShowJKArticle();

    @GET("recommendations/notes")
    rx.Observable<String> getArticleList1();

    @GET("recommendations/notes")
    rx.Observable<String> getArticleList2(@Query("category_id") int cate,@Query("max_id") int id);

    @Headers("Accept: application/json")
    @GET("search/do")
    rx.Observable<String> getSearchArticle(@Query("q") String q,@Query("type") String type,@Query("page") int page);

    @GET("p/{jklink}")
    rx.Observable<String> getArticleDetail(@Path("jklink") String jklink);




}
