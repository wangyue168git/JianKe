package com.example.ubuntu.market;

import com.activeandroid.query.Select;
import com.example.ubuntu.market.entity.ArticleDetail;

import java.util.List;

/**
 * 作者： wangyue on 17-4-1 17:15
 * 邮箱：973459080@qq.com
 */
public class MyLoveArticleModel {

    public static List<ArticleDetail> onQuerylist(){
        return new Select().from(ArticleDetail.class).execute();
    }
    public static boolean onQuery(ArticleDetail articleDetail) {
        ArticleDetail item = new Select().from(ArticleDetail.class).where("articleId=?",articleDetail.getArticleId()).executeSingle();
        if(item!=null){
            return true;
        }
        return false;
    }

    public static void onDelete(ArticleDetail articleDetail) {
        ArticleDetail item = new Select().from(ArticleDetail.class).where("articleId=?",articleDetail.getArticleId()).executeSingle();
        item.delete();//item引用指向要被删除的对象
    }

    public static void onInsert(ArticleDetail articleDetail) {
        articleDetail.save();
    }
}
