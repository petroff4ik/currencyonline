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
import android.widget.LinearLayout;
import android.widget.TextView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;


/**
 * 
 * @author petroff
 */
public class CurrencyAdapter extends ArrayAdapter<Currency> {

	private final List<Currency> list;
	private final List<Currency> list_old;
	private final Activity context;
	private int type;
	private CurrencyModel model;

	public CurrencyAdapter(Activity context, List<Currency> list,
			List<Currency> list_old, CurrencyModel model) {
		super(context, R.layout.currencylist, list);
		this.context = context;
		this.list = list;
		this.list_old = list_old;
		this.type = 0;
		this.model = model;
	}

	public CurrencyAdapter(Activity context, List<Currency> list) {
		super(context, R.layout.currencylist, list);
		this.context = context;
		this.list = list;
		this.list_old = new ArrayList<Currency>();
		this.type = 1;
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
		protected LinearLayout ll;
	}

	static class ViewHolderS {

		protected TextView text;
		protected ImageView imageview;
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
			holder.ll = (LinearLayout)view.findViewById(R.id.ll);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		Double t = ((list.get(position).getValue() / model.getCurrentValue() ) * model.getCurrentNominal());
		Double t1 = ((list_old.get(position).getValue() / model.getCurrentValue() ) * model.getCurrentNominal());
		if (list.get(position).getCharCode().equals(list_old.get(position).getCharCode())) {
			Grafic gf = new Grafic(list.get(position).getCharCode(), t, t1);
			holder.imageviewdiff.setImageResource(gf.getImgResourse());
			holder.value_diff.setText(gf.getValueDiff());
		} else {
			// check sort
		}
		holder.text.setText(list.get(position).getCharCode());
		holder.desc.setText(list.get(position).getName());
		t = new BigDecimal(t).setScale(3, RoundingMode.UP).doubleValue();
		holder.value.setText(t.toString());
		String charlow = list.get(position).getCharCode().toLowerCase();
		charlow = charlow.substring(0, (charlow.length() - 1));
		Integer i = getDrawable(charlow);

		if (i > 0) {
			holder.imageview.setImageResource(i);
		} else {
			holder.imageview.setImageResource(R.drawable.none);
		}
		
		if(model.getSelectCurrency() != null && model.getSelectCurrency().getCharCode().equals(list.get(position).getCharCode())){
			holder.ll.setBackgroundResource(R.drawable.border);
		}else{
			holder.ll.setBackgroundDrawable(null);
		}
		return view;
	}

	public int getDrawable(String name) {
		return Gd.getDrawable(name, context);
	}

	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return ViewForSpin(position, convertView, parent);
	}

	public View ViewForSpin(int position, View convertView, ViewGroup parent) {

		View view = null;
		ViewHolderS holder;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.spinner, parent, false);
			holder = new ViewHolderS();
			holder.text = (TextView) view.findViewById(R.id.textView1);
			holder.imageview = (ImageView) view.findViewById(R.id.imageView1);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolderS) view.getTag();
		}

		holder.text.setText(list.get(position).getCharCode());
		String charlow = list.get(position).getCharCode().toLowerCase();
		charlow = charlow.substring(0, (charlow.length() - 1));
		Integer i = getDrawable(charlow);
		holder.imageview.setImageResource(i);



		return view;

	}
}
