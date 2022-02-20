package com.example.shoppie;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signup extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView alreadyaccount;
    private FrameLayout parentframmelayout;
    private ProgressBar progressbar;
    private EditText email;
    public static boolean disableclosebutton=false;
    private EditText password;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private EditText confirmpass;
    private EditText name;
    private FirebaseAuth auth;
    private ImageButton closebutton;
    private Button SIGNUP;
    private FirebaseFirestore firebaseFirestore;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signup.
     */
    // TODO: Rename and change types and number of parameters
    public static signup newInstance(String param1, String param2) {
        signup fragment = new signup();
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
        View view= inflater.inflate(R.layout.fragment_signup, container, false);
        alreadyaccount=view.findViewById(R.id.tv);
        parentframmelayout=getActivity().findViewById(R.id.fl);
        email=view.findViewById(R.id.e1);
        password=view.findViewById(R.id.e3);
        confirmpass=view.findViewById(R.id.e4);
        progressbar=view.findViewById(R.id.progressBar);
        name=view.findViewById(R.id.e2);
        firebaseFirestore=FirebaseFirestore.getInstance();
        SIGNUP=view.findViewById(R.id.signup);
        closebutton=view.findViewById(R.id.cncl);
        auth=FirebaseAuth.getInstance();
        if(disableclosebutton==true)
        {closebutton.setVisibility(View.GONE);
        }
        else
        {closebutton.setVisibility(View.VISIBLE);}
        return view;
    }

  /*  @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFragment(new signin());
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        alreadyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 setFragment(new signin());
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
          checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });
        SIGNUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailCheck();
            }
        });
    }

    private void emailCheck() {
        if(email.getText().toString().matches(emailPattern))
        {
            progressbar.setVisibility(View.VISIBLE);
             SIGNUP.setEnabled(false);
            SIGNUP.setTextColor(Color.argb(50,250,00,00));
            auth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful())
                    {


                        progressbar.setVisibility(View.INVISIBLE);
                        String error=task.getException().getMessage();
                        SIGNUP.setEnabled(true);
                        SIGNUP.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Map<String,Object> userdata=new HashMap<>();
                        userdata.put("Full Name",name.getText().toString());
                        firebaseFirestore.collection("Users").document(auth.getUid())
                                .set(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    CollectionReference userDataReference= firebaseFirestore.collection("Users").document(auth.getUid()).collection("USER_DATA");
                                    //maps
                                    Map<String,Object> wishlistmap=new HashMap<>();
                                    wishlistmap.put("list_size", (long) 0);
                                    Map<String,Object> ratingsmap=new HashMap<>();
                                    ratingsmap.put("list_size", (long) 0);
                                    Map<String,Object> cartmap=new HashMap<>();
                                    cartmap.put("list_size", (long) 0);
                                    Map<String,Object> myaddressmap=new HashMap<>();
                                    myaddressmap.put("list_size", (long) 0);

                                    final List<String> documentNames=new ArrayList<>();
                                    documentNames.add("MY_WISHLIST");
                                    documentNames.add("MY_RATINGS");
                                    documentNames.add("MY_CART");
                                    documentNames.add("MY_ADDRESSES");

                                    List<Map<String,Object>> documentFields=new ArrayList<>();
                                    documentFields.add(wishlistmap);
                                    documentFields.add(ratingsmap);
                                    documentFields.add(cartmap);
                                    documentFields.add(myaddressmap);

                                    for(int x=0;x<documentNames.size();x++)
                                    {
                                        final int finalX = x;
                                        userDataReference.document(documentNames.get(x)).set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful())
                                               {
                                                   if(finalX ==documentNames.size()-1)
                                                   mainIntent();
                                               }else
                                                {progressbar.setVisibility(View.INVISIBLE);
                                                    String error=task.getException().getMessage();
                                                    SIGNUP.setEnabled(true);
                                                    SIGNUP.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                                    Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                                else{
                                    String error=task.getException().getMessage();
                                    Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            });
        }
        else{
            email.setError("invalid email!!");
        }
    }

    private void mainIntent() {
        if(disableclosebutton)
        {
            disableclosebutton=false;
        }
        else
        {
            Intent i=new Intent(getActivity(),MainActivity2.class);
            startActivity(i);}
        getActivity().finish();}


    private void checkInputs() {
        if(!TextUtils.isEmpty(email.getText()))
        {
            if(!TextUtils.isEmpty(name.getText()))
            {
                if(!TextUtils.isEmpty(password.getText()) && password.length()>=8)
                {
                    if(!TextUtils.isEmpty(confirmpass.getText()) && confirmpass.getText().toString().equals(password.getText().toString()))
                    {
                      SIGNUP.setEnabled(true);
                      SIGNUP.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                    else{SIGNUP.setEnabled(false);
                        SIGNUP.setBackgroundColor(getResources().getColor(R.color.peach));
                        confirmpass.setError("Passwords do not match");
                    }
                }else{SIGNUP.setEnabled(false);
                password.setError("password length too short");
                    SIGNUP.setBackgroundColor(getResources().getColor(R.color.peach));}
            }else{SIGNUP.setEnabled(false);
                SIGNUP.setBackgroundColor(getResources().getColor(R.color.peach));}

        }
        else
        {
            SIGNUP.setEnabled(false);
            SIGNUP.setBackgroundColor(getResources().getColor(R.color.peach));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
     //   fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentframmelayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}