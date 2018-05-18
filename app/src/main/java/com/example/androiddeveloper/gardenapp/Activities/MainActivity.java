package com.example.androiddeveloper.gardenapp.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.androiddeveloper.gardenapp.BeaconService;
import com.example.androiddeveloper.gardenapp.Fragments.BookGarden;
import com.example.androiddeveloper.gardenapp.Fragments.MapActivity;
import com.example.androiddeveloper.gardenapp.Fragments.SavePhoto;
import com.example.androiddeveloper.gardenapp.Fragments.galleryFragment;
import com.example.androiddeveloper.gardenapp.Models.ImageUploadInfo;
import com.example.androiddeveloper.gardenapp.ImageViewer;
import com.example.androiddeveloper.gardenapp.LoginActivity;
import com.example.androiddeveloper.gardenapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private RecyclerView recyclerView;
    //  private AlbumsAdapter adapter;
    // private List<Album> albumList;
    Context c;
    private static final int LOC_PERM_REQ_CODE = 1;
    ImageButton imgbtnfull, imgcam;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    private String userChoosenTask;
    DatabaseReference databaseReference;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter;

    // Creating Progress dialog
    ProgressDialog progressDialog;

    // Creating List of ImageUploadInfo class.
    List<ImageUploadInfo> list = new ArrayList<>();
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    private Drawer result = null;
    TextView txtview;
    TextView txtview1;
    ImageView imgview;
    Button btnnext, home;
    boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String nameText = prefs.getString("name", null);
        String emailText = prefs.getString("email", null);
        editor = prefs.edit();

        txtview = (TextView) findViewById(R.id.txttitle);
        txtview1 = (TextView) findViewById(R.id.txtdetail);
        imgview = (ImageView) findViewById(R.id.img);
        btnnext = (Button) findViewById(R.id.btnnext);
        home = (Button) findViewById(R.id.home);

        home = (Button) findViewById(R.id.home);
        imgbtnfull = (ImageButton) findViewById(R.id.imgfullbtn);
        imgcam = (ImageButton) findViewById(R.id.imgbtncam);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                result.openDrawer();

            }
        });
        imgbtnfull = (ImageButton) findViewById(R.id.imgfullbtn);
        imgbtnfull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ImageViewer.class);
                startActivity(i);

            }
        });
        imgcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UploadImage.class);
                startActivity(i);

            }
        });
        /////////////////////Drawer
        // new DrawerBuilder().withActivity(this).build();
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.primary)
                .addProfiles(
                        new ProfileDrawerItem().withName(nameText).withEmail(emailText).withIcon(getResources().getDrawable(R.drawable.profile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withDrawerWidthDp(250)
                .withHasStableIds(true)
                .withShowDrawerOnFirstLaunch(false)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_items).withIcon(R.drawable.knowus)
                                .withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName("Delete Account").withIcon(R.drawable.delete)
                                .withIdentifier(4).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_items3).withIcon(R.drawable.logout)
                                .withIdentifier(3).withSelectable(false))

                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                result.closeDrawer();
                                Intent i = new Intent(MainActivity.this, Aboutus.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            } else if (drawerItem.getIdentifier() == 3) {
                                FirebaseAuth.getInstance().signOut();

                                prefs.edit().clear().commit();
                                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                result.closeDrawer();

                            } else if (drawerItem.getIdentifier() == 4) {
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:

                                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                                DatabaseReference delete = firebaseDatabase.getReference().child("users").child(user.getUid());
                                                delete.removeValue();
                                                user.delete();
                                                FirebaseAuth.getInstance().signOut();

                                                prefs.edit().clear().commit();


                                                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);
                                                finish();
                                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                                result.closeDrawer();
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                result.closeDrawer();
                                                break;
                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Are you sure you want to delete Account?").setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();


                                // Get auth credentials from the user for re-authentication. The example below shows
                                // email and password credentials but there are multiple possible providers,
                                // such as GoogleAuthProvider or FacebookAuthProvider.


                            }
                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Gallery", R.drawable.gallery, R.color.colorAccent);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Photos Library", R.drawable.savephoto, R.color.colorAccent);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Map", R.drawable.map, R.color.colorAccent);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Booking", R.drawable.booking, R.color.colorAccent);
        AHBottomNavigationItem item0 = new AHBottomNavigationItem("Home", R.drawable.home, R.color.colorAccent);
// Add items
        bottomNavigation.addItem(item0);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.setAccentColor(R.color.black);
        bottomNavigation.setInactiveColor(R.color.black);

        bottomNavigation.setDefaultBackgroundResource(R.drawable.bottomback1);
        //  bottomNavigation.setDefaultBackgroundColor(R.color.primary);
        //  bottomNavigation.setColored(true);
        //   bottomNavigation.setBehaviorTranslationEnabled(false);
        //   bottomNavigation.setTranslucentNavigationEnabled(true);
        //  bottomNavigation.setColored(true);

        //bottomNavigation.setNotification(notification, 1);
        //    bottomNavigation.enableItemAtPosition(4);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {

            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (position == 3) {
                    BookGarden bookGarden = new BookGarden();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_con, bookGarden).commit();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
                if (position == 2) {

                    MapActivity mapActivity = new MapActivity();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_con, mapActivity).commit();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            /*       LocationAlertActivity mapActivity = new LocationAlertActivity();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_con, mapActivity).commit();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);*/
                }
                if (position == 1) {
                    SavePhoto savePhoto = new SavePhoto();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_con, savePhoto).commit();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    //   Toast.makeText(getApplicationContext(), "Please tell us what to do here?", Toast.LENGTH_SHORT).show();
                }
                if (position == 0) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                    //    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
                if (position == 4) {
                    galleryFragment galleryfragment = new galleryFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_con, galleryfragment).commit();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    //    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
                return true;
            }
        });
     /*   recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
*/
        if (isLocationAccessPermitted()) {
            requestLocationAccessPermission();
        }
        startService(new Intent(getApplicationContext(), BeaconService.class));

    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            stopService(new Intent(getApplicationContext(), BeaconService.class));
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
    private boolean isLocationAccessPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                LOC_PERM_REQ_CODE);
    }

}
