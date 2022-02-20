package com.example.shoppie;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link resetPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class resetPassword extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private EditText regisemail;
    private Button reset;
    private TextView goback;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FirebaseAuth auth;
    private FrameLayout parentframmelayout;
    private ViewGroup linlayout;
    private ImageView gml;
    private TextView gmltext;
    private ProgressBar p;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public resetPassword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment resetPassword.
     */
    // TODO: Rename and change types and number of parameters
    public static resetPassword newInstance(String param1, String param2) {
        resetPassword fragment = new resetPassword();
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
        View view= inflater.inflate(R.layout.fragment_reset_password, container, false);
        regisemail=view.findViewById(R.id.email);
        reset=view.findViewById(R.id.reset);
        gml=view.findViewById(R.id.gmail);
        gmltext=view.findViewById(R.id.sent);
        auth=FirebaseAuth.getInstance();
        parentframmelayout=getActivity().findViewById(R.id.fl);
        goback=view.findViewById(R.id.back);
        p=view.findViewById(R.id.pb);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        regisemail.addTextChangedListener(new TextWatcher() {
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
        reset.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                gml.setVisibility(View.VISIBLE);
                gmltext.setVisibility(View.VISIBLE);
                reset.setEnabled(false);
                p.setVisibility(View.VISIBLE);
                reset.setBackgroundColor(getResources().getColor(R.color.peach));
                auth.sendPasswordResetEmail(regisemail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                           gml.setBackground(getResources().getDrawable(R.drawable.gmail));
                            p.setVisibility(View.GONE);
                           gmltext.setTextColor(getResources().getColor(R.color.green));
                           gmltext.setText(R.string.sent);
                            reset.setEnabled(false);
                            reset.setBackgroundColor(getResources().getColor(R.color.peach));
                        }
                        else
                        {   p.setVisibility(View.GONE);

                            String error=task.getException().getMessage();
                            gmltext.setText(error);
                            gmltext.setTextColor(getResources().getColor(R.color.colorAccent));
                            gmltext.setVisibility(View.VISIBLE);
                           }
                    }
                });
                p.setVisibility(View.GONE);

            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new signin());
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentframmelayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if(TextUtils.isEmpty(regisemail.getText()))
        {
            reset.setEnabled(false);
            reset.setBackgroundColor(getResources().getColor(R.color.peach));
        }
        else {
            reset.setEnabled(true);
            reset.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

    }
}