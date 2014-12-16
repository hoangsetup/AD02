package com.longnd.tracuudiemthi.adapters;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.longnd.tracuudiemthi.R;
import com.longnd.tracuudiemthi.models.Thisinh;

public class MainListAdapter extends ArrayAdapter<Thisinh> {
	private Context context;
	private int idLayout;
	private Vector<Thisinh> thisinhs = new Vector<Thisinh>();

	public MainListAdapter(Context context, int resource,
			Vector<Thisinh> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.idLayout = resource;
		this.thisinhs = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(idLayout, null);
		}
		Thisinh thisinh = thisinhs.get(position);
		TextView tv_d = (TextView) convertView
				.findViewById(R.id.textView_d);
		tv_d.setText(thisinh.getDiem());
		return convertView;
	}
}
