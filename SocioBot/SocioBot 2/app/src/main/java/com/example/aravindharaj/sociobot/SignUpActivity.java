package com.example.aravindharaj.sociobot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.SingleLaunchActivityTestCase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    public static final int REQ_CODE_IMAGE = 1001;
    byte[] byteArray = null;
    String profileText = null;
    String gender = null;
    ParseUser user;
    ParseFile file;
    ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (getIntent().getStringExtra("from").equals("login")) {
            getSupportActionBar().setTitle("Sign Up");
        }

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.custom_frame_layout);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 19) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQ_CODE_IMAGE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQ_CODE_IMAGE);
                }
            }
        });

        final EditText firstName = (EditText) findViewById(R.id.editTextFirstName);
        final EditText lastName = (EditText) findViewById(R.id.editTextLastName);
        final EditText email = (EditText) findViewById(R.id.editTextEmail);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton b = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        gender = b.getText().toString();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                gender = radioButton.getText().toString();
            }
        });
        final EditText dob = (EditText) findViewById(R.id.editTextDOB);
        final EditText phoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        final EditText password = (EditText) findViewById(R.id.editTextSUPassword);
        final EditText cpassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        final Switch profileSwitch = (Switch) findViewById(R.id.switchPublicProfile);
        profileText = (profileSwitch.isChecked()) ? profileSwitch.getTextOn().toString() : profileSwitch.getTextOff().toString();
        profileSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    profileText = profileSwitch.getTextOn().toString();
                else
                    profileText = profileSwitch.getTextOff().toString();
            }
        });
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxNotifications);
        Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstName.getText().toString().length() < 1 || lastName.getText().toString().length() < 1 || email.getText().toString().length() < 1 || dob.getText().toString().length() < 1 || phoneNumber.getText().toString().length() < 1 || password.getText().toString().length() < 1 || cpassword.getText().toString().length() < 1) {
                    Toast.makeText(SignUpActivity.this, "All the inputs are mandatory", Toast.LENGTH_LONG).show();
                } else {
                    if (!(password.getText().toString().equals(cpassword.getText().toString()))) {
                        Toast.makeText(SignUpActivity.this, "Confirm Password doesn't match with the Password", Toast.LENGTH_LONG).show();
                    } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() == false) {
                        Toast.makeText(SignUpActivity.this, "Enter a valid e-mail address", Toast.LENGTH_LONG).show();
                    } else {
                        pd = new ProgressDialog(SignUpActivity.this);
                        pd.setMessage("Signing up...");
                        pd.show();
                        user = new ParseUser();
                        user.put("firstname", firstName.getText().toString());
                        user.put("lastname", lastName.getText().toString());
                        user.put("name", firstName.getText().toString() + " " + lastName.getText().toString());
                        user.setEmail(email.getText().toString().toLowerCase());
                        user.setUsername(email.getText().toString().toLowerCase());
                        user.setPassword(password.getText().toString());
                        user.put("gender", gender);
                        user.put("dob", dob.getText().toString());
                        user.put("phone", phoneNumber.getText().toString());
                        user.put("privacy", profileText);
                        if (checkBox.isChecked())
                            user.put("push", "true");
                        else
                            user.put("push", "false");
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    if (byteArray != null) {
                                        file = new ParseFile(email.getText().toString() + ".png", byteArray);
                                    } else {
                                        Drawable drawable = getResources().getDrawable(R.drawable.default_user);
                                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                        byteArray = stream.toByteArray();
                                        file = new ParseFile(email.getText().toString() + ".png", byteArray);
                                    }
                                    file.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                user.put("profile_pic", file);
                                                user.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            try {
                                                                if (user.fetchIfNeeded().getString("privacy").equals("Public")) {
                                                                    ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
                                                                    userParseQuery.whereNotEqualTo("username", user.getUsername());
                                                                    userParseQuery.findInBackground(new FindCallback<ParseUser>() {
                                                                        @Override
                                                                        public void done(List<ParseUser> objects, ParseException e) {
                                                                            if (e == null) {
                                                                                if (objects.size() > 0) {
                                                                                    for (int i = 0; i < objects.size(); i++) {
                                                                                        try {
                                                                                            Map<String, Object> input = new HashMap<String, Object>();
                                                                                            input.put("title", "SocioBot - New User has joined");
                                                                                            input.put("alert", user.fetchIfNeeded().get("name") + " has joined our SocioBot Community! You can now send a message or share a photo album with the user");
                                                                                            input.put("users", objects.get(i).getObjectId());
                                                                                            ParseCloud.callFunctionInBackground("notifyUsers", input, new FunctionCallback<ParseUser>() {
                                                                                                @Override
                                                                                                public void done(ParseUser object, ParseException e) {
                                                                                                    if (e == null) {
                                                                                                        Log.d("notifyUsers", object.getUsername());
                                                                                                    } else {
                                                                                                        if (pd != null)
                                                                                                            pd.dismiss();
                                                                                                        e.printStackTrace();
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        } catch (ParseException e2) {
                                                                                            if (pd != null)
                                                                                                pd.dismiss();
                                                                                            e2.printStackTrace();
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                if (pd != null)
                                                                                    pd.dismiss();
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            } catch (ParseException e1) {
                                                                if (pd != null)
                                                                    pd.dismiss();
                                                                e1.printStackTrace();
                                                            }
                                                            if (pd != null)
                                                                pd.dismiss();
                                                            ParseUser.getCurrentUser().logOut();
                                                            SignUpActivity.this.finish();
                                                        } else {
                                                            if (pd != null)
                                                                pd.dismiss();
                                                            Toast.makeText(SignUpActivity.this, "There was a problem in creating your account", Toast.LENGTH_LONG).show();
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            } else {
                                                if (pd != null)
                                                    pd.dismiss();
                                                Toast.makeText(SignUpActivity.this, "There was a problem in uploading your profile picture", Toast.LENGTH_LONG).show();
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } else {
                                    if (pd != null)
                                        pd.dismiss();
                                    Toast.makeText(SignUpActivity.this, "E-mail address already exists! Please enter a new one", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_IMAGE) {
            if (resultCode == RESULT_OK) {
                ImageView imageView = (ImageView) findViewById(R.id.imageViewImage);
                imageView.setImageURI(data.getData());
                TextView imageTextView = (TextView) findViewById(R.id.textViewEditLabel);
                imageTextView.setText("");
                Bitmap picture = null;
                try {
                    picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
            }
        }
    }
}
