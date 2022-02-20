package com.example.shoppie;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.shoppie.DBqueries.lists;
import static com.example.shoppie.DBqueries.loadFragmentData;
import static com.example.shoppie.DBqueries.loadedCategoriesNames;

public class categoryActivity extends AppCompatActivity {
    private RecyclerView categoryRecyclerView;
    private List<home_page_model> homePageModelListfake=new ArrayList<>();

    home_page_adapter homePageAdapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryRecyclerView=(RecyclerView)findViewById(R.id.categoryActivityRecyclerView);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title=getIntent().getStringExtra("categoryName");
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ////////////////////////
     /*   List<slider_model> sliderModelListfake=new ArrayList<>();
        sliderModelListfake.add(new slider_model("null","#ffffff"));
        sliderModelListfake.add(new slider_model("null","#ffffff"));
        sliderModelListfake.add(new slider_model("null","#ffffff"));
        sliderModelListfake.add(new slider_model("null","#ffffff"));*/
        List<horizontal_product_model>horizontalProductModelListfake=new ArrayList<>();
        horizontalProductModelListfake.add(new horizontal_product_model("","","","",""));
        horizontalProductModelListfake.add(new horizontal_product_model("","","","",""));
        horizontalProductModelListfake.add(new horizontal_product_model("","","","",""));
        horizontalProductModelListfake.add(new horizontal_product_model("","","","",""));
        horizontalProductModelListfake.add(new horizontal_product_model("","","","",""));

      //  homePageModelListfake.add(new home_page_model(0,sliderModelListfake));
        homePageModelListfake.add(new home_page_model(1,"","#ffffff"));
        homePageModelListfake.add(new home_page_model(2,"","#ffffff",horizontalProductModelListfake,new ArrayList<wishList_model>()));
        homePageModelListfake.add(new home_page_model(3,"","#fffffff",horizontalProductModelListfake));
/////////////////////



  //      homePageAdapter=new home_page_adapter(lists.get(loadedCategoriesNames.size()-1));
        LinearLayoutManager layoutManager=new LinearLayoutManager(categoryActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(layoutManager);
        homePageAdapter=new home_page_adapter(homePageModelListfake);





        int listposition=0;


        for(int x=0;x<DBqueries.loadedCategoriesNames.size();x++)
        {
            if( DBqueries.loadedCategoriesNames.get(x).equals(title.toUpperCase()))
            {
                listposition=x;
            }
        }
        if(listposition==0)
        {
            loadedCategoriesNames.add(title.toUpperCase());
            lists.add(new ArrayList<home_page_model>());
            loadFragmentData(categoryRecyclerView,this,loadedCategoriesNames.size()-1,title);
           // categoryRecyclerView.setAdapter(homePageAdapter);categoryRecyclerView.setAdapter(homePageAdapter);

        }
        else
        {
            homePageAdapter=new home_page_adapter(lists.get(listposition));
            categoryRecyclerView.setAdapter(homePageAdapter);//98
        }


        homePageAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.searchicon)
        {return true;}
        else if(id==android.R.id.home)

        {finish();
            return true;}
        return super.onOptionsItemSelected(item);
    }


}