package objavi.samo.android.studentskiposlovi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import objavi.samo.android.studentskiposlovi.fragments.HomeFragment;
import objavi.samo.android.studentskiposlovi.fragments.InfoFragment;
import objavi.samo.android.studentskiposlovi.fragments.ProfileFragment;
import objavi.samo.android.studentskiposlovi.fragments.SettingsFragment;
import objavi.samo.android.studentskiposlovi.utils.BottomNavigationViewHelper;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    final Fragment homeFragment = new HomeFragment();
    final Fragment profileFragment = new ProfileFragment();
    final Fragment settingsFragment = new SettingsFragment();
    final Fragment infoFragment = new InfoFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment;



    private Context mContext = HomeActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting.");
        System.out.println(TAG + " onCreate started");

        //**********************************For fragments**********************************************
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_navigation_view_bar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(mNavigationListener);

        fm.beginTransaction().add(R.id.main_container, infoFragment, getString(R.string.fragment_info)).hide(infoFragment).commit();
        fm.beginTransaction().add(R.id.main_container, settingsFragment, getString(R.string.fragment_settings)).hide(settingsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, profileFragment, getString(R.string.fragment_profile)).hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFragment, getString(R.string.fragment_home)).commit();
        //**********************************For fragments**********************************************

        if(!isConnected(mContext)){
            showNoInternetDialog(); // no-internet dialog to show
        }

    }

    private BottomNavigationViewEx.OnNavigationItemSelectedListener mNavigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    fm.beginTransaction().hide(active).show(homeFragment).commit();
                    active = homeFragment;
                    return true;

                case R.id.menu_profile:
                    fm.beginTransaction().hide(active).show(profileFragment).commit();
                    active = profileFragment;
                    return true;

                case R.id.menu_add:
                    startActivity(new Intent(mContext, AddJobActivity.class));
                    //overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up); --> alternative animation
                    overridePendingTransition(0,0);
                    break;

                case R.id.menu_settings:
                    fm.beginTransaction().hide(active).show(settingsFragment).commit();
                    active = settingsFragment;
                    return true;

                case R.id.menu_info:
                    fm.beginTransaction().hide(active).show(infoFragment).commit();
                    active = infoFragment;
                    return true;
            }
            return false;
        }
    };

    private void toastMessage(String message){
        Toast.makeText(HomeActivity.this,message,Toast.LENGTH_LONG).show();
    }

    private void showNoInternetDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_no_internet);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), ((AppCompatButton) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        } else
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!isConnected(mContext)){
            showNoInternetDialog(); // no internet dialog to show
        }
    }

}
