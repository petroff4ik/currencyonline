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
import java.math.BigDecimal;
import java.math.RoundingMode;

import android.util.Log;

/**
 * 
 * @author petroff
 */
public class CurrencyAdapter extends ArrayAdapter<Currency> {

	private final List<Currency> list;
	private final List<Currency> list_old;
	private final Activity context;
	private int type;

	public CurrencyAdapter(Activity context, List<Currency> list,
			List<Currency> list_old, int type) {
		super(context, R.layout.currencylist, list);
		this.context = context;
		this.list = list;
		this.list_old = list_old;
		this.type = type;
	}

	class Grafic {

		private Double current;
		private Integer imgResourse;
		final String UP = "up";
		final String DOWN = "down";
		final String EXACTLY = "exactly";

		public Grafic(String charCode, Double valueCurrent, Double valueOld) {

			current = valueCurrent - valueOld;

			if (current > 0) {
				imgResourse = getDrawable(UP);
			} else if (current < 0) {
				imgResourse = getDrawable(DOWN);
			} else {
				imgResourse = getDrawable(EXACTLY);
			}
		}

		public Integer getImgResourse() {
			return this.imgResourse;
		}

		public String getValueDiff() {
			Double newDouble = new BigDecimal(this.current).setScale(3, RoundingMode.UP).doubleValue();
			return newDouble.toString();
		}
	}

	static class ViewHolder {

		protected TextView text;
		protected TextView desc;
		protected TextView value;
		protected ImageView imageview;
		protected ImageView imageviewdiff;
		protected TextView value_diff;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		switch (type) {

			case 0:
				return ViewForListView(position, convertView, parent);

			case 1:
				return ViewForSpin(position, convertView, parent);
			default:
				return convertView;
		}


	}//end

	public View ViewForListView(int position, View convertView, ViewGroup parent) {
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
			holder.imageviewdiff = (ImageView) view.findViewById(R.id.icon_diff);
			holder.value_diff = (TextView) view.findViewById(R.id.value_diff);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		if (list.get(position).getCharCode().equals(list_old.get(position).getCharCode())) {
			Grafic gf = new Grafic(list.get(position).getCharCode(), list.get(
					position).getValue(), list_old.get(position).getValue());
			holder.imageviewdiff.setImageResource(gf.getImgResourse());
			holder.value_diff.setText(gf.getValueDiff());
		} else {
			// check sort
		}

		holder.text.setText(list.get(position).getCharCode());
		holder.desc.setText(list.get(position).getName());
		holder.value.setText(list.get(position).getValue().toString());
		String charlow = list.get(position).getCharCode().toLowerCase();
		charlow = charlow.substring(0, (charlow.length() - 1));
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

		return context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
	}

	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return ViewForSpin(position, convertView, parent);
	}

	public View ViewForSpin(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View row = inflater.inflate(R.layout.spinner, parent, false);

		TextView label = (TextView) row.findViewById(R.id.textView1);
		label.setText(list.get(position).getCharCode());

		String charlow = list.get(position).getCharCode().toLowerCase();
		charlow = charlow.substring(0, (charlow.length() - 1));
		Integer i = getDrawable(charlow);
		ImageView icon = (ImageView) row.findViewById(R.id.imageView1);
		icon.setImageResource(i);

		return row;

	}
}
