package com.example.aravindharaj.a49ersportsexpress;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TicketHistoryActivity extends AppCompatActivity {

    ArrayList<Events> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);
        String user = getIntent().getStringExtra("user");
        new GetTicketHistory().execute("http://104.196.50.212:3000/tickethistory?user=" + user);
    }

    public class GetTicketHistory extends AsyncTask<String, Void, ArrayList<Events>> {

        ProgressDialog pd = new ProgressDialog(TicketHistoryActivity.this);

        @Override
        protected ArrayList<Events> doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String temp;
                StringBuilder sb = new StringBuilder();
                while ((temp = br.readLine()) != null)
                    sb.append(temp);
                br.close();
                con.disconnect();
                System.out.println(sb.toString());
                JSONArray jsonArray = null;
                if (!(sb.toString().equalsIgnoreCase("failure"))) {
                    jsonArray = new JSONArray(sb.toString());
                    arrayList.clear();
                    arrayList = GetEvents.getEvents(jsonArray).get(1);
                } else {
                    arrayList = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                pd.dismiss();
            }
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Events> eventses) {
            super.onPostExecute(eventses);
            pd.dismiss();
            if (eventses == null) {

            } else {
                if (eventses.size() > 0) {
                    ListView listView = (ListView) findViewById(R.id.listViewTicketHistory);
                    TicketHistoryAdapter appAdapter = new TicketHistoryAdapter(TicketHistoryActivity.this, eventses);
                    appAdapter.setNotifyOnChange(true);
                    listView.setAdapter(appAdapter);
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading Ticket History..");
            pd.show();
        }
    }
}
