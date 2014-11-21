package com.sugardefynery.animeconvention.scheduler.Alerts;

import java.util.ArrayList;
import java.util.List;

import com.sugardefynery.animeconvention.scheduler.R;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AlertAdapter extends ArrayAdapter<Alerts> {

	private List<Alerts> items;
	private Activity activity;
	private int row;

	public AlertAdapter(Context act, int resource, List<Alerts> items) {
		super(act, resource, items);
		this.items = items;
		this.activity = (Activity) act;
		this.row = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.row, null);
		}
		Alerts alerts = items.get(position);
		if (alerts != null) {
			TextView toptxt = (TextView) view.findViewById(R.id.toptext);
			TextView bottomtxt = (TextView) view.findViewById(R.id.bottomtext);
			TextView middletxt = (TextView) view.findViewById(R.id.middletext);

			if (alerts.getEventState() == 1) {
				

				if (toptxt != null) {
					toptxt.setText(alerts.getEventName() );
					toptxt.setTextColor(Color.WHITE);

				}
				

				if (middletxt != null) {
					middletxt.setText("This event has been canceled.");
					middletxt.setTextColor(Color.WHITE);


				}
				
				bottomtxt.setText("");
				view.setBackgroundColor(getContext().getResources().getColor(R.color.DarkestBlue));
			} 
						
			
			else if (alerts.getAlertState() == 1) {						

				if (toptxt != null) {
					toptxt.setText(alerts.getEventName());					
					toptxt.setTextColor(Color.WHITE);

				}
				

				if (middletxt != null) {
					middletxt.setText("Alert is off.");
					middletxt.setTextColor(Color.WHITE);


				}
				bottomtxt.setText("");
				
				view.setBackgroundColor(Color.GRAY);
			} 
			

			else {
				if (toptxt != null) {
					toptxt.setTextColor(Color.BLACK);
					toptxt.setText(alerts.getEventName());

				}

				if (middletxt != null) {
					middletxt.setTextColor(Color.BLACK);
					middletxt.setText("Event Time: "+ alerts.getStartTime() +" at "+ alerts.getLocation());
				}


				if (bottomtxt != null) {
					bottomtxt.setTextColor(Color.BLACK);
					bottomtxt.setText("Alert Set "
							+ alerts.getAlertMinutes()
							+ " minutes before event");
				}

				view.setBackgroundColor(Color.WHITE);

			}

		}
		return view;
	}
}