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
public class FitnessStudioFragment extends Fragment {

    ArrayList<Fitness> arrayList = new ArrayList<>();

    public FitnessStudioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fitness_studio, container, false);
        Context context = getActivity();
        new GetTodayFitness(context, view).execute("http://104.196.50.212:3000/fitness");
        return view;
    }

    public class GetTodayFitness extends AsyncTask<String, Void, ArrayList<Fitness>> {

        ProgressDialog pd = new ProgressDialog(getActivity());
        Context context;
        View view;

        public GetTodayFitness(Context context, View view) {
            this.context = context;
            this.view = view;
        }

        @Override
        protected ArrayList<Fitness> doInBackground(String... strings) {
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
                    arrayList = GetFitness.getFitness(jsonArray);
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
        protected void onPostExecute(ArrayList<Fitness> eventses) {
            super.onPostExecute(eventses);
            pd.dismiss();
            if (eventses != null) {
                ListView listView = (ListView) view.findViewById(R.id.listViewFitnessStudio);
                FitnessAdapter appAdapter = new FitnessAdapter(context, eventses);
                appAdapter.setNotifyOnChange(true);
                listView.setAdapter(appAdapter);
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
