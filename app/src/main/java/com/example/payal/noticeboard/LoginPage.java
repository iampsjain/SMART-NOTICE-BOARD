package com.example.payal.noticeboard;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;

public class LoginPage extends AppCompatActivity {
    static int TYPE;
    EditText etUsername, etPassword;
    String status;
    private TextView tv;
    static  String type;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_login_testing);
        boolean check=isNetworkAvailable();

        if(check == true) {

        }
        else {
            Toast.makeText(this, " Opps internet connection is not available....!!!", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final TextView tx = (TextView) findViewById(R.id.textview1);

        tx.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tx.setSelected(true);
        tx.setSingleLine(true);
        tx.setText("Vivekanand Arts, Sardar DalipSingh Commerce And Science College, Aurangabad");

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void CheckLogin(View v) {
        boolean check=isNetworkAvailable();
        if(check == true) {
            etUsername = (EditText) findViewById(R.id.user);
            etPassword = (EditText) findViewById(R.id.pass);
            if (etUsername.toString().equals(null) && etPassword.toString().equals(null)) {
                Toast.makeText(this, "Enter Correct username or password", Toast.LENGTH_SHORT).show();
            } else {
                new AsyncLogin().execute(etUsername.getText().toString(), etPassword.getText().toString(), "http://dhoondlee.com/smart/login.php");
            }
        }
        else{

            Toast.makeText(this, " Opps internet connection is not available....!!!", Toast.LENGTH_SHORT).show();

        }
    }


    public class AsyncLogin extends AsyncTask<String, Void, String> {

        String err = "";
        String username;
        String password;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LoginPage.this);
            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
        }


        @Override
        protected String doInBackground(String... strings) {
            String result = "", line;
            try {
                username = strings[0];
                password = strings[1];
                URL url = new URL(strings[2]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.flush();
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = bufferedReader.readLine()) != null) {
                    result = result + line;

                }
                inputStream.close();


                return result;

            } catch (UnsupportedEncodingException e) {
                err = err + e;
                e.printStackTrace();
            } catch (ProtocolException e) {
                err = err + e;
                e.printStackTrace();
            } catch (MalformedURLException e) {
                err = err + e;
                e.printStackTrace();
            } catch (IOException e) {
                err = err + e;
                e.printStackTrace();
            }

            return "something went wrong" + err;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            
            decodeJsonList(aVoid);
            
            try {
           int i=((Number) NumberFormat.getInstance().parse(status)).intValue();
            if (i==1) {
                Toast.makeText(LoginPage.this, "LOGIN SUCCESSGULLY ", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(LoginPage.this,MainActivity.class);

                intent.putExtra("username", etUsername.getText().toString());
                startActivity(intent);
                etUsername.getText().clear();
                etPassword.getText().clear();

            } else {
                Toast.makeText(LoginPage.this, "LOGIN UNSUCCESSGULLY ", Toast.LENGTH_SHORT).show();

            }

            } catch(Exception nfe) {
                Toast.makeText(LoginPage.this, nfe + " ", Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();


        /*
            if (Status==1) {

                Intent i = new Intent(LoginPage.this, MainActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(getApplicationContext(), "WELCOME!!!", Toast.LENGTH_LONG).show();
                pd.dismiss();

            } else {
                Toast.makeText(getApplicationContext(), aVoid, Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
         */

        }

        private void decodeJsonList(String aVoid) {
            try {
                JSONArray ja=new JSONArray(aVoid);
              status=  ja.getString(0);
              type=  ja.getString(1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }


}
