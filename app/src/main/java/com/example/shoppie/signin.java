package com.example.shoppie;

import android.content.Intent;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signin extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView noaccount;
    public static boolean disablebutton=false;
    private ImageButton cncl;
    private EditText emails;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    protected EditText passwords;
    private ProgressBar progressBar;
    private TextView forgetPassword;
    private FirebaseAuth auth;
    private Button SIGNIN;
    private FrameLayout parentframeLayout;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signin.
     */
    // TODO: Rename and change types and number of parameters
    public static signin newInstance(String param1, String param2) {
        signin fragment = new signin();
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
        View view= inflater.inflate(R.layout.fragment_signin, container, false);
        noaccount=view.findViewById(R.id.tv1);
        SIGNIN=view.findViewById(R.id.signin);
        parentframeLayout=getActivity().findViewById(R.id.fl);
        emails=view.findViewById(R.id.e1);
        passwords=view.findViewById(R.id.e2);
        forgetPassword=view.findViewById(R.id.forget);
        progressBar=view.findViewById(R.id.progressBar);
        auth=FirebaseAuth.getInstance();
        cncl=view.findViewById(R.id.cncl);
        if(disablebutton==true)
        {cncl.setVisibility(View.GONE);
        }
        else
        {cncl.setVisibility(View.VISIBLE);}
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        noaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragment(new signup());
            }
        });
        emails.addTextChangedListener(new TextWatcher() {
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
        passwords.addTextChangedListener(new TextWatcher() {
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
        SIGNIN.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             emailCheck();
             
         }
     });
        cncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Register.onResetFragment = true;

                setFragment(new resetPassword());
            }
        });

    }

    private void mainIntent() {
        if(disablebutton)
        {
            disablebutton=false;
        }
        else
        {
        Intent i=new Intent(getActivity(),MainActivity2.class);
        startActivity(i);}
        getActivity().finish();}


    private void emailCheck() {
        if(emails.getText().toString().matches(emailPattern))
        {

            if(passwords.getText().toString().length()>=8)
            {

                progressBar.setVisibility(View.VISIBLE);
                SIGNIN.setEnabled(false);
                SIGNIN.setBackgroundColor(getResources().getColor(R.color.peach));
                auth.signInWithEmailAndPassword(emails.getText().toString(),passwords.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                          mainIntent();
                        }
                        else {
                            progressBar.setVisibility(View.INVISIBLE);
                            SIGNIN.setEnabled(true);
                            SIGNIN.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            String error=task.getException().getMessage();
                            Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else
            {
              Toast.makeText(getActivity(),"incorrect email or password",Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
           Toast.makeText(getActivity(),"incorrect email or password",Toast.LENGTH_SHORT).show();
        }

    }

    private void checkInputs() {
        if(!TextUtils.isEmpty(emails.getText()))
        {
            if(!TextUtils.isEmpty(passwords.getText()))
            {
                SIGNIN.setEnabled(true);
                SIGNIN.setBackgroundColor(getResources().getColor(R.color.colorAccent));

            }
            else{SIGNIN.setEnabled(false);
                SIGNIN.setBackgroundColor(getResources().getColor(R.color.peach));}
        }
        else{SIGNIN.setEnabled(false);
            SIGNIN.setBackgroundColor(getResources().getColor(R.color.peach));}

    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
     //   fragmentTransaction.setCustomAnimations(R.anim.slide_form_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentframeLayout.getId(),fragment);
        fragmentTransaction.commit();


    }
}