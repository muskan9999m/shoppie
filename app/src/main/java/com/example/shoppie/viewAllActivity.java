package com.example.shoppie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.List;

public class viewAllActivity extends AppCompatActivity {
    Toolbar toolbar;
    private RecyclerView recyclerView_viewall;
    private GridView gridView;
    public static   List<horizontal_product_model> horizontalProductModelLists ;// for view all for grid list.
    public static   List<wishList_model> wishListModelList ;// for view all for horizontal list.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        recyclerView_viewall=findViewById(R.id.recycler_view);
        gridView=findViewById(R.id.grid_view);

        int layout_code=getIntent().getIntExtra("layout_code",-1);
                if(layout_code==0) {
                    recyclerView_viewall.setVisibility(View.VISIBLE);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(viewAllActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView_viewall.setLayoutManager(layoutManager);
                     wishlist_adapter wishlistadapter = new wishlist_adapter(wishListModelList, false);
                    recyclerView_viewall.setAdapter(wishlistadapter);
                    wishlistadapter.notifyDataSetChanged();
                }
                else if(layout_code==1) {
                    gridView.setVisibility(View.VISIBLE);

                    grid_product_layout_adapter gridadapter = new grid_product_layout_adapter(horizontalProductModelLists);
                    gridView.setAdapter(gridadapter);

                }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}