package com.example.shoppie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class produc_details_adapter extends FragmentPagerAdapter {
    private String productDescription;
    private String productOtherDetails;
    private List<product_specification_model> productSpecificationModelList;

    private int totalTabs;


    public produc_details_adapter(@NonNull FragmentManager fm,int totalTabs, String productDescription, String productOtherDetails, List<product_specification_model> productSpecificationModelList) {
        super(fm);
        this.productDescription = productDescription;
        this.productOtherDetails = productOtherDetails;
        this.productSpecificationModelList = productSpecificationModelList;
        this.totalTabs = totalTabs;
    }
/*
    public produc_details_adapter(@NonNull FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }



 */


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                productDetailsFragment productDetailsFragment1=new productDetailsFragment();
                productDetailsFragment1.body=productDescription;
                return  productDetailsFragment1;
            case 1:
                productSpecificationFragment productSpecificationFragment1=new productSpecificationFragment();
                productSpecificationFragment1.productSpecificationModelList=productSpecificationModelList;
                return productSpecificationFragment1;

            case 2:
                productDetailsFragment productDetailsFragment2=new productDetailsFragment();
                productDetailsFragment2.body=productOtherDetails;
                return  productDetailsFragment2;
            default:return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
