package com.example.shoppie;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.shoppie.DBqueries.cartItemModelLiist;
import static com.example.shoppie.DBqueries.cartlist;

import org.json.JSONException;
import org.json.JSONObject;

public class DeliveryActivity extends AppCompatActivity {
    Toolbar toolbar;
    private Dialog paymentDialog;
    public static Dialog lodingDialog;
    public static boolean codorderconfirm = false;
    private TextView totalAmount;
    private int totam = 0;
    public static boolean getqtyids = true;
    public static List<cart_item_model> cartitemmodellist;
    private RecyclerView deliveryRecyclerview;
    public static final int SELECT_ADDRESS = 0;
    public static cart_adapter cartAdapter;
    private Button chngeaddaddressbutton;
    private boolean successresponse = false;
    private TextView fullname;
    private TextView fulladdress;
    private TextView pincode;
    private Button continueBUTTON;
    private ImageView paytm;
    private RelativeLayout orderconflayout;
    private ImageView continueshoppingbtn;
    private TextView orderId;
    public static boolean fromcart;
    private ImageView cod;
    private String name, mobno;
    private String order_id;
    private FirebaseFirestore firebaseFirestore;
    private String paymentMethod = "PAYTM";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        deliveryRecyclerview = (RecyclerView) findViewById(R.id.deliveryRecyclerView);
        chngeaddaddressbutton = findViewById(R.id.change_add_address_button);
        toolbar = findViewById(R.id.toolbar);
        totalAmount = findViewById(R.id.total_cart_amount);
        fulladdress = findViewById(R.id.address);
        fullname = findViewById(R.id.fullname);
        continueBUTTON = findViewById(R.id.cart_continue_button);
        pincode = findViewById(R.id.pincode);
        orderconflayout = (RelativeLayout) findViewById(R.id.order_confirmation_layout);
        continueshoppingbtn = (ImageView) findViewById(R.id.continueshoppingbutton);
        orderId = (TextView) findViewById(R.id.orderid);
        firebaseFirestore = FirebaseFirestore.getInstance();
        getqtyids = true;

        lodingDialog = new Dialog(DeliveryActivity.this);
        lodingDialog.setContentView(R.layout.loading_progress_dialog);
        lodingDialog.getWindow().setBackgroundDrawable(DeliveryActivity.this.getDrawable(R.drawable.slider_background));
        lodingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lodingDialog.setCancelable(false);

        paymentDialog = new Dialog(DeliveryActivity.this);
        paymentDialog.setContentView(R.layout.payment_method);
        paymentDialog.getWindow().setBackgroundDrawable(DeliveryActivity.this.getDrawable(R.drawable.slider_background));
        paymentDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paymentDialog.setCancelable(true);
        paytm = paymentDialog.findViewById(R.id.paytm);
        cod = paymentDialog.findViewById(R.id.cod);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerview.setLayoutManager(linearLayoutManager);

        cartAdapter = new cart_adapter(cartitemmodellist, totalAmount, false);
        deliveryRecyclerview.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        chngeaddaddressbutton.setVisibility(View.VISIBLE);
        chngeaddaddressbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getqtyids = false;
                Intent myaddressesI = new Intent(DeliveryActivity.this, myAdressesActivity.class);
                myaddressesI.putExtra("MODE", SELECT_ADDRESS);
                startActivity(myaddressesI);
            }
        });
        continueBUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allproductsavailable = true;
                for (cart_item_model cartItemModel : cartitemmodellist) {
                    if (cartItemModel.isQtyerror()) {
                        allproductsavailable = false;
                    }
                }
                if (allproductsavailable) {
                    paymentDialog.show();
                }
            }
        });
        order_id = UUID.randomUUID().toString().substring(0, 28);

        ///////////////////cod
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod="COD";
                placeOrderDetails();
            }
        });
        //////////////////cod
//dummy paytm testing code


        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod="PAYTM";
                placeOrderDetails();
            }
        });

