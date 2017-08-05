package mivadounou.projet2.uut.ucao.mivadounou.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.fragments.BottomNavFragment;
import mivadounou.projet2.uut.ucao.mivadounou.fragments.auth.ChooseAuthFragment;
import mivadounou.projet2.uut.ucao.mivadounou.fragments.create.NewRestauFragment;
import mivadounou.projet2.uut.ucao.mivadounou.fragments.user.UserRestauFragment;
import mivadounou.projet2.uut.ucao.mivadounou.models.Restau;
import mivadounou.projet2.uut.ucao.mivadounou.other.FirebaseRef;
import mivadounou.projet2.uut.ucao.mivadounou.services.NotificationService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    
    public static String TAG_BOTTOM_NAV_FRAGMENT = "TAG_BOTTOM_NAV_FRAGMENT";
    public static String TAG_NEW_RESTAU_FRAGMENT = "TAG_NEW_RESTAU_FRAGMENT";
    public static String TAG_NEW_MENU_FRAGMENT = "TAG_NEW_MENU_FRAGMENT";
    public static String TAG_PHONE_AUTH_FRAGMENT = "TAG_PHONE_AUTH_FRAGMENT";
    public static String TAG_USER_RESTAU_FRAGMENT = "TAG_USER_RESTAU_FRAGMENT";
    public static String TAG_CHOOSE_AUTH_FRAGMENT = "TAG_CHOOSE_AUTH_FRAGMENT";
    public static String TAG_EMAIL_PASSWORD_FRAGMENT = "TAG_EMAIL_PASSWORD_FRAGMENT";
    public static String TAG_GOOGLE_SIGNIN_FRAGMENT = "TAG_GOOGLE_SIGNIN_FRAGMENT";
    
    public static boolean isNewMenu = false;
    public static Activity msActivity;
    public static ProgressDialog mProgressDialog;
    public BottomNavFragment bottomNavFragment;
    public UserRestauFragment userRestauFragment;
    public String oldTag = null;
    
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private SharedPreferences userSharedPreferences;
    
    public static NotificationManager notificationManager;
    
    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    
    public static void showProgressDialog() {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }
    
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    public static Dialog mDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.msActivity);
        
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setTitle(title);
        
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    
                    }
                });
        
        return builder.create();
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        msActivity = this;
        
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        
        bottomNavFragment = new BottomNavFragment();
        userRestauFragment = new UserRestauFragment();
        
        mAuth = FirebaseAuth.getInstance();
        
        databaseReference = FirebaseDatabase.getInstance().getReference();
        
        userSharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        
