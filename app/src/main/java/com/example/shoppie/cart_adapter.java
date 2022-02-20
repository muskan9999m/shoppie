package com.example.shoppie;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class cart_adapter extends RecyclerView.Adapter {
    private static long a = 0;
    long availableqty = 0;
    private int lastpos = -1;

    boolean showdeltbutton;
    private TextView cartTotalAmount;
    List<cart_item_model> cartItemModelList;

    public cart_adapter(List<cart_item_model> cartItemModelList, TextView cartTotalAmount, boolean showdeltbutton) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showdeltbutton = showdeltbutton;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return cart_item_model.CART_ITEM;
            case 1:
                return cart_item_model.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case cart_item_model.CART_ITEM:
                View cartitemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new cartItemViewHolder(cartitemview);
            case cart_item_model.TOTAL_AMOUNT:
                View carttotalview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout, parent, false);
                return new cartTotalAmountViewHolder(carttotalview);
            default:
                return null;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        switch (cartItemModelList.get(position).getType()) {
            case cart_item_model.CART_ITEM:
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                long freecoupons = cartItemModelList.get(position).getFreeCoupons();
                String prodid = cartItemModelList.get(position).getProductID();
                long offersapplied = cartItemModelList.get(position).getOffersApplied();
                String productPrice = cartItemModelList.get(position).getProductPrcice();
                String cuttedprice = cartItemModelList.get(position).getCuttedPrice();
                boolean instok = cartItemModelList.get(position).isInstock();
                long proqty = cartItemModelList.get(position).getProductQuantity();
                long maxproqty = cartItemModelList.get(position).getMaxqty();
                List<String> qtyids = cartItemModelList.get(position).getQtyIDS();
                long stockqty = cartItemModelList.get(position).getStockqty();
                boolean qtyerror = cartItemModelList.get(position).isQtyerror();
                ((cartItemViewHolder) holder).setItemDetails(prodid, resource, title, freecoupons, productPrice, cuttedprice, offersapplied, position, instok, String.valueOf(proqty), maxproqty, qtyerror, qtyids, stockqty);
                break;
            case cart_item_model.TOTAL_AMOUNT:
                int totalitems = 0;
                int totalitemprice = 0;
                String deliveryprice;
                int totalAmoun;
                int savedAmount = 0;

                for (int x = 0; x < cartItemModelList.size(); x++) {
                    if (cartItemModelList.get(x).getType() == cart_item_model.CART_ITEM && cartItemModelList.get(x).isInstock()) {
                        int quantity=Integer.parseInt(String.valueOf(cartItemModelList.get(x).getProductQuantity()));
                        totalitems=totalitems+quantity;
                        if (TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCouponId())){
                        totalitemprice = Integer.parseInt(totalitemprice + cartItemModelList.get(x).getProductPrcice())*quantity;
                    }else
                    {
                        totalitemprice = Integer.parseInt(totalitemprice + cartItemModelList.get(x).getDiscountedpric())*quantity;

                    }
                        if (!TextUtils.isEmpty(cartItemModelList.get(x).getCuttedPrice()))
                        {
                            savedAmount=savedAmount+Integer.parseInt(cartItemModelList.get(x).getCuttedPrice())-Integer.parseInt(cartItemModelList.get(x).getProductPrcice())*quantity;
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCouponId()))
                            {
                                savedAmount=savedAmount+Integer.parseInt(cartItemModelList.get(x).getProductPrcice())-Integer.parseInt(cartItemModelList.get(x).getDiscountedpric())*quantity;
                            }
                        }
                        else
                        { if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCouponId()))
                        {
                            savedAmount=savedAmount+Integer.parseInt(cartItemModelList.get(x).getProductPrcice())-Integer.parseInt(cartItemModelList.get(x).getDiscountedpric())*quantity;
                        }}

                    }

                }
                if (totalitemprice > 500) {
                    deliveryprice = "free";
                    totalAmoun = totalitemprice;
                } else {
                    deliveryprice = "60";
                    totalAmoun = totalitemprice + 60;
                }

                 cartItemModelList.get(position).setTotalItems(totalitems);
                cartItemModelList.get(position).setTotalItemPrice(totalitemprice);
                cartItemModelList.get(position).setDeliveryPrice(deliveryprice);
                cartItemModelList.get(position).setTotalAmount(totalAmoun);
                cartItemModelList.get(position).setSavedAmount(savedAmount);
                ((cartTotalAmountViewHolder) holder).setTotalAmount(totalitems, totalitemprice, deliveryprice, totalAmoun, savedAmount);

                break;
            default:
                return;
        }

        if (lastpos < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastpos = position;
        }


    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class cartItemViewHolder extends RecyclerView.ViewHolder {
        private String prodoriginalvalue;
        private TextView coupontitle;
        private Button removecouponbtn;
        private Button applycouponbtn;
        private TextView couponexpirydate;
        private TextView discountedprice;
        private LinearLayout selectedCoupon;
        private TextView originalprice;
        private TextView couponbody;
        private RecyclerView coupons_rv;
        private ImageView productImage;
        private TextView freeCoupons;
        private TextView productQuantitytv;
        private ImageView productQuantityiv;
        private TextView offersApplied;
        private TextView couponsApplied;
        private ImageView freeCouponIcon;
        private TextView productTitle;
        private TextView productPrcice;
        private LinearLayout deleteitembutton;
        private TextView cuttedPrice;
        private LinearLayout couponredlayout;
        private LinearLayout prqtyll;
        private View dividercp;
        private LinearLayout applyorremcont;
        private TextView footertext;
        private Button redeemybtn;
        private TextView couponRedeemption_tv;

        public cartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            couponredlayout = itemView.findViewById(R.id.coupon_redemption_layout);
            redeemybtn = itemView.findViewById(R.id.couponredemptionbutton);
            couponRedeemption_tv = itemView.findViewById(R.id.tv_couponredemption);
            productImage = itemView.findViewById(R.id.prodictImage);
            productTitle = itemView.findViewById(R.id.product_title);
            deleteitembutton = itemView.findViewById(R.id.remove_item_button);
            freeCouponIcon = itemView.findViewById(R.id.freecouponicon);
            freeCoupons = itemView.findViewById(R.id.tv_freecoupon);
            productPrcice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            couponsApplied = itemView.findViewById(R.id.coupons_applied);
            productQuantitytv = itemView.findViewById(R.id.product_quantity_tv);
            productQuantityiv = itemView.findViewById(R.id.product_quantity_iv);
            prqtyll = itemView.findViewById(R.id.product_quantity_ll);
            dividercp = itemView.findViewById(R.id.divider4);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setItemDetails(final String prodid, String resource, String title, long freeCouponsno, final String prodPrice, String cutprice, long offersappliedno, final int position, boolean instok, final String quantity, final long maxprodquantity, boolean qtyerror, final List<String> qtyids, final long stockqty) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.place)).into(productImage);
            productTitle.setText(title);


            if (showdeltbutton) {
                deleteitembutton.setVisibility(View.VISIBLE);
            } else {
                deleteitembutton.setVisibility(View.GONE);
            }
            deleteitembutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {
                        for (reward_model rewardModel : DBqueries.rewardmodelList) {
                            if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                                rewardModel.setAlreadyused(false);
                            }
                        }
                        cartItemModelList.get(position).setSelectedCouponId(null);
                    }

                    if (!productDetailsActivity.runningcartquery) {
                        productDetailsActivity.runningcartquery = true;
                        DBqueries.removefromCart(position, itemView.getContext(), cartTotalAmount);
                    }
                }
            });
            /////////////redeem btn
            final Dialog pricedialog = new Dialog(itemView.getContext());

            pricedialog.setContentView(R.layout.couponredeemdialog);
            pricedialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pricedialog.setCancelable(false);

            ImageView togglerv = pricedialog.findViewById(R.id.togglerecyclerView);
            //  togglerv.setImageResource(R.drawable.heart);
            coupons_rv = (RecyclerView) pricedialog.findViewById(R.id.couponsrecyclerview1);
            selectedCoupon = pricedialog.findViewById(R.id.selectedcoupon);

            coupontitle = pricedialog.findViewById(R.id.couponTitle);
            couponexpirydate = pricedialog.findViewById(R.id.couponvalidity);
            couponbody = pricedialog.findViewById(R.id.couponbody);
            originalprice = pricedialog.findViewById(R.id.originalprice);
            discountedprice = pricedialog.findViewById(R.id.afterprice);
            removecouponbtn = pricedialog.findViewById(R.id.removebtn);
            applycouponbtn = pricedialog.findViewById(R.id.applybtn);
            footertext = pricedialog.findViewById(R.id.footertext);
            applyorremcont = pricedialog.findViewById(R.id.applyremovell);
            footertext.setVisibility(View.GONE);
            applyorremcont.setVisibility(View.VISIBLE);

            LinearLayoutManager lm = new LinearLayoutManager(itemView.getContext());
            lm.setOrientation(LinearLayoutManager.VERTICAL);
            coupons_rv.setLayoutManager(lm);
            prodoriginalvalue = prodPrice;
            originalprice.setText("Rs."+prodoriginalvalue+"/-");
            myrewards_adapter myrewardsAdapter = new myrewards_adapter(position, DBqueries.rewardmodelList, true, coupons_rv, selectedCoupon, prodoriginalvalue, coupontitle, couponexpirydate, couponbody,cartItemModelList);
            coupons_rv.setAdapter(myrewardsAdapter);
            myrewardsAdapter.notifyDataSetChanged();


            redeemybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (reward_model rewardModel : DBqueries.rewardmodelList) {
                        if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                            rewardModel.setAlreadyused(false);
                        }
                    }
                    pricedialog.show();

                }
            });

            ////////////////////////////////////
            if (instok) {
                if (freeCouponsno > 0) {
                    freeCouponIcon.setVisibility(View.VISIBLE);
                    freeCoupons.setText("free " + freeCouponsno + " coupons");
                } else
                    freeCouponIcon.setVisibility(View.INVISIBLE);
                productPrcice.setText("Rs." + prodPrice + "/-");
                couponredlayout.setVisibility(View.VISIBLE);

                //////same when applybutton onclik taki jab vo item recycle hoke dobara dikhe to updated price show ho
                if(!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {
                    for (reward_model rewardModel : DBqueries.rewardmodelList) {
                        if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                            couponredlayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_gradient_background));
                            couponRedeemption_tv.setText(rewardModel.getCouponbody());
                            redeemybtn.setText("Coupon");
                            couponbody.setText(rewardModel.getCouponbody());
                            if(rewardModel.getType().equals("Discount"))
                            {
                                coupontitle.setText(rewardModel.getType());
                            }
                            else
                            {
                                coupontitle.setText("Flat Rs."+rewardModel.getDiscoramount()+" OFF");
                            }
                            final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd MMMM YYYY");
                            couponexpirydate.setText("till "+simpleDateFormat.format(rewardModel.getTimestamp()));
                        }
                    }
                    discountedprice.setText("Rs."+cartItemModelList.get(position).getDiscountedpric()+"/-");
                    couponsApplied.setVisibility(View.VISIBLE);
                    productPrcice.setText("Rs."+cartItemModelList.get(position).getDiscountedpric()+"/-");
                    String offerDiscAmount = String.valueOf(Long.valueOf(prodPrice) - Long.valueOf(cartItemModelList.get(position).getDiscountedpric()));
                    couponsApplied.setText("Coupon Applied - Rs." + offerDiscAmount + "/-");
                }
                else
                {  couponsApplied.setVisibility(View.INVISIBLE);
                    couponredlayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                    couponRedeemption_tv.setText("Apply your coupon here.");
                    redeemybtn.setText("Redeem");}
                ////////////////////////////////////
                cuttedPrice.setText("Rs." + cutprice + "/-");
                dividercp.setVisibility(View.VISIBLE);

                productQuantitytv.setText("Qty: " + quantity);
                if (!showdeltbutton) {
                    if (qtyerror) {
                        productQuantitytv.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                        productQuantitytv.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorAccent)));
                    } else {
                        productQuantitytv.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                        productQuantitytv.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.black)));
                    }
                }
                prqtyll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog qtyd = new Dialog(itemView.getContext());
                        qtyd.setContentView(R.layout.quantity_dialog);
                        qtyd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        qtyd.setCancelable(false);
                        final EditText qtyno = qtyd.findViewById(R.id.quantityNo);
                        Button cancelb = qtyd.findViewById(R.id.cancelbutton);
                        Button okb = qtyd.findViewById(R.id.okbutton);
                        qtyno.setHint("MAX " + String.valueOf(maxprodquantity));
                        cancelb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                qtyd.dismiss();
                            }
                        });

                        okb.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if (!TextUtils.isEmpty(qtyno.getText())) {
                                                           if (Long.parseLong(qtyno.getText().toString()) <= maxprodquantity && Long.parseLong(qtyno.getText().toString()) != 0 && !qtyno.getText().equals("")) {
                                                               if (itemView.getContext() instanceof MainActivity2) {
                                                                   cartItemModelList.get(position).setProductQuantity(Long.parseLong(qtyno.getText().toString()));

                                                               } else {
                                                                   if (DeliveryActivity.fromcart) {
                                                                       cartItemModelList.get(position).setProductQuantity(Long.parseLong(qtyno.getText().toString()));
                                                                   } else {
                                                                       DeliveryActivity.cartitemmodellist.get(position).setProductQuantity(Long.parseLong(qtyno.getText().toString()));
                                                                   }
                                                               }
                                                               productQuantitytv.setText("Qty: " + qtyno.getText());
                                                           /*    DeliveryActivity.allproductsvailable = true;
                                                               if (availableqty > 0) {
                                                                   a = availableqty;

                                                                   if (Long.parseLong(qtyno.getText().toString()) <= a) {
                                                                       DeliveryActivity.allproductsvailable = true;
                                                                   }
                                                               }

                                                            */
                                                               notifyItemChanged(cartItemModelList.size()-1);

                                                               if (!showdeltbutton) //delivery activity

                                                               {
                                                                   DeliveryActivity.lodingDialog.show();
                                                                   DeliveryActivity.cartitemmodellist.get(position).setQtyerror(false);
                                                                   final int initialqty = Integer.parseInt(quantity);
                                                                   final int finalqty = Integer.parseInt(qtyno.getText().toString());
                                                                   final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                                                                   if (finalqty > initialqty) {
                                                                       for (int y = 0; y < (finalqty - initialqty); y++) {
                                                                           final String qtydocname = UUID.randomUUID().toString().substring(0, 20);
                                                                           Map<String, Object> timestamp = new HashMap<>();
                                                                           timestamp.put("time", FieldValue.serverTimestamp());
                                                                           final int finalY = y;
                                                                           firebaseFirestore.collection("PRODUCTS").document(prodid).collection("QUANTITY").document(qtydocname).set(timestamp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                               @Override
                                                                               public void onSuccess(Void aVoid) {
                                                                                   qtyids.add(qtydocname);
                                                                                   if (finalY + 1 == finalqty - initialqty) {
                                                                                       firebaseFirestore.collection("PRODUCTS").document(prodid).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(stockqty).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                           @Override
                                                                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                               if (task.isSuccessful()) {
                                                                                                   List<String> serverqty = new ArrayList<>();
                                                                                                   for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                                                       serverqty.add(queryDocumentSnapshot.getId());

                                                                                                   }
                                                                                                   //      long availableqty=0;
                                                                                                   boolean nolongervailable = true;
                                                                                                   for (String qtyid : qtyids) {
                                                                                                       if (!serverqty.contains(qtyid)) {


                                                                                                           DeliveryActivity.cartitemmodellist.get(position).setQtyerror(true);
                                                                                                           DeliveryActivity.cartitemmodellist.get(position).setMaxqty(availableqty);
                                                                                                           Toast.makeText(itemView.getContext(), "Sorry! ,all products may not be available in required quantity.", Toast.LENGTH_SHORT).show();


                                                                                                       } else {
                                                                                                           availableqty++;
                                                                                                       }

                                                                                                   }
                                                                                                   DeliveryActivity.cartAdapter.notifyDataSetChanged();

                                                                                               } else {
                                                                                                   String e = task.getException().getMessage();
                                                                                                   Toast.makeText(itemView.getContext(), e, Toast.LENGTH_SHORT).show();
                                                                                               }
                                                                                               DeliveryActivity.lodingDialog.dismiss();
                                                                                           }
                                                                                       });

                                                                                   }
                                                                               }
                                                                           });
                                                                       }
                                                                   } else if (initialqty > finalqty) {
                                                                       for (int x = 0; x < (initialqty - finalqty); x++) {
                                                                           final String qtyid = qtyids.get(qtyids.size() - 1 - x);

                                                                           final int finalX = x;
                                                                           firebaseFirestore.collection("PRODUCTS").document(prodid).collection("QUANTITY").document(qtyid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                               @Override
                                                                               public void onSuccess(Void aVoid) {
                                                                                   qtyids.remove(qtyid);
                                                                                   DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                                   if(finalX +1==(initialqty-finalqty))
                                                                                       DeliveryActivity.lodingDialog.dismiss();
                                                                               }
                                                                           });
                                                                       }

                                                                   }
                                                               }


                                                           } else {
                                                               Toast.makeText(itemView.getContext(), "max quantity: " + maxprodquantity, Toast.LENGTH_SHORT).show();
                                                           }
                                                       }
                                                       qtyd.dismiss();
                                                   }
                                               }
                        );
                        qtyd.show();
                    }
                });
                if (offersappliedno > 0) {
                    offersApplied.setVisibility(View.VISIBLE);
                    String offerDiscAmount1=String.valueOf(Long.valueOf(prodPrice)-Long.valueOf(cutprice));
                    offersApplied.setText("offer applied- "+offerDiscAmount1);
                } else {
                    offersApplied.setVisibility(View.INVISIBLE);
                }

            } else {
                productPrcice.setText("out of Stock");
                productPrcice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                couponredlayout.setVisibility(View.GONE);
                cuttedPrice.setText("");
                dividercp.setVisibility(View.GONE);
                freeCoupons.setVisibility(View.INVISIBLE);
                freeCouponIcon.setVisibility(View.INVISIBLE);
                offersApplied.setVisibility(View.INVISIBLE);
                couponsApplied.setVisibility(View.GONE);
                deleteitembutton.setVisibility(View.VISIBLE);
                prqtyll.setVisibility(View.INVISIBLE);
            }
            /////////////////////////////////////
            applycouponbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {
                        for (reward_model rewardModel : DBqueries.rewardmodelList) {
                            if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                                rewardModel.setAlreadyused(true);
                                couponredlayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_gradient_background));
                                couponRedeemption_tv.setText(rewardModel.getCouponbody());
                                redeemybtn.setText("Coupon");
                            }
                        }
                    }
                    couponsApplied.setVisibility(View.VISIBLE);
                    cartItemModelList.get(position).setDiscountedpric(discountedprice.getText().toString().substring(3,discountedprice.getText().length()-2));
                    productPrcice.setText(discountedprice.getText());
                    String offerDiscAmount=String.valueOf(Long.valueOf(prodPrice)-Long.valueOf(discountedprice.getText().toString().substring(3,discountedprice.getText().length()-2)));
                    couponsApplied.setText("Coupon Applied - Rs." + offerDiscAmount+ "/-");
                    notifyItemChanged(cartItemModelList.size()-1);
                    pricedialog.dismiss();
                }
            });
            removecouponbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (reward_model rewardModel : DBqueries.rewardmodelList) {
                        if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                            rewardModel.setAlreadyused(false);
                        }
                    }
                    coupontitle.setText("Coupon");
                    couponexpirydate.setText("Validity");
                    couponbody.setText("Tap the icon on top right corner to select your coupon.");
                    productPrcice.setText("Rs."+prodPrice+"/-");
                    couponsApplied.setVisibility(View.INVISIBLE);
                    couponredlayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                    couponRedeemption_tv.setText("Apply your coupon here.");
                    redeemybtn.setText("Redeem");
                    cartItemModelList.get(position).setSelectedCouponId(null);
                    pricedialog.dismiss();
                }
            });

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
            /////////////////////////////////////
        }
    }

    class cartTotalAmountViewHolder extends RecyclerView.ViewHolder {
        private TextView total_items;
        private TextView total_amount;
        private TextView deliveryPrice;
        private TextView savedAmount;
        private TextView total_item_price;

        public cartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            total_items = itemView.findViewById(R.id.total_items);
            total_item_price = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);
            total_amount = itemView.findViewById(R.id.total_price);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        private void setTotalAmount(int totalitemtext, int totalitempricetext, String deliverypricetext, int totalamounttext, int savedamounttext) {
            total_items.setText("Price ( " + totalitemtext + " items)");
            total_item_price.setText("Rs." + totalitempricetext + "/-");
            if (deliverypricetext.equals("free")) {
                deliveryPrice.setText(deliverypricetext);
            } else {
                deliveryPrice.setText("Rs." + deliverypricetext + "/-");
            }

            savedAmount.setText("You saved Rs." + savedamounttext + "/- on this order.");
            total_amount.setText("Rs." + totalamounttext + "/-");
            cartTotalAmount.setText("Rs." + totalamounttext + "/-");
            //  Toast.makeText(itemView.getContext()," "+cartTotalAmount.getText().toString(),Toast.LENGTH_LONG).show();
            LinearLayout p = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalitempricetext == 0) {
                if (DeliveryActivity.fromcart) {
                    DeliveryActivity.cartitemmodellist.remove(DeliveryActivity.cartitemmodellist.size() - 1);
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                }
                if (showdeltbutton) {
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                }
                p.setVisibility(View.GONE);
            } else {
                p.setVisibility(View.VISIBLE);
            }
        }


    }
}