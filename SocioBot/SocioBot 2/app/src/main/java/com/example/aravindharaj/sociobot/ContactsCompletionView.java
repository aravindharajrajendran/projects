package com.example.aravindharaj.sociobot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Created by Aravindharaj on 11/28/2015.
 */
public class ContactsCompletionView extends TokenCompleteTextView<UserCompletionView> {

    public ContactsCompletionView (Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject (UserCompletionView user)
    {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(LoginActivity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout)l.inflate(R.layout.contact_token, (ViewGroup) ContactsCompletionView.this.getParent(), false);
        ((TextView)view.findViewById(R.id.name)).setText(user.getName());
        return view;
    }

    protected UserCompletionView defaultObject(String completionText)
    {
        return new UserCompletionView(completionText);
    }
}