/////////////////paytm integration
/*
        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            paytm();
        });
    }
 */
    }


    @Override
    protected void onPause() {
        super.onPause();
        lodingDialog.dismiss();
        if (getqtyids) {
            for (int x = 0; x < cartitemmodellist.size() - 1; x++) {
                if (!successresponse) {
                    for (final String qtyid : cartitemmodellist.get(x).getQtyIDS()) {
                        final int finalX = x;
                        firebaseFirestore.collection("PRODUCTS").document(cartitemmodellist.get(x).getProductID()).collection("QUANTITY").document(qtyid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (qtyid.equals(cartitemmodellist.get(finalX).getQtyIDS().get(cartitemmodellist.get(finalX).getQtyIDS().size() - 1)))//last doc
                                {
                                    cartitemmodellist.get(finalX).getQtyIDS().clear();

                                }

                            }
                        });
                    }
                } else {
                    cartitemmodellist.get(x).getQtyIDS().clear();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //////////////////////accessing quantity
        if (getqtyids) {
            lodingDialog.show();
            for (int x = 0; x < cartitemmodellist.size() - 1; x++) {
                for (int y = 0; y < cartitemmodellist.get(x).getProductQuantity(); y++) {
                    final String qtydocname = UUID.randomUUID().toString().substring(0, 20);
                    Map<String, Object> timestamp = new HashMap<>();
                    timestamp.put("time", FieldValue.serverTimestamp());
                    final int finalX = x;
                    final int finalY = y;
                    firebaseFirestore.collection("PRODUCTS").document(cartitemmodellist.get(x).getProductID()).collection("QUANTITY").document(qtydocname).set(timestamp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                cartitemmodellist.get(finalX).getQtyIDS().add(qtydocname);
                                if (finalY + 1 == cartitemmodellist.get(finalX).getProductQuantity()) {
                                    firebaseFirestore.collection("PRODUCTS").document(cartitemmodellist.get(finalX).getProductID()).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(cartitemmodellist.get(finalX).getStockqty()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                List<String> serverqty = new ArrayList<>();
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                    serverqty.add(queryDocumentSnapshot.getId());

                                                }
                                                long availableqty = 0;
                                                boolean nolongervailable = true;
                                                for (String qtyid : cartitemmodellist.get(finalX).getQtyIDS()) {
                                                    cartitemmodellist.get(finalX).setQtyerror(false);
                                                    if (!serverqty.contains(qtyid)) {
                                                        if (nolongervailable) {
                                                            cartitemmodellist.get(finalX).setInstock(false);
                                                        } else {
                                                            cartitemmodellist.get(finalX).setQtyerror(true);
                                                            cartitemmodellist.get(finalX).setMaxqty(availableqty);
                                                            Toast.makeText(getApplicationContext(), "Sorry! ,all products may not be available in required quantity.", Toast.LENGTH_SHORT).show();
                                                        }

                                                    } else {
                                                        availableqty++;
                                                        nolongervailable = false;
                                                    }

                                                }
                                                cartAdapter.notifyDataSetChanged();

                                            } else {
                                                String e = task.getException().getMessage();
                                                Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
                                            }
                                            lodingDialog.dismiss();
                                        }
                                    });

                                }
                            } else {
                                String e = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
                                lodingDialog.dismiss();
                            }
                        }
                    });
                }
            }
        } else {
            getqtyids = true;
        }
        name = DBqueries.addressesModelList.get(DBqueries.selectedaddress).getFullname();
        mobno = DBqueries.addressesModelList.get(DBqueries.selectedaddress).getMobileno();
        fullname.setText(name + " - " + mobno);
        fulladdress.setText(DBqueries.addressesModelList.get(DBqueries.selectedaddress).getAddress());
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedaddress).getPincode());
        if (codorderconfirm) {
            codorderconfirm = false;
            showconfirmationlayout();
        }

    }

    @Override
    public void onBackPressed() {

        if (successresponse) {
            finish();
            return;
        }
        super.onBackPressed();
    }

    private void showconfirmationlayout() {
        codorderconfirm = false;
        successresponse = true;
        getqtyids = false;

        for (int x = 0; x < cartitemmodellist.size() - 1; x++) {
            for (String qtyid : cartitemmodellist.get(x).getQtyIDS()) {
                firebaseFirestore.collection("PRODUCTS").document(cartitemmodellist.get(x).getProductID()).collection("QUANTITY").document(qtyid).update("user_ID", FirebaseAuth.getInstance().getUid());

            }
            cartitemmodellist.get(x).getQtyIDS().clear();

        }


        if (MainActivity2.mainActivity != null) {
            MainActivity2.mainActivity.finish();
            MainActivity2.mainActivity = null;
            MainActivity2.showcart = false;
        } else {
            MainActivity2.resetmainact = true;
        }
        if (productDetailsActivity.productDetailsActivity != null) {
            productDetailsActivity.productDetailsActivity.finish();
            productDetailsActivity.productDetailsActivity = null;
        }


        ////////sms
        String SMS_API = "https://www.fast2sms.com/dev/bulk";
        StringRequest stringRequesta = new StringRequest(Request.Method.POST, SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("authorization", "5loru6Iqh2NkVU7dGbA3vZR0XzfsgHEmC9M1YeP8StJLijWcnTVzqZdntFfUW5HwKjhIbRDSLmTGNyM9");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("sender_id", "FSTSMS");
                body.put("language", "english");
                body.put("route", "qt");
                body.put("numbers", mobno);
                body.put("message", "35608");
                body.put("variables", "{#FF#}");
                body.put("variables_values", String.valueOf(order_id));
                return body;
            }


        };
        stringRequesta.setRetryPolicy(new DefaultRetryPolicy(
                5000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);
        requestQueue.add(stringRequesta);


        if (fromcart) {
            lodingDialog.show();
            Map<String, Object> updatecartlist = new HashMap<>();
            long cartlistsize = 0;
            lodingDialog.show();
            final List<Integer> indexlist = new ArrayList<>();
            for (int x = 0; x < cartlist.size(); x++) {
                if (!cartItemModelLiist.get(x).isInstock()) {
                    updatecartlist.put("product_ID_" + cartlistsize, cartItemModelLiist.get(x).getProductID());
                    cartlistsize++;
                } else {
                    indexlist.add(x);
                }
            }
            updatecartlist.put("list_size", cartlistsize);
            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART").set(updatecartlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        for (int x = 0; x < indexlist.size(); x++) {
                            cartlist.remove(indexlist.get(x).intValue());
                            cartItemModelLiist.remove(indexlist.get(x).intValue());

                        }
                        cartItemModelLiist.remove(cartItemModelLiist.size() - 1);
                    } else {
                        String e = task.getException().getMessage();
                        Toast.makeText(DeliveryActivity.this, e, Toast.LENGTH_SHORT).show();
                    }
                    lodingDialog.dismiss();
                }
            });
        }
        continueBUTTON.setEnabled(false);
        chngeaddaddressbutton.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        orderconflayout.setVisibility(View.VISIBLE);

        orderId.setText("Order Id: " + order_id);
        continueshoppingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    private void placeOrderDetails() {
        lodingDialog.show();
        String userId = FirebaseAuth.getInstance().getUid();
        for (cart_item_model cartItemModel : cartitemmodellist) {
            if (cartItemModel.getType() == cartItemModel.CART_ITEM) {
                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("Order Id", order_id);
                orderDetails.put("Product Id", cartItemModel.getProductID());
                orderDetails.put("User Id", userId);
                orderDetails.put("Product Quantity", cartItemModel.getProductQuantity());
                if (cartItemModel.getCuttedPrice() != null)
                orderDetails.put("Cutted Price", cartItemModel.getCuttedPrice());
                else
                { orderDetails.put("Cutted Price", "");
                }
                orderDetails.put("Product Price", cartItemModel.getProductPrcice());
                if (cartItemModel.getSelectedCouponId() != null)
                orderDetails.put("Coupon Id", cartItemModel.getSelectedCouponId());
                else
                { orderDetails.put("Coupon Id","");
                }
                if (cartItemModel.getDiscountedpric() != null)
                orderDetails.put("Discounted Price", null);
                else
                {orderDetails.put("Discounted Price", "null");
                }
                orderDetails.put("Date", FieldValue.serverTimestamp());
                orderDetails.put("Payment Method", paymentMethod);
                orderDetails.put("Address", fulladdress.getText());
                orderDetails.put("Fullname", fulladdress.getText());
                orderDetails.put("Pincode", pincode.getText());
                orderDetails.put("Free Coupons", cartItemModel.getFreeCoupons());

                firebaseFirestore.collection("ORDERS").document("order_id").collection("orderItems").document(cartItemModel.getProductID()).set(orderDetails)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    String e=task.getException().getMessage();
                                    Toast.makeText(DeliveryActivity.this, e, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
            else
            {
                Map<String,Object> orderDetails=new HashMap<>();
                orderDetails.put("Total Items",cartItemModel.getTotalItems());
                orderDetails.put("Total Item Price",cartItemModel.getTotalItemPrice());
                orderDetails.put("Delivery Price",cartItemModel.getDeliveryPrice());
                orderDetails.put("Total Amount",cartItemModel.getTotalAmount());
                orderDetails.put("Saved Amount",cartItemModel.getSavedAmount());
                orderDetails.put("Payment Status", "not paid");
                orderDetails.put("Order Status", "Cancelled");

                firebaseFirestore.collection("ORDERS").document("order_id")
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        { if (paymentMethod.equals("PAYTM")) {
                            paytm();
                        } else {
                            cod();
                        }}
                        else
                        { String e=task.getException().getMessage();
                            Toast.makeText(DeliveryActivity.this, e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        }
    }

     /*private void paytm()
         {
             paymentDialog.dismiss();
             lodingDialog.show();
             if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                 ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
             }
             final String M_id = "ZLjVsc50390575338404";
             final String customer_id = FirebaseAuth.getInstance().getUid();
             order_id = UUID.randomUUID().toString().substring(0, 28);
             String url = "https://accumulative-subord.000webhostapp.com/ptm/PaytmChecksum.php";
             final String callBackUrl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

             RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);

             StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {
                     try {
                         JSONObject jsonObject = new JSONObject(response);
                         if (jsonObject.has("CHECKSUMHASH")) {
                             String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");

                             HashMap<String, String> paramMap = new HashMap<String, String>();
                             paramMap.put("MID", M_id);
                             paramMap.put("ORDER_ID", order_id);
                             paramMap.put("CUST_ID", customer_id);
                             //paramMap.put("MOBILE_NO", "7777777777");
                             //paramMap.put("EMAIL", "username@emailprovider.com");
                             paramMap.put("CHANNEL_ID", "WAP");
                             paramMap.put("TXN_AMOUNT", totalAmount.getText().toString().substring(3, totalAmount.getText().length() - 2));
                             paramMap.put("WEBSITE", "WEBSTAGING");
                             paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                             paramMap.put("CALLBACK_URL", callBackUrl);
                             paramMap.put("CHECKSUMHASH", CHECKSUMHASH);
                             PaytmPGService paytmPGService = PaytmPGService.getProductionService();

                             PaytmOrder order = new PaytmOrder(paramMap);
                             paytmPGService.initialize(order, null);
                             paytmPGService.startPaymentTransaction(DeliveryActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                             //    @Override
                                 public void onTransactionResponse(Bundle inResponse) {
                                     //Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                                     showconfirmationlayout();
                                 }




                            //     @Override
                                 public void networkNotAvailable() {
                                     Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();

                                 }

                                 @Override
                                 public void clientAuthenticationFailed(String inErrorMessage) {
                                     Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();


                                 }

                                 @Override
                                 public void someUIErrorOccurred(String inErrorMessage) {
                                     Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();

                                 }

                                 @Override
                                 public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                     Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

                                 }

                                 @Override
                                 public void onBackPressedCancelTransaction() {
                                     Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();

                                 }

                                 @Override
                                 public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                     Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();

                                 }
                             });


                         }
                     } catch (JSONException | JSONException e) {
                         e.printStackTrace();
                     }

                 }

             }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {
                     lodingDialog.dismiss();
                     Toast.makeText(DeliveryActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                 }
             }) {
                 @Override
                 protected Map<String, String> getParams() throws AuthFailureError {

                     Map<String, String> paramMap = new HashMap<String, String>();
                     paramMap.put("MID", M_id);
                     paramMap.put("ORDER_ID", order_id);
                     paramMap.put("CUST_ID", customer_id);
                     //paramMap.put("MOBILE_NO", "7777777777");
                     //paramMap.put("EMAIL", "username@emailprovider.com");
                     paramMap.put("CHANNEL_ID", "WAP");
                     paramMap.put("TXN_AMOUNT", totalAmount.getText().toString().substring(3, totalAmount.getText().length() - 2));
                     paramMap.put("WEBSITE", "WEBSTAGING");
                     paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                     paramMap.put("CALLBACK_URL", callBackUrl);
                     return paramMap;
                 }
             };
             requestQueue.add(stringRequest);
         }*/


    private void cod() {
        getqtyids = false;
        paymentDialog.dismiss();
        Intent otpi = new Intent(DeliveryActivity.this, otpVerificationActivity.class);
        otpi.putExtra("mobileno", mobno.substring(0, 10));
        otpi.putExtra("orderid", order_id);
        startActivity(otpi);

    }
    private void paytm()
    {
        getqtyids = false;
        paymentDialog.dismiss();
            Map <String,Object> updateStatus=new HashMap<>();
            updateStatus.put("Payment Status", "paid");
            updateStatus.put("Order Status", "ordered");
            firebaseFirestore.collection("ORDERS").document(order_id).update(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        showconfirmationlayout();
                    }
                    else
                    {
                        Toast.makeText(DeliveryActivity.this, "order cancelled", Toast.LENGTH_LONG).show();
                    }
                }
            });


    }
}



