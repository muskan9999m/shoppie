package com.example.shoppie;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class product_details_adapter extends FragmentPagerAdapter {
    private int totalTabs;
    public product_details_adapter(@NonNull FragmentManager fm,int totalTabs) {

        super(fm);
        this.totalTabs=totalTabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                productDetailsFragment productDetailsFragment1=new productDetailsFragment();
                return  productDetailsFragment1;
            case 1:
                productSpecificationFragment productSpecificationFragment1=new productSpecificationFragment();
                return productSpecificationFragment1;

            case 2:
                productDetailsFragment productDetailsFragment2=new productDetailsFragment();
                return  productDetailsFragment2;
            default:return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
