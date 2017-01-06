package com.example.aravindharaj.a49ersportsexpress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Aravindharaj on 10/27/2016.
 */

public class GameHistoryAdapter extends ArrayAdapter<Events> {

    Context context;
    ArrayList<Events> result;

    public GameHistoryAdapter(Context context, ArrayList<Events> result) {
        super(context, R.layout.layout_game_history, result);
        this.context = context;
        this.result = result;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_game_history, parent, false);
        }
        TextView textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);
        textViewDate.setText(result.get(position).getDate());
        TextView textViewTime = (TextView) convertView.findViewById(R.id.textViewTime);
        textViewTime.setText(result.get(position).getTime());
        TextView textViewGameType = (TextView) convertView.findViewById(R.id.textViewGameType);
        textViewGameType.setText(result.get(position).getGame_type());
        TextView textViewTeam1 = (TextView) convertView.findViewById(R.id.textViewTeam1);
        textViewTeam1.setText(result.get(position).getTeam1());
        TextView textViewTeam2 = (TextView) convertView.findViewById(R.id.textViewTeam2);
        textViewTeam2.setText(result.get(position).getTeam2());
        TextView textViewResult = (TextView) convertView.findViewById(R.id.textViewResult);
        textViewResult.setText(result.get(position).getResult());
        return convertView;
    }
}
