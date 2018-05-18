package com.example.androiddeveloper.gardenapp;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.androiddeveloper.gardenapp.Models.Album;

import java.util.List;

public class ImagesRequests extends AppCompatActivity {

    private RecyclerView recyclerView;
    //private AlbumsAdapter adapter;
    private List<Album> albumList;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_requests);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
        imageView = (ImageView) findViewById(R.id.image);

        /*recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);*/

        prepareAlbums();
    }

    private void prepareAlbums() {


        Glide.with(this /* context */)

                .load("https://firebasestorage.googleapis.com/v0/b/batterysaver-8be94.appspot.com/o/images%2Fgardens2.jpg?alt=media&token=b59d7e20-10be-44bc-bf2c-d8c503a6d5cdfirebase%20download%20images")
                .into(imageView);
     /*   FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("images");
        StorageReference[] covers = new StorageReference[]{
                storageRef.child("images")};
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference imagesa =  firebaseDatabase.getReference().child("pendingimages");

        // Adding Add Value Event Listener to databaseReference.
        storageRef = storage.getReferenceFromUrl(getResources().getString(R.string.image_reference));

       final String a= storageRef.child("images/garden1.png").getDownloadUrl().getResult().toString();
        new CountDownTimer(30000, 9000) {

            public void onTick(long millisUntilFinished) {
              //  mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                Picasso.with(ImagesRequests.this).load(a).into(imageView);
            }

        }.start();


*/


        //  adapter.notifyDataSetChanged();
    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}