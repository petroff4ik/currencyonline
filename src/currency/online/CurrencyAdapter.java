/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import junit.framework.Assert;
import android.util.Log;

/**
 *
 * @author petroff
 */
public class CurrencyAdapter extends ArrayAdapter<Currency> {

	private final List<Currency> list;
	private final Activity context;

	public CurrencyAdapter(Activity context, List<Currency> list) {
		super(context, R.layout.currencylist, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {

		protected TextView text;
		protected TextView desc;
		protected TextView value;
		protected ImageView imageview;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.currencylist, null);
			holder = new ViewHolder();
			holder.text = (TextView) view.findViewById(R.id.label);
			holder.desc = (TextView) view.findViewById(R.id.desc);
			holder.value = (TextView) view.findViewById(R.id.value);
			holder.imageview = (ImageView) view.findViewById(R.id.icon);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		holder.text.setText(list.get(position).getCharCode());
		holder.desc.setText(list.get(position).getName());
		holder.value.setText(list.get(position).getValue().toString());
		String charlow = list.get(position).getCharCode().toLowerCase();
		Integer i = getDrawable(charlow);

		if (i > 0) {
			holder.imageview.setImageResource(i);
		} else {
			holder.imageview.setImageResource(R.drawable.none);
		}
		return view;
	}

	public int getDrawable(String name) {
		Assert.assertNotNull(context);
		Assert.assertNotNull(name);

		return context.getResources().getIdentifier(name,
				"drawable", context.getPackageName());
	}
}
