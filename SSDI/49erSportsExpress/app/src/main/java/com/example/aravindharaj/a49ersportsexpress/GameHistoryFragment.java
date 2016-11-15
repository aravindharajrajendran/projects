package com.example.aravindharaj.a49ersportsexpress;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
public class GameHistoryFragment extends Fragment {

    ArrayList<Events> arrayList = new ArrayList<>();

    public GameHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_history, container, false);
        Context context = getActivity();
        new GetGameHistory(context, view).execute("http://104.196.50.212:3000/gamehistory");
        return view;
    }

    public class GetGameHistory extends AsyncTask<String, Void, ArrayList<Events>> {

        ProgressDialog pd = new ProgressDialog(getActivity());
        Context context;
        View view;

        public GetGameHistory(Context context, View view) {
            this.context = context;
            this.view = view;
        }

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
                    ListView listView = (ListView) view.findViewById(R.id.listViewGameHistory);
                    GameHistoryAdapter appAdapter = new GameHistoryAdapter(context, eventses);
                    appAdapter.setNotifyOnChange(true);
                    listView.setAdapter(appAdapter);
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading Game History..");
            pd.show();
        }
    }
}
