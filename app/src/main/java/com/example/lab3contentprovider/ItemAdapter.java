package com.example.lab3contentprovider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends BaseAdapter {
    private List<Item> list;

    public ItemAdapter(List<Item> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            holder = new ViewHolder();

            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvDescription = convertView.findViewById(R.id.tvDescription);
            holder.imgIcon = convertView.findViewById(R.id.imgIcon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Item item = (Item) getItem(position);

//        holder.imgIcon.setImageResource(item.getIcon());
        holder.imgIcon.setImageBitmap(item.getIcon2());
        holder.tvName.setText(item.getName());
        holder.tvDescription.setText(item.getDescription());

        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvDescription;
        ImageView imgIcon;
    }


}
