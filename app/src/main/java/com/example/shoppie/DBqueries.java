package com.example.shoppie;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.shoppie.productDetailsActivity.initialrating;

public class DBqueries {
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static List<List<home_page_model>> lists = new ArrayList<>();

    public static List<String> loadedCategoriesNames = new ArrayList<>();
    public static List<categorymodel> categorymodelLists = new ArrayList<categorymodel>();

    public static List<String> cartlist = new ArrayList<>();
    public static List<cart_item_model> cartItemModelLiist = new ArrayList<>();

    public static List<String> Wishlist = new ArrayList<>();
    public static List<wishList_model> wishListModelLiist = new ArrayList<>();

    public static List<String> myratedids = new ArrayList<>();
    public static List<Long> myrating = new ArrayList<>();

    public static List<addresses_model> addressesModelList = new ArrayList<>();
    public static int selectedaddress=-1;

    public static List<reward_model> rewardmodelList=new ArrayList<>();


    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context) {
        categorymodelLists.clear();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        categorymodelLists.add(new categorymodel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                    }
                    categoryadapter categoryadapter1 = new categoryadapter(categorymodelLists);
                    categoryRecyclerView.setAdapter(categoryadapter1);
                    categoryadapter1.notifyDataSetChanged();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static void loadFragmentData(final RecyclerView homeRecyclerView, final Context context, final int index, String categoryName) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("CATEGORIES").document(categoryName.toUpperCase()).collection("TOP_DEALS").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                 //   List<slider_model> sliderModelList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if ((long) documentSnapshot.get("view_type") == 0) {
//                            long noofbanners = (long) documentSnapshot.get("no_of_banners");
//                            for (long x = 1; x <= noofbanners; x++) {
//
//                                sliderModelList.add(new slider_model(documentSnapshot.get("banner_" + x).toString(), documentSnapshot.get("banner_" + x + "_background").toString()));


                            }
                       //     lists.get(index).add(new home_page_model(0, sliderModelList));
                        else if ((long) documentSnapshot.get("view_type") == 1) {
                            //   lists.get(index).add(new home_page_model(1,documentSnapshot.get("strip_ad_banner").toString(),documentSnapshot.get("background").toString()));
                            lists.get(index).add(new home_page_model(1, documentSnapshot.get("strip_ad_banner").toString(), "#ffffff"));

                        } else if ((long) documentSnapshot.get("view_type") == 2) {
                            List<horizontal_product_model> horizontalProductModelList = new ArrayList<>();
                            List<wishList_model> viewallproductlist = new ArrayList<>();
                            long noofproducts = (long) documentSnapshot.get("no_of_products");
                            for (long x = 1; x <= noofproducts; x++) {
                                horizontalProductModelList.add(new horizontal_product_model(documentSnapshot.get("product_ID_" + x).toString(),
                                        documentSnapshot.get("product_image_" + x).toString(),
                                        documentSnapshot.get("product_title_" + x).toString(),
                                        documentSnapshot.get("product_subtitle_" + x).toString(),
                                        documentSnapshot.get("product_price_" + x).toString()));

                                viewallproductlist.add(new wishList_model(documentSnapshot.get("product_ID_" + x).toString(),
                                        documentSnapshot.get("product_image_" + x).toString(),
                                        documentSnapshot.get("product_full_title_" + x).toString(),
                                        (long) documentSnapshot.get("free_coupons_" + x),
                                        documentSnapshot.get("average_rating_" + x).toString(),
                                        (long) documentSnapshot.get("total_ratings_" + x),
                                        documentSnapshot.get("product_price_" + x).toString(),
                                        documentSnapshot.get("cutted_price_" + x).toString(),
                                        (boolean) documentSnapshot.get("COD_" + x),
                                        (boolean) documentSnapshot.get("in_stock_" + x)
                                ));

                            }
                            lists.get(index).add(new home_page_model(2, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), horizontalProductModelList, viewallproductlist));


                        } else if ((long) documentSnapshot.get("view_type") == 3) {
                            //  Toast.makeText(context,documentSnapshot.get("product_ID_1").toString(),Toast.LENGTH_LONG).show();

                            List<horizontal_product_model> gridProductModelList = new ArrayList<>();
                            long noofproducts = (long) documentSnapshot.get("no_of_products");
                            for (long x = 1; x <= noofproducts; x++) {
                                gridProductModelList.add(new horizontal_product_model(documentSnapshot.get("product_ID_" + x).toString(),
                                        documentSnapshot.get("product_image_" + x).toString(),
                                        documentSnapshot.get("product_title_" + x).toString(),
                                        documentSnapshot.get("product_subtitle_" + x).toString(),
                                        documentSnapshot.get("product_price_" + x).toString()));


                            }
                            lists.get(index).add(new home_page_model(3, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), gridProductModelList));

                        }

                    }
                    home_page_adapter homePageAdapter = new home_page_adapter(lists.get(index));
                    homeRecyclerView.setAdapter(homePageAdapter);
                    homePageAdapter.notifyDataSetChanged();
                    homesFragment.swipeRefreshLayout.setRefreshing(false);
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public static void loadWishlist(final Context context, final Dialog d, final boolean loadProductData) {
        Wishlist.clear();
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                        Wishlist.add(task.getResult().get("product_ID_" + x).toString());
                        if (DBqueries.Wishlist.contains(productDetailsActivity.prodId)) {
                            productDetailsActivity.ALREADYADDEDTOWISHLIST = true;
                            if (productDetailsActivity.addToWishListButton != null) {
                                productDetailsActivity.addToWishListButton.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorAccent));
                            }
                        } else {
                            if (productDetailsActivity.addToWishListButton != null) {
                                productDetailsActivity.addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                            }
                            productDetailsActivity.ALREADYADDEDTOWISHLIST = false;
                        }
                        if (loadProductData) {
                            wishListModelLiist.clear();
                            final String productid = task.getResult().get("product_ID_"+x).toString();
                            final long finalX = x;
                            final long finalX1 = x;
                            final long finalX2 = x;
                            firebaseFirestore.collection("PRODUCTS").document(productid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> tsk) {
                                    if (tsk.isSuccessful()) {
                                        final DocumentSnapshot documentSnapshot=task.getResult();
                                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productid).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().getDocuments().size() <  (long)documentSnapshot.get("stock_quantity")) {
                                                        wishListModelLiist.add(new wishList_model(
                                                                productid,
                                                                documentSnapshot.get("product_image_1").toString(),
                                                                documentSnapshot.get("product_title").toString(),
                                                                (long)documentSnapshot.get("free_coupons"),
                                                                documentSnapshot.get("average_rating").toString(),
                                                                (long)documentSnapshot.get("total_ratings"),
                                                                documentSnapshot.get("product_price").toString(),
                                                                documentSnapshot.get("cutted_price").toString(),
                                                                (boolean)documentSnapshot.get("COD"),
                                                                true


                                                                )
                                                        );
                                                    }
                                                    else
                                                    {   wishListModelLiist.add(new wishList_model(
                                                                    productid,
                                                                    documentSnapshot.get("product_image_1").toString(),
                                                            documentSnapshot.get("product_title").toString(),
                                                                    (long)documentSnapshot.get("free_coupons"),
                                                            documentSnapshot.get("average_rating").toString(),
                                                                    (long)documentSnapshot.get("total_ratings"),
                                                            documentSnapshot.get("product_price").toString(),
                                                            documentSnapshot.get("cutted_price").toString(),
                                                                    (boolean)documentSnapshot.get("COD"),
                                                                    false


                                                            )
                                                    );
                                                    }
                                                    myWishlistFragment.wishlistadapter.notifyDataSetChanged();
                                                } else {
                                                    String e = task.getException().getMessage();
                                                    Toast.makeText(context, e, Toast.LENGTH_SHORT).show();
                                                }}}
                                            );


                                        //  Toast.makeText(context,tsk.getResult().get("product_title").toString(),Toast.LENGTH_SHORT).show();


                                    } else {
                                        productDetailsActivity.addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                        String
                                                e = tsk.getException().getMessage();
                                        Toast.makeText(context, e, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                } else {
                    String e = task.getException().getMessage();
                    Toast.makeText(context, e, Toast.LENGTH_SHORT).show();

                }
                d.dismiss();
            }
        });
    }

    public static void removefromwishlist(final int i, final Context context) {
        final String removedproductId = Wishlist.get(i);
        Wishlist.remove(i);
        Map<String, Object> updateWishlist = new HashMap<>();
        for (int x = 0; x < Wishlist.size(); x++) {
            updateWishlist.put("product_ID_" + x, Wishlist.get(x));
        }
        updateWishlist.put("list_size", (long) Wishlist.size());
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST").set(updateWishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    if (wishListModelLiist.size() != 0) {
                        wishListModelLiist.remove(i);
                    }
                    productDetailsActivity.ALREADYADDEDTOWISHLIST = false;
                    Toast.makeText(context, "removed sucessfully", Toast.LENGTH_SHORT).show();
                    if (myWishlistFragment.wishlistadapter != null)
                        myWishlistFragment.wishlistadapter.notifyDataSetChanged();
                } else {
                    if (productDetailsActivity.addToWishListButton != null)
                        productDetailsActivity.addToWishListButton.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorAccent));
                    Wishlist.add(i, removedproductId);
                    String e = task.getException().getMessage();
                    Toast.makeText(context, e, Toast.LENGTH_SHORT).show();
                }

                productDetailsActivity.runningwishlistquery = false;
            }

        });
    }

    public static void loadcartlist(final Context context, final Dialog d, final boolean loadProductData, final TextView badgecount,final TextView Carttotalamount) {
        cartlist.clear();
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                        cartlist.add(task.getResult().get("product_ID_" + x).toString());
                        if (DBqueries.cartlist.contains(productDetailsActivity.prodId)) {
                            productDetailsActivity.ALREADYADDEDTOCART = true;
                        } else {
                            productDetailsActivity.ALREADYADDEDTOCART = false;
                        }
                        if (loadProductData) {
                            cartItemModelLiist.clear();
                            final String productid = task.getResult().get("product_ID_" + x).toString();
                            final long finalX = x;
                            final long finalX1 = x;
                            firebaseFirestore.collection("PRODUCTS").document(productid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        final DocumentSnapshot documentSnapshot=task.getResult();
                                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productid).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int index = 0;
                                                    if (cartlist.size() >= 2) {
                                                        index = cartlist.size() - 2;

                                                    }
                                                    if (task.getResult().getDocuments().size() <  (long)documentSnapshot.get("stock_quantity")) {
                                                        cartItemModelLiist.add(index,new cart_item_model(cart_item_model.CART_ITEM,
                                                                productid,
                                                                documentSnapshot.get("product_image_1").toString(),
                                                                (long) documentSnapshot.get("free_coupons"),
                                                                (long) 1,
                                                                (long) documentSnapshot.get("offers_applied"),
                                                                (long) 0,
                                                                documentSnapshot.get("product_title").toString(),
                                                                documentSnapshot.get("product_price").toString(),
                                                                documentSnapshot.get("cutted_price").toString(),
                                                               true,(long)documentSnapshot.get("max_quantity")
                                                                ,(long)documentSnapshot.get("stock_quantity")));
                                                    }
                                                    else
                                                    {   cartItemModelLiist.add(index,new cart_item_model(cart_item_model.CART_ITEM,
                                                            productid,
                                                            documentSnapshot.get("product_image_1").toString(),
                                                            (long) documentSnapshot.get("free_coupons"),
                                                            (long) 1,
                                                            (long) documentSnapshot.get("offers_applied"),
                                                            (long) 0,
                                                            documentSnapshot.get("product_title").toString(),
                                                            documentSnapshot.get("product_price").toString(),
                                                            documentSnapshot.get("cutted_price").toString(),
                                                            false,(long)documentSnapshot.get("max_quantity")
                                                            ,(long)documentSnapshot.get("stock_quantity")));
                                                    }
                                                    if (cartlist.size() == 1) {
                                                        cartItemModelLiist.add(new cart_item_model(cart_item_model.TOTAL_AMOUNT));
                                                        LinearLayout p = (LinearLayout) Carttotalamount.getParent().getParent();
                                                        p.setVisibility(View.VISIBLE);

                                                    }
                                                    if (cartlist.size() == 0) {
                                                        cartItemModelLiist.clear();
                                                    }


                                                    myCartFragment.cart_adapter.notifyDataSetChanged();

                                                } else {
                                                    String e = task.getException().getMessage();
                                                    Toast.makeText(context, e, Toast.LENGTH_SHORT).show();
                                                }}}
                                        );



                                    } else {
                                        //   productDetailsActivity.addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                        String e = task.getException().getMessage();
                                        Toast.makeText(context, e, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                    if (cartlist.size() != 0) {
                        badgecount.setVisibility(View.VISIBLE);
                    } else {
                        badgecount.setVisibility(View.INVISIBLE);
                    }

                    if (DBqueries.cartlist.size() < 99)
                        badgecount.setText(String.valueOf(DBqueries.cartlist.size()));
                    else
                        badgecount.setText("99");

                } else {
                    String e = task.getException().getMessage();
                    Toast.makeText(context, e, Toast.LENGTH_SHORT).show();

                }

                d.dismiss();
            }
        });
    }

    public static void removefromCart(final int i, final Context context,final TextView cartTotalAmount) {
        final String removedproductId = cartlist.get(i);
        cartlist.remove(i);
        Map<String, Object> updatecartlist = new HashMap<>();
        for (int x = 0; x < cartlist.size(); x++) {
            updatecartlist.put("product_ID_" + x, cartlist.get(x));
        }
        updatecartlist.put("list_size", (long) cartlist.size());
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART").set(updatecartlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    if (cartItemModelLiist.size() != 0) {
                        cartItemModelLiist.remove(i);
                        //  myCartFragment.cart_adapter.notifyDataSetChanged();
                    }
                    if (cartlist.size() == 0){
                        LinearLayout p = (LinearLayout) cartTotalAmount.getParent().getParent();
                        p.setVisibility(View.GONE);
                        cartItemModelLiist.clear();
                    }

                    Toast.makeText(context, "removed sucessfully", Toast.LENGTH_SHORT).show();
                    productDetailsActivity.ALREADYADDEDTOCART = false;
                    if (myCartFragment.cart_adapter != null)
                        myCartFragment.cart_adapter.notifyDataSetChanged();
                } else {
                    cartlist.add(i, removedproductId);
                    String e = task.getException().getMessage();
                    Toast.makeText(context, e, Toast.LENGTH_SHORT).show();
                }

                productDetailsActivity.runningcartquery = false;
            }

        });
    }

    public static void clearData() {
        categorymodelLists.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        Wishlist.clear();
        wishListModelLiist.clear();
        cartlist.clear();
        cartItemModelLiist.clear();
        myratedids.clear();
        myrating.clear();
        addressesModelList.clear();
        rewardmodelList.clear();
    }

    public static void loadRewards(final Context context, final Dialog dialog , final boolean onrewardFragment)
    {
        rewardmodelList.clear();
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    final Date lastseen=task.getResult().getDate("Last Seen");
                    firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_REWARDS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                                {
                                    if(documentSnapshot.get("type").toString().equals("Discount") &&  lastseen.before(documentSnapshot.getDate("validity")))
                                    {
                                        rewardmodelList.add(new reward_model(documentSnapshot.getId(),documentSnapshot.get("type").toString(),
                                                documentSnapshot.get("lower_limit").toString(),
                                                documentSnapshot.get("upper_limit").toString(),
                                                documentSnapshot.get("percentage").toString(),
                                                (Date) documentSnapshot.get("validity"),
                                                documentSnapshot.get("body").toString(),
                                                (boolean)documentSnapshot.get("alreadyUsed")
                                        ));
                                    }else  if(documentSnapshot.get("type").toString().equals("Flat Rs.* OFF") &&  lastseen.before(documentSnapshot.getDate("validity")))
                                    {  rewardmodelList.add(new reward_model(documentSnapshot.getId(),documentSnapshot.get("type").toString(),
                                            documentSnapshot.get("lower_limit").toString(),
                                            documentSnapshot.get("upper_limit").toString(),
                                            documentSnapshot.get("amount").toString(),
                                            (Date) documentSnapshot.get("validity"),
                                            documentSnapshot.get("body").toString(),
                                            (boolean)documentSnapshot.get("alreadyUsed")
                                    ));}
                                    if(onrewardFragment){
                                        myRewardsFragment.myrewardsAdapter.notifyDataSetChanged();
                                    }

                                }
                            }
                            else
                            {

                                String e = task.getException().getMessage();
                                Toast.makeText(context, e, Toast.LENGTH_SHORT).show();}
                            dialog.dismiss();
                        }
                    });
                }
                else
                {

                    String e = task.getException().getMessage();
                    Toast.makeText(context, e, Toast.LENGTH_SHORT).show();}
                dialog.dismiss();
            }
        });

    }

    public static void loadAdresses(final Context context,final Dialog d)
    {
        addressesModelList.clear();
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    Intent deliveryi;

                    if((long)task.getResult().get("list_size")==0)
                    {
                        deliveryi=new Intent(context,addAdressActivity.class);
                        deliveryi.putExtra("INTENT","delivery");
                        context.startActivity(deliveryi);
                    }
                    else
                    {
                        for(long x=1;x<(long)task.getResult().get("list_size")+1;x++)
                        {
                            addressesModelList.add(new addresses_model(task.getResult().get("fullname_"+x).toString(),
                                    task.getResult().get("address_"+x).toString(),
                                    task.getResult().get("pincode_"+x).toString(),
                                    (boolean)task.getResult().get("SELECTED_"+x),
                                    task.getResult().get("mobile_no_"+x).toString()));
                            if((boolean)task.getResult().get("SELECTED_"+x))
                            {
                                selectedaddress= Integer.parseInt(String.valueOf(x-1));
                            }
                        }
                        deliveryi=new Intent(context,DeliveryActivity.class);
                        context.startActivity(deliveryi);
                    }
                }
                else
                { String e = task.getException().getMessage();
                    Toast.makeText(context, e, Toast.LENGTH_SHORT).show();
                }
                d.dismiss();
            }
        });

        }


    public static void loadRatingList(final Context context) {
        if (!productDetailsActivity.runningratingquery) {
            productDetailsActivity.runningratingquery = true;
            myratedids.clear();
            myrating.clear();
            firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                            myratedids.add(task.getResult().get("product_ID_" + x).toString());
                            myrating.add((long) task.getResult().get("rating_" + x));
                            if (task.getResult().get("product_ID_" + x).toString().equals(productDetailsActivity.prodId)) {
                                productDetailsActivity.initialrating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if (productDetailsActivity.rateNowContainer != null)
                                    productDetailsActivity.setRating(initialrating);
                            }
                        }
                    } else {
                        String e = task.getException().getMessage();
                        Toast.makeText(context, e, Toast.LENGTH_SHORT).show();
                    }
                    productDetailsActivity.runningratingquery = false;
                }

            });
        }
    }
}
