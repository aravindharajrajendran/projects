package com.example.aravindharaj.a49ersportsexpress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Aravindharaj on 10/27/2016.
 */

public class EquipmentsAdapter extends ArrayAdapter<Equipments> {

    Context context;
    ArrayList<Equipments> result;
    SharedPreferences sharedPreferences;

    public EquipmentsAdapter(Context context, ArrayList<Equipments> result) {
        super(context, R.layout.layout_equipments, result);
        this.context = context;
        this.result = result;
        this.sharedPreferences = context.getSharedPreferences("sportsexpress", Context.MODE_PRIVATE);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_equipments, parent, false);
        }

        TextView event = (TextView) convertView.findViewById(R.id.textViewEquipment);
        event.setText(result.get(position).getEquipment_type());
        ImageView logo2 = (ImageView) convertView.findViewById(R.id.imageViewLogo);
        Picasso.with(context).load(result.get(position).getLogo()).into(logo2);
        final TextView avail = (TextView) convertView.findViewById(R.id.textViewAvailable);
        avail.setText("Available: " + result.get(position).getAvailability());
        final Spinner spinner = (Spinner) convertView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(context, R.array.values_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        final Button button = (Button) convertView.findViewById(R.id.buttonCheckout);
        boolean isUser = false;
        if (result.get(position).getCheckedOutBy() != null) {
            Iterator<String> iterator = result.get(position).getCheckedOutBy().keySet().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().equalsIgnoreCase(sharedPreferences.getString("_id", null))) {
                    isUser = true;
                    break;
                }
            }
        }
        if (isUser == false && Integer.parseInt(result.get(position).getAvailability()) > 0) {
            button.setEnabled(true);
            spinner.setEnabled(true);
            button.setText("Checkout");
            button.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int availability = Integer.parseInt(result.get(position).getAvailability());
                    int spinner_pos = spinner.getSelectedItemPosition();
                    int count = spinner_pos + 1;
                    availability = availability - count;
                    if (availability < 0)
                        Toast.makeText(context, "Selected value exceeds the available equipment count", Toast.LENGTH_LONG).show();
                    else
                        new CheckOutData(avail, button, spinner, availability, count).execute("http://104.196.50.212:3000/equipmentcheckout?id=" + result.get(position).get_id() + "&avail=" + availability + "&count=" + count + "&user=" + sharedPreferences.getString("_id", null));
                }
            });
        } else if (button.getText().toString().equalsIgnoreCase("checkin") || isUser == true) {
            button.setText("Checkin");
            button.setBackgroundColor(Color.RED);
            final int count = result.get(position).getCheckedOutBy().get(sharedPreferences.getString("_id", null));
            spinner.setSelection(count - 1, true);
            spinner.setEnabled(false);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int availability = Integer.parseInt(result.get(position).getAvailability());
                    availability = availability + count;
                    new CheckInData(avail, button, spinner, availability, count).execute("http://104.196.50.212:3000/equipmentcheckin?id=" + result.get(position).get_id() + "&avail=" + availability + "&count=" + count + "&user=" + sharedPreferences.getString("_id", null));
                }
            });
        } else {
            button.setAlpha((float) 0.5);
            button.setText("Checkout");
            button.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            spinner.setEnabled(false);
        }
        return convertView;
    }

    public class CheckOutData extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(context);
        Button button;
        TextView availability;
        Spinner spinner;
        int a, c;

        public CheckOutData(TextView textView, Button button, Spinner spinner, int a, int c) {
            this.button = button;
            this.availability = textView;
            this.a = a;
            this.c = c;
            this.spinner = spinner;
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
            pd.setMessage("Checking out..");
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if (s.equalsIgnoreCase("success")) {
                button.setText("Checkin");
                button.setBackgroundColor(Color.RED);
                spinner.setSelection(c - 1);
                spinner.setEnabled(false);
                availability.setText("Availability: " + a);
                Toast.makeText(context, "Checkout successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Checkout unsuccessful!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class CheckInData extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(context);
        Button button;
        TextView availability;
        Spinner spinner;
        int a, c;

        public CheckInData(TextView textView, Button button, Spinner spinner, int a, int c) {
            this.button = button;
            this.availability = textView;
            this.a = a;
            this.c = c;
            this.spinner = spinner;
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
            pd.setMessage("Checking in..");
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if (s.equalsIgnoreCase("success")) {
                button.setEnabled(true);
                spinner.setEnabled(true);
                spinner.setSelection(0, true);
                button.setText("Checkout");
                button.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                availability.setText("Availability: " + a);
                Toast.makeText(context, "Checkin successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Checkin unsuccessful!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
