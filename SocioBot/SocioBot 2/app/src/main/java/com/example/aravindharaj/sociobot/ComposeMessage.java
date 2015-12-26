package com.example.aravindharaj.sociobot;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ComposeMessage extends AppCompatActivity implements ContactsCompletionView.TokenListener {

    ContactsCompletionView recipient;
    HashMap<String, String> usermap = new HashMap<>();
    UserCompletionView[] users;
    ArrayList<ParseUser> tokenlist = new ArrayList<>();
    ParseObject parse_message;
    ParseACL parseACL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_message);
        setTitle("Compose Message");
        Button cancelButton = (Button) findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final EditText message = (EditText) findViewById(R.id.editTextMessage);
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TextView textView = (TextView) findViewById(R.id.textViewCount);
                int count = 140 - message.getText().toString().length();
                textView.setText(count + "");
            }
        });
        recipient = (ContactsCompletionView) findViewById(R.id.editTextRecipient);
        recipient.setTokenListener(this);
        recipient.allowDuplicates(false);
        if (getIntent().getStringExtra("from").equals("compose")) {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("privacy", "Public");
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        users = new UserCompletionView[objects.size()];
                        for (int i = 0; i < objects.size(); i++) {
                            users[i] = new UserCompletionView(objects.get(i).get("name").toString());
                            usermap.put(users[i].getName(), objects.get(i).getEmail());
                        }
                        ArrayAdapter<UserCompletionView> adapter = new ArrayAdapter<UserCompletionView>(ComposeMessage.this, android.R.layout.simple_list_item_1, users);
                        recipient.setAdapter(adapter);
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            ParseObject object = MessageFragmentActivity.parseObjects.get(getIntent().getIntExtra("position", 0));
            ParseUser user = object.getParseUser("from");
            recipient.setText(user.get("name").toString());
            tokenlist.add(user);
            usermap.put(user.get("name").toString(), user.getEmail());
            recipient.setFocusable(false);
        }
        Button sendButton = (Button) findViewById(R.id.buttonSend);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipient.getText().toString().length() < 1) {
                    recipient.setError("Enter a recipient");
                } else if (message.getText().toString().length() < 1) {
                    message.setError("Enter the message");
                } else {
                    parse_message = new ParseObject("Message");
                    parseACL = new ParseACL();
                    parseACL.setPublicReadAccess(false);
                    parse_message.put("from", ParseUser.getCurrentUser());
                    parse_message.put("msg", message.getText().toString());
                    parse_message.addAllUnique("to", tokenlist);
                    for (int j = 0; j < tokenlist.size(); j++) {
                        parseACL.setWriteAccess(tokenlist.get(j), true);
                        parseACL.setReadAccess(tokenlist.get(j), true);
                    }
                    parse_message.setACL(parseACL);
                    parse_message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                for (int k = 0; k < tokenlist.size(); k++) {
                                    ParseObject flag = new ParseObject("Flag");
                                    flag.put("msg", parse_message);
                                    flag.put("user", tokenlist.get(k));
                                    flag.put("access_flag", "false");
                                    flag.saveEventually();
                                }
                                ComposeMessage.this.finish();
                                Snackbar.make(MainActivity.coordinatorLayout, "Your Message has been sent successfully!", Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(MainActivity.coordinatorLayout, "There was a problem in sending your message", Snackbar.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onTokenAdded(Object token) {
        ParseQuery<ParseUser> user = ParseUser.getQuery();
        user.whereEqualTo("username", usermap.get(token.toString()));
        user.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    tokenlist.add(objects.get(0));
                } else {
                    Toast.makeText(ComposeMessage.this, "Please select a valid user to continue", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onTokenRemoved(Object token) {
        tokenlist.remove(token);
    }
}
