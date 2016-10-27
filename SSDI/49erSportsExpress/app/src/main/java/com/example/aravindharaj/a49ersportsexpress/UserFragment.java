package com.example.aravindharaj.a49ersportsexpress;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sportsexpress", Context.MODE_PRIVATE);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewUser);
        Picasso.with(getActivity()).load(sharedPreferences.getString("picture", null)).into(imageView);
        EditText editTextUsername = (EditText) view.findViewById(R.id.editTextUsername);
        editTextUsername.setText(sharedPreferences.getString("_id", null));
        editTextUsername.setFocusable(false);
        EditText editTextFirstName = (EditText) view.findViewById(R.id.editTextFirstName);
        editTextFirstName.setText(sharedPreferences.getString("firstname", null));
        editTextFirstName.setFocusable(false);
        EditText editTextLastName = (EditText) view.findViewById(R.id.editTextLastName);
        editTextLastName.setText(sharedPreferences.getString("lastname", null));
        editTextLastName.setFocusable(false);
        EditText editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        editTextEmail.setText(sharedPreferences.getString("email", null));
        editTextEmail.setFocusable(false);
        return view;
    }
}
