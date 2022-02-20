package com.example.shoppie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class home_page_adapter extends RecyclerView.Adapter {
    List<home_page_model> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private int lastpos=-1;

    public home_page_adapter(List<home_page_model> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool=new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
          /*  case 0:
                return home_page_model.BANNER_SLIDER;*/
            case 1:
                return home_page_model.STRIP_AD_BANNER;
            case 2:
                return home_page_model.HORIZONTAL_PRODUCT_VIEW;
            case 3:
                return home_page_model.GRID_PRODUCT_VIEW;

            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
           /* case home_page_model.BANNER_SLIDER:
                View bannerSliderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_add_layout, parent, false);
                return new bannerSliderViewHolder(bannerSliderView);*/
            case home_page_model.STRIP_AD_BANNER:
                View stripAdView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout, parent, false);
                return new stripAdBannerViewHolder(stripAdView);
            case home_page_model.HORIZONTAL_PRODUCT_VIEW:
                View horizontalView=LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout,parent,false);
                return new horizontalProductViewHolder(horizontalView);
            case home_page_model.GRID_PRODUCT_VIEW:
                View gridView=LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout,parent,false);
                return new gridProductViewHolder(gridView);
            default:

                return null;
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        switch (homePageModelList.get(position).getType()) {
           /* case home_page_model.BANNER_SLIDER:
                List<slider_model> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((bannerSliderViewHolder) holder).setBannerSlider_viewPager(sliderModelList);
                break;*/
            case home_page_model.STRIP_AD_BANNER:
                String resource=homePageModelList.get(position).getResource();
                String color=homePageModelList.get(position).getBgcolor();
                ((stripAdBannerViewHolder)holder).setStripAd(resource,color);
                break;
            case home_page_model.HORIZONTAL_PRODUCT_VIEW:
               List<wishList_model> wishListModelList=homePageModelList.get(position).getViewallList();
                List<horizontal_product_model> horizontalProductModelList = homePageModelList.get(position).getHorizontalProductModelList();
                String title=homePageModelList.get(position).getTitle();
                String colors=homePageModelList.get(position).getBgcolor();
                ((horizontalProductViewHolder) holder).setHorizontalProductLayout(horizontalProductModelList,title,colors,wishListModelList);
                break;
            case home_page_model.GRID_PRODUCT_VIEW:
                List<horizontal_product_model> gridProductModelList = homePageModelList.get(position).getHorizontalProductModelList();
                String grid_title=homePageModelList.get(position).getTitle();
                String gridcolorss=homePageModelList.get(position).getBgcolor();
                ((gridProductViewHolder) holder).setGridProductLayout(gridProductModelList,grid_title,gridcolorss);
                break;

            default:
                return;
        }
        if(lastpos<position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastpos=position;
        }
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

 /*   public class bannerSliderViewHolder extends RecyclerView.ViewHolder {
        Timer timer;
        private sliderAdapter sliderAdapter;
        private int currentPage ;
        private final long delayTime = 1000;
        private ViewPager bannerSlider_viewPager;
        private final long periodTime = 3000;
        private List<slider_model> adjustedList;

        public bannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerSlider_viewPager = itemView.findViewById(R.id.viewpager_banner);
        }
        private void setBannerSlider_viewPager(final List<slider_model> sliderModelList) {
            currentPage=2;
            if(timer!=null)
            {timer.cancel();}
            adjustedList=new ArrayList<>();
            for(int x=0;x<sliderModelList.size();x++)
            {
                adjustedList.add(x,sliderModelList.get(x));
            }
            adjustedList.add(0,sliderModelList.get(sliderModelList.size()-2));
            adjustedList.add(1,sliderModelList.get(sliderModelList.size()-1));
            adjustedList.add(sliderModelList.get(0));
            adjustedList.add(sliderModelList.get(1));
            sliderAdapter = new sliderAdapter(adjustedList);
            bannerSlider_viewPager.setAdapter(sliderAdapter);
            bannerSlider_viewPager.setClipToPadding(false);
            bannerSlider_viewPager.setPageMargin(20);
            bannerSlider_viewPager.setCurrentItem(currentPage);
            //   startBannerSlideShow();
            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE)
                        pageLooper(adjustedList);

                }
            };
            bannerSlider_viewPager.addOnPageChangeListener(onPageChangeListener);
            startBannerSlideShow(adjustedList);
            bannerSlider_viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pageLooper(adjustedList);
                    stopBannerSlideShow();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startBannerSlideShow(adjustedList);
                    }

                    return false;
                }
            });
        }
        private void startBannerSlideShow(final List<slider_model> sliderModelList) {
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= sliderModelList.size())
                        currentPage = 1;
                    bannerSlider_viewPager.setCurrentItem(currentPage++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, delayTime, periodTime);
        }
        private void stopBannerSlideShow() {
            timer.cancel();
        }
        private void pageLooper(List<slider_model> sliderModelList) {
            if (currentPage == sliderModelList.size() - 2) {
                currentPage = 2;
                bannerSlider_viewPager.setCurrentItem(currentPage, false);
            }
            if (currentPage == 1) {
                currentPage = sliderModelList.size() - 3;
                bannerSlider_viewPager.setCurrentItem(currentPage, false);
            }


        }
    }*/
    public static class stripAdBannerViewHolder extends RecyclerView.ViewHolder {
        private ImageView stripImage;
        private RelativeLayout stripAdContainer;
        public stripAdBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);
            stripImage = itemView.findViewById(R.id.strip_ad_image);
        }
        private void setStripAd(String resource, String colour) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.placeholderbanner)).into(stripImage);
            stripAdContainer.setBackgroundColor(Color.parseColor(colour));

        }
    }
    public class horizontalProductViewHolder extends RecyclerView.ViewHolder{
        private Button horizontal_viewall;
        private RelativeLayout container;
        private String color;
        private RecyclerView horizontal_recyclerView;
        private TextView horizontal_layoutTitle;
        public horizontalProductViewHolder(@NonNull View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.container);
            horizontal_layoutTitle=itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontal_viewall=itemView.findViewById(R.id.viewalll);
            horizontal_recyclerView=itemView.findViewById(R.id.horizontal_scroll_layout_recyclerView);
            horizontal_recyclerView.setRecycledViewPool(recycledViewPool);

        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setHorizontalProductLayout(List<horizontal_product_model> horizontalProductModelList, final String title, String color, final List<wishList_model> viewallprolist)
        {
          //container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
          container.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.white)));
            horizontal_layoutTitle.setText(title);
            if(horizontalProductModelList.size()>8) {
                horizontal_viewall.setVisibility(View.VISIBLE);
                horizontal_viewall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewAllActivity.wishListModelList=viewallprolist;
                        Intent viewalli=new Intent(itemView.getContext(),viewAllActivity.class);
                        viewalli.putExtra("layout_code",0);//horizontal
                        viewalli.putExtra("title",title);
                        itemView.getContext().startActivity(viewalli);
                    }
                });
            }
            else
                horizontal_viewall.setVisibility(View.INVISIBLE);
            horizontal_product_scroll_adapter horizontalProductScrollAdapter=new horizontal_product_scroll_adapter(horizontalProductModelList);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontal_recyclerView.setLayoutManager(linearLayoutManager);
            horizontal_recyclerView.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();
        }
    }
    public class gridProductViewHolder extends RecyclerView.ViewHolder {
        private TextView gridLayoutTitle;
        private Button gridLayoutViewall;
        private RelativeLayout container;
        private GridLayout gridLayout;
        public gridProductViewHolder(@NonNull View itemView) {
            super(itemView);
             gridLayoutTitle=itemView.findViewById(R.id.grid_layout_title);
             gridLayoutViewall =itemView.findViewById(R.id.grid_viewall);
             gridLayout=itemView.findViewById(R.id.grid_layout);
             container=itemView.findViewById(R.id.containerr);


        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setGridProductLayout(final List<horizontal_product_model> horizontalProductModelList, final String title, String color)
        {
          //  container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            gridLayoutTitle.setText(title);
            container.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.white)));

            //   gridView.setAdapter(new grid_product_layout_adapter(horizontalProductModelList));
            for(int x=0;x<4;x++)
            {
                ImageView prodimage=gridLayout.getChildAt(x).findViewById(R.id.horizontal_scroll_image);
                TextView prodtitle=gridLayout.getChildAt(x).findViewById(R.id.horizontal_scroll_t1);
                TextView description=gridLayout.getChildAt(x).findViewById(R.id.horizontal_scroll_t2);
                TextView prodprice=gridLayout.getChildAt(x).findViewById(R.id.horizontal_scroll_productPrice);

                Glide.with(itemView.getContext()).load(horizontalProductModelList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.place)).into(prodimage);
                prodtitle.setText(horizontalProductModelList.get(x).getPrductTitle());
                description.setText(horizontalProductModelList.get(x).getProductDescription());
                prodprice.setText("Rs."+horizontalProductModelList.get(x).getProductPrice()+"/-");
                gridLayout.getChildAt(x).setBackgroundColor(itemView.getContext().getResources().getColor(R.color.white));
                if(!title.equals(""))
                {
                    final int finalX = x;
                    gridLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productDetailsIntent = new Intent(itemView.getContext(),productDetailsActivity.class);
                      //  Toast.makeText(itemView.getContext(),horizontalProductModelList.get(finalX).getProductID(),Toast.LENGTH_LONG).show();

                        productDetailsIntent.putExtra("PRODUCT_ID",horizontalProductModelList.get(finalX).getProductID());
                        itemView.getContext().startActivity(productDetailsIntent);
                    }
                });}
            }
            if(!title.equals(""))
            {
            gridLayoutViewall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewAllActivity.horizontalProductModelLists=horizontalProductModelList;
                    Intent gridi=new Intent(itemView.getContext(),viewAllActivity.class);
                    gridi.putExtra("layout_code",1);
                    gridi.putExtra("title",title);
                    itemView.getContext().startActivity(gridi);
                }
            });}
        }
    }


}
