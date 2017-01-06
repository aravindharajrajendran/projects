package com.example.aravindharaj.a49ersportsexpress;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchDayFragment extends Fragment {

    ArrayList<ArrayList<Events>> arrayList = new ArrayList<>();

    public MatchDayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_day, container, false);
        Context context = getActivity();
        new GetTodayEvents(context, view).execute("http://104.196.50.212:3000/events");
        return view;
    }

    public class GetTodayEvents extends AsyncTask<String, Void, ArrayList<ArrayList<Events>>> {

        ProgressDialog pd = new ProgressDialog(getActivity());
        Context context;
        View view;

        public GetTodayEvents(Context context, View view) {
            this.context = context;
            this.view = view;
        }

        @Override
        protected ArrayList<ArrayList<Events>> doInBackground(String... strings) {
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
                JSONArray jsonArray = null;
                if (!(sb.toString().equalsIgnoreCase("failure"))) {
                    jsonArray = new JSONArray(sb.toString());
                    arrayList.clear();
                    arrayList = GetEvents.getEvents(jsonArray);
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
        protected void onPostExecute(ArrayList<ArrayList<Events>> eventses) {
            super.onPostExecute(eventses);
            pd.dismiss();
            if (eventses == null) {

            } else {
                if (eventses.get(0).size() > 0) {
                    ListView listView = (ListView) view.findViewById(R.id.listViewToday);
                    AppAdapter appAdapter = new AppAdapter(context, eventses.get(0));
                    appAdapter.setNotifyOnChange(true);
                    listView.setAdapter(appAdapter);
                }
                if (eventses.get(1).size() > 0) {
                    ListView listView = (ListView) view.findViewById(R.id.listViewFuture);
                    AppAdapter appAdapter = new AppAdapter(context, eventses.get(1));
                    appAdapter.setNotifyOnChange(true);
                    listView.setAdapter(appAdapter);
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading Events..");
            pd.show();
        }
    }
}
