package com.example.shoppie;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import static com.example.shoppie.DeliveryActivity.SELECT_ADDRESS;

public class myAdressesActivity extends AppCompatActivity {
    Toolbar toolbar;
    private RecyclerView myaddressesrecyclerview;
    private Button deliverhereButton;
    private TextView addressessaved;
    private static AdressAdapter adressAdapter;
    private int preselectedAddress;
    private LinearLayout addnewaddressbutton;
    private Dialog lodingDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adresses);
        toolbar = findViewById(R.id.toolbar);
        myaddressesrecyclerview=findViewById(R.id.addresses_recycler_view);
        deliverhereButton=(Button)findViewById(R.id.deliver_here_button);
        addressessaved=findViewById(R.id.addresses_saved);
        addnewaddressbutton=findViewById(R.id.ll);
        preselectedAddress=DBqueries.selectedaddress;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add Address");

        lodingDialog=new Dialog(myAdressesActivity.this);
        lodingDialog.setContentView(R.layout.loading_progress_dialog);
        lodingDialog.getWindow().setBackgroundDrawable(myAdressesActivity.this.getDrawable(R.drawable.slider_background));
        lodingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lodingDialog.setCancelable(false);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(myAdressesActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myaddressesrecyclerview.setLayoutManager(linearLayoutManager);



        int MODE=getIntent().getIntExtra("MODE",-1);
        if(MODE==SELECT_ADDRESS)
        {deliverhereButton.setVisibility(View.VISIBLE);}
        else
        {deliverhereButton.setVisibility(View.GONE);}
        adressAdapter=new AdressAdapter(DBqueries.addressesModelList,MODE);
        myaddressesrecyclerview.setAdapter(adressAdapter);
        ((SimpleItemAnimator)myaddressesrecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);
        adressAdapter.notifyDataSetChanged();

        addnewaddressbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(myAdressesActivity.this,addAdressActivity.class);
                i.putExtra("INTENT","null");
                startActivity(i);
            }
        });
        deliverhereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DBqueries.selectedaddress!=preselectedAddress){
                    final int previousaddressIndex=preselectedAddress;
                    lodingDialog.show();
                    Map<String,Object> updateselection=new HashMap<>();

                updateselection.put("SELECTED_"+String.valueOf(preselectedAddress+1),false);
                updateselection.put("SELECTED_"+String.valueOf(DBqueries.selectedaddress+1),true);
                    preselectedAddress=DBqueries.selectedaddress;

                    FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES").update(updateselection).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                finish();

                            } else {
                                String e = task.getException().getMessage();
                                Toast.makeText(myAdressesActivity.this, e, Toast.LENGTH_SHORT).show();
                                preselectedAddress=previousaddressIndex;
                            }lodingDialog.dismiss();
                        }});




    }
            else {finish();}}});}
    public static void refreshitem(int deselect,int select)
    {
        adressAdapter.notifyItemChanged(deselect);
        adressAdapter.notifyItemChanged(select);

    }

    @Override
    protected void onStart() {
        super.onStart();
        addressessaved.setText(String.valueOf(DBqueries.addressesModelList.size())+" saved addresses.");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            if(DBqueries.selectedaddress!=preselectedAddress) {
                DBqueries.addressesModelList.get(DBqueries.selectedaddress).setSELECTED(false);
                DBqueries.addressesModelList.get(preselectedAddress).setSELECTED(true);
                DBqueries.selectedaddress = preselectedAddress;
            }
                finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(DBqueries.selectedaddress!=preselectedAddress)
        {
            DBqueries.addressesModelList.get(DBqueries.selectedaddress).setSELECTED(false);
            DBqueries.addressesModelList.get(preselectedAddress).setSELECTED(true);
            DBqueries.selectedaddress=preselectedAddress;
        }
    }
}