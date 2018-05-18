package com.example.androiddeveloper.gardenapp.Activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androiddeveloper.gardenapp.Admin.AdminActivity;
import com.example.androiddeveloper.gardenapp.R;
import com.example.androiddeveloper.gardenapp.Utility;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class UploadImage extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference imgcount;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private ImageView ivImage;
    private String userChoosenTask;
    int h;
    private ProgressDialog pDialog;
    Button upload;
    EditText settitle, setdetails;
    String folder;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
      /*  btnSelect = (Button) findViewById(R.id.btnSelectPhoto);
        btnSelect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();

            }
        });
        selectImage();*/
        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        id = prefs.getString("id", null);
        final String emailText = prefs.getString("email", null);
        folder = "pendingimages";
        if (getIntent().getExtras() != null) {
            folder = getIntent().getStringExtra("folder");
        }

        ivImage = (ImageView) findViewById(R.id.ivImage);
        upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settitle.getText().toString().equalsIgnoreCase("")) {
                    settitle.setError("Title can not be empty!");
                    //    Toast.makeText(getApplicationContext(),"Title can not be empty!",Toast.LENGTH_SHORT);
                } else {
                    uploadImage();
                }
            }
        });
        settitle = (EditText) findViewById(R.id.settitle);
        setdetails = (EditText) findViewById(R.id.setdetail);
        settitle.setMaxLines(1);
        settitle.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        setdetails.setImeOptions(EditorInfo.IME_ACTION_GO);
        settitle.setVisibility(View.GONE);
        setdetails.setVisibility(View.GONE);
        upload.setVisibility(View.GONE);
        selectImage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        h = 0;
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadImage.this);
        builder.setTitle("Wisley’s Gallery!");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(UploadImage.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                    // goback();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                    //goback();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    goback();

                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
        upload.setVisibility(View.VISIBLE);
        settitle.setVisibility(View.VISIBLE);
        setdetails.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                // bm contains the link of images selected from gallery.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
        upload.setVisibility(View.VISIBLE);
        settitle.setVisibility(View.VISIBLE);
        setdetails.setVisibility(View.VISIBLE);
    }


    private void uploadImage() {
        pDialog = new ProgressDialog(UploadImage.this);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Uploading Image...");
        pDialog.setCancelable(false);
        pDialog.show();

        ////*************Storing images in storage***********//////////

        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference storageRef = storage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        imgcount = firebaseDatabase.getReference("imagcount");

        imgcount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String i = dataSnapshot.getValue().toString();

                if (h != 99999999) {
                    h = Integer.parseInt(i) + 1;
                    StorageReference mountainsRef = storageRef.child("images/gardens" + h + ".jpg");


                    ivImage.setDrawingCacheEnabled(true);
                    ivImage.buildDrawingCache();

                    //***Converting image into bytes form***/////

                    Bitmap bitmap = ivImage.getDrawingCache();
                    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    //////******Uploading image in firebase storage********////////

                    UploadTask uploadTask = mountainsRef.putBytes(data);//////Putting image in uploadtask
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();// getting image link so we can store the link into the database so we can access this image later using that link
                            Toast.makeText(getApplicationContext(), "image upload successfully", Toast.LENGTH_LONG).show();

                            DatabaseReference imgref = firebaseDatabase.getReference().child(folder).child("img" + h);

                            Map<String, String> map = new HashMap<>();
                            map.put("imgid", downloadUrl.toString().trim());
                            map.put("status", "pending");
                            map.put("title", settitle.getText().toString());
                            map.put("details", setdetails.getText().toString());
                            map.put("dkey", "img" + h);
                            imgref.setValue(map);
                            imgcount.setValue(h);
                            h = 99999999;
                            pDialog.dismiss();
                            if (folder.equalsIgnoreCase("pendingimages")) {
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(getApplicationContext(), AdminActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        }
                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void goback() {
        if (folder.equalsIgnoreCase("pendingimages")) {
            Intent i = new Intent(UploadImage.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(UploadImage.this, AdminActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        if (folder.equalsIgnoreCase("pendingimages")) {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(this, AdminActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }


}