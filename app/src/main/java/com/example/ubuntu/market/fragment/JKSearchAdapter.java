package com.example.ubuntu.market.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ubuntu.market.R;
import com.example.ubuntu.market.RoundImageView;
import com.example.ubuntu.market.entity.JKArticle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * 作者： wangyue on 17-4-6 10:32
 * 邮箱：973459080@qq.com
 */
public class JKSearchAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private List<JKArticle> mDatas;
    private int mLayoutRes;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public JKSearchAdapter(Context context, int layoutRes, List<JKArticle> datas){
        this.mDatas = datas;
        this.mLayoutRes = layoutRes;
        mInflater = LayoutInflater.from(context);
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.xiaolian)
                .showImageForEmptyUri(R.drawable.xiaolian)
                .showImageOnFail(R.drawable.xiaolian).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();
    }
    public List<JKArticle> getmDatas() {
        return this.mDatas;
    }

    public void addAll(List<JKArticle> mDatas) {
        this.mDatas.addAll(mDatas);
    }

    public void setDatas(List<JKArticle> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            holder = new ViewHolder();
            holder.authorName = (TextView) convertView
                    .findViewById(R.id.search_authorName);
            holder.title = (TextView) convertView
                    .findViewById(R.id.search_list_title);
            holder.authorImg= (RoundImageView) convertView
                    .findViewById(R.id.search_authorImg);
            holder.articleAbstract = (TextView)convertView.findViewById(R.id.search_list_abstract);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        JKArticle jkArticle = mDatas.get(position);
        if(holder!=null) {
            holder.title.setText(jkArticle.getTitle());
            holder.authorName.setText(jkArticle.getAuthorName());
            holder.articleAbstract.setText(jkArticle.getArticleAbstract());
            //imagekloader子线程中下载图片
            imageLoader.displayImage(jkArticle.getAuthorImg(), holder.authorImg, options);
        }
        return convertView;
    }

    private final class ViewHolder {
        TextView title;
        TextView authorName;
        RoundImageView authorImg;
        TextView articleAbstract;
    }

}
