package com.example.shoppie;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class myOrderAdapter extends RecyclerView.Adapter {
    private List<myOrderItemModel> myOrderItemModelList;

    public myOrderAdapter(List<myOrderItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item_layout, parent, false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int resource = myOrderItemModelList.get(position).getProductImage();
        String title = myOrderItemModelList.get(position).getProductTitle();
        int rating = myOrderItemModelList.get(position).getRating();
        String delivereddate = myOrderItemModelList.get(position).getDeliveryStatus();
        ((viewHolder)holder).setData(resource,title,delivereddate,rating);



    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }


    class viewHolder extends RecyclerView.ViewHolder {
        private ImageView orderIndicator;
        private ImageView productImage;
        private TextView productTitle;
        private TextView deliveryStatus;
        private LinearLayout rateNowContainer;


        public viewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            orderIndicator = itemView.findViewById(R.id.order_indicator);
            productTitle = itemView.findViewById(R.id.productTitle);
            deliveryStatus = itemView.findViewById(R.id.order_delivered_date);
            rateNowContainer = itemView.findViewById(R.id.rate_now_container);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderDetailsIntent= new Intent(itemView.getContext(),orderDetailsActivity.class);
                    itemView.getContext().startActivity(orderDetailsIntent);
                }
            });

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setData(int resource, String title, String deliveredDate, int rating) {
            productImage.setImageResource(resource);
            productTitle.setText(title);
            if (deliveredDate.equals("cancelled")) {
                orderIndicator.setImageTintList(itemView.getResources().getColorStateList(R.color.colorAccent));
            }
            else
                {
                orderIndicator.setImageTintList(itemView.getResources().getColorStateList(R.color.green));
            }
            deliveryStatus.setText(deliveredDate);
            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                final int starPosition = x;
                rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        setRating(starPosition);
                    }
                });

            }
            setRating(rating);

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setRating(int starPosition) {
            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                ImageView starButton = (ImageView) rateNowContainer.getChildAt(x);
                starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#BDBABA")));
                if (x <= starPosition) {
                    starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFBB00")));
                }

            }
        }
    }

}