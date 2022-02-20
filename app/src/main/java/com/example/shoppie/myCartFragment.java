package com.example.shoppie;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.shoppie.DBqueries.cartItemModelLiist;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link myCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class myCartFragment extends Fragment {
    private RecyclerView cartItemsRecyclerView;
    Dialog lodingDialog;
    private Button continuebutton;
    public static cart_adapter cart_adapter;
    private TextView totalAmount;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public myCartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment myCartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static myCartFragment newInstance(String param1, String param2) {
        myCartFragment fragment = new myCartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_cart, container, false);
        cartItemsRecyclerView=view.findViewById(R.id.mycartRecyclerView);
        continuebutton=view.findViewById(R.id.cart_continue_button);
        totalAmount=view.findViewById(R.id.total_cartamount);


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lodingDialog=new Dialog(getContext());
        lodingDialog.setContentView(R.layout.loading_progress_dialog);
        lodingDialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.slider_background));
        lodingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lodingDialog.setCancelable(false);
        lodingDialog.show();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);

        cart_adapter  = new cart_adapter(cartItemModelLiist,totalAmount,true);
        cartItemsRecyclerView.setAdapter(cart_adapter);
        cart_adapter.notifyDataSetChanged();


        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeliveryActivity.fromcart=true;
                DeliveryActivity.cartitemmodellist=new ArrayList<>();
                for(int x=0;x< cartItemModelLiist.size();x++)
                {
                    cart_item_model cartItemModel= cartItemModelLiist.get(x);
                    if(cartItemModel.isInstock())
                    {DeliveryActivity.cartitemmodellist.add(cartItemModel);
                    }
                }
                DeliveryActivity.cartitemmodellist.add(new cart_item_model(cart_item_model.TOTAL_AMOUNT));
                lodingDialog.show();
                if(DBqueries.addressesModelList.size()==0){
                DBqueries.loadAdresses(getContext(),lodingDialog);}
                else
                {   Intent deliveryintent = new Intent(getContext(), DeliveryActivity.class);
                    startActivity(deliveryintent);
                    lodingDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        cart_adapter.notifyDataSetChanged();
        if(DBqueries.rewardmodelList.size()==0)
        {
            DBqueries.loadRewards(getActivity(),lodingDialog,false);
        }
        if(cartItemModelLiist.size()==0)
        {
            DBqueries.cartlist.clear();
            DBqueries.loadcartlist(getContext(),lodingDialog,true,new TextView(getActivity()),totalAmount);
        }
        else
        {
            if(cartItemModelLiist.get(cartItemModelLiist.size() - 1).getType() == cart_item_model.TOTAL_AMOUNT)
            {
                LinearLayout p = (LinearLayout) totalAmount.getParent().getParent();
                p.setVisibility(View.VISIBLE);
            }
            lodingDialog.dismiss();}


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (cart_item_model cartItemModel: cartItemModelLiist)
        {

            if(!TextUtils.isEmpty(cartItemModel.getSelectedCouponId()))
            {
                for (reward_model rewardModel : DBqueries.rewardmodelList) {
                    if (rewardModel.getCouponId().equals(cartItemModel.getSelectedCouponId())) {
                        rewardModel.setAlreadyused(false);
                    }
                }
                cartItemModel.setSelectedCouponId(null);
                if(myRewardsFragment.myrewardsAdapter!=null)
                myRewardsFragment.myrewardsAdapter.notifyDataSetChanged();
            }
        }
    }
}