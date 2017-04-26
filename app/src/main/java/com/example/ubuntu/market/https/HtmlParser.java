package com.example.ubuntu.market.https;

import android.util.Log;

import com.example.ubuntu.market.JKModel;
import com.example.ubuntu.market.SlideShowView;
import com.example.ubuntu.market.entity.ArticleDetail;
import com.example.ubuntu.market.entity.JKArticle;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wangyue on 17-3-30 10:18
 * 邮箱：973459080@qq.com
 */
public class HtmlParser {

    public int getMaxIds() {
        return maxIds;
    }

    public void setMaxIds(int maxIds) {
        this.maxIds = maxIds;
    }

    private int maxIds;

    public static int cate = 56;
    public static int total_page;

    public  List<JKArticle> parserHtml(String msg){
        List<JKArticle> JKArticleList = new ArrayList<>();
        Document doc = Jsoup.parse(msg);
        Elements unit = doc.getElementsByClass("note-list");
        Elements units = unit.get(0).getElementsByTag("li");
        Elements authors = doc.getElementsByClass("author");
        Elements authornames = doc.getElementsByClass("blue-link");
        Elements titles = doc.getElementsByClass("title");
        Elements metas = doc.getElementsByClass("meta");
        Elements times = doc.getElementsByClass("time");
       // Elements kinds = doc.getElementsByClass("collection-tag");
        Element max_id = units.last();
        String maxId = max_id.attr("data-recommended-at");
        maxIds = Integer.parseInt(maxId)-1;
        //Log.i("units",String.valueOf(units.size()));
        for(int i = 0; i< units.size();i++) {
            JKArticle jkArticle = new JKArticle();
            Element detail_article = units.get(i);
            Element author = authors.get(i);
            Element title = titles.get(i);
            Element meta = metas.get(i);
            Element authorname = authornames.get(i);
            String authorName = authorname.text();
            String titlename = title.text();
            Elements links = detail_article.getElementsByClass("title");
            String link1[] =links.get(0).attr("href").split("/");
            String link = link1[2];
            Element imagelink_ele = detail_article.getElementsByTag("img").get(0);
            String imageLink = imagelink_ele.attr("src");
            Element authorImg_ele = author.getElementsByTag("img").get(0);
            String authorImg = authorImg_ele.attr("src");
            Element as = detail_article.getElementsByTag("p").get(0);
            String articleAbstract = as.text().replaceAll("图片发自简书App","");
            Element kind_k = meta.getElementsByTag("a").get(0);
            String kind = kind_k.text();
            if(kind.matches("[0-9]+")){
                kind = "文章";
            }
            Element time_ele = times.get(i);
            Log.i("asd",time_ele.text());
            String date = time_ele.text();


            jkArticle.setKind(kind);
            jkArticle.setAuthorImg(authorImg);
            jkArticle.setAuthorName(authorName);
            jkArticle.setDate(date);
            jkArticle.setImgLink(imageLink);
            jkArticle.setTitle(titlename);
            jkArticle.setLink(link);
            jkArticle.setArticleAbstract(articleAbstract);
            JKArticleList.add(jkArticle);

        }
        return JKArticleList;
    }
    public  ArticleDetail parserHtmlToDetail(String jk) {
        String content;
        Document doc = Jsoup.parse(jk);
        Element title_ele = doc.getElementsByClass("title").get(0);
        String title = title_ele.html();
        String authorImage = doc.getElementsByClass("avatar").get(0).getElementsByTag("img").get(0).attr("src");
        String authorName = doc.getElementsByClass("name").get(0).getElementsByTag("a").text();
        Elements mata = doc.getElementsByClass("meta");
        String date1[] = doc.getElementsByClass("publish-time").get(0).text().split("\\*");
        String date = date1[0];
        String wordage = doc.getElementsByClass("wordage").get(0).text();
        String articleId = JKModel.presentArticleId;
       // Log.i("asdh",articleId+authorName);

        Element article = doc.getElementsByClass("show-content").get(0);
        content = article.html().replaceAll("图片发自简书App","");
        ArticleDetail articleDetail= new ArticleDetail();
        articleDetail.setTitle(title);
        articleDetail.setWordAge(wordage);
        articleDetail.setContent(content);
        articleDetail.setDate(date);
        articleDetail.setAuthorImage(authorImage);
        articleDetail.setAuthorName(authorName);
        articleDetail.setArticleId(articleId);
        return articleDetail;
    }
    public static List<SlideShowView.SliderShowViewItem> parserHtmlToSliderShow(String msg) {
        ArrayList<SlideShowView.SliderShowViewItem> list=new ArrayList<>();
        SlideShowView.SliderShowViewItem viewItem = null;
        Document doc = Jsoup.parse(msg);
        Elements content = doc.getElementsByClass("carousel-inner");
        Elements links = content.get(0).getElementsByTag("a");
        for (int i = 0; i < 3; i++) {
            viewItem = new SlideShowView.SliderShowViewItem();
            String link = links.get(i).attr("href");
            String imagelink = "http:"+links.get(i).getElementsByTag("img").get(0).attr("src");
            viewItem.setImgLink(imagelink);
            viewItem.setLink(link);
            list.add(viewItem);
        }
        return list;
    }
    public  List<JKArticle> parserJson_search(String msg){
        List<JKArticle> JKArticleList = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(msg);
        JsonArray jsonArray = jsonObject.get("entries").getAsJsonArray();
        total_page = jsonObject.get("total_pages").getAsInt();
        for(int i=0;i<jsonArray.size();i++){
            JKArticle jkArticle = new JKArticle();
            JsonObject subObject=jsonArray.get(i).getAsJsonObject();
            JsonObject user = subObject.get("user").getAsJsonObject();
            JsonObject meta = subObject.get("notebook").getAsJsonObject();
            jkArticle.setAuthorImg(user.get("avatar_url").getAsString());
            jkArticle.setLink(subObject.get("slug").getAsString());
            String content = subObject.get("content").getAsString().replaceAll("<[^>]+>","");
            String name = user.get("nickname").getAsString().replaceAll("<[^>]+>","");
            String title =  subObject.get("title").getAsString().replaceAll("<[^>]+>","");
            jkArticle.setAuthorName(name);
            jkArticle.setArticleAbstract(content);
            jkArticle.setTitle(title);
            JKArticleList.add(jkArticle);
        }
        return JKArticleList;
    }

}
