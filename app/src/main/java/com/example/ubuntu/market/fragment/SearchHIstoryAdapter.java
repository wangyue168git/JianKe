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
 * 作者： wangyue on 17-4-5 15:01
 * 邮箱：973459080@qq.com
 */
public class SearchHIstoryAdapter extends ArrayAdapter<String> {

    Context context;
    int textViewResourceId;
    List<String> objects;

    public SearchHIstoryAdapter(Context context, int resource,List<String> objects) {
        super(context, resource,objects);
        this.context = context;
        this.textViewResourceId  = resource;
        this.objects = objects;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View view = null;
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            //layoutInflater.inflate(resourceId, root);inflate()方法一般接收两个参数，
            // 第一个参数就是要加载的布局id，第二个参数是指给该布局的外部再嵌套一层父布局，如果不需要就直接传null。
            view = LayoutInflater.from(context).inflate(textViewResourceId,null);
            viewHolder.searchhistory_item = (TextView) view.findViewById(R.id.search_history_item);
            //view.setTag(object),设置的是一个object对象，给view设置唯一的标记，通过viwe.getTag()可获得这个标记。
            //通过setTag()可以将viewHolder暂时缓存起来。
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.searchhistory_item.setText(objects.get(position));
        return view;


    }
    //ViewHolder的出现，为的是listview滚动的时候快速设置值，而不必每次都重新创建很多对象，从而提升性能。
    class ViewHolder
    {
        TextView searchhistory_item;
    }
}
