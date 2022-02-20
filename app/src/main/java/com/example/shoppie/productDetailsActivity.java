package com.example.shoppie;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.example.shoppie.DBqueries.cartItemModelLiist;
import static com.example.shoppie.DBqueries.loadcartlist;
import static com.example.shoppie.DBqueries.myratedids;
import static com.example.shoppie.DBqueries.myrating;
import static com.example.shoppie.DBqueries.wishListModelLiist;
import static com.example.shoppie.MainActivity2.showcart;
import static com.example.shoppie.Register.setSignupFragment;

public class productDetailsActivity extends AppCompatActivity {
    private TextView badgecount;
    public static MenuItem cartitem;
    public static boolean ALREADYADDEDTOCART = false;
    public static boolean runningcartquery = false;
    public static int initialrating;
    public static boolean runningwishlistquery = false;
    public static boolean runningratingquery = false;
    //////////ratings layout
    public static LinearLayout rateNowContainer;
    private TextView totalRatings;
    private TextView averageRating;
    ///////////coupon dialog
    private TextView coupontitle;
    private TextView couponexpirydate;
    private  TextView discountedprice;
    private  LinearLayout selectedCoupon;
    private TextView couponbody;
    ///////////coupon dialog
    private Dialog signinDialog;
    private LinearLayout addtocartbutton;

    ////////////
    private Boolean instock=false;
    private Dialog lodingDialog;

    private ViewPager productDetailsViewpager;

    private TabLayout viewPagerIndicator;
    private ViewPager productImagesViewPager;
    public static FloatingActionButton addToWishListButton;

    private Button buynow;
    public static RecyclerView coupons_rv;
    private LinearLayout couponredeemll;
    private Button couponredeemButton;
    private String productDescription;
    private String productOtherDetails;

    private DocumentSnapshot documentSnapshot;


    public static boolean ALREADYADDEDTOWISHLIST = false;
    /////////////product description variables

    private TabLayout productDetailsTablayout;
    private RelativeLayout tablayoutcontainer;
    private RelativeLayout productOnlyDetailsLayout;
    private TextView producttitle;
    private TextView productprice;
    private ImageView cod_indicator;
    private ImageView reward_icon;
    private TextView rewardBody;
    private TextView cuttedprice;
    private String prodoriginalvalue;
    private TextView tv_cod_indicator;
    private TextView reward_title;
    private TextView avg_rating_miniview;
    private TextView total_rating_miniview;
    private TextView productOnlyDescbody;
    public static Activity productDetailsActivity;
    private LinearLayout ratingsnocontainer;
    private TextView totalRatingsFigure;
    public static myrewards_adapter myrewardsAdapter;
    /////////////////
    public static FirebaseUser curruser;
    private LinearLayout ratingsProgressbarContainer;
    private List<product_specification_model> productSpecificationModelList;
    public static String prodId;
    private TextView originalprice;



    private FirebaseFirestore firebaseFirestore;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        initialrating = -1;
        productSpecificationModelList = new ArrayList<>();
        addToWishListButton = (FloatingActionButton) findViewById(R.id.addtowishlist);
        ratingsProgressbarContainer = findViewById(R.id.ratings_progressbar_container);
        couponredeemButton = (Button) findViewById(R.id.couponredemptionbutton);
        ratingsnocontainer = (LinearLayout) findViewById(R.id.ratings_number_container);
        //viewpager tblayout connect
        productImagesViewPager = (ViewPager) findViewById(R.id.product_images_viewpager);
        viewPagerIndicator = (TabLayout) findViewById(R.id.viewPager_indicator);
        viewPagerIndicator.setupWithViewPager(productImagesViewPager, true);

