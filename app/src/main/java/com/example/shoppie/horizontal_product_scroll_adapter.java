package com.example.shoppie;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class horizontal_product_scroll_adapter extends RecyclerView.Adapter<horizontal_product_scroll_adapter.ViewHolder> {
 private List<horizontal_product_model> horizontalModelList;

    public horizontal_product_scroll_adapter(List<horizontal_product_model> horizontalModelList) {
        this.horizontalModelList = horizontalModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,parent,false);

       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String resource=horizontalModelList.get(position).getProductImage();
        String title=horizontalModelList.get(position).getPrductTitle();
        String description=horizontalModelList.get(position).getProductDescription();
        String price=horizontalModelList.get(position).getProductPrice();
        String productid=horizontalModelList.get(position).getProductID();

        holder.setProductData(resource,title,description,price,productid);


    }

    @Override
    public int getItemCount() {
        if(horizontalModelList.size()>8)
            return 8;
        else
            return horizontalModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private   TextView productPrice;
        private   TextView productTitle;
        private   TextView productDescription;
        public ViewHolder(@NonNull final View itemView) {

            super(itemView);
            productImage=itemView.findViewById(R.id.horizontal_scroll_image);
            productTitle=itemView.findViewById(R.id.horizontal_scroll_t1);
            productDescription=itemView.findViewById(R.id.horizontal_scroll_t2);
            productPrice=itemView.findViewById(R.id.horizontal_scroll_productPrice);


        }
        private void setProductData(String resource, final String title, String desc, String price, final String productid)
        {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.place)).into(productImage);
            productTitle.setText(title);
            productDescription.setText(desc);
            productPrice.setText("Rs."+price+"/-");
            if(!title.equals(""))
            { itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent=new Intent(itemView.getContext(),productDetailsActivity.class);
                    productDetailsIntent.putExtra("PRODUCT_ID",productid);
                 //   Toast.makeText(itemView.getContext(),productid,Toast.LENGTH_LONG);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });}
        }

    }
}
