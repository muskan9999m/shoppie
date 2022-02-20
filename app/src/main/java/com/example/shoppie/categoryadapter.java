package com.example.shoppie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class categoryadapter extends RecyclerView.Adapter<categoryadapter.ViewHolder> {
    private List<categorymodel> CategoryModelList;
private int lastpos=-1;
    public categoryadapter(List<categorymodel> categoryModelList) {
        CategoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public categoryadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.categorylist,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull categoryadapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String icon=CategoryModelList.get(position).getCategoryLink();
        String name= CategoryModelList.get(position).getCategoryName();

        holder.setCategoryicon(icon);
        holder.setCategory(name,position);
        ///fading effect
        if(lastpos<position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastpos=position;
        }
    }

    @Override
    public int getItemCount() {
        return CategoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryicon;
        private TextView categoryname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryicon=itemView.findViewById(R.id.homee);
            categoryname=itemView.findViewById(R.id.textView);

        }
        private void setCategoryicon(String iconurl)
        {
            if(!iconurl.equals("null")) {
                Glide.with(itemView.getContext()).load(iconurl).apply(new RequestOptions().placeholder(R.drawable.place)).into(categoryicon);
            }
            else
                categoryicon.setImageResource(R.drawable.homeee);
        }
        private void setCategory(final String cname,final int position)
        {
            categoryname.setText(cname);
            if(!cname.equals(""))
            {   itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position!=0)
                    {
                    Intent categoryIntent=new Intent(itemView.getContext(),categoryActivity.class);
                    categoryIntent.putExtra("categoryName",cname);
                    itemView.getContext().startActivity(categoryIntent);
                }}
            });}

        }
    }
}
