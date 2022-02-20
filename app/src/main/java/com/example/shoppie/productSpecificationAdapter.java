package com.example.shoppie;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class productSpecificationAdapter extends RecyclerView.Adapter<productSpecificationAdapter.ViewHolder> {
    private List<product_specification_model> productSpecificationModelList;

    public productSpecificationAdapter(List<product_specification_model> productSpecificationModelList) {
        this.productSpecificationModelList = productSpecificationModelList;
    }

    @NonNull
    @Override
    public productSpecificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case (product_specification_model.SPECIFICATION_TITLE):
                TextView title = new TextView(parent.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setDp(16, parent.getContext()), setDp(16, parent.getContext()), setDp(16, parent.getContext()), setDp(8, parent.getContext()));
                title.setLayoutParams(layoutParams);
                return new ViewHolder(title);
            case (product_specification_model.SPECIFICATION_BODY):
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_specification_item_layout, parent, false);
                return new ViewHolder(view);
            default:
                return null;
        }

    }

    private int setDp(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getItemViewType(int position) {
        switch (productSpecificationModelList.get(position).getType()) {
            case 0:
                return product_specification_model.SPECIFICATION_TITLE;
            case 1:
                return product_specification_model.SPECIFICATION_BODY;
            default:
                return -1;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull productSpecificationAdapter.ViewHolder holder, int position) {
        switch (productSpecificationModelList.get(position).getType()) {
            case product_specification_model.SPECIFICATION_TITLE:
                holder.setTitle(productSpecificationModelList.get(position).getTitle());
                break;
            case product_specification_model.SPECIFICATION_BODY:
                String featurename = productSpecificationModelList.get(position).getFeatureName();
                String featurevalue = productSpecificationModelList.get(position).getFeatureVakue();
                holder.setFeatures(featurename, featurevalue);
                break;
            default:
                return;
        }





    }

    @Override
    public int getItemCount() {
        return productSpecificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView featureName;
        private TextView featureValue;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        private void setTitle(String titleText) {
            title = (TextView) itemView;
            title.setText(titleText);
        }

        private void setFeatures(String feature_name, String feature_value) {
            featureValue = itemView.findViewById(R.id.feature_value);
            featureName = itemView.findViewById(R.id.feature_name);

            featureName.setText(feature_name);
            featureValue.setText(feature_value);
        }

    }
}
