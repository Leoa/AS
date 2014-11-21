package com.sugardefynery.animeconvention.scheduler.EventList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.Notifications.Notes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsRowAdapter extends ArrayAdapter<Item> {

	private Activity activity;
	private List<Item> items;
	private Item objBean;
	private int row;

	View view;


	public NewsRowAdapter(Context act, int resource, List<Item> arrayList) {
		super(act, resource, arrayList);
		this.activity = (Activity) act;
		this.row = resource;
		this.items = arrayList;

	}

	public int getCount() {
		return items.size();
	}

	public Item getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			// ViewHolder is a custom class that gets TextViews by name: tvName,
			// tvCity, tvBDate, tvGender, tvAge;
			holder = new ViewHolder();

			/*
			 * setTag Sets the tag associated with this view. A tag can be used
			 * to mark a view in its hierarchy and does not have to be unique
			 * within the hierarchy. Tags can also be used to store data within
			 * a view without resorting to another data structure.
			 */
			view.setTag(holder);
		} else {

			view.setBackgroundColor(Color.WHITE);

			// the Object stored in this view as a tag
			holder = (ViewHolder) view.getTag();
		}

		if ((items == null) || ((position + 1) > items.size()))
			return view;

		objBean = items.get(position);

		holder.tv_event_name = (TextView) view.findViewById(R.id.tv_event_name);

		holder.tv_event_start = (TextView) view.findViewById(R.id.tv_event_start);
		holder.tv_event_end = (TextView) view.findViewById(R.id.tv_event_end);
		holder.tv_event_date = (TextView) view.findViewById(R.id.tv_event_date);
		holder.tv_event_location = (TextView) view.findViewById(R.id.tv_event_location);

		if (holder.tv_event_name != null && null != objBean.getName()
				&& objBean.getName().trim().length() > 0) {
			holder.tv_event_name.setText(Html.fromHtml(objBean.getName()));

		}
		if (holder.tv_event_date != null && null != objBean.getDate()
				&& objBean.getDate().trim().length() > 0) {
			holder.tv_event_date.setText(Html.fromHtml(objBean.getDate()));
		}
		if (holder.tv_event_start != null && null != objBean.getStartTime()
				&& objBean.getStartTime().trim().length() > 0) {
			holder.tv_event_start
					.setText(Html.fromHtml(objBean.getStartTime()));
		}
		if (holder.tv_event_end != null && null != objBean.getEndTime()
				&& objBean.getEndTime().trim().length() > 0) {
			holder.tv_event_end.setText(Html.fromHtml(objBean.getEndTime()));
		}
		if (holder.tv_event_location != null && null != objBean.getLocation()
				&& objBean.getLocation().trim().length() > 0) {
			holder.tv_event_location.setText(Html.fromHtml(objBean
					.getLocation()));

		}
		
		String time =objBean.getStartTime();

	    System.out.println("*****START TIME" + time);
	  
		

	   
	   
		return view;
	}


	 public void sortByNoteDesc() {
        Comparator<Item> comparator = new Comparator<Item>() {

         @Override
         public int compare(Item object1, Item object2) {
         
          return Double.compare( object1.getStartTimeCompare(),object2.getStartTimeCompare());
         }
        };
        Collections.sort(items, comparator);
           
       }
     
//     
	
	public class ViewHolder {
		public TextView tv_event_name, tv_event_date, tv_event_start,
				tv_event_end, tv_event_location
				/* tv_event_delete_flag */;

	}
	
	

}