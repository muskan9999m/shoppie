package com.example.shoppie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class otpVerificationActivity extends AppCompatActivity {
    String mobno;
    private TextView phoneno;
    private EditText otp;
    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        phoneno=(TextView)findViewById(R.id.mobileno);
        otp=(EditText)findViewById(R.id.otp);
        verify=(Button)findViewById(R.id.verify);

        mobno=getIntent().getStringExtra("mobileno");

        phoneno.setText("Verification code has been sent to 91+ "+mobno);

        Random random=new Random();
        final int otp_no=random.nextInt(999999-111111)+111111;
        String SMS_API="https://www.fast2sms.com/dev/bulk";
       StringRequest stringRequesta=new StringRequest(Request.Method.POST,SMS_API , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               verify.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if(otp.getText().toString().equals(String.valueOf(otp_no)))
                       {
                           Map <String,Object> updateStatus=new HashMap<>();
                           updateStatus.put("Payment Status", "paid");
                           updateStatus.put("Order Status", "ordered");
                           String orderid=getIntent().getStringExtra("orderid");
                           FirebaseFirestore.getInstance().collection("ORDERS").document(orderid).update(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful())
                                   {
                                       DeliveryActivity.codorderconfirm=true;
                                       finish();
                                   }
                                   else
                                   {
                                       Toast.makeText(otpVerificationActivity.this, "order cancelled", Toast.LENGTH_LONG).show();
                                   }
                               }
                           });

                       }else
                       {
                           Toast.makeText(otpVerificationActivity.this, "incorrect OTP!", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "failed to get otp verification code!", Toast.LENGTH_SHORT).show();
                 finish();
            }
        }){
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String,String>headers=new HashMap<>();
            headers.put("authorization","5loru6Iqh2NkVU7dGbA3vZR0XzfsgHEmC9M1YeP8StJLijWcnTVzqZdntFfUW5HwKjhIbRDSLmTGNyM9");
            return headers;
        }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>body=new HashMap<>();
                body.put("sender_id","FSTSMS");
                body.put("language","english");
                body.put("route","qt");
                body.put("numbers",mobno);
                body.put("message","35292");
                body.put("variables","{#BB#}");
                body.put("variables_values",String.valueOf(otp_no));
                return body;
            }


        };
        stringRequesta.setRetryPolicy(new DefaultRetryPolicy(
                5000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue= Volley.newRequestQueue(otpVerificationActivity.this);
        requestQueue.add(stringRequesta);



    }
}