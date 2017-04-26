package com.example.ubuntu.market.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
 * 作者： wangyue on 17-3-30 15:34
 * 邮箱：973459080@qq.com
 */
public class JKAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<JKArticle> mDatas;
    private int mLayoutRes;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            if (msg.what == 0x1234){
                Log.i("po",msg.arg1+"");
            }
        }
    };

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    Context context;

    public JKAdapter(Context context, int layoutRes, List<JKArticle> datas){
        this.mDatas = datas;
        this.context = context;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            holder = new ViewHolder();
            holder.authorName = (TextView) convertView
                    .findViewById(R.id.authorName);
            holder.title = (TextView) convertView
                    .findViewById(R.id.list_title);
            holder.image = (ImageView) convertView
                    .findViewById(R.id.list_img);
            holder.authorImg= (RoundImageView) convertView
                    .findViewById(R.id.authorImg);
            holder.date = (TextView) convertView.findViewById(R.id.list_date);
            holder.articleAbstract = (TextView)convertView.findViewById(R.id.list_abstract);
            holder.kind = (TextView) convertView.findViewById(R.id.kind);
            holder.delete = (TextView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("MSG");
                intent.putExtra("msg",position);
                context.sendBroadcast(intent);
            }
        });

        JKArticle jkArticle = mDatas.get(position);
        if(holder!=null) {
            //Log.i("date",jkArticle.getAuthorName()+jkArticle.getDate());
            holder.kind.setText(" "+jkArticle.getKind()+" ");
            holder.date.setText(jkArticle.getDate());
            holder.title.setText(jkArticle.getTitle());
            holder.authorName.setText(jkArticle.getAuthorName());
            holder.articleAbstract.setText(jkArticle.getArticleAbstract());
            //imagekloader子线程中下载图片
            imageLoader.displayImage("http:"+jkArticle.getAuthorImg(), holder.authorImg, options);
            if(jkArticle.getImgLink()!=null) {
                imageLoader.displayImage("http:" + jkArticle.getImgLink(), holder.image, options);
            }
        }
        return convertView;
    }


    private final class ViewHolder {
        TextView title;
        TextView authorName;
        RoundImageView authorImg;
        ImageView image;
        TextView date;
        TextView articleAbstract;
        TextView kind;
        TextView delete;
    }
}
