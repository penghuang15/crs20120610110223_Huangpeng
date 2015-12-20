package com.iot.ecjtu.project7.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.iot.ecjtu.project7.R;

import java.util.List;

/**
 * Created by 497908738@qq.com on 2015/12/18.
 */
public class Adapter extends BaseAdapter {
    private List<Friend> mList;
    private Context mContext;

    public Adapter(Context context,List<Friend> list){
        mList=list;
        mContext=context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Friend getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.headImageView=(ImageView) convertView.findViewById(R.id.headImageView);
            holder.nameLabel= (TextView)convertView.findViewById(R.id.nameLabel);
            holder.ratingBar = (RatingBar)convertView.findViewById(R.id.ratingBar);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Friend friend = getItem(position);
        holder.headImageView.setImageResource(friend.getHeadImageId());
        holder.nameLabel.setText((friend.getName()));
        holder.ratingBar.setProgress(friend.getRate());

        return convertView;
    }

    private static class ViewHolder {
        ImageView headImageView;
        TextView nameLabel;
        RatingBar ratingBar;
    }
}

