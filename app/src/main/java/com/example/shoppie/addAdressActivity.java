package com.example.shoppie;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
//import android.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;

public class addAdressActivity extends AppCompatActivity {
    Toolbar toolbar;
    private Dialog lodingDialog;
    private Button savebutton;
    private EditText city;
    private EditText locality;
    private EditText flatno;
    private EditText pincode;
    private EditText landmark;
    private EditText name;
    private EditText mobileno;
    private EditText altmobileno;
    private Spinner statespinner;
    private String[] statelist;
    private String selecedState;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);

        statelist = getResources().getStringArray(R.array.india_states);

        toolbar = findViewById(R.id.toolbar);
        savebutton = (Button) findViewById(R.id.save_button);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add a new Address");

        lodingDialog = new Dialog(addAdressActivity.this);
        lodingDialog.setContentView(R.layout.loading_progress_dialog);
        lodingDialog.getWindow().setBackgroundDrawable(addAdressActivity.this.getDrawable(R.drawable.slider_background));
        lodingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lodingDialog.setCancelable(false);


        statespinner = findViewById(R.id.stateSpinner);
        city = findViewById(R.id.city);
        locality = findViewById(R.id.locality);
        flatno = findViewById(R.id.flatno);
        pincode = findViewById(R.id.pincodE);
        landmark = findViewById(R.id.landmark);
        name = findViewById(R.id.namee);
        mobileno = findViewById(R.id.mobno);
        altmobileno = findViewById(R.id.alternatemobno);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(addAdressActivity.this, android.R.layout.simple_spinner_item, statelist);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statespinner.setAdapter(spinnerAdapter);
        statespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecedState = statelist[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!TextUtils.isEmpty(city.getText())) {
                    if (!TextUtils.isEmpty(locality.getText())) {
                        if (!TextUtils.isEmpty(flatno.getText())) {
                            if (!TextUtils.isEmpty(pincode.getText()) && pincode.getText().length() == 6) {
                                if (!TextUtils.isEmpty(name.getText())) {
                                    if (!TextUtils.isEmpty(mobileno.getText()) && mobileno.getText().length() == 10) {
                                        lodingDialog.show();
                                        final String fulladdress = flatno.getText().toString() + " " + locality.getText().toString() + " " + landmark.getText().toString() + " " + city.getText().toString() + " " + selecedState;
                                        Map<String, Object> addaddress = new HashMap<>();
                                        addaddress.put("list_size", (long) DBqueries.addressesModelList.size() + 1);
                                        if (TextUtils.isEmpty(altmobileno.getText())) {
                                            addaddress.put("mobile_no_" + String.valueOf((long) DBqueries.addressesModelList.size() + 1), mobileno.getText().toString());
                                        } else {
                                            addaddress.put("mobile_no_" + String.valueOf((long) DBqueries.addressesModelList.size() + 1),  mobileno.getText().toString() + " or " + altmobileno.getText().toString());
                                        }
                                        addaddress.put("fullname_" + String.valueOf((long) DBqueries.addressesModelList.size() + 1), name.getText().toString());
                                        addaddress.put("address_" + String.valueOf((long) DBqueries.addressesModelList.size() + 1), fulladdress);
                                        addaddress.put("pincode_" + String.valueOf((long) DBqueries.addressesModelList.size() + 1), pincode.getText().toString());
                                        addaddress.put("SELECTED_" + String.valueOf((long) DBqueries.addressesModelList.size() + 1), true);
                                        if (DBqueries.addressesModelList.size() > 0)
                                            addaddress.put("SELECTED_" + String.valueOf((long) DBqueries.addressesModelList.size() + 1), false);


                                        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES").update(addaddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if (DBqueries.addressesModelList.size() > 0)
                                                        DBqueries.addressesModelList.get(DBqueries.selectedaddress).setSELECTED(false);
                                                    if (TextUtils.isEmpty(altmobileno.getText())) {
                                                        DBqueries.addressesModelList.add(new addresses_model(name.getText().toString(),
                                                                fulladdress, pincode.getText().toString(), true,mobileno.getText().toString()));
                                                    } else {
                                                        DBqueries.addressesModelList.add(new addresses_model(name.getText().toString(),
                                                                fulladdress, pincode.getText().toString(), true,mobileno.getText().toString() + " or " + altmobileno.getText().toString()));
                                                    }

                                                    if (getIntent().getStringExtra("INTENT").equals("delivery")) {
                                                        //jabhi bhi new address dal rhe hai sirf tab delvery activity create ho
                                                        //shift to delivery activity after on the spot adding address
                                                        Intent delivery = new Intent(addAdressActivity.this, DeliveryActivity.class);
                                                        startActivity(delivery);
                                                    }
                                                    else
                                                    {
                                                        myAdressesActivity.refreshitem(DBqueries.selectedaddress,DBqueries.addressesModelList.size()-1);
                                                    }
                                                    finish();

                                                } else {
                                                    String e = task.getException().getMessage();
                                                    Toast.makeText(addAdressActivity.this, e, Toast.LENGTH_SHORT).show();
                                                }
                                                DBqueries.selectedaddress = DBqueries.addressesModelList.size() - 1;
                                                lodingDialog.dismiss();

                                            }
                                        });
                                    } else {
                                        mobileno.requestFocus();
                                        Toast.makeText(addAdressActivity.this, "please provide valid number!!", Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    name.requestFocus();
                                }
                            } else {
                                pincode.requestFocus();
                                Toast.makeText(addAdressActivity.this, "please provide valid pincode!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            flatno.requestFocus();
                        }
                    } else {
                        locality.requestFocus();
                    }
                } else {
                    city.requestFocus();
                }

            }
        });
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
}