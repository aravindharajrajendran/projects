package com.example.aravindharaj.a49ersportsexpress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aravindharaj on 11/12/2016.
 */

public class TicketHistoryAdapter extends ArrayAdapter<Events> {

    Context context;
    ArrayList<Events> result;

    public TicketHistoryAdapter(Context context, ArrayList<Events> result) {
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
        textViewResult.setText("$" + result.get(position).getTicket_price());
        return convertView;
    }

}