//        Toast.makeText(msActivity, userSharedPreferences.getString(NewRestauFragment.RESTAU_KEY, null), Toast.LENGTH_SHORT).show();
        
        if (userSharedPreferences.getString(NewRestauFragment.RESTAU_KEY, null) != null
                && mAuth.getCurrentUser() != null) {
            
            Intent intent = new Intent(this, NotificationService.class);
            intent.putExtra(FirebaseRef.RESTAU_COMMANDE_NOTIFICATIONS,
                    userSharedPreferences.getString(NewRestauFragment.RESTAU_KEY, null));
            intent.putExtra(FirebaseRef.USER_COMMANDE_NOTIFICATIONS,
                    mAuth.getCurrentUser().getUid());
            startService(intent);
            
        } else if (userSharedPreferences.getString(NewRestauFragment.RESTAU_KEY, null) != null) {
            
            Intent intent = new Intent(this, NotificationService.class);
            intent.putExtra(FirebaseRef.USER_COMMANDE_NOTIFICATIONS,
                    userSharedPreferences.getString(NewRestauFragment.RESTAU_KEY, null));
            startService(intent);
            
        } else if (mAuth.getCurrentUser() != null) {
            
            Intent intent = new Intent(this, NotificationService.class);
            intent.putExtra(FirebaseRef.USER_COMMANDE_NOTIFICATIONS,
                    mAuth.getCurrentUser().getUid());
            stopService(intent);
            startService(intent);
            
        }
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_frame_container, userRestauFragment, MainActivity.TAG_USER_RESTAU_FRAGMENT)
                .commit();
        
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_frame_container, bottomNavFragment, MainActivity.TAG_BOTTOM_NAV_FRAGMENT)
                .commit();
        
        init();
    }
    
    public void findIfUserHasRestau() {
        
        MainActivity.mProgressDialog.setMessage("Veillez Patientez ...");
        MainActivity.showProgressDialog();
        
        DatabaseReference userRestauRef = databaseReference
                .child(FirebaseRef.USER_RESTAUS)
                .child(mAuth.getCurrentUser().getUid());
        
        userRestauRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    
                    String value = dataSnapshot.getValue().toString();
                    String userRestauKey = value.substring(1, value.indexOf("="));
                    
                    userSharedPreferences.edit().putString(NewRestauFragment.RESTAU_KEY, userRestauKey).apply();
                    userSharedPreferences.edit().putBoolean(NewRestauFragment.RESTAU_CREATED, true).apply();
                    userSharedPreferences.edit().putBoolean(NewRestauFragment.FIRST_TIME_USER_RESTAU, false).apply();
                    
                    getRestauByKey(userRestauKey);
                    
                } else {
                    
                    MainActivity.hideProgressDialog();
                    findWhereGo();
                    
                }
            }
            
            @Override
            public void onCancelled(DatabaseError databaseError) {
            
            }
        });
    }
    
    private void getRestauByKey(String key) {
        
        DatabaseReference restauRef = databaseReference.child(FirebaseRef.RESTAU).child(key);
        
        restauRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                
                Restau restau = dataSnapshot.getValue(Restau.class);
                
                userSharedPreferences.edit().putString(NewRestauFragment.RESTAU_TITLE, restau.getTitle()).apply();
                
                findWhereGo();
            }
            
            @Override
            public void onCancelled(DatabaseError databaseError) {
            
            }
        });
    }
    
    private void findWhereGo() {
        
        if (userSharedPreferences.getBoolean(NewRestauFragment.RESTAU_CREATED, false)) {
            
            MainActivity.hideProgressDialog();
            
            updateToolbarText(userSharedPreferences.getString(NewRestauFragment.RESTAU_TITLE, ""));
            
            hideAndShow(MainActivity.TAG_USER_RESTAU_FRAGMENT, new UserRestauFragment());
            
        } else {
            
            updateToolbarText("Gérer un Restaurant");
            
            hideAndShow(MainActivity.TAG_NEW_RESTAU_FRAGMENT, new NewRestauFragment());
        }
    }
    
    private void init() {
        SharedPreferences shared = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = shared.edit();
        editor.putString("splash", "non");
        editor.apply();
        
        String valeur = shared.getString("splash", "rien");
        Log.i("VALEUR DE LA PREF : ", "VALEUR :" + valeur);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

//        MenuItem homeItem = mBottomNav.getMenu().getItem(0);
//        if (mSelectedItem != homeItem.getItemId()) {
//            // select home item
//            selectFragment(homeItem);
//        } else {
//            super.onBackPressed();
//        }
    }
    
    public void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }
    
    private int getColorFromRes(@ColorRes int resId) {
        return ContextCompat.getColor(this, resId);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(this, "Setting Toast", Toast.LENGTH_LONG).show();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    public void hideAndShow(String tag, Fragment newFragment) {
        
        if (tag.equals(MainActivity.TAG_BOTTOM_NAV_FRAGMENT)) {
            if (newFragment != null) {
                
                if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(getSupportFragmentManager().findFragmentByTag(tag))
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(userRestauFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_BOTTOM_NAV_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    return;
                    
                } else {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(userRestauFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_BOTTOM_NAV_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                }
                
                return;
                
            } else {
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(getSupportFragmentManager().findFragmentByTag(tag))
                        .commit();
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(userRestauFragment)
                        .commit();
                
                if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                        && !oldTag.equals(MainActivity.TAG_BOTTOM_NAV_FRAGMENT)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                            .commit();
                }
                
            }
            
            return;
        }
        
        if (tag.equals(MainActivity.TAG_NEW_RESTAU_FRAGMENT)) {
            if (newFragment != null) {
                
                if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(getSupportFragmentManager().findFragmentByTag(tag))
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(userRestauFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_NEW_RESTAU_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    return;
                    
                } else {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(userRestauFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_NEW_RESTAU_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                }
                
                return;
                
            } else {
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(getSupportFragmentManager().findFragmentByTag(tag))
                        .commit();
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(userRestauFragment)
                        .commit();
                
                if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                        && !oldTag.equals(MainActivity.TAG_NEW_RESTAU_FRAGMENT)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                            .commit();
                }
                
            }
            
            return;
        }
        
        if (tag.equals(MainActivity.TAG_NEW_MENU_FRAGMENT)) {
            
            if (newFragment != null) {
                
                if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(getSupportFragmentManager().findFragmentByTag(tag))
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(bottomNavFragment)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(userRestauFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_NEW_MENU_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    return;
                } else {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(bottomNavFragment)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(userRestauFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_NEW_MENU_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                }
                return;
                
            } else {
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(bottomNavFragment)
                        .commit();
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(userRestauFragment)
                        .commit();
                
                if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                        && !oldTag.equals(MainActivity.TAG_NEW_MENU_FRAGMENT)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                            .commit();
                }
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(getSupportFragmentManager().findFragmentByTag(tag))
                        .commit();
            }
            
            return;
        }
        
        if (tag.equals(MainActivity.TAG_EMAIL_PASSWORD_FRAGMENT)) {
            
            if (newFragment != null) {
                
                if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(getSupportFragmentManager().findFragmentByTag(tag))
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(bottomNavFragment)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(userRestauFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_EMAIL_PASSWORD_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    return;
                } else {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(bottomNavFragment)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(userRestauFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_EMAIL_PASSWORD_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                }
                return;
                
            } else {
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(bottomNavFragment)
                        .commit();
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(userRestauFragment)
                        .commit();
                
                if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                        && !oldTag.equals(MainActivity.TAG_EMAIL_PASSWORD_FRAGMENT)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                            .commit();
                }
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(getSupportFragmentManager().findFragmentByTag(tag))
                        .commit();
            }
            
            return;
        }
        
        if (tag.equals(MainActivity.TAG_GOOGLE_SIGNIN_FRAGMENT)) {
            
            if (newFragment != null) {
                
                if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(getSupportFragmentManager().findFragmentByTag(tag))
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(bottomNavFragment)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(userRestauFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_GOOGLE_SIGNIN_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    return;
                } else {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(bottomNavFragment)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(userRestauFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_GOOGLE_SIGNIN_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                }
                return;
                
            } else {
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(bottomNavFragment)
                        .commit();
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(userRestauFragment)
                        .commit();
                
                if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                        && !oldTag.equals(MainActivity.TAG_GOOGLE_SIGNIN_FRAGMENT)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                            .commit();
                }
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(getSupportFragmentManager().findFragmentByTag(tag))
                        .commit();
                
            }
            
            return;
        }
        
        if (tag.equals(MainActivity.TAG_CHOOSE_AUTH_FRAGMENT)) {
            
            if (newFragment != null) {
                
                if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(getSupportFragmentManager().findFragmentByTag(tag))
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(bottomNavFragment)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(userRestauFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_CHOOSE_AUTH_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    return;
                } else {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(bottomNavFragment)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(userRestauFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_CHOOSE_AUTH_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                }
                return;
                
            } else {
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(bottomNavFragment)
                        .commit();
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(userRestauFragment)
                        .commit();
                
                if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                        && !oldTag.equals(MainActivity.TAG_CHOOSE_AUTH_FRAGMENT)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                            .commit();
                }
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(getSupportFragmentManager().findFragmentByTag(tag))
                        .commit();
            }
            
            return;
        }
        
        if (tag.equals(MainActivity.TAG_PHONE_AUTH_FRAGMENT)) {
            
            if (newFragment != null) {
                
                if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(getSupportFragmentManager().findFragmentByTag(tag))
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(bottomNavFragment)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(userRestauFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_PHONE_AUTH_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    return;
                    
                } else {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(bottomNavFragment)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(userRestauFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_PHONE_AUTH_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                }
                return;
                
            } else {
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(bottomNavFragment)
                        .commit();
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(userRestauFragment)
                        .commit();
                
                if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                        && !oldTag.equals(MainActivity.TAG_PHONE_AUTH_FRAGMENT)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                            .commit();
                }
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(getSupportFragmentManager().findFragmentByTag(tag))
                        .commit();
            }
            
            return;
        }
        
        if (tag.equals(MainActivity.TAG_USER_RESTAU_FRAGMENT)) {
            
            if (newFragment != null) {
                if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(getSupportFragmentManager().findFragmentByTag(tag))
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(bottomNavFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_USER_RESTAU_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    return;
                } else {
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(bottomNavFragment)
                            .commit();
                    
                    if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                            && !oldTag.equals(MainActivity.TAG_USER_RESTAU_FRAGMENT)) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                                .commit();
                    }
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_container, newFragment, tag)
                            .commit();
                }
                
            } else {
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(bottomNavFragment)
                        .commit();
                
                if (oldTag != null && getSupportFragmentManager().findFragmentByTag(oldTag) != null
                        && !oldTag.equals(MainActivity.TAG_USER_RESTAU_FRAGMENT)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(getSupportFragmentManager().findFragmentByTag(oldTag))
                            .commit();
                }
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(getSupportFragmentManager().findFragmentByTag(tag))
                        .commit();
            }
        }
    }
    
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            
            case R.id.nav_camera:
                
                updateToolbarText("Acceuill");
                
                hideAndShow(MainActivity.TAG_BOTTOM_NAV_FRAGMENT, new BottomNavFragment());
                
                break;
            
            case R.id.nav_gallery:
                
                mAuth = FirebaseAuth.getInstance();
                
                if (mAuth.getCurrentUser() != null) {
                    
                    if (userSharedPreferences.getBoolean(NewRestauFragment.FIRST_TIME_USER_RESTAU, true)) {
                        
                        findIfUserHasRestau();
                        
                    } else {
                        
                        findWhereGo();
                        
                    }
                    
                } else {
                    
                    updateToolbarText("Authentification");
                    
                    oldTag = MainActivity.TAG_CHOOSE_AUTH_FRAGMENT;
                    
                    hideAndShow(MainActivity.TAG_CHOOSE_AUTH_FRAGMENT, new ChooseAuthFragment());
                }
                
                break;
//            case R.id.nav_slideshow:
//                break;
//
//            case R.id.nav_share:
//                break;
//
//            case R.id.nav_send:
//                break;
        }
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawer(GravityCompat.START);
            }
        }, 40);
        return true;
    }
}