        addtocartbutton = (LinearLayout) findViewById(R.id.add_to_cart_button);
        producttitle = findViewById(R.id.product_title);
        avg_rating_miniview = findViewById(R.id.tv_product_rating_miniview);
        total_rating_miniview = findViewById(R.id.total_ratings_miniview);
        productprice = findViewById(R.id.productprice);
        cuttedprice = findViewById(R.id.cutted_price);
        tv_cod_indicator = findViewById(R.id.tv_codindicator);
        cod_indicator = findViewById(R.id.cod_indicatorimage);
        reward_icon = findViewById(R.id.reward_icon);
        reward_title = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);
        tablayoutcontainer = findViewById(R.id.product_details_tabs_container);
        productOnlyDetailsLayout = findViewById(R.id.product_details_container);
        productOnlyDescbody = findViewById(R.id.product_details_body);
        totalRatings = findViewById(R.id.total_ratings);
        averageRating = findViewById(R.id.avg_rating);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        productDetailsTablayout = (TabLayout) findViewById(R.id.product_details_tablayout);
        couponredeemll = (LinearLayout) findViewById(R.id.coupon_redemption_layout);
        buynow = findViewById(R.id.buynow_button);

        //////////loading dialog
        lodingDialog = new Dialog(productDetailsActivity.this);
        lodingDialog.setContentView(R.layout.loading_progress_dialog);
        lodingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        lodingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lodingDialog.setCancelable(false);
        lodingDialog.show();

        /////////////coupon dialog
        final Dialog pricedialog = new Dialog(productDetailsActivity.this);

        pricedialog.setContentView(R.layout.couponredeemdialog);
        pricedialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pricedialog.setCancelable(true);

        ImageView togglerv = pricedialog.findViewById(R.id.togglerecyclerView);
        //  togglerv.setImageResource(R.drawable.heart);
        coupons_rv = (RecyclerView) pricedialog.findViewById(R.id.couponsrecyclerview1);
        selectedCoupon = pricedialog.findViewById(R.id.selectedcoupon);

        coupontitle = pricedialog.findViewById(R.id.couponTitle);
        couponexpirydate = pricedialog.findViewById(R.id.couponvalidity);
        couponbody = pricedialog.findViewById(R.id.couponbody);
        originalprice = pricedialog.findViewById(R.id.originalprice);
        discountedprice = pricedialog.findViewById(R.id.afterprice);

        LinearLayoutManager lm = new LinearLayoutManager(productDetailsActivity.this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        coupons_rv.setLayoutManager(lm);


        togglerv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coupons_rv.getVisibility() == View.GONE) {
                    coupons_rv.setVisibility(View.VISIBLE);
                    selectedCoupon.setVisibility(View.GONE);
                } else {
                    coupons_rv.setVisibility(View.GONE);
                    selectedCoupon.setVisibility(View.VISIBLE);
                }

            }
        });


        firebaseFirestore = FirebaseFirestore.getInstance();
        final List<String> productImages = new ArrayList<>();
        prodId = getIntent().getStringExtra("PRODUCT_ID");

        firebaseFirestore.collection("PRODUCTS").document(getIntent().getStringExtra("PRODUCT_ID")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();
                    firebaseFirestore.collection("PRODUCTS").document(prodId).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
                                    productImages.add(documentSnapshot.get("product_image_" + x).toString());
                                }
                                product_images_adapter productImagesAdapter = new product_images_adapter(productImages);
                                productImagesViewPager.setAdapter(productImagesAdapter);
                                producttitle.setText(documentSnapshot.get("product_title").toString());
                                avg_rating_miniview.setText(documentSnapshot.get("average_rating").toString());
                                total_rating_miniview.setText("(" + documentSnapshot.get("total_ratings").toString() + " ratings)");
                                productprice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");
                                prodoriginalvalue=documentSnapshot.get("product_price").toString();
                                //for coupon dialog
                                originalprice.setText(productprice.getText());
                                 myrewardsAdapter = new myrewards_adapter(DBqueries.rewardmodelList, true,coupons_rv,selectedCoupon,prodoriginalvalue,coupontitle,couponexpirydate,couponbody,discountedprice);
                                coupons_rv.setAdapter(myrewardsAdapter);
                                myrewardsAdapter.notifyDataSetChanged();
                                cuttedprice.setText("Rs." + documentSnapshot.get("cutted_price").toString() + "/-");
                                if ((boolean) documentSnapshot.get("COD")) {
                                    cod_indicator.setVisibility(View.VISIBLE);
                                    tv_cod_indicator.setVisibility(View.VISIBLE);
                                } else {
                                    cod_indicator.setVisibility(View.INVISIBLE);
                                    tv_cod_indicator.setVisibility(View.INVISIBLE);
                                }

                                reward_title.setText((long) documentSnapshot.get("free_coupons") + " " + documentSnapshot.get("free_coupon_title").toString());
                                rewardBody.setText(documentSnapshot.get("free_coupon_body").toString());
                                if ((boolean) documentSnapshot.get("use_tab_layout")) {
                                    tablayoutcontainer.setVisibility(View.VISIBLE);
                                    productOnlyDetailsLayout.setVisibility(View.GONE);
                                    productDescription = documentSnapshot.get("product_description").toString();

                                    for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
                                        productSpecificationModelList.add(new product_specification_model(0, documentSnapshot.get("spec_title_" + x).toString()));
                                        for (long i = 1; i < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; i++) {
                                            productSpecificationModelList.add(new product_specification_model(1, documentSnapshot.get("spec_title_" + x + "_field_" + i + "_name").toString(), documentSnapshot.get("spec_title_" + x + "_field_" + i + "_value").toString()));
                                        }
                                    }
                                    productOtherDetails = documentSnapshot.get("product_other_details").toString();
                                } else {
                                    tablayoutcontainer.setVisibility(View.GONE);
                                    productOnlyDetailsLayout.setVisibility(View.VISIBLE);
                                    productOnlyDescbody.setText(documentSnapshot.get("product_description").toString());

                                }
                                totalRatings.setText("(" + (long) documentSnapshot.get("total_ratings") + " ratings)");
                                //for setting progress bar
                                for (int x = 1; x < 6; x++) {
                                    TextView rating = (TextView) ratingsnocontainer.getChildAt(x - 1);
                                    rating.setText(String.valueOf((long) documentSnapshot.get((6 - x) + "_star")));
                                    int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                    ProgressBar progressBar = (ProgressBar) ratingsProgressbarContainer.getChildAt(x - 1);
                                    progressBar.setMax(maxProgress);
                                    progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((6 - x) + "_star"))));
                                }
                                totalRatingsFigure.setText(documentSnapshot.get("total_ratings").toString());
                                averageRating.setText(documentSnapshot.get("average_rating").toString());
                                //setting adapter for product dtails fragments viewpager tablayout
                                productDetailsViewpager.setAdapter(new produc_details_adapter(getSupportFragmentManager(), productDetailsTablayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModelList));
                                if (curruser != null) {
                                    if (DBqueries.myrating.size() == 0) {
                                        DBqueries.loadRatingList(productDetailsActivity.this);
                                    }
                                    if (DBqueries.Wishlist.size() == 0) {
                                        DBqueries.loadWishlist(productDetailsActivity.this, lodingDialog, false);
                                    }
                                    if (DBqueries.rewardmodelList.size() == 0) {
                                        DBqueries.loadRewards(productDetailsActivity.this, lodingDialog, false);
                                    }
                                    if (DBqueries.cartlist.size() != 0 && DBqueries.Wishlist.size() != 0 && DBqueries.rewardmodelList.size() != 0) {
                                        lodingDialog.dismiss();
                                    }

                                } else {
                                    lodingDialog.dismiss();
                                }
                                if (DBqueries.cartlist.contains(prodId)) {
                                    ALREADYADDEDTOCART = true;
                                } else {
                                    ALREADYADDEDTOCART = false;
                                }
                                if (DBqueries.Wishlist.contains(prodId)) {
                                    ALREADYADDEDTOWISHLIST = true;
                                    addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent));

                                } else {
                                    addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                    ALREADYADDEDTOWISHLIST = false;
                                }
                                if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {
                                    instock = true;
                                    buynow.setVisibility(View.VISIBLE);
   //add to cart button
                                    addtocartbutton.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v) {
                                            if (curruser == null) {
                                                signinDialog.show();
                                            } else {
                                                if (!runningcartquery) {
                                                    runningcartquery = true;
                                                    if (ALREADYADDEDTOCART) {
                                                        runningcartquery = false;
                                                        Toast.makeText(productDetailsActivity.this, "Already added to cart", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Map<String, Object> productId = new HashMap<>();
                                                        productId.put("product_ID_" + String.valueOf(DBqueries.cartlist.size()), prodId);
                                                        productId.put("list_size", (long) DBqueries.cartlist.size() + 1);

                                                        firebaseFirestore.collection("Users").document(curruser.getUid()).collection("USER_DATA").document("MY_CART")
                                                                .update(productId).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    int index = 0;
                                                                    if (DBqueries.cartlist.size() >= 2) {
                                                                        index = DBqueries.cartlist.size() - 2;
                                                                    }
                                                                    //if(DBqueries.cartItemModelList.size!=0){
                                                                    cartItemModelLiist.add(index, new cart_item_model(cart_item_model.CART_ITEM,
                                                                            prodId,
                                                                            documentSnapshot.get("product_image_1").toString(),
                                                                            (long) documentSnapshot.get("free_coupons"),
                                                                            (long) 1,
                                                                            (long) documentSnapshot.get("offers_applied"),
                                                                            (long) 0,
                                                                            documentSnapshot.get("product_title").toString(),
                                                                            documentSnapshot.get("product_price").toString(),
                                                                            documentSnapshot.get("cutted_price").toString(),
                                                                            instock,
                                                                            (long) documentSnapshot.get("max_quantity"),
                                                                            (long) documentSnapshot.get("stock_quantity")));
                                                                    //}
                                                                    if (DBqueries.cartlist.size() == 1) {
                                                                        cartItemModelLiist.add(new cart_item_model(cart_item_model.TOTAL_AMOUNT));
                                                                    }
                                                                    if (DBqueries.cartlist.size() == 0) {
                                                                        cartItemModelLiist.clear();
                                                                    }

                                                                    if (myCartFragment.cart_adapter != null)
                                                                        myCartFragment.cart_adapter.notifyDataSetChanged();
                                                                    // myCartFragment.cart_adapter.notifyDataSetChanged();
                                                                    ALREADYADDEDTOCART = true;
                                                                    DBqueries.cartlist.add(prodId);
                                                                    Toast.makeText(productDetailsActivity.this, "Added to cart successfully!", Toast.LENGTH_SHORT).show();
                                                                    invalidateOptionsMenu();
                                                                } else {
                                                                    String error = task.getException().getMessage();
                                                                    Toast.makeText(productDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                    ALREADYADDEDTOCART = false;
                                                                }
                                                                runningcartquery = false;
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    buynow.setVisibility(View.GONE);
                                    instock = false;
                                    ImageView outofs = (ImageView) addtocartbutton.getChildAt(1);
                                    outofs.setVisibility(View.GONE);
                                    TextView outofstock = (TextView) addtocartbutton.getChildAt(0);
                                    outofstock.setText("Out of Stock");
                                    outofstock.setTextColor(getResources().getColor(R.color.colorAccent));
                                    outofstock.setCompoundDrawables(null, null, null, null);
                                }
                            } else {
                                String e = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    if (myratedids.contains(prodId)) {
                        int index = myratedids.indexOf(prodId);
                        initialrating = Integer.parseInt(String.valueOf(myrating.get(index))) - 1;
                        setRating(initialrating);
                    }
                } else {
                    lodingDialog.dismiss();
                    String e = task.getException().getMessage();
                    Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        productDetailsViewpager = (ViewPager) findViewById(R.id.product_details_viewpager);


        //////////////// coupon red diolog setup

        ////////////////////dialog setup


        couponredeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pricedialog.show();
            }
        });


        productDetailsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));
        productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        ///////////rating layout
        rateNowContainer = (LinearLayout) findViewById(R.id.rate_now_container);
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    if (curruser == null) {
                        signinDialog.show();
                    } else {
                        if (starPosition != initialrating) {
                            if (!runningratingquery) {
                                runningratingquery = true;
                                setRating(starPosition);
                                Map<String, Object> updateRating = new HashMap<>();
                                TextView oldrating = (TextView) ratingsnocontainer.getChildAt(5 - initialrating - 1);
                                TextView frating = (TextView) ratingsnocontainer.getChildAt(5 - starPosition - 1);

                                /////////////updating database PRODUCTS
                                if (myratedids.contains(prodId)) {
                                    updateRating.put(initialrating + 1 + "_star", Long.parseLong(oldrating.getText().toString()) - 1);
                                    updateRating.put(starPosition + 1 + "_star", Long.parseLong(frating.getText().toString()) + 1);
                                    updateRating.put("average_rating", calculateaveragerating((long) starPosition - initialrating, true));
                                } else {

                                    updateRating.put((starPosition + 1) + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                    updateRating.put("average_rating", calculateaveragerating((long) starPosition + 1, false));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);

                                }
                                firebaseFirestore.collection("PRODUCTS").document(prodId).update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Map<String, Object> myrating = new HashMap<>();
                                            ///////////////updating database USERS->MY RATINGS
                                            if (myratedids.contains(prodId)) {
                                                myrating.put("rating_" + myratedids.indexOf(prodId), (long) starPosition + 1);
                                            } else {
                                                myrating.put("list_size", (long) DBqueries.myratedids.size() + 1);
                                                myrating.put("product_ID_" + DBqueries.myratedids.size(), prodId);
                                                myrating.put("rating_" + DBqueries.myrating.size(), (long) starPosition + 1);
                                            }

                                            firebaseFirestore.collection("Users").document(curruser.getUid()).
                                                    collection("USER_DATA").document("MY_RATINGS").update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        /////////////updating content view of product id
                                                        if (myratedids.contains(prodId)) {
                                                            DBqueries.myrating.set(myratedids.indexOf(prodId), (long) starPosition + 1);
                                                            TextView oldrating = (TextView) ratingsnocontainer.getChildAt(5 - initialrating - 1);
                                                            TextView frating = (TextView) ratingsnocontainer.getChildAt(5 - starPosition - 1);
                                                            frating.setText(String.valueOf(Integer.parseInt(frating.getText().toString()) + 1));
                                                            oldrating.setText(String.valueOf(Integer.parseInt(oldrating.getText().toString()) - 1));
                                                        } else {
                                                            myratedids.add(prodId);
                                                            DBqueries.myrating.add((long) starPosition + 1);
                                                            totalRatings.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + " ratings)");
                                                            totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));
                                                            total_rating_miniview.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + " ratings)");
                                                            TextView rating = (TextView) ratingsnocontainer.getChildAt(5 - starPosition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));
                                                            Toast.makeText(productDetailsActivity.this, "thank you for rating", Toast.LENGTH_SHORT).show();
                                                        }
                                                        for (int x = 0; x < 5; x++) {
                                                            TextView ratingfigures = (TextView) ratingsnocontainer.getChildAt(x);
                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressbarContainer.getChildAt(x);
                                                            int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                            progressBar.setMax(maxProgress);
                                                            progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));
                                                        }
                                                        initialrating = starPosition;
                                                        avg_rating_miniview.setText(calculateaveragerating(0, true));
                                                        averageRating.setText(calculateaveragerating(0, true));
                                                        if (DBqueries.Wishlist.contains(prodId) && wishListModelLiist.size() != 0) {
                                                            int index = DBqueries.Wishlist.indexOf(prodId);
                                                            wishListModelLiist.get(index).setRating(averageRating.getText().toString());
                                                            wishListModelLiist.get(index).setTotalRatings(Long.parseLong(totalRatingsFigure.getText().toString()));
                                                        }

                                                    } else {
                                                        setRating(initialrating);
                                                        String e = task.getException().getMessage();
                                                        Toast.makeText(productDetailsActivity.this, e, Toast.LENGTH_SHORT).show();
                                                    }
                                                    runningratingquery = false;
                                                }
                                            });
                                        } else {
                                            runningratingquery = false;
                                            setRating(initialrating);
                                            String e = task.getException().getMessage();
                                            Toast.makeText(productDetailsActivity.this, e, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addToWishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curruser == null) {
                    signinDialog.show();
                } else {
                    // addToWishListButton.setEnabled(false);
                    if (!runningwishlistquery) {
                        runningwishlistquery = true;
                        if (ALREADYADDEDTOWISHLIST == true) {
                            int index = DBqueries.Wishlist.indexOf(prodId);
                            DBqueries.removefromwishlist(index, productDetailsActivity.this);
                            addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                        } else {
                            Map<String, Object> productId = new HashMap<>();
                            productId.put("product_ID_" + String.valueOf(DBqueries.Wishlist.size()), prodId);
                            productId.put("list_size", (long) DBqueries.Wishlist.size() + 1);
                            //  Toast.makeText(productDetailsActivity.this, prodId, Toast.LENGTH_LONG).show();
                            firebaseFirestore.collection("Users").document(curruser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                    .update(productId).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        wishListModelLiist.add(new wishList_model(prodId, documentSnapshot.get("product_image_1").toString(),
                                                documentSnapshot.get("product_title").toString(),
                                                (long) documentSnapshot.get("free_coupons"),
                                                documentSnapshot.get("average_rating").toString(),
                                                (long) documentSnapshot.get("total_ratings"),
                                                documentSnapshot.get("product_price").toString(),
                                                documentSnapshot.get("cutted_price").toString(),
                                                (boolean) documentSnapshot.get("COD"),
                                                instock
                                        ));
                                        ALREADYADDEDTOWISHLIST = true;
                                        addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent));
                                        DBqueries.Wishlist.add(prodId);
                                        Toast.makeText(productDetailsActivity.this, "Added to wishlist successfully!", Toast.LENGTH_SHORT).show();

                                    }

                                    //  addToWishListButton.setEnabled(true);


                                    else {
                                        addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                        String error = task.getException().getMessage();
                                        Toast.makeText(productDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    runningwishlistquery = false;
                                }
                            });
                        }
                    }
                }
            }
        });

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curruser == null) {
                    signinDialog.show();
                } else {
                    DeliveryActivity.fromcart = false;
                    productDetailsActivity = productDetailsActivity.this;
                    lodingDialog.show();
                    DeliveryActivity.cartitemmodellist = new ArrayList<>();
                    DeliveryActivity.cartitemmodellist.add(new cart_item_model(cart_item_model.CART_ITEM,
                            prodId,
                            documentSnapshot.get("product_image_1").toString(),
                            (long) documentSnapshot.get("free_coupons"),
                            (long) 1,
                            (long) documentSnapshot.get("offers_applied"),
                            (long) 0,
                            documentSnapshot.get("product_title").toString(),
                            documentSnapshot.get("product_price").toString(),
                            documentSnapshot.get("cutted_price").toString(),
                            instock,
                            (long) documentSnapshot.get("max_quantity"),
                            (long) documentSnapshot.get("stock_quantity")));
                    DeliveryActivity.cartitemmodellist.add(new cart_item_model(cart_item_model.TOTAL_AMOUNT));

                    if (DBqueries.addressesModelList.size() == 0) {
                        DBqueries.loadAdresses(productDetailsActivity.this, lodingDialog);
                    } else {
                        Intent deliveryintent = new Intent(productDetailsActivity.this, DeliveryActivity.class);
                        startActivity(deliveryintent);
                        lodingDialog.dismiss();

                    }
                }
            }
        });
        signinDialog = new Dialog(productDetailsActivity.this);
        signinDialog.setContentView(R.layout.signin_dialog);
        signinDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        signinDialog.setCancelable(true);
        Button signinbutton = signinDialog.findViewById(R.id.signinbutton);
        Button signupbutton = signinDialog.findViewById(R.id.signupbutton);
        final Intent i = new Intent(productDetailsActivity.this, Register.class);
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin.disablebutton = true;
                signup.disableclosebutton = true;
                signinDialog.dismiss();
                setSignupFragment = false;
                startActivity(i);
            }
        });
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup.disableclosebutton = true;
                signin.disablebutton = true;
                signinDialog.dismiss();
                setSignupFragment = true;
                startActivity(i);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        curruser = FirebaseAuth.getInstance().getCurrentUser();
        if (curruser == null) {
            couponredeemll.setVisibility(View.GONE);
        } else {
            couponredeemll.setVisibility(View.VISIBLE);
        }
        if (curruser != null) {
            if (DBqueries.myrating.size() == 0) {
                DBqueries.loadRatingList(productDetailsActivity.this);
            }
            if (DBqueries.Wishlist.size() == 0) {
                DBqueries.loadWishlist(productDetailsActivity.this, lodingDialog, false);
            }
            if (DBqueries.rewardmodelList.size() == 0) {
                DBqueries.loadRewards(productDetailsActivity.this, lodingDialog, false);
            }
            if (DBqueries.cartlist.size() != 0 && DBqueries.Wishlist.size() != 0 && DBqueries.rewardmodelList.size() != 0) {
                lodingDialog.dismiss();
            }

        } else {
            lodingDialog.dismiss();
        }

        if (myratedids.contains(prodId)) {
            int index = myratedids.indexOf(prodId);
            initialrating = Integer.parseInt(String.valueOf(myrating.get(index))) - 1;
            setRating(initialrating);
        }
        if (DBqueries.cartlist.contains(prodId)) {
            ALREADYADDEDTOCART = true;
        } else {
            ALREADYADDEDTOCART = false;
        }

        if (DBqueries.Wishlist.contains(prodId)) {
            ALREADYADDEDTOWISHLIST = true;
            addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent));

        } else {
            addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
            ALREADYADDEDTOWISHLIST = false;
        }
        invalidateOptionsMenu();
    }

    private void showdialogrecyclerview() {
        if (coupons_rv.getVisibility() == View.GONE) {
            coupons_rv.setVisibility(View.VISIBLE);
            selectedCoupon.setVisibility(View.GONE);
        } else {
            coupons_rv.setVisibility(View.GONE);
            selectedCoupon.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setRating(int starPosition) {


        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView starButton = (ImageView) rateNowContainer.getChildAt(x);
            starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#BDBABA")));
            if (x <= starPosition) {
                starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFBB00")));
            }

        }
    }

    private String calculateaveragerating(long currentUserRating, boolean update) {
        Double totalstars = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView ratingno = (TextView) ratingsnocontainer.getChildAt(5 - x);
            totalstars = totalstars + (Long.parseLong(ratingno.getText().toString()) * x);
        }
        totalstars = totalstars + currentUserRating;
        if (update) {
            return String.valueOf(totalstars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0, 3);
        } else {
            return String.valueOf(totalstars / (Long.parseLong(totalRatingsFigure.getText().toString()) + 1)).substring(0, 3);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);


        cartitem = menu.findItem(R.id.cart);

        cartitem.setActionView(R.layout.badge_layout);
        ImageView badgeicon = cartitem.getActionView().findViewById(R.id.badge_icon);
        badgecount = cartitem.getActionView().findViewById(R.id.badge_count);
        badgeicon.setImageResource(R.drawable.buying);

        if (curruser != null) {
            if (DBqueries.cartlist.size() == 0) {
                loadcartlist(productDetailsActivity.this, lodingDialog, false, badgecount, new TextView(productDetailsActivity.this));
                //     badgecount.setVisibility(View.INVISIBLE);
            } else {
                badgecount.setVisibility(View.VISIBLE);

                if (DBqueries.cartlist.size() < 99)
                    badgecount.setText(String.valueOf(DBqueries.cartlist.size()));
                else
                    badgecount.setText("99");
            }
        }


        cartitem.getActionView().
                setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        if (curruser != null) {
                            Intent carti = new Intent(productDetailsActivity.this, MainActivity2.class);
                            showcart = true;
                            startActivity(carti);
                        } else {
                            signinDialog.show();
                        }

                    }
                });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.searchicon) {
            return true;
        } else if (id == android.R.id.home) {
            productDetailsActivity = null;
            finish();
            return true;
        } else if (id == R.id.cart) {
            if (curruser != null) {
                Intent carti = new Intent(productDetailsActivity.this, MainActivity2.class);
                showcart = true;
                startActivity(carti);
            } else {
                signinDialog.show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        productDetailsActivity = null;
    }
}