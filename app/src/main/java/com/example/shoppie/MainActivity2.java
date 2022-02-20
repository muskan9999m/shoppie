package com.example.shoppie;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.example.shoppie.Register.setSignupFragment;

public class MainActivity2 extends AppCompatActivity {
    private FrameLayout frameLayout;
    private int scrollFlags;
    private FirebaseUser curruser;
    public static DrawerLayout drawer;
    private ImageView noiconnection;
    public static Boolean showcart=false;
    private NavigationView navigationView;
    private static final int HOME_FRAGMENT=0;
    private static final int CART_FRAGMENT=1;
    private static final int ORDERS_FRAGMENT=2;
    private static final int WISHLIST_FRAGMENT=3;
    private static final int REWARDS_FRAGMENT=4;
    private static final int ACCOUNT_FRAGMENT=5;
    Toolbar toolbar;
    private AppBarLayout.LayoutParams params;
    private ImageView actionBarlogo;
    private Window window;
    private Dialog signinDialog;
    public static Activity mainActivity;
    private TextView badgecount;
    public static boolean resetmainact=false;


    private  int currentFragment=-1;

    private AppBarConfiguration mAppBarConfiguration;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        noiconnection=findViewById(R.id.noiconnection);



        frameLayout=(FrameLayout)findViewById(R.id.framel) ;
        actionBarlogo=(ImageView)findViewById(R.id.action_bar_logo) ;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        params= (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags=params.getScrollFlags();

       // setFragment(new orderDetailsFragment(),HOME_FRAGMENT);
        navigationView.getMenu().getItem(0).setChecked(true);
       // navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        if(showcart)
        {
            mainActivity=this;
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotofragment("My Cart",new myCartFragment(),-2);
        }
        else {

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            setFragment(new homesFragment(), HOME_FRAGMENT);
        }
        signinDialog=new Dialog(MainActivity2.this);
        signinDialog.setContentView(R.layout.signin_dialog);
        signinDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        signinDialog.setCancelable(true);
        final Button signinbutton=signinDialog.findViewById(R.id.signinbutton);
        Button signupbutton=signinDialog.findViewById(R.id.signupbutton);
        final Intent i=new Intent(MainActivity2.this,Register.class);
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin.disablebutton=true;
                signup.disableclosebutton=true;
                signinDialog.dismiss();
                setSignupFragment=false;
                startActivity(i);
            }
        });
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup.disableclosebutton=true;
                signin.disablebutton=true;
                signinDialog.dismiss();
                setSignupFragment=true;
                startActivity(i);
            }
        });
        final MenuItem[] m = new MenuItem[1];
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawer(GravityCompat.START);
                m[0] =item;
                if(curruser!=null) {
                    drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                        @Override
                        public void onDrawerClosed(View drawerView) {
                            super.onDrawerClosed(drawerView);
                            int id= m[0].getItemId();
                            if (id == R.id.my_mall) {

                                actionBarlogo.setVisibility(View.VISIBLE);
                                invalidateOptionsMenu();
                                setFragment(new homesFragment(), HOME_FRAGMENT);
                            } else if (id == R.id.my_rewards) {
                                gotofragment("My Rewards", new myRewardsFragment(), REWARDS_FRAGMENT);
                            } else if (id == R.id.my_cart) {
                                gotofragment("My Cart", new myCartFragment(), CART_FRAGMENT);
                            } else if (id == R.id.my_orders) {
                                gotofragment("My oeders", new myOrdersFragment(), ORDERS_FRAGMENT);
                            } else if (id == R.id.my_wishlist) {
                                gotofragment("My Wishlist", new myWishlistFragment(), WISHLIST_FRAGMENT);
                            } else if (id == R.id.my_account) {
                                gotofragment("My Account", new myAccountFragment(), ACCOUNT_FRAGMENT);
                            } else if (id == R.id.signout) {
                                FirebaseAuth.getInstance().signOut();
                                DBqueries.clearData();
                                Intent rintent=new Intent(MainActivity2.this,Register.class);
                                startActivity(rintent);
                                finish();
                            }
                        }
                    });

                    return true;
                }
                else
                {
                    signinDialog.show();
                    return false;
                }

            }
        });




    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();

        curruser= FirebaseAuth.getInstance().getCurrentUser();
        if(resetmainact)
        {
            resetmainact=false;
            actionBarlogo.setVisibility(View.VISIBLE);
            setFragment(new homesFragment(),HOME_FRAGMENT);
            navigationView.getMenu().getItem(0).setChecked(true);
        }


        if(curruser == null)
        {
            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(false);
        }
        else {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);

        }
        invalidateOptionsMenu();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setFragment(Fragment fragment, int fragmentno) {
        if(fragmentno!=currentFragment) {
            if(fragmentno==REWARDS_FRAGMENT)
            {
                toolbar.setBackgroundColor(getResources().getColor(R.color.purple));
                window.setStatusBarColor(getResources().getColor(R.color.purple));
            }
            else
            {
                toolbar.setBackgroundColor(getResources().getColor(R.color.peach));
                window.setStatusBarColor(getResources().getColor(R.color.peach));
            }
            currentFragment = fragmentno;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(currentFragment==HOME_FRAGMENT)
            {
               super.onBackPressed();
                currentFragment=-1;
                //finishAffinity();
            }
            else{  if(showcart)
            {
                mainActivity=null;
                showcart=false;
                finish();
            }
else{
                actionBarlogo.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
                setFragment(new homesFragment(),HOME_FRAGMENT);
                navigationView.getMenu().getItem(0).setChecked(true);}}

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(currentFragment==HOME_FRAGMENT)
        { getSupportActionBar().setDisplayShowTitleEnabled(false);
        getMenuInflater().inflate(R.menu.main_activity2, menu);

            MenuItem cartitem=menu.findItem(R.id.cart);
            cartitem.setActionView(R.layout.badge_layout);
            ImageView badgeicon=cartitem.getActionView().findViewById(R.id.badge_icon);
            badgecount=cartitem.getActionView().findViewById(R.id.badge_count);

            if(curruser!=null)
            {
                if (DBqueries.cartlist.size() == 0) {
                    DBqueries.loadcartlist(MainActivity2.this, new Dialog(MainActivity2.this), false,badgecount,new TextView(this));
                }
                else{
                    badgecount.setVisibility(View.VISIBLE);

                    if (DBqueries.cartlist.size() < 99)
                        badgecount.setText(String.valueOf(DBqueries.cartlist.size()));
                    else
                        badgecount.setText("99");}
            }



            cartitem.getActionView().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    if(curruser==null) {
                        signinDialog.show();
                    }
                    else {
                        gotofragment("My Cart", new myCartFragment(), CART_FRAGMENT);
                    }
                }
            });


        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.searchicon)
        {return true;}
        else if(id==R.id.bell)
        {return true;}
        else if(id==R.id.cart)
        {
            if(curruser==null) {
                signinDialog.show();
            }
            else {
                gotofragment("My Cart", new myCartFragment(), CART_FRAGMENT);
            }
            return true;}
        else  if(id==android.R.id.home)
        {
            if(showcart)
            {
                mainActivity=null;
                showcart=false;
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void
    gotofragment(String title, Fragment fragment, int fragmentNo) {
        actionBarlogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment,fragmentNo);
        if(fragmentNo==CART_FRAGMENT || showcart) {
            navigationView.getMenu().getItem(3).setChecked(true);
            params.setScrollFlags(0);
        }else{params.setScrollFlags(scrollFlags);}
         if(fragmentNo==WISHLIST_FRAGMENT) {
            navigationView.getMenu().getItem(4).setChecked(true);
        }
        else if(fragmentNo==REWARDS_FRAGMENT) {
            navigationView.getMenu().getItem(2).setChecked(true);}
             else if(fragmentNo==ACCOUNT_FRAGMENT) {
                navigationView.getMenu().getItem(5).setChecked(true);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



}