package com.example.ubuntu.market.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * 作者： wangyue on 17-3-30 11:27
 * 邮箱：973459080@qq.com
 */
@Table(name = "AticleDetail")
public class ArticleDetail extends Model implements Serializable{

    @Column(name = "articleId")
    String articleId;

    @Column(name = "title")
    String title;

    @Column(name = "authorImage")
    String authorImage;

    @Column(name = "authorName")
    String authorName;

    @Column(name = "date")
    String date;

    @Column(name = "content")
    String content;

    @Column(name ="wordAge")
    String wordAge;

    public String getWordAge() {
        return wordAge;
    }

    public void setWordAge(String wordAge) {
        this.wordAge = wordAge;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String id) {
        this.articleId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
