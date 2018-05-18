package com.example.androiddeveloper.gardenapp.EmailService;

/**
 * Created by Android Developer on 4/12/2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddeveloper.gardenapp.Admin.BookingRequests;
import com.example.androiddeveloper.gardenapp.R;

public class Sender extends Activity {
    //   Button Send;
    TextView text;
    String toemail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        toemail = getIntent().getStringExtra("toemail");
        //  Send = (Button) findViewById(R.id.send);
        text = (TextView) findViewById(R.id.textView2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        new SendMail().execute();


    }

    private class SendMail extends AsyncTask<String, Void, Integer> {
        ProgressDialog pd = null;
        String error = null;
        Integer result;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(Sender.this);
            pd.setTitle("Sending Mail");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            // TODO Auto-generated method stub

            MailSender sender = new MailSender("gardenwisleyapp@gmail.com", "hello.11");

            sender.setTo(new String[]{toemail});
            sender.setFrom("gardenwisleyapp@gmail.com");
            sender.setSubject("Wisley Garden");
            sender.setBody("This is the confirmation That your Booking order has been accepted and you can come to the Garden at the selected date and time. Thankyou!");
            try {
                if (sender.send()) {
                    System.out.println("Message sent");
                    return 1;
                } else {
                    return 2;
                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("SendMail", e.getMessage(), e);
            }

            return 3;
        }

        protected void onPostExecute(Integer result) {
            pd.dismiss();
            if (error != null) {
                text.setText(error);
            }
            if (result == 1) {
                Toast.makeText(Sender.this,
                        "Email was sent successfully.", Toast.LENGTH_LONG)
                        .show();
            } else if (result == 2) {
                Toast.makeText(Sender.this,
                        "Email was not sent.", Toast.LENGTH_LONG).show();
            } else if (result == 3) {
                Toast.makeText(Sender.this,
                        "There was a problem sending the email.",
                        Toast.LENGTH_LONG).show();
            }
            Intent i = new Intent(Sender.this, BookingRequests.class);

            startActivity(i);
            finish();
        }
    }
}