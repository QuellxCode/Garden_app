package com.example.androiddeveloper.gardenapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddeveloper.gardenapp.Activities.MainActivity;
import com.example.androiddeveloper.gardenapp.Admin.AdminActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isSigninScreen = true;
    private static final int RC_SIGN_IN = 9001;
    private TextView tvSignupInvoker;
    private LinearLayout llSignup;
    private TextView tvSigninInvoker;
    private LinearLayout llSignin;
    Button btnSignup;
    RadioButton rbtnuser, rbtnadmin;
    private Button btnSignin;
    private FirebaseAuth gauth;
    private static FirebaseAuth mAuth;
    String uptxtemail, uptxtpass, uptxtname, uptxtsname, txtemail, txtpass;
    LinearLayout llsignin, llsignup;

    private static final String TAG = "SignupActivity";
    private ProgressDialog pDialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @BindView(R.id.uptxtname)
    EditText _uptxtname;
    @BindView(R.id.uptxtsname)
    EditText _uptxtsname;
    @BindView(R.id.txtemail)
    EditText _emailText;
    @BindView(R.id.txtpass)
    EditText _passwordText;
    @BindView(R.id.uptxtemail)
    EditText _emailupText;
    @BindView(R.id.uptxtpass)
    EditText _passwordupText;
    int i = 1;
    int role = 0;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);


        editor = prefs.edit();
        if (!prefs.getString("id", "").isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, SplashScreen.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
            window.setNavigationBarColor(getResources().getColor(R.color.primary_dark));
        }
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
        llSignin = (LinearLayout) findViewById(R.id.llSignin);
        llSignin.setOnClickListener(this);


        llsignup = (LinearLayout) findViewById(R.id.llSignup);
        llsignup.setOnClickListener(this);
        tvSignupInvoker = (TextView) findViewById(R.id.tvSignupInvoker);
        tvSigninInvoker = (TextView) findViewById(R.id.tvSigninInvoker);
        rbtnadmin = (RadioButton) findViewById(R.id.radioadmin);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignin = (Button) findViewById(R.id.btnSignin);

        llSignup = (LinearLayout) findViewById(R.id.llSignup);
        llSignin = (LinearLayout) findViewById(R.id.llSignin);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (role == 1) {
                    loginadmin();
                } else {
                    login();
                }

            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        rbtnadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (role == 0) {
                    role = 1;
                } else {
                    rbtnadmin.setChecked(false);
                    role = 0;
                }


            }
        });

        tvSignupInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSigninScreen = false;
                showSignupForm();
            }
        });

        tvSigninInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSigninScreen = true;
                showSigninForm();
            }
        });
        showSigninForm();
    }

    public void login() {
        Log.d(TAG, "Login");
        txtemail = _emailText.getText().toString().trim();
        txtpass = _passwordText.getText().toString().trim();
        if (validate1() == false) {
            onLoginFailed();
            return;
        }

        loginByServer();
    }

    public void loginadmin() {
        Log.d(TAG, "Login");
        txtemail = _emailText.getText().toString().trim();
        txtpass = _passwordText.getText().toString().trim();
        if (validate1() == false) {
            onLoginFailed();
            return;
        }

        loginByServeradmin();
    }

    private void loginByServeradmin() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Creating Account...");
        pDialog.setCancelable(false);

        signinexiestadmin(txtemail, txtpass);
        showpDialog1();
        hidepDialog1();

    }

    private void loginByServer() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Creating Account...");
        pDialog.setCancelable(false);

        signinexiestusers(txtemail, txtpass);
        showpDialog1();
        hidepDialog1();

    }

    private void showpDialog1() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog1() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onLoginSuccess() {
        btnSignin.setEnabled(true);
        Toast.makeText(LoginActivity.this, "login successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
//        hidepDialog1();
        btnSignin.setEnabled(true);
    }

    public boolean validate1() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            requestFocus(_emailText);
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Password is empty");
            requestFocus(_passwordText);
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

    }

    public void signup() {
        Log.d(TAG, "Signup");
        if (validate() == false) {
            onSignupFailed();
            return;
        }

        saveToServerDB();
        // onSignupSuccess();

    }

    public void onSignupSuccess() {
        Toast.makeText(LoginActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign Up failed", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        String uptxtemail = _emailupText.getText().toString();
        String uptxtpass = _passwordupText.getText().toString();
        String uptxtname = _uptxtname.getText().toString();
        String uptxtsname = _uptxtsname.getText().toString();

        if (uptxtemail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(uptxtemail).matches()) {
            _emailupText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailupText.setError(null);
        }


        if (uptxtpass.isEmpty() || uptxtpass.length() < 5 || uptxtpass.length() > 10) {
            _passwordupText.setError("between 6 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordupText.setError(null);
        }
        if (uptxtname.isEmpty() || uptxtname.length() < 3) {
            _uptxtname.setError("enter name");
            valid = false;
        } else {
            _uptxtname.setError(null);
        }
        if (uptxtsname.isEmpty() || uptxtsname.length() < 3) {
            _uptxtsname.setError("enter surname");
            valid = false;
        } else {
            _uptxtsname.setError(null);
        }

        return valid;
    }

    private void saveToServerDB() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Creating Account...");
        pDialog.setCancelable(false);

        showpDialog();
        registeruser();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showSignupForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.15f;
        llSignin.requestLayout();
        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.85f;
        llSignup.requestLayout();
        tvSignupInvoker.setVisibility(View.GONE);
        tvSigninInvoker.setVisibility(View.VISIBLE);
        Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right_to_left);
        llSignup.startAnimation(translate);
        Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_right_to_left);
        btnSignup.startAnimation(clockwise);
    }

    private void showSigninForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.85f;
        llSignin.requestLayout();
        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.15f;
        llSignup.requestLayout();
        Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left_to_right);
        llSignin.startAnimation(translate);
        tvSignupInvoker.setVisibility(View.VISIBLE);
        tvSigninInvoker.setVisibility(View.GONE);
        Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_left_to_right);
        btnSignin.startAnimation(clockwise);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llSignin || v.getId() == R.id.llSignup) {
            InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void registeruser() {
        uptxtemail = _emailupText.getText().toString().trim();
        uptxtpass = _passwordupText.getText().toString().trim();
        uptxtname = _uptxtname.getText().toString().trim();
        uptxtsname = _uptxtsname.getText().toString().trim();
        /*DatabaseReference Email= database.getReference("email");
        DatabaseReference pass= database.getReference("password");
        Email.setValue(uptxtemail);
        pass.setValue(uptxtpass);*/
        createAccount(uptxtemail, uptxtpass);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    int dum = 1;

    public void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)   //mAuth is Firebase reference and its calling its builtin function to take email and pass and create account. This is authorization usage.
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {  //This is called if user is created
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");



                            FirebaseUser user = mAuth.getCurrentUser();  // getting the current user details from reference
                            String userid = user.getUid();
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference insertuser = firebaseDatabase.getReference().child("users");  // storing creating a child table with name users and storing current user information in the database. This is database usage. For saving data.
                            DatabaseReference count = firebaseDatabase.getReference().child("count");
                            count.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String k = String.valueOf(dataSnapshot.getValue());//called when data stored successfully into the database
                                    i = Integer.parseInt(k);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Error in Database", Toast.LENGTH_SHORT).show();
                                }

                            });

                            /////////******************Saving user's details in database after successful creation*********************//////////////////////

                            DatabaseReference dbref = insertuser.child(userid);

                            Map<String, String> map = new HashMap<>();//We will put all the user details in map
                            map.put("name", _uptxtname.getText().toString().trim());
                            map.put("sname", _uptxtsname.getText().toString().trim());
                            map.put("email", _emailupText.getText().toString().trim());


                            dbref.setValue(map); //This will save all the user details to database
                            //DatabaseReference imgref =insertuser.child(userid);
                            dbref.child("imgs").setValue("null0");


                            FirebaseUser user1 = mAuth.getCurrentUser();
                            String id = user1.getUid();
                            String name = (String) map.get("name");
                            String email = (String) map.get("email");
                            editor.putString("id", id);
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.commit();
                            hidepDialog();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            onSignupSuccess();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        } else {
                            hidepDialog();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed. OOOPs",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signinexiestusers(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onLoginSuccess();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            String userEmail = user.getEmail();
                            String userName = user.getDisplayName();
                            editor.putString("id", uid);
                            editor.putString("email", userEmail);
                            editor.commit();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signinexiestadmin(String email, String password) {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference adminuser = fdb.getReference().child("admin");
        adminuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dsp) {

                String adminemail = dsp.child("email").getValue().toString().trim();
                String adminpasword = dsp.child("password").getValue().toString().trim();
                // String adminusername =   dsp.child("username").getValue().toString().trim();
                txtemail = _emailText.getText().toString().trim();
                txtpass = _passwordText.getText().toString().trim();
                if (adminemail.equals(txtemail) && adminpasword.equals(txtpass)) {

                    Intent i = new Intent(getApplicationContext(), AdminActivity.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(LoginActivity.this, "Admin  Successful Login",
                            Toast.LENGTH_SHORT).show();


                } else if (adminemail != (txtemail) && adminpasword != (txtpass)) {
                    Toast.makeText(LoginActivity.this, "You are client : Uncheck Admin Button",
                            Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(LoginActivity.this, "Unauthorized Admin : Login failed",
                            Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
