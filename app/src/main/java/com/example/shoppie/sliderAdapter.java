package com.example.shoppie;

import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

public class sliderAdapter extends PagerAdapter {
    private List<slider_model> sliderModellist;
    public sliderAdapter(List<slider_model> sliderModellist) {
        this.sliderModellist = sliderModellist;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(container.getContext()).inflate(R.layout.slider_layout,container,false);
        ImageView banner=view.findViewById(R.id.img_banner);
        RelativeLayout bannerContainer=view.findViewById(R.id.banner_container);
       // bannerContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sliderModellist.get(position).getBgcolor())));
        bannerContainer.setBackgroundTintList(ColorStateList.valueOf(container.getContext().getResources().getColor(R.color.white)));

        Glide.with(container.getContext()).load(sliderModellist.get(position).getBanner()).apply(new RequestOptions().placeholder(R.drawable.placeholderbanner)).into(banner);
       // container.addView(view,0);
        container.addView(view);
        return view ;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public int getCount() {
        return sliderModellist.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }


}
