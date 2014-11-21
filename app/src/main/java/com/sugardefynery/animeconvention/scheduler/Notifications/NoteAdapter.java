package com.sugardefynery.animeconvention.scheduler.Notifications;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.sugardefynery.animeconvention.scheduler.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NoteAdapter extends ArrayAdapter<Notes>{
	
	private List<Notes> items;
	private Context activity;
	private int row;

	public NoteAdapter(Context act, int resource, List<Notes> items) {
		super(act, resource, items);
		this.items = items;
		this.activity =  act;
		this.row = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		
		if (view == null) {
			
			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.row_notes, null);
		}
		
		Notes notes = items.get(position);
		
		if (notes != null) {
			
			TextView toptxt = (TextView) view.findViewById(R.id.toptext);
			TextView bottomtxt = (TextView) view.findViewById(R.id.bottomtext);
			

			if (toptxt != null) {
				
					toptxt.setText(notes.getNote() );

				}
				
			if (bottomtxt != null) {
				
					bottomtxt.setText(notes.getTimeStamp());

				}

		
				view.setBackgroundColor(Color.WHITE);

			}
		return view;

		
	
	
}

	 public void sortByNoteDesc() {
         Comparator<Notes> comparator = new Comparator<Notes>() {
 
          @Override
          public int compare(Notes object1, Notes object2) {
          
           return Double.compare(object2.getId(), object1.getId());
          }
         };
         Collections.sort(items, comparator);
            
        }
	

	
	 
}
