package com.example.aravindharaj.sociobot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aravindharaj on 11/22/2015.
 */
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.CustomViewHolder>{

    public static class CustomViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView;

        public CustomViewHolder(View view)
        {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.imageViewIcon);
            this.textView = (TextView) view.findViewById(R.id.textViewLabel);
        }
    }

    private ArrayList<Value> value;
    private Context mContext;

    public AppAdapter(Context context, ArrayList<Value> value)
    {
        this.mContext = context;
        this.value = value;
    }

    @Override
    public int getItemCount()
    {
        return value.size();
    }

    @Override
    public void onBindViewHolder (CustomViewHolder holder, int position)
    {
        holder.imageView.setImageResource(value.get(position).getImage());
        holder.textView.setText(value.get(position).getValue());
    }

    @Override
    public CustomViewHolder onCreateViewHolder (ViewGroup viewGroup, int viewType)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_my_profile, viewGroup, false);
        CustomViewHolder cvh = new CustomViewHolder(v);
        return cvh;
    }
}
