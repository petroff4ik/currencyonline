/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author petroff
 */
public class SpinnerUpdateAdapter extends ArrayAdapter<SpinnerUpdateAdapterElement> {

	private Context context;
	private int textViewResourceId;
	private List<SpinnerUpdateAdapterElement> objects;
	public static boolean flag = false;

	public SpinnerUpdateAdapter(Activity context, int textViewResourceId,
			List<SpinnerUpdateAdapterElement> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, textViewResourceId, null);
		}

		TextView tv = (TextView) convertView;
		tv.setText(objects.get(position).getName());

		return convertView;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent);
	}
}
