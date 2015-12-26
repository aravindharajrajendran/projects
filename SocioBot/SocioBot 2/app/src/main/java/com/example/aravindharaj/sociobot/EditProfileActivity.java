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
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    public static final int REQ_CODE_IMAGE = 1001;
    byte[] byteArray = null;
    String profileText = null;
    String gender = null;
    ParseFile file;
    ProgressDialog pd = null;
    RadioGroup radioGroup;
    Switch profileSwitch;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText dob;
    EditText phone;
    CheckBox checkBox;
    ParseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setTitle("Edit Profile");
        user = ParseUser.getCurrentUser();

        final ImageView imageView = (ImageView) findViewById(R.id.imageViewImage1);
        final ParseFile image = user.getParseFile("profile_pic");
        image.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    imageView.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(EditProfileActivity.this, "Error retrieving profile picture", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        firstName = (EditText) findViewById(R.id.editTextFirstName1);
        firstName.setText(user.get("firstname").toString());
        lastName = (EditText) findViewById(R.id.editTextLastName1);
        lastName.setText(user.get("lastname").toString());
        email = (EditText) findViewById(R.id.editTextEmail1);
        email.setText(user.getEmail());
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        gender = user.get("gender").toString();
        if (gender.toLowerCase().equals(getResources().getString(R.string.stringMale).toLowerCase())) {
            radioGroup.clearCheck();
            radioGroup.check(R.id.radioButtonMale1);
        } else {
            radioGroup.clearCheck();
            radioGroup.check(R.id.radioButtonFemale1);
        }
        dob = (EditText) findViewById(R.id.editTextDOB1);
        dob.setText(user.get("dob").toString());
        phone = (EditText) findViewById(R.id.editTextPhoneNumber1);
        phone.setText(user.get("phone").toString());
        profileSwitch = (Switch) findViewById(R.id.switchPublicProfile1);
        profileText = user.get("privacy").toString();
        final String privacy_value = profileText;
        if (profileText.equals(profileSwitch.getTextOff())) {
            profileSwitch.setChecked(false);
        } else
            profileSwitch.setChecked(true);
        checkBox = (CheckBox) findViewById(R.id.checkBoxNotifications1);
        if (user.get("push").equals("true"))
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.custom_frame_layout1);
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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                gender = radioButton.getText().toString();
            }
        });

        profileSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    profileText = profileSwitch.getTextOn().toString();
                else
                    profileText = profileSwitch.getTextOff().toString();
            }
        });
        Button saveButton = (Button) findViewById(R.id.buttonSave1);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstName.getText().toString().length() < 1 || lastName.getText().toString().length() < 1 || email.getText().toString().length() < 1 || dob.getText().toString().length() < 1 || phone.getText().toString().length() < 1) {
                    Toast.makeText(EditProfileActivity.this, "All the inputs are mandatory", Toast.LENGTH_LONG).show();
                } else {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() == false) {
                        Toast.makeText(EditProfileActivity.this, "Enter a valid e-mail address", Toast.LENGTH_LONG).show();
                    } else {
                        pd = new ProgressDialog(EditProfileActivity.this);
                        pd.setMessage("Updating your Profile..");
                        pd.show();
                        user.put("firstname", firstName.getText().toString());
                        user.put("lastname", lastName.getText().toString());
                        user.put("name", firstName.getText().toString() + " " + lastName.getText().toString());
                        user.setEmail(email.getText().toString().toLowerCase());
                        user.setUsername(email.getText().toString().toLowerCase());
                        user.put("gender", gender);
                        user.put("dob", dob.getText().toString());
                        user.put("privacy", profileText);
                        user.put("phone", phone.getText().toString());
                        if (checkBox.isChecked())
                            user.put("push", "true");
                        else
                            user.put("push", "false");
                        if (byteArray != null) {
                            file = new ParseFile(email.getText().toString() + ".png", byteArray);
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
                                                        if ((user.fetchIfNeeded().getString("privacy").equals("Public")) && (privacy_value.equals(profileSwitch.getTextOff()))) {
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
                                                                                    input.put("title", "SocioBot - User changed their Privacy to Public");
                                                                                    input.put("alert", user.fetchIfNeeded().get("name") + " has changed their Privacy to Public. You can now send a message or share a photo album with the user");
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
                                                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                                    startActivity(intent);
                                                    EditProfileActivity.this.finish();
                                                } else {
                                                    if (pd != null)
                                                        pd.dismiss();
                                                    Toast.makeText(EditProfileActivity.this, "There was a problem in creating your account", Toast.LENGTH_LONG).show();
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    } else {
                                        if (pd != null)
                                            pd.dismiss();
                                        Toast.makeText(EditProfileActivity.this, "There was a problem in uploading your profile picture", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            final ParseFile image = user.getParseFile("profile_pic");
                            image.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        file = new ParseFile(email.getText().toString() + ".png", data);
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
                                                                    if ((user.fetchIfNeeded().getString("privacy").equals("Public")) && (privacy_value.equals(profileSwitch.getTextOff()))) {
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
                                                                                                input.put("title", "SocioBot - User changed their Privacy to Public");
                                                                                                input.put("alert", user.fetchIfNeeded().get("name") + " has changed their Privacy to Public. You can now send a message or share a photo album with the user");
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
                                                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                                                startActivity(intent);
                                                                EditProfileActivity.this.finish();
                                                            } else {
                                                                if (pd != null)
                                                                    pd.dismiss();
                                                                Toast.makeText(EditProfileActivity.this, "There was a problem in creating your account", Toast.LENGTH_LONG).show();
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    if (pd != null)
                                                        pd.dismiss();
                                                    Toast.makeText(EditProfileActivity.this, "There was a problem in uploading your profile picture", Toast.LENGTH_LONG).show();
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    } else {
                                        if (pd != null)
                                            pd.dismiss();
                                        Toast.makeText(EditProfileActivity.this, "Error retrieving profile picture", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_IMAGE) {
            if (resultCode == RESULT_OK) {
                ImageView imageView = (ImageView) findViewById(R.id.imageViewImage1);
                imageView.setImageURI(data.getData());
                TextView imageTextView = (TextView) findViewById(R.id.textViewEditLabel1);
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
