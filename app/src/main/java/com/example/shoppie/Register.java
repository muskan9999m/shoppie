package com.example.shoppie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class Register extends AppCompatActivity {
    private FrameLayout frameLayout;
    public static boolean setSignupFragment=false;
    public static boolean onResetFragment=false;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK)
        {signin.disablebutton=false;
        signup.disableclosebutton=false;

            if(onResetFragment)
            {
                onResetFragment=false;
                setFragment(new signin());
               return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        frameLayout=(FrameLayout)findViewById(R.id.fl);
        if(setSignupFragment)
        {setSignupFragment=false;
            setDefaultFragment(new signup());}
        else
        setDefaultFragment(new signin());
    }

    private void setDefaultFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
    //    fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}