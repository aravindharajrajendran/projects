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

public class FitnessAdapter extends ArrayAdapter<Fitness> {

    Context context;
    ArrayList<Fitness> result;
    SharedPreferences sharedPreferences;

    public FitnessAdapter(Context context, ArrayList<Fitness> result) {
        super(context, R.layout.layout_fitness, result);
        this.context = context;
        this.result = result;
        this.sharedPreferences = context.getSharedPreferences("sportsexpress", Context.MODE_PRIVATE);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_fitness, parent, false);
        }

        TextView team1 = (TextView) convertView.findViewById(R.id.textViewTeam1);
        team1.setText(result.get(position).getEvent_type());
        TextView vs = (TextView) convertView.findViewById(R.id.textViewVS);
        vs.setText(result.get(position).getDate() + "   " + result.get(position).getTime());
        TextView team2 = (TextView) convertView.findViewById(R.id.textViewTeam2);
        team2.setText(result.get(position).getVenue());
        ImageView logo2 = (ImageView) convertView.findViewById(R.id.imageViewTeam1);
        Picasso.with(context).load(result.get(position).getLogo()).into(logo2);
        final TextView avail = (TextView) convertView.findViewById(R.id.textViewAvailability);
        avail.setText("Availability: " + result.get(position).getAvailability() + "/" + result.get(position).getMax_capacity());
        final Button button = (Button) convertView.findViewById(R.id.buttonBuyTicket);
        boolean isUser = false;
        if (result.get(position).getUserList() != null) {
            for (int i = 0; i < result.get(position).getUserList().size(); i++) {
                if (result.get(position).getUserList().get(i).equalsIgnoreCase(sharedPreferences.getString("_id", null))) {
                    isUser = true;
                    break;
                }
            }
        }
        if (isUser == false && Integer.parseInt(result.get(position).getAvailability()) > 0) {
            button.setEnabled(true);
            button.setAlpha((float) 1);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int availability = Integer.parseInt(result.get(position).getAvailability());
                    availability--;
                    new UpdateData(avail, button, availability + "/" + result.get(position).getMax_capacity()).execute("http://104.196.50.212:3000/fitnessupdate?id=" + result.get(position).getObject_id() + "&inp=" + availability + "&user=" + sharedPreferences.getString("_id", null));
                }
            });
        } else {
            button.setEnabled(false);
            button.setAlpha((float) 0.5);
        }
        return convertView;
    }

    public class UpdateData extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(context);
        String a;
        Button button;
        TextView availability;

        public UpdateData(TextView textView, Button button, String s) {
            this.button = button;
            this.availability = textView;
            this.a = s;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = new String();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = br.readLine();
                br.close();
                con.disconnect();
            } catch (IOException e) {
                result = "failure";
                pd.dismiss();
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Registration in progress..");
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if (s.equalsIgnoreCase("success")) {
                button.setEnabled(false);
                button.setAlpha((float) 0.5);
                availability.setText("Availability: " + a);
                Toast.makeText(context, "Registration successful!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Registration unsuccessful!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
