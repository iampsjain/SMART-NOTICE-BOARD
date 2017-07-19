package com.example.payal.noticeboard;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        TextToSpeech.OnInitListener {
    ArrayList<StudentNotice> arrayList;
    ListView lv;

    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       lv = (ListView) findViewById(R.id.listView);
        tts = new TextToSpeech(this, this);

        new asyncNot().execute("student","http://dhoondlee.com/JsonList.php");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StudentNotice st = arrayList.get(i);
               // Toast.makeText(MainActivity.this, st.getDescription() + "", Toast.LENGTH_SHORT).show();

               // private TextToSpeech tts;

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.notice);
                Button btnExit= (Button) dialog.findViewById(R.id.button2);
               btnExit.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       dialog.dismiss();
                   }
               });
                TextView tvTitle = (TextView) dialog.findViewById(R.id.tvMtitle);
                TextView tvDate = (TextView) dialog.findViewById(R.id.tvMDate);
                TextView tvDes = (TextView) dialog.findViewById(R.id.tvMDescription);
                tvTitle.setText(st.getTitle());
                tvDate.setText(st.getDate());
                tvDes.setText(st.getDescription());
speakOut(st.getDescription());
        /*TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
*//*
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
*/
                dialog.show();


            }
        });




    }
    public void studentNotice(View v)
    {

        new asyncNot().execute("student","http://dhoondlee.com/smart/JsonList.php");
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.gallery:
                Intent i=new Intent(MainActivity.this,Gallery1.class);
                startActivity(i);
                break;

            case R.id.about:
                showAbout();
                break;

            case R.id.Feedback:

                showFeedback();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showFeedback() {
        final Dialog dialog = new Dialog(MainActivity.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.feedback_box);
        // Set dialog title

        // set values for custom dialog components - text, image and button

        TextView btnSubmit = (TextView) dialog.findViewById(R.id.tvSubmit);
        //TextView btnCancel= (TextView) dialog.findViewById(R.id.tvCancel);

        dialog.show();

        // if decline button is clicked, close the custom dialog
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etName = (EditText) dialog.findViewById(R.id.etName);
                EditText etEmail = (EditText) dialog.findViewById(R.id.etEmail);
                EditText etMessage = (EditText) dialog.findViewById(R.id.etMessage);
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String message = etMessage.getText().toString();
                if (!message.equals("")) {


                    new SendFeedback().execute(name, email, message);
                    Toast.makeText(MainActivity.this, "Sending", Toast.LENGTH_SHORT).show();
                    etName.getText().clear();
                    etMessage.getText().clear();
                    etEmail.getText().clear();
                    dialog.dismiss();

                } else {
                    Toast.makeText(MainActivity.this, "Sorry!!!You cannot send empty message", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void showAbout() {
        final Dialog dialog = new Dialog(MainActivity.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.about_box);
        dialog.show();
    }



    public void teacherNotice(View v)
    {

        if(LoginPage.type.equals("teacher")) {
                new asyncNot().execute("teacher", "http://dhoondlee.com/smart/JsonList.php");
            }
        else
            {
                Toast.makeText(MainActivity.this, "You are not authorized user to view this notice", Toast.LENGTH_SHORT).show();
            }
            }



    class asyncNot extends AsyncTask<String, Void, String> {

        String err = "";

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
        }



        @Override
        protected String doInBackground(String... strings) {
            String type;
            String result = "", line;
            try {
                type = strings[0];

                URL url = new URL(strings[1]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
                String data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
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
        protected void onPostExecute(String result) {
            arrayList = new ArrayList<StudentNotice>();
                decodeJsonString(result);
            pd.dismiss();
            NoticeAdapter na = new NoticeAdapter(getApplicationContext(), arrayList);
            lv.setAdapter(na);
        lv.setDivider(null);
            lv.setDividerHeight(10);

        }

        private void decodeJsonString(String result) {

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("notice");

                StudentNotice studentNotice;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jb = jsonArray.getJSONObject(i);
                    studentNotice = new StudentNotice();
                    studentNotice.setTitle(jb.getString("title"));
                    studentNotice.setDate(jb.getString("date"));
                    studentNotice.setDescription(jb.getString("descrip"));
                    arrayList.add(studentNotice);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    private void addDataToArray() {
        StudentNotice studentNotice;

        for (int i = 0; i < 50; i++) {
            studentNotice = new StudentNotice();
            studentNotice.setTitle("Title Num " + i);
            studentNotice.setDate("date " + i);
            studentNotice.setDescription("Description numb" + i);
            arrayList.add(studentNotice);

        }


    }
    private class SendFeedback extends AsyncTask<String, Void, String> {
        String err = "";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String name = strings[0];
            String email = strings[1];
            String message = strings[2];

            try {
                URL url = new URL("http://dhoondlee.com/smart/get_feedback.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");

                bufferedWriter.flush();
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String result = "", line;
                while ((line = bufferedReader.readLine()) != null) {
                    result = result + line;
                }
                inputStream.close();
                return result;
            } catch (Exception e1) {
                e1.printStackTrace();
                err = err + e1;
            }
            return " " + err;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
           speakOut(" ");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut(String text) {

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}
