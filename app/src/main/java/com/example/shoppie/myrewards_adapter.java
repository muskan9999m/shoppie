package com.example.shoppie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myrewards_adapter extends RecyclerView.Adapter<myrewards_adapter.ViewHolder> {
    private List<reward_model> rewardModelList;
    private Boolean useminilayout=false;
    private List<cart_item_model> cartItemModelList;
    private RecyclerView coupons_rv;
    private LinearLayout selected_coupon;
    private String prodOriginalPrice;
    private TextView coupontitle1;
    private TextView couponexpdate1;
    private TextView discountedprice;
    private int cartitempositon=-1;
    private TextView couponbody1;

    public myrewards_adapter(int cartitempositon,List<reward_model> rewardModelList, Boolean useminilayout, RecyclerView coupons_rv, LinearLayout selected_coupon, String prodOriginalPrice, TextView coupontitle1, TextView couponexpdate1, TextView couponbody1,List<cart_item_model> cartItemModelList) {
        this.rewardModelList = rewardModelList;
        this.useminilayout=useminilayout;
        this.coupons_rv=coupons_rv;
        this.selected_coupon=selected_coupon;
        this.prodOriginalPrice=prodOriginalPrice;
        this.cartitempositon=cartitempositon;
        this.coupontitle1=coupontitle1;
        this.couponexpdate1=couponexpdate1;
        this.couponbody1=couponbody1;
        this.cartItemModelList=cartItemModelList;

    }

    public myrewards_adapter(List<reward_model> rewardModelList, Boolean useminilayout, RecyclerView coupons_rv, LinearLayout selected_coupon, String prodOriginalPrice, TextView coupontitle1, TextView couponexpdate1, TextView couponbody1,TextView discountedprice) {
        this.rewardModelList = rewardModelList;
        this.useminilayout=useminilayout;
        this.coupons_rv=coupons_rv;
        this.selected_coupon=selected_coupon;
        this.prodOriginalPrice=prodOriginalPrice;
        this.coupontitle1=coupontitle1;
        this.couponexpdate1=couponexpdate1;
        this.discountedprice=discountedprice;
        this.couponbody1=couponbody1;

    }
    public myrewards_adapter(List<reward_model> rewardModelList, Boolean useminilayout) {
        this.rewardModelList = rewardModelList;
        this.useminilayout=useminilayout;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(useminilayout)
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewards_item_layout,parent,false);
        else
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title=rewardModelList.get(position).getType();
        String couponId=rewardModelList.get(position).getCouponId();
        Date expirydate =rewardModelList.get(position).getTimestamp();
        String body=rewardModelList.get(position).getCouponbody();
        String lowerlimit=rewardModelList.get(position).getLowerlimit();
        String upperlimit=rewardModelList.get(position).getUpperlimit();
        String discoramount=rewardModelList.get(position).getDiscoramount();
        boolean alreadyused=rewardModelList.get(position).isAlreadyused();

        ((ViewHolder)holder).setData(couponId,title,expirydate,body,lowerlimit,upperlimit,discoramount,alreadyused);
    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView coupontitle;
        private TextView couponexpirydate;
        private TextView couponbody;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coupontitle=itemView.findViewById(R.id.couponTitle);
            couponexpirydate=itemView.findViewById(R.id.couponvalidity);
            couponbody=itemView.findViewById(R.id.couponbody);

        }
        private void setData(final String couponid, final String type, final Date date, final String body, final String lower, final String upper, final String discamt, boolean alreadyused)
        {
            if(type.equals("Discount"))
            {
                coupontitle.setText(type);
            }
            else {
                coupontitle.setText("Flat Rs."+discamt+"*OFF");
            }
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM YYYY");
            if (alreadyused)
            {
                couponbody.setTextColor(itemView.getContext().getResources().getColor(R.color.back));
                coupontitle.setTextColor(itemView.getContext().getResources().getColor(R.color.back));
                couponexpirydate.setText("Already used");
                couponexpirydate.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
            }
            else {
                couponbody.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                coupontitle.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                couponexpirydate.setTextColor(itemView.getContext().getResources().getColor(R.color.purple));
                couponexpirydate.setText("till " + simpleDateFormat.format(date));
            }
            couponbody.setText(body);

            if(useminilayout)
            {
                if(!alreadyused){
                    itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        coupontitle1.setText(type);
                        couponbody1.setText(body);
                        couponexpdate1.setText(simpleDateFormat.format(date));
                        
                        if (Long.parseLong(prodOriginalPrice)>Long.parseLong(lower) && Long.parseLong(prodOriginalPrice)<Long.parseLong(upper))
                        {
                            if (type.equals("Discount"))
                            {
                                long discountamount=Long.valueOf(prodOriginalPrice)*Long.valueOf(discamt)/100;
                                discountedprice.setText("Rs."+(Long.valueOf(prodOriginalPrice)-discountamount)+"/-");
                            }
                            else
                            {
                                discountedprice.setText("Rs."+(Long.valueOf(prodOriginalPrice)-Long.valueOf(discamt))+"/-");
                            }
                            if(cartitempositon!=-1) {
                               cartItemModelList.get(cartitempositon).setSelectedCouponId(couponid);
                            }
                        }
                        else
                        {
                            if(cartitempositon!=-1)
                            cartItemModelList.get(cartitempositon).setSelectedCouponId(null);
                            discountedprice.setText("Sorry! product does not match the coupon terms.");
                        }
                        if (coupons_rv.getVisibility() == View.GONE)
                        {
                            coupons_rv.setVisibility(View.VISIBLE);
                            selected_coupon.setVisibility(View.GONE);
                        } else
                            {
                            coupons_rv.setVisibility(View.GONE);
                            selected_coupon.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }
        }}
    }
}
