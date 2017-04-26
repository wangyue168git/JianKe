package com.example.ubuntu.market.fragment;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ubuntu.market.FixedGridView;

import com.example.ubuntu.market.R;
import com.example.ubuntu.market.RoundImageView;
import com.example.ubuntu.market.UserModel;
import com.example.ubuntu.market.entity.DynamicItem;
import com.example.ubuntu.market.entity.User;
import com.example.ubuntu.market.impl.makemodelimpl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * 作者： wangyue on 17-4-11 10:14
 * 邮箱：973459080@qq.com
 */
public class DynamicAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<DynamicItem> dynamicItemList;
    private int mLayoutRes;
    private Context context;
    ViewHolder holder = null;
    ArticleViewHolder articleViewHolder = null;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private UserModel userModel = new UserModel();

    public DynamicAdapter(Context context,int layoutRes,List<DynamicItem> dynamicItemListd) {
        this.context = context;
        this.mLayoutRes = layoutRes;
        this.dynamicItemList = dynamicItemListd;
        inflater = LayoutInflater.from(context);
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.xiaolian)
                .showImageForEmptyUri(R.drawable.xiaolian)
                .showImageOnFail(R.drawable.xiaolian).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();

    }

    public List<DynamicItem> returnmDatas() {
        return this.dynamicItemList;
    }

    public void addAll(List<DynamicItem> mDatas) {
        this.dynamicItemList.addAll(mDatas);
    }

    public void setDatas(List<DynamicItem> mDatas) {
        this.dynamicItemList.clear();
        this.dynamicItemList.addAll(mDatas);
    }

    /**
     * getCount()返回的值即为getview()执行的次数，返回0，则不执行getview()函数
     * @return
     */
    @Override
    public int getCount() {
        return dynamicItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {

        String type = dynamicItemList.get(position).getType();
        if(type.equals("动态")){
            return 0;
        }else{
            return 1;
        }
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        int type = getItemViewType(i);
        if(type == 0) {
            if (view == null) {
                view = inflater.inflate(mLayoutRes, null);
                holder = new ViewHolder();
                holder.write_photo = (RoundImageView) view.findViewById(R.id.dyna_photo);
                holder.write_name = (TextView) view.findViewById(R.id.dyna_name);
                holder.write_date = (TextView) view.findViewById(R.id.dyna_date);
                holder.dynamic_text = (TextView) view.findViewById(R.id.dyna_text);
                holder.dynamic_photo = (FixedGridView) view.findViewById(R.id.dynamics_photo);
                holder.comment = (ListView) view.findViewById(R.id.comment);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();//TAG是一个object对象,holder始终只有一个实例，多引用，减少内存占有
            }
            DynamicItem dynamicItem = dynamicItemList.get(i);
            final ViewHolder viewHolder = holder;
            userModel.getUser(dynamicItem.getWriter().getObjectId(), new makemodelimpl.BaseListener() {
                @Override
                public void getSuccess(Object o) {
                    User user = (User) o;
                    imageLoader.displayImage(user.getPhoto().getUrl(), viewHolder.write_photo, options);
                    viewHolder.write_name.setText(user.getName());
                }

                @Override
                public void getFailure() {

                }
            });
            viewHolder.write_date.setText(dynamicItem.getCreatedAt());
            if (dynamicItem.getDynamicContent().length() > 220) {
                holder.dynamic_text.setText(dynamicItem.getDynamicContent().substring(0, 220) + "...");
            } else {
                holder.dynamic_text.setText(dynamicItem.getDynamicContent());
            }
            holder.dynamic_photo.setAdapter(new DynamicPhotoAdapter(context, R.layout.dynamic_gridview_item, dynamicItem.getPhotoList()));
        }else{
            if(view==null){
                view = inflater.inflate(mLayoutRes,null);
                articleViewHolder = new ArticleViewHolder();
                articleViewHolder.write_photo = (RoundImageView) view.findViewById(R.id.dyna_photo);
                articleViewHolder.write_name = (TextView) view.findViewById(R.id.dyna_name);
                articleViewHolder.write_date = (TextView) view.findViewById(R.id.dyna_date);
                articleViewHolder.article_text = (TextView)view.findViewById(R.id.dyna_text);
                articleViewHolder.type = (TextView) view.findViewById(R.id.type);
                view.setTag(articleViewHolder);
            }else{
                articleViewHolder = (ArticleViewHolder) view.getTag();
            }
            DynamicItem dynamicItem = dynamicItemList.get(i);
            final ArticleViewHolder articleViewHolder1 = articleViewHolder ;
            articleViewHolder.type.setText(" 文章 ");
            userModel.getUser(dynamicItem.getWriter().getObjectId(), new makemodelimpl.BaseListener() {
                @Override
                public void getSuccess(Object o) {
                    User user = (User) o;
                    imageLoader.displayImage(user.getPhoto().getUrl(), articleViewHolder1.write_photo, options);
                    articleViewHolder1.write_name.setText(user.getName());
                }

                @Override
                public void getFailure() {
                }
            });
            articleViewHolder.write_date.setText(dynamicItem.getCreatedAt());
            String article = dynamicItem.getDynamicContent()
                    .replaceAll("<style>[\\s\\S]*?</style>","")
                    .replaceAll("<[^>]+>","");
            if(article.length()>250) {
                articleViewHolder.article_text.setText(article.substring(0,250));
            }else {
                articleViewHolder.article_text.setText(article);
            }
        }
        return view;
    }



    private final class ViewHolder {
        RoundImageView write_photo;
        TextView write_name;
        TextView write_date;
        TextView dynamic_text;
        FixedGridView dynamic_photo;
        ListView comment;
    }
    private class ArticleViewHolder {
        RoundImageView write_photo;
        TextView write_name;
        TextView write_date;
        TextView article_text;
        TextView type;
    }
}
