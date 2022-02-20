package com.example.shoppie;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.example.shoppie.DBqueries.categorymodelLists;
import static com.example.shoppie.DBqueries.lists;
import static com.example.shoppie.DBqueries.loadCategories;
import static com.example.shoppie.DBqueries.loadFragmentData;
import static com.example.shoppie.DBqueries.loadedCategoriesNames;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RecyclerView categoryView;
    private RecyclerView homepageRecyclerview;
    private ImageView noiconnection;
    private categoryadapter categoryadapter;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    home_page_adapter homePageAdapter;
    private List<categorymodel> categorymodelListfake=new ArrayList<>();
    private List<home_page_model> homePageModelListfake=new ArrayList<>();
    private Button retrybutton;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homesFragment newInstance(String param1, String param2) {
        homesFragment fragment = new homesFragment();
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
        View view= inflater.inflate(R.layout.fragment_homes, container, false);
        noiconnection=view.findViewById(R.id.noiconnection);
        connectivityManager= (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo=connectivityManager.getActiveNetworkInfo();
        categoryView=view.findViewById(R.id.category);
        retrybutton=view.findViewById(R.id.refreshButton);
        swipeRefreshLayout=view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorAccent),getContext().getResources().getColor(R.color.colorAccent));

        homepageRecyclerview=view.findViewById(R.id.homel);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homepageRecyclerview.setLayoutManager(layoutManager);

        //   homePageAdapter=new home_page_adapter(homePageModelListfake);
        LinearLayoutManager layoutManager1=new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryView.setLayoutManager(layoutManager1);

        categorymodelListfake.add(new categorymodel("null"," "));
        categorymodelListfake.add(new categorymodel(""," "));
        categorymodelListfake.add(new categorymodel(""," "));
        categorymodelListfake.add(new categorymodel(""," "));
        categorymodelListfake.add(new categorymodel(""," "));
        categorymodelListfake.add(new categorymodel(""," "));
        categorymodelListfake.add(new categorymodel(""," "));
        categorymodelListfake.add(new categorymodel(""," "));
        categorymodelListfake.add(new categorymodel(""," "));
        categorymodelListfake.add(new categorymodel(""," "));
        categorymodelListfake.add(new categorymodel(""," "));

       /* List<slider_model> sliderModelListfake=new ArrayList<>();
        sliderModelListfake.add(new slider_model("null","#dfdfdf"));
        sliderModelListfake.add(new slider_model("null","#dfdfdf"));
        sliderModelListfake.add(new slider_model("null","#dfdfdf"));
        sliderModelListfake.add(new slider_model("null","#dfdfdf"));*/
        List<horizontal_product_model>horizontalProductModelListfake=new ArrayList<>();
        horizontalProductModelListfake.add(new horizontal_product_model("","","","",""));
        horizontalProductModelListfake.add(new horizontal_product_model("","","","",""));
        horizontalProductModelListfake.add(new horizontal_product_model("","","","",""));
        horizontalProductModelListfake.add(new horizontal_product_model("","","","",""));
        horizontalProductModelListfake.add(new horizontal_product_model("","","","",""));

        //homePageModelListfake.add(new home_page_model(0,sliderModelListfake));
        homePageModelListfake.add(new home_page_model(1,"","#dfdfdf"));
        homePageModelListfake.add(new home_page_model(3,"","#dfdfdf",horizontalProductModelListfake));
        homePageModelListfake.add(new home_page_model(2,"","#dfdfdf",horizontalProductModelListfake,new ArrayList<wishList_model>()));

        categoryadapter = new categoryadapter(categorymodelListfake);
        homePageAdapter=new home_page_adapter(homePageModelListfake);


        if(networkInfo!=null && networkInfo.isConnected()==true){

            MainActivity2.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            noiconnection.setVisibility(View.GONE);
            retrybutton.setVisibility(View.GONE);
            categoryView.setVisibility(View.VISIBLE);
            homepageRecyclerview.setVisibility(View.VISIBLE);
            //category
            if(categorymodelLists.size()==0)
            {
                loadCategories(categoryView,getContext());
            }
            else
            {
                categoryadapter=new categoryadapter(categorymodelLists);
                categoryView.setAdapter(categoryadapter);//169
                categoryadapter.notifyDataSetChanged();
            }


            if(lists.size()==0)
            {
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<home_page_model>());
                loadFragmentData(homepageRecyclerview,getContext(),0,"home");
            }
            else
            {
                homePageAdapter=new home_page_adapter(lists.get(0));
                homePageAdapter.notifyDataSetChanged();
                homepageRecyclerview.setAdapter(homePageAdapter);//183
            }


        }else{
            MainActivity2.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            Glide.with(this).load(R.drawable.heart).into(noiconnection);
            noiconnection.setVisibility(View.VISIBLE);
            categoryView.setVisibility(View.GONE);
            homepageRecyclerview.setVisibility(View.GONE);
            retrybutton.setVisibility(View.VISIBLE);

        }
        /////refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reload();
            } });
        retrybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });

        return view;
    }


    private void reload() {
            networkInfo=connectivityManager.getActiveNetworkInfo();
            DBqueries.clearData();

//            categorymodelLists.clear();
//            lists.clear();
//            loadedCategoriesNames.clear();

            if(networkInfo!=null && networkInfo.isConnected()==true){
                MainActivity2.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                categoryView.setVisibility(View.VISIBLE);
                retrybutton.setVisibility(View.GONE);
                homepageRecyclerview.setVisibility(View.VISIBLE);
                noiconnection.setVisibility(View.GONE);
                categoryadapter=new categoryadapter(categorymodelListfake);
                categoryView.setAdapter(categoryadapter);
                homePageAdapter=new home_page_adapter(homePageModelListfake);
                homepageRecyclerview.setAdapter(homePageAdapter);
                loadCategories(categoryView,getContext());

                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<home_page_model>());
                loadFragmentData(homepageRecyclerview,getContext(),0,"home");
            }
            else{
                Glide.with(homesFragment.this).load(R.drawable.frus).into(noiconnection);
                MainActivity2.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                noiconnection.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                categoryView.setVisibility(View.GONE);
                homepageRecyclerview.setVisibility(View.GONE);
                retrybutton.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),"no connection",Toast.LENGTH_SHORT).show();

            }

        }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}