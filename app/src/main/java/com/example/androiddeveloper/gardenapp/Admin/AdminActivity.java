package com.example.androiddeveloper.gardenapp.Admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androiddeveloper.gardenapp.Activities.UploadImage;
import com.example.androiddeveloper.gardenapp.LoginActivity;
import com.example.androiddeveloper.gardenapp.R;

public class AdminActivity extends AppCompatActivity {
    Button imagereq, regusers, bookingrequests, addimages, logout;
    boolean doubleBackToExitPressedOnce = false;
    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);


        editor = prefs.edit();
        if (!prefs.getString("id", "").isEmpty()) {
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
            AdminActivity.this.finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

        imagereq = (Button) findViewById(R.id.imgrequest);
        regusers = (Button) findViewById(R.id.regusers);
        bookingrequests = (Button) findViewById(R.id.bookingrequest);
        addimages = (Button) findViewById(R.id.addimages);
        ;
        logout = (Button) findViewById(R.id.logout);
        ;


        imagereq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "image Request Clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AdminActivity.this, DisplayImagesActivity.class);
                startActivity(i);


            }
        });


        regusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "reqistered users Clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AdminActivity.this, RegisteredUsers.class);
                startActivity(i);

            }
        });


        bookingrequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Booking Requests Clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AdminActivity.this, BookingRequests.class);
                startActivity(i);

            }
        });

        addimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Add Images to Gallery", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AdminActivity.this, UploadImage.class);
                i.putExtra("folder", "gallery");
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FirebaseAuth.getInstance().signOut();


                Intent i = new Intent(AdminActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
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
}
