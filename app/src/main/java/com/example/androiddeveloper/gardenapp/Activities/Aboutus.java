package com.example.androiddeveloper.gardenapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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

/**
 * Created by Android Developer on 4/12/2018.
 */

public class Aboutus extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    private Drawer result = null;
    Button home;
    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.know_us);
        context = getApplicationContext();
        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String nameText = prefs.getString("name", null);
        String emailText = prefs.getString("email", null);
        editor = prefs.edit();
        home = (Button) findViewById(R.id.homeabout);

        ImageView logo = (ImageView) findViewById(R.id.imageView);
        ImageView fb = (ImageView) findViewById(R.id.imageView2);
        ImageView twi = (ImageView) findViewById(R.id.imageView3);
        ImageView insta = (ImageView) findViewById(R.id.imageView4);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.rhs.org.uk/gardens/wisley";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://m.facebook.com/rhswisley/?locale2=en_GB";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        twi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://twitter.com/RHSWisley?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/The_RHS/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


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
                                Intent i = new Intent(Aboutus.this, Aboutus.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            } else if (drawerItem.getIdentifier() == 3) {
                                FirebaseAuth.getInstance().signOut();

                                prefs.edit().clear().commit();
                                Intent i = new Intent(Aboutus.this, LoginActivity.class);
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
                                                Intent i = new Intent(Aboutus.this, LoginActivity.class);
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

                                AlertDialog.Builder builder = new AlertDialog.Builder(Aboutus.this);
                                builder.setMessage("Are you sure you want to delete Account?").setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();


                                // Get auth credentials from the user for re-authentication. The example below shows
                                // email and password credentials but there are multiple possible providers,
                                // such as GoogleAuthProvider or FacebookAuthProvider.


                            }
                            if (intent != null) {
                                Aboutus.this.startActivity(intent);
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.openDrawer();
            }
        });


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent i = new Intent(Aboutus.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);


    }
}