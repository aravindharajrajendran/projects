package com.example.aravindharaj.a49ersportsexpress;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
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
public class EquipmentFragment extends Fragment {

    ArrayList<Equipments> arrayList = new ArrayList<>();

    public EquipmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_equipment, container, false);
        Context context = getActivity();
        new GetTodayEquipments(context, view).execute("http://104.196.50.212:3000/equipments");
        return view;
    }

    public class GetTodayEquipments extends AsyncTask<String, Void, ArrayList<Equipments>> {

        ProgressDialog pd = new ProgressDialog(getActivity());
        Context context;
        View view;

        public GetTodayEquipments(Context context, View view) {
            this.context = context;
            this.view = view;
        }

        @Override
        protected ArrayList<Equipments> doInBackground(String... strings) {
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
                    arrayList = GetEquipments.getEquipments(jsonArray);
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
        protected void onPostExecute(ArrayList<Equipments> eventses) {
            super.onPostExecute(eventses);
            pd.dismiss();
            if (eventses != null) {
                GridView gridView = (GridView) view.findViewById(R.id.gridView);
                EquipmentsAdapter appAdapter = new EquipmentsAdapter(context, eventses);
                appAdapter.setNotifyOnChange(true);
                gridView.setAdapter(appAdapter);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading Equipments..");
            pd.show();
        }
    }

}
