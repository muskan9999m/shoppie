package com.example.shoppie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.shoppie.DeliveryActivity.SELECT_ADDRESS;
import static com.example.shoppie.myAccountFragment.MANAGE_ADDRESS;
import static com.example.shoppie.myAdressesActivity.refreshitem;

public class AdressAdapter extends RecyclerView.Adapter<AdressAdapter.ViewHolder> {
    private List<addresses_model> addressesModelList;
    private int MODE;
    private int preselectedposition=-1;

    public AdressAdapter(List<addresses_model> addressesModelList,int MODE) {
        this.addressesModelList = addressesModelList;
         preselectedposition=DBqueries.selectedaddress;
        this.MODE=MODE;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String n=addressesModelList.get(position).getFullname();
        String a=addressesModelList.get(position).getAddress();
        String p=addressesModelList.get(position).getPincode();
        String mobileno=addressesModelList.get(position).getMobileno();
        Boolean selected=addressesModelList.get(position).getSELECTED();
        ((ViewHolder)holder).setAdressData(n,a,p,selected,position,mobileno);

    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fullname;
        private TextView address;
        private ImageView icon;
        private LinearLayout option_container;
        private TextView pincode;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullname=itemView.findViewById(R.id.NAME);
            address=itemView.findViewById(R.id.ADDRESS);
            icon=itemView.findViewById(R.id.icon_view);
            pincode=itemView.findViewById(R.id.PINCODE);
            option_container=itemView.findViewById(R.id.option_container);
        }
        private void setAdressData(String ufullname, String uaddress, String upincode, Boolean selected, final int pos,String mob)
        {

            fullname.setText(ufullname+"-"+mob);
            address.setText(uaddress);
            pincode.setText(upincode);
            if(MODE==SELECT_ADDRESS)
            {
                icon.setImageResource(R.drawable.tick);
                if(selected) {
                    icon.setVisibility(View.VISIBLE);
                    preselectedposition=pos;
                }
                else
                {  icon.setVisibility(View.GONE);}
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(preselectedposition!=pos){
                        addressesModelList.get(pos).setSELECTED(true);
                        addressesModelList.get(preselectedposition).setSELECTED(false);
                        refreshitem(preselectedposition,pos);
                        preselectedposition=pos;
                        DBqueries.selectedaddress=pos;
                        }

                    }
                });

            }
            else if(MODE==MANAGE_ADDRESS){
                option_container.setVisibility(View.GONE);
                icon.setImageResource(R.drawable.verticaldots);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        option_container.setVisibility(View.VISIBLE);
                        icon.setVisibility(View.GONE);
                        refreshitem(preselectedposition,preselectedposition);
                        preselectedposition=pos;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshitem(preselectedposition,preselectedposition);
                        preselectedposition=-1;
                    }
                });
            }
        }
    }
}
