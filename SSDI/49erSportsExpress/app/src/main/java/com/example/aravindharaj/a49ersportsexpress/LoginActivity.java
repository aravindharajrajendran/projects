package com.example.aravindharaj.a49ersportsexpress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = (EditText) findViewById(R.id.editTextEmail);
                EditText password = (EditText) findViewById(R.id.editTextPassword);
                CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox);
                if (username.getText().toString().length() > 1 && password.getText().toString().length() > 1) {
                    if (checkbox.isChecked())
                        new GetLogin().execute(username.getText().toString() + ":" + password.getText().toString() + ":" + "true");
                    else
                        new GetLogin().execute(username.getText().toString() + ":" + password.getText().toString() + ":" + "false");
                } else
                    Toast.makeText(LoginActivity.this, "Username/Password cannot be left empty", Toast.LENGTH_LONG).show();
            }
        });
    }

    public class GetLogin extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(LoginActivity.this);

        @Override
        protected String doInBackground(String... strings) {
            String result = new String();
            HttpURLConnection con = null;
            try {
                URL url = new URL("http://104.196.50.212:3000/login");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.connect();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", strings[0].split(":")[0]);
                jsonObject.put("password", strings[0].split(":")[1]);
                jsonObject.put("keeploggedin", strings[0].split(":")[2]);
                DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                dos.writeBytes(jsonObject.toString());
                dos.flush();
                dos.close();
                if (con.getResponseCode() == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    result = response.toString();
                } else
                    result = "failure";
            } catch (IOException e) {
                e.printStackTrace();
                result = "failure";
            } catch (JSONException e) {
                e.printStackTrace();
                result = "failure";
            } finally {
                con.disconnect();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            if (!(result.equalsIgnoreCase("failure"))) {
                SharedPreferences sharedPreferences = getSharedPreferences("sportsexpress", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("_id", result.split(",")[0]);
                editor.putString("firstname", result.split(",")[1]);
                editor.putString("lastname", result.split(",")[2]);
                editor.putString("email", result.split(",")[3]);
                editor.putString("keeploggedin", result.split(",")[4]);
                editor.apply();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Invalid Login Credentials!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Logging in..");
            pd.show();
        }
    }
}
