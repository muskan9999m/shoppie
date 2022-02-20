package com.example.shoppie;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link myOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class myOrdersFragment extends Fragment {
    private RecyclerView myOrdersRecyclerView;
    private myOrderAdapter myOrderAdapter1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public myOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment myOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static myOrdersFragment newInstance(String param1, String param2) {
        myOrdersFragment fragment = new myOrdersFragment();
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
       View view= inflater.inflate(R.layout.fragment_my_orders, container, false);
       myOrdersRecyclerView=view.findViewById(R.id.my_orders_recylerView);
       return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrdersRecyclerView.setLayoutManager(layoutManager);

        List<myOrderItemModel> myOrderItemModelList=new ArrayList<>();
        myOrderItemModelList.add(new myOrderItemModel(R.drawable.perfume,2,"Engaged","cancelled"));
        myOrderItemModelList.add(new myOrderItemModel(R.drawable.paris,4,"Paris Flag","Delivered at 2 pm Monday, Jan 2020."));
        myOrderItemModelList.add(new myOrderItemModel(R.drawable.perfume,1,"Engaged","cancelled"));
        myOrderItemModelList.add(new myOrderItemModel(R.drawable.perfume,0,"ks","Delivery pending"));

        myOrderAdapter1=new myOrderAdapter(myOrderItemModelList);
        myOrdersRecyclerView.setAdapter(myOrderAdapter1);
        myOrderAdapter1.notifyDataSetChanged();


    }
}