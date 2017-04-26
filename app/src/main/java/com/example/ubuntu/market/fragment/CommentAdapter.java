package com.example.ubuntu.market.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ubuntu.market.R;

import java.util.List;

/**
 * 作者： wangyue on 17-4-17 11:49
 * 邮箱：973459080@qq.com
 */
public class CommentAdapter extends ArrayAdapter<String> {

    Context context;
    int textViewResourceId;
    List<String> objects;

    public CommentAdapter(Context context, int resource, List<String> objects) {
        super(context, resource,objects);
        this.context = context;
        this.textViewResourceId  = resource;
        this.objects = objects;
    }
    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = null;
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(textViewResourceId,null);
            viewHolder.comment_item = (TextView) view.findViewById(R.id.comment_item);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.comment_item.setText(objects.get(i));
        return view;
    }

    class ViewHolder
    {
        TextView comment_item;
    }
}
