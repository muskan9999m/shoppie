
package com.example.shoppie;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class grid_product_layout_adapter extends BaseAdapter {
    List<horizontal_product_model> horizontalProductModelList;

    public grid_product_layout_adapter(List<horizontal_product_model> horizontalProductModelList) {
        this.horizontalProductModelList = horizontalProductModelList;
    }

    @Override
    public int getCount() {
        return horizontalProductModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;
        if(convertView==null)
        {

            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,null);

            view.setBackgroundColor(Color.parseColor("#ffffff"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent = new Intent(parent.getContext(),productDetailsActivity.class);
                  //  Toast.makeText(parent.getContext(), horizontalProductModelList.get(position).getProductID(), Toast.LENGTH_LONG).show();
                    productDetailsIntent.putExtra("PRODUCT_ID",horizontalProductModelList.get(position).getProductID());
                    parent.getContext().startActivity(productDetailsIntent);
                }
            });
            ImageView productImage=view.findViewById(R.id.horizontal_scroll_image);
            TextView productDescription=view.findViewById(R.id.horizontal_scroll_t2);
            TextView productTitle=view.findViewById(R.id.horizontal_scroll_t1);
            TextView productPrice=view.findViewById(R.id.horizontal_scroll_productPrice);

            Glide.with(parent.getContext()).load(horizontalProductModelList.get(position).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.place)).into(productImage);
            productDescription.setText(horizontalProductModelList.get(position).getProductDescription());
            productTitle.setText(horizontalProductModelList.get(position).getPrductTitle());
            productPrice.setText("Rs."+horizontalProductModelList.get(position).getProductPrice()+"/-");

        }
        else
        {view=convertView;}
        return view;
    }
}
