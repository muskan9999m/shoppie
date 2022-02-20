package com.example.shoppie;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link productSpecificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class productSpecificationFragment extends Fragment {
    private RecyclerView productSpecificationRecyclerView;
    public List<product_specification_model> productSpecificationModelList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public productSpecificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment productSpecificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static productSpecificationFragment newInstance(String param1, String param2) {
        productSpecificationFragment fragment = new productSpecificationFragment();
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
        View view= inflater.inflate(R.layout.fragment_product_specification, container, false);

        productSpecificationRecyclerView=view.findViewById(R.id.product_specification_recyclerView);

        /*  productSpecificationModelList.add(new product_specification_model(0,"General"));
        productSpecificationModelList.add(new product_specification_model(1,"RAM","4GB"));
        productSpecificationModelList.add(new product_specification_model(1,"RAM","4GB"));
        productSpecificationModelList.add(new product_specification_model(1,"RAM","4GB"));
        productSpecificationModelList.add(new product_specification_model(1,"RAM","4GB"));
        productSpecificationModelList.add(new product_specification_model(0,"Display"));
        productSpecificationModelList.add(new product_specification_model(1,"RAM","4GB"));
        productSpecificationModelList.add(new product_specification_model(1,"RAM","4GB"));
        productSpecificationModelList.add(new product_specification_model(1,"RAM","4GB"));
        productSpecificationModelList.add(new product_specification_model(1,"RAM","4GB"));

       */

        LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        productSpecificationRecyclerView.setLayoutManager(layoutManager);


        productSpecificationAdapter productSpecificationAdapter1=new productSpecificationAdapter(productSpecificationModelList);
       productSpecificationRecyclerView.setAdapter(productSpecificationAdapter1);
        productSpecificationAdapter1.notifyDataSetChanged();
        return view;
    }
}