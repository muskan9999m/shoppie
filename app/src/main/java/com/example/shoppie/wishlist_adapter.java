package com.example.shoppie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class wishlist_adapter extends RecyclerView.Adapter<wishlist_adapter.ViewHolder> {
    private int lastpos=-1;
    List<wishList_model> wishListModelList;
    private Boolean wishlist;

    public wishlist_adapter(List<wishList_model> wishListModelList,Boolean wishlist) {
        this.wishListModelList = wishListModelList;
        this.wishlist=wishlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String productID=wishListModelList.get(position).getProductID();
        String res=wishListModelList.get(position).getProductImage();
        long totratings=wishListModelList.get(position).getTotalRatings();
        long freecoupons=wishListModelList.get(position).getFreeCoupons();
        String prodname=wishListModelList.get(position).getProductTitle();
        boolean instock=wishListModelList.get(position).isInstock();
        String rating=wishListModelList.get(position).getRating();
        String prodprice=wishListModelList.get(position).getProductPrice();
        String cuttedprice=wishListModelList.get(position).getCuttedPrice();
        Boolean paymethod=wishListModelList.get(position).isCod();
        ((ViewHolder)holder).setData(res,prodname,freecoupons,rating,totratings,prodprice,cuttedprice,paymethod,position,productID,instock);

        if(lastpos<position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastpos=position;
        }


    }

    @Override
    public int getItemCount() {
        return wishListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView productImage;
        private TextView productTitle;
        private TextView freecoupons;
        private View pricecut;
        private ImageView couponIcon;
        private  TextView productPrice;
        private TextView cuttedPrice;
        private ImageView deletebutton;
        private TextView paymentMethod;
        private TextView rating;
        private TextView totalRatings;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.Productimage);
            productTitle=itemView.findViewById(R.id.Producttitle);
            freecoupons=itemView.findViewById(R.id.freecoupontextview);
            pricecut=itemView.findViewById(R.id.pricecut);
            couponIcon=itemView.findViewById(R.id.coupon_icon);
            productPrice=itemView.findViewById(R.id.Productprice);
            cuttedPrice=itemView.findViewById(R.id.Cuttedprice);
            deletebutton=itemView.findViewById(R.id.delete_button);
            paymentMethod=itemView.findViewById(R.id.paymentMethod);
            rating=itemView.findViewById(R.id.tv_product_rating_miniview);
            totalRatings=itemView.findViewById(R.id.total_ratings);

        }
        private void setData(String  resource, String title, long freecouponsno, String averagerate, long totalratingsno, String price, String cuttedpricev, boolean paymethod, final int index, final String productID,boolean instock)
        {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.place)).into(productImage);
            productTitle.setText(title);
            if(freecouponsno!=0 && instock)
            {
                couponIcon.setVisibility(View.VISIBLE);
                freecoupons.setText("free "+freecouponsno+" coupons");
            }
            else {
                couponIcon.setVisibility(View.INVISIBLE);
                freecoupons.setVisibility(View.INVISIBLE);
            }
            LinearLayout ll= (LinearLayout) rating.getParent();

            if(instock) {
                rating.setVisibility(View.VISIBLE);
                totalRatings.setVisibility(View.VISIBLE);
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                cuttedPrice.setVisibility(View.VISIBLE);

                rating.setText(averagerate);
                totalRatings.setText("("+totalratingsno+" ratings)");
                productPrice.setText("Rs."+price+"/-");
                cuttedPrice.setText("Rs."+cuttedpricev+"/-");
                ll.setVisibility(View.VISIBLE);

                if(paymethod==true) {
                    paymentMethod.setVisibility(View.VISIBLE);

                }
                else{ paymentMethod.setVisibility(View.INVISIBLE);}
            }else
            {
                ll.setVisibility(View.INVISIBLE);
                paymentMethod.setVisibility(View.INVISIBLE);
                rating.setVisibility(View.INVISIBLE);
                totalRatings.setVisibility(View.INVISIBLE);
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                cuttedPrice.setVisibility(View.INVISIBLE);

            }


            if(wishlist)
            {deletebutton.setVisibility(View.VISIBLE);}
            else
            {deletebutton.setVisibility(View.GONE);}
            deletebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   deletebutton.setEnabled(false);
                    if( !productDetailsActivity.runningwishlistquery){
                    productDetailsActivity.runningwishlistquery=true;
                   DBqueries.removefromwishlist(index,itemView.getContext());
                }}
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsi=new Intent(itemView.getContext(),productDetailsActivity.class);
                    productDetailsi.putExtra("PRODUCT_ID",productID);
                    itemView.getContext().startActivity(productDetailsi);
                }
            });

        }
    }
}
