package com.androidcoursework.niobec1508775;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidcoursework.niobec1508775.rssreader.RssNews;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cyril on 27/04/16.
 */
public class MyRssNewsAdapter extends ArrayAdapter<RssNews> {
    private Context context;
    private List<RssNews> mNews;


    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDesc;
        public TextView tvUrl;
        public TextView tvDate;
    }

    public MyRssNewsAdapter(Context context, int list_item_resource,List<RssNews> news) {
        super(context,list_item_resource, news);
        this.context = context;
        this.mNews = new ArrayList<>(news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView;
        ViewHolder viewHolder;

        if (convertView == null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listItemView = inflater.inflate(R.layout.list_news, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) listItemView.findViewById(R.id.tvTitle);
            viewHolder.tvDesc = (TextView) listItemView.findViewById(R.id.tvDescription);
            viewHolder.tvDate = (TextView) listItemView.findViewById(R.id.tvDate);
            viewHolder.tvUrl = (TextView) listItemView.findViewById(R.id.tvUrlOriginalSite);

            listItemView.setTag(viewHolder);

        } else {
            listItemView = convertView;
            viewHolder = (ViewHolder) listItemView.getTag();
        }

        viewHolder.tvTitle.setText(mNews.get(position).getTitle());
        viewHolder.tvDesc.setText(mNews.get(position).getDescription());
        //viewHolder.tvDate.setText(mNews.get(position).getDatePublication().toString());
        viewHolder.tvUrl.setText(mNews.get(position).getOriginalSite());


        return listItemView;
    }
}

