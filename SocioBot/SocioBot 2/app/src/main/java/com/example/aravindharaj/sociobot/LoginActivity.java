package com.example.aravindharaj.sociobot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.parse.internal.signpost.OAuthConsumer;
import com.parse.internal.signpost.basic.DefaultOAuthConsumer;
import com.parse.twitter.Twitter;


import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {

    List<String> permissions = Arrays.asList("public_profile", "email");
    CallbackManager callbackManager;
    byte[] byteArray = null;
    ParseFile file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        if (ParseUser.getCurrentUser() == null) {
            setContentView(R.layout.activity_login);
            getSupportActionBar().hide();

            TextView rpTextView = (TextView) findViewById(R.id.textViewResetPassword);
            rpTextView.setText(Html.fromHtml(getResources().getString(R.string.stringResetPassword)));

            TextView suTextView = (TextView) findViewById(R.id.textViewSignUp);
            suTextView.setText(Html.fromHtml(getResources().getString(R.string.stringSignUp)));

            suTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    intent.putExtra("from", "login");
                    startActivity(intent);
                }
            });


            LoginButton loginButton = (LoginButton) findViewById(R.id.fb_button_login);
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    final AccessToken accessToken = loginResult.getAccessToken();

                    ParseFacebookUtils.logInInBackground(accessToken, new LogInCallback() {
                        @Override
                        public void done(final ParseUser user, final ParseException e) {
                            if (user.isNew()) {
                                Log.d("MyApp", "new User");
                                final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                                pd.setMessage("Loading..");
                                pd.show();
                                GraphRequest request = GraphRequest.newMeRequest(accessToken,
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                                Log.d("demo", object.toString());
                                                try {
                                                    pd.dismiss();
                                                    String url = "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large";
                                                    URL picture_url = new URL(url);
                                                    Log.d("picture url", url);
                                                    new GetProfilePicture(user, object).execute(picture_url);
                                                } catch (JSONException e) {
                                                    pd.dismiss();
                                                    Log.d("MyApp", "JSON Error " + e.getMessage());
                                                    Toast.makeText(LoginActivity.this, "Problem signing up with facebook", Toast.LENGTH_LONG).show();
                                                    e.printStackTrace();
                                                    ParseUser.getCurrentUser().deleteInBackground();
                                                    ParseUser.logOut();
                                                } catch (MalformedURLException e1) {
                                                    pd.dismiss();
                                                    e1.printStackTrace();
                                                    Log.d("MyApp", "JSON Error " + e.getMessage());
                                                    Toast.makeText(LoginActivity.this, "Problem signing up with facebook", Toast.LENGTH_LONG).show();
                                                    ParseUser.getCurrentUser().deleteInBackground();
                                                    ParseUser.logOut();
                                                }
                                            }
                                        });

                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,email,first_name,name,last_name,gender,link,picture");
                                request.setParameters(parameters);
                                request.executeAsync();
                            } else {
                                Intent intent;
                                Log.d("MyApp", "user log in again");
                                ParseInstallation.getCurrentInstallation().put("user", user);
                                ParseInstallation.getCurrentInstallation().saveInBackground();
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("from", "login");
                                startActivity(intent);
                            }
                        }
                    });
                }

                @Override
                public void onCancel() {
                    Log.d("MyApp", "User cancelled the login");
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.d("MyApp", "Facebook login error");
                    Toast.makeText(LoginActivity.this, "Facebook login error", Toast.LENGTH_LONG).show();
                }
            });

    /*        findViewById(R.id.tw_button_login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseTwitterUtils.logIn(LoginActivity.this, new LogInCallback() {

                        Intent intent;

                        @Override
                        public void done(ParseUser user, ParseException err) {
                            if (user == null) {
                                Log.d("MyApp", "Uh oh. The user cancelled the Twitter login.");
                            } else {
                                ParseInstallation.getCurrentInstallation().put("user", user);
                                ParseInstallation.getCurrentInstallation().saveInBackground();
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("from", "login");
                                startActivity(intent);
                            }
                        }
                    });
                }
            });
*/
            findViewById(R.id.tw_button_login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseTwitterUtils.logIn(LoginActivity.this, new LogInCallback() {
                        Intent intent;

                        @Override
                        public void done(ParseUser user, ParseException err) {
                            if (user == null) {
                                Log.d("MyApp", "Uh oh. The user cancelled the Twitter login.");
                            } else if (user.isNew()) {
                                Twitter twitter = ParseTwitterUtils.getTwitter();
                                new GetTwitterInformation(twitter).execute("https://api.twitter.com/1.1/users/show.json?screen_name=" + twitter.getScreenName());
                                Log.d("MyApp", "User signed up and logged in through Twitter!");
                            } else {
                                Log.d("MyApp", "User logged in through Twitter!");
                                ParseInstallation.getCurrentInstallation().put("user", user);
                                ParseInstallation.getCurrentInstallation().saveInBackground();
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("from", "login");
                                startActivity(intent);
                            }
                        }
                    });
                }
            });

            TextView resetPassword = (TextView) findViewById(R.id.textViewResetPassword);
            resetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    final EditText input = new EditText(LoginActivity.this);
                    input.setHint("Enter your e-mail address");
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (input.getText().length() > 0) {
                                ParseUser.requestPasswordResetInBackground(input.getText().toString().toLowerCase(), new RequestPasswordResetCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(LoginActivity.this, "An email was successfully sent with reset instructions", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "There was a problem with resetting your password", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else
                                Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });

            final EditText username = (EditText) findViewById(R.id.editTextUsername);
            final EditText password = (EditText) findViewById(R.id.editTextPassword);

            findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (username.getText().toString().length() < 1 || password.getText().toString().length() < 1) {
                        Toast.makeText(LoginActivity.this, "Please enter a valid username/password", Toast.LENGTH_LONG).show();
                    } else {
                        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (e == null) {
                                    ParseInstallation.getCurrentInstallation().put("user", user);
                                    ParseInstallation.getCurrentInstallation().saveInBackground();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    intent.putExtra("from", "login");
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Invalid E-mail address/Password", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });
        } else {
            ParseInstallation.getCurrentInstallation().put("user", ParseUser.getCurrentUser());
            ParseInstallation.getCurrentInstallation().saveInBackground();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("from", "login");
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    public void setUpUser(JSONObject object, Bitmap bitmap) {
        try {
            final ParseUser user = ParseUser.getCurrentUser();
            user.put("username", object.getString("screen_name") + "@twitter.com");
            user.put("email", object.getString("screen_name") + "@twitter.com");
            String[] name = object.getString("name").split(" ");
            if (name.length > 2) {
                user.put("firstname", name[0]);
                String lname = new String();
                for (int i = 1; i < name.length; i++) {
                    lname = lname + name[i];
                    lname = lname + " ";
                }
                user.put("lastname", lname.trim().toString());
            } else {
                user.put("firstname", name[0]);
                user.put("lastname", name[1]);
            }
            user.put("gender", "Male");
            user.put("name", object.getString("name"));
            user.put("privacy", "Private");
            user.put("phone", "");
            user.put("push", "true");
            user.put("dob", "");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            file = new ParseFile(object.getString("id") + ".png", byteArray);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        user.put("profile_pic", file);
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    ParseInstallation.getCurrentInstallation().put("user", user);
                                    ParseInstallation.getCurrentInstallation().saveInBackground();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    intent.putExtra("from", "login");
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                    ParseUser.getCurrentUser().deleteInBackground();
                                    ParseUser.logOut();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "There was a problem in uploading your profile picture", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        ParseUser.getCurrentUser().deleteInBackground();
                        ParseUser.logOut();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GetProfilePicture extends AsyncTask<URL, Void, Bitmap> {

        ProgressDialog pd = null;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Getting your Profile information from Facebook");
            pd.show();
        }

        ParseUser user;
        JSONObject object;

        public GetProfilePicture(ParseUser user, JSONObject object) {
            this.user = user;
            this.object = object;
        }

        @Override
        protected Bitmap doInBackground(URL... params) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(params[0].openConnection().getInputStream());
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap == null) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "Problem retrieving Profile Picture from Facebook", Toast.LENGTH_LONG).show();
            } else {
                try {
                    final ParseUser currentUser = ParseUser.getCurrentUser();
                    String email = null;
                    try {
                        email = object.getString("email");
                    } catch (JSONException e) {
                        email = object.getString("id") + "@facebook.com";
                    }
                    currentUser.put("email", email);
                    currentUser.put("firstname", object.getString("first_name"));
                    currentUser.put("lastname", object.getString("last_name"));
                    String gender = object.getString("gender");
                    currentUser.put("gender", gender.substring(0, 1).toUpperCase() + gender.substring(1));
                    currentUser.put("name", object.getString("name"));
                    currentUser.put("privacy", "Private");
                    currentUser.put("username", email);
                    currentUser.put("push", "true");
                    currentUser.put("phone", "");
                    currentUser.put("dob", "");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                    file = new ParseFile(email + ".png", byteArray);
                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                currentUser.put("profile_pic", file);
                                currentUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            pd.dismiss();
                                            Intent intent;
                                            ParseInstallation.getCurrentInstallation().put("user", user);
                                            ParseInstallation.getCurrentInstallation().saveInBackground();
                                            Log.d("MyApp", "User logged in through Facebook!");
                                            intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            intent.putExtra("from", "login");
                                            startActivity(intent);
                                        } else {
                                            pd.dismiss();
                                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                            ParseUser.getCurrentUser().deleteInBackground();
                                            ParseUser.logOut();
                                        }
                                    }
                                });
                            } else {
                                pd.dismiss();
                                Toast.makeText(LoginActivity.this, "There was a problem in uploading your profile picture", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    pd.dismiss();
                    Log.d("MyApp", "JSON Error " + e.getMessage());
                    Toast.makeText(LoginActivity.this, "Problem signing up with facebook", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    ParseUser.getCurrentUser().deleteInBackground();
                    ParseUser.logOut();
                }
            }
        }
    }

    public class GetTwitterInformation extends AsyncTask<String, Void, Bitmap> {

        Twitter twitter;
        ProgressDialog pd = null;
        JSONObject object;

        GetTwitterInformation(Twitter twitter) {
            this.twitter = twitter;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Getting your Profile information from Twitter");
            pd.show();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap == null) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "Problem retrieving Profile Picture from Twitter", Toast.LENGTH_LONG).show();
            } else {
                setUpUser(object, bitmap);
                pd.dismiss();
            }
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                signRequest(twitter, con);
                InputStream in = new BufferedInputStream(con.getInputStream());
                object = new JSONObject(IOUtils.toString(in));
                String picture = object.getString("profile_image_url");
                String image = picture.replace("_normal","");
                URL url1 = new URL(image);
                Bitmap bitmap = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
                return bitmap;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void signRequest(Twitter twitter, HttpsURLConnection request) {
        OAuthConsumer consumer = new DefaultOAuthConsumer(twitter.getConsumerKey(), twitter.getConsumerSecret());
        consumer.setTokenWithSecret(twitter.getAuthToken(), twitter.getAuthTokenSecret());
        try {
            consumer.sign(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
