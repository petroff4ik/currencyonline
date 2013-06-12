/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

import android.widget.ListView;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import java.util.Calendar;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author petroff
 */
public class CurrencyModel {

	String url = "http://www.cbr.ru/scripts/XML_daily_eng.asp?";
	//http://www.cbr.ru/scripts/XML_daily_eng.asp?date_req=02/03/2002
	List<Currency> currency;
	List<Currency> currency_old;
	Activity activity;
	ListView lvMain;
	String CurrentDate = "";
	TaskPreperDate t;
	CurrencyAdapter adapter;
	SharedPreferences sPref;
	static Spinner spinner;
	static Spinner spinner2;
	static Spinner spinner3;
	static Spinner spinner4;
	static EditText editText2;
	static EditText edit;
	private static Double Spinner2_value;
	private static int Spinner2_nominal;
	private static Double Spinner3_value;
	private static int Spinner3_nominal;
	private String currentCurrency = "RUB";
	private Double currentValue = 1.0;
	private int currentNominal = 1;
	private String currentDescr = "Russian rub";
	private Currency selectCurrency;
	final String CURRENT_CURRENCY = "current_currency";
	final String CURRENT_NOMINAL = "current_nominal";
	final String CURRENT_VALUE = "current_value";
	final String CURRENT_ALARAM = "current_alaram";
	final String CURRENT_UPDATEPERIOD = "current_updateperiod";
	final String CURRENT_SELECT = "currentSelect";
	static final List<SpinnerUpdateAdapterElement> UPL = new ArrayList<SpinnerUpdateAdapterElement>();
	private int updatePeriod;
	private int alaram;

	static {
		UPL.add(new SpinnerUpdateAdapterElement(1420, "once a day"));
		UPL.add(new SpinnerUpdateAdapterElement(10, "10 min"));
		UPL.add(new SpinnerUpdateAdapterElement(20, "20 min"));
		UPL.add(new SpinnerUpdateAdapterElement(30, "30 min"));
		UPL.add(new SpinnerUpdateAdapterElement(60, "1 hour"));
		UPL.add(new SpinnerUpdateAdapterElement(720, "12 hour"));
	}

	public Currency getSelectCurrency() {
		return selectCurrency;
	}

	public void setSelectCurrency(Currency selectCurrency) {
		this.selectCurrency = selectCurrency;
	}

	public int getAlaram() {
		return alaram;
	}

	public void setAlaram(int alaram) {
		this.alaram = alaram;
	}

	public int getUpdatePeriod() {
		return updatePeriod;
	}

	public void setUpdatePeriod(int updatePeriod) {
		this.updatePeriod = updatePeriod;
	}

	public CurrencyModel(Activity activity) {
		this.activity = activity;
	}

	public static EditText getEditText2() {
		return editText2;
	}

	public static void setEditText2(EditText editText2) {
		CurrencyModel.editText2 = editText2;
	}

	public static Spinner getSpinner4() {
		return spinner4;
	}

	public static void setSpinner4(Spinner spinner4) {
		CurrencyModel.spinner4 = spinner4;
	}

	public static Double getSpinner2_value() {
		return Spinner2_value;
	}

	public static void setSpinner2_value(Double spinner2_value) {
		Spinner2_value = spinner2_value;
	}

	public static int getSpinner2_nominal() {
		return Spinner2_nominal;
	}

	public static void setSpinner2_nominal(int spinner2_nominal) {
		Spinner2_nominal = spinner2_nominal;
	}

	public static Double getSpinner3_value() {
		return Spinner3_value;
	}

	public static void setSpinner3_value(Double spinner3_value) {
		Spinner3_value = spinner3_value;
	}

	public static int getSpinner3_nominal() {
		return Spinner3_nominal;
	}

	public static void setSpinner3_nominal(int spinner3_nominal) {
		Spinner3_nominal = spinner3_nominal;
	}

	/**
	 * @return the currentCurrency
	 */
	public String getCurrentCurrency() {
		return currentCurrency;
	}

	/**
	 * @param currentCurrency the currentCurrency to set
	 */
	public void setCurrentCurrency(String currentCurrency) {
		this.currentCurrency = currentCurrency;
	}

	/**
	 * @return the currentValue
	 */
	public Double getCurrentValue() {
		return currentValue;
	}

	/**
	 * @param currentValue the currentValue to set
	 */
	public void setCurrentValue(Double currentValue) {
		this.currentValue = currentValue;
	}

	/**
	 * @return the currentNominal
	 */
	public int getCurrentNominal() {
		return currentNominal;
	}

	/**
	 * @param currentNominal the currentNominal to set
	 */
	public void setCurrentNominal(int currentNominal) {
		this.currentNominal = currentNominal;
	}

	public Boolean threadPreDate() {

		try {
			TimeUnit.SECONDS.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		FileOperation fo = new FileOperation(activity);
		if (!Http.hasConnection(activity)) {
			String data = fo.readFile();
			if (data == null || data.length() == 0) {
				return false;
			} else {
				currency = XmlParser.parserXml(data);
				fo.setFileName("data_old");
				CurrentDate = XmlParser.getCDT();
				data = fo.readFile();
				currency_old = XmlParser.parserXml(data);
			}
		} else {
			//create current list
			String data = Http.connect(url);
			currency = XmlParser.parserXml(data);
			fo.writeFile(data);
			CurrentDate = XmlParser.getCDT();
			//create old list
			url = url + "date_req=" + getPrevData(CurrentDate);
			data = Http.connect(url);
			currency_old = XmlParser.parserXml(data);
			fo.setFileName("data_old");
			fo.writeFile(data);
		}
		Currency rub = new Currency("RUB", "Russian rub", 1.0, getPrevData(CurrentDate).toString(), 1);
		if (listFindByArray(this.currency, "RUB") == 0) {
			currency.add(rub);
			currency_old.add(rub);
		}
		selectCurrency = rub;
		return true;
	}

	public void restoreData() {
		sPref = activity.getPreferences(activity.MODE_PRIVATE);
		String ts = sPref.getString(this.CURRENT_CURRENCY, null);
		if (ts != null) {
			this.currentCurrency = sPref.getString(this.CURRENT_CURRENCY, "");
			this.currentNominal = sPref.getInt(this.CURRENT_NOMINAL, currentNominal);
			Float t = sPref.getFloat(this.CURRENT_VALUE, 1);
			this.currentValue = t.doubleValue();
			this.updatePeriod = sPref.getInt(this.CURRENT_UPDATEPERIOD, 1420);
			this.alaram = sPref.getInt(this.CURRENT_ALARAM, 0);
			String tempCharCurrency = sPref.getString(this.CURRENT_SELECT, "RUB");
			Log.v("test", "CHOOSE " + tempCharCurrency);
			int pos = listFindByArray(currency, tempCharCurrency);
			selectCurrency = currency.get(pos);
		}
	}

	public void preloadData() {
		t = new TaskPreperDate(this);
		t.execute();
	}

	private void setCurrentDate() {
		TextView textview = (TextView) activity.findViewById(R.id.dateCurrency);
		textview.setText(CurrentDate);
	}

	private void setUpLV() {
		lvMain = (ListView) activity.findViewById(R.id.lvCurrency);
		if (currency != null) {
			adapter = new CurrencyAdapter(activity, this.currency, this.currency_old, this);
			lvMain.setAdapter(adapter);
			lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
						long id) {
					Currency t = (Currency) parent.getItemAtPosition(position);
					setSelectCurrency(t);
					adapter.notifyDataSetChanged();
					sPref = activity.getPreferences(activity.MODE_PRIVATE);
					Editor ed = sPref.edit();
					ed.putString(CURRENT_SELECT, selectCurrency.getCharCode());
					ed.commit();
				}
			});
		}
	}

	public void setupData() {
		restoreData();
		setUpLV();
		setCurrentDate();
		setCurrentCurrency();
	}

	public void reload(Activity activity) {
		this.activity = activity;
		if (t != null) {
			String statusT = t.getStatus().toString();
			if (statusT.equals("RUNNING")) {
				CProgressBar.finish();
				CProgressBar.onCreateDialog(1, activity);
				CProgressBar.setProgress();
			}
		} else {
			setupData();
		}
	}

	public String getPrevData(String dt) {
		Date dt2 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		try {
			dt2 = sdf.parse(dt);
		} catch (Exception e) {
			//TODO can't parse string
		}
		Calendar c = Calendar.getInstance();
		c.setTime(dt2);
		c.add(Calendar.DATE, -1);
		sdf.applyPattern("dd/MM/yyyy");
		return sdf.format(c.getTime());
	}

	public static Spinner getSpinner() {
		return spinner;
	}

	public static void setAdapterForSpiner(Activity a, List<Currency> l, String currentCurrency) {

		spinner = (Spinner) a.findViewById(R.id.spinner1);
		spinner2 = (Spinner) a.findViewById(R.id.spinner2);
		spinner3 = (Spinner) a.findViewById(R.id.spinner3);
		edit = (EditText) a.findViewById(R.id.editText1);
		if (l != null) {
			CurrencyAdapter adapter = new CurrencyAdapter(a, l);
			spinner.setAdapter(adapter);
			spinner.setSelection(listFindByArray(l, currentCurrency));
			spinner2.setAdapter(adapter);
			spinner3.setAdapter(adapter);

		}
	}

	public static int listFindByArray(List<Currency> l, String search) {
		int i = 0;
		for (Currency ob : l) {
			if (ob.getCharCode() != null && ob.getCharCode().contains(search)) {
				i = l.indexOf(ob);
			}
			//something here
		}
		return i;
	}

	public static String currencyExchange(String str) {
		String result = "";
		NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
		int cash = 0;
		try {
			Number number = format.parse(str);
			cash = number.intValue();
		} catch (ParseException p) {
			result = "Error, only numeric";
		}
		Double t = cash * ((Spinner2_value / Spinner2_nominal) / (Spinner3_value / Spinner3_nominal));
		t = new BigDecimal(t).setScale(3, RoundingMode.UP).doubleValue();
		result = t.toString();
		return result;
	}

	public void setCurrentCurrency() {
		int p = Gd.getDrawable(Gd.preName(this.currentCurrency), activity);
		ImageView image = (ImageView) activity.findViewById(R.id.icon_root);
		image.setImageResource(p);
		TextView text = (TextView) activity.findViewById(R.id.value_root);
		text.setText(this.currentCurrency);
	}

	public void parseDataFromIntentAndRecalc(Intent data) {
		this.currentCurrency = data.getStringExtra("selectCurrency");
		this.currentNominal = data.getIntExtra("nominal", 1);
		this.currentValue = data.getDoubleExtra("value", 1.0);
		this.alaram = data.getIntExtra("alaram", 0);
		this.updatePeriod = data.getIntExtra("updatePeriod", 1420);
		setCurrentCurrency();
		adapter.notifyDataSetChanged();
		sPref = activity.getPreferences(activity.MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putString(CURRENT_CURRENCY, currentCurrency);
		ed.putInt(CURRENT_NOMINAL, currentNominal);
		ed.putFloat(CURRENT_VALUE, currentValue.floatValue());
		ed.putInt(CURRENT_ALARAM, alaram);
		ed.putInt(CURRENT_UPDATEPERIOD, updatePeriod);
		ed.commit();
	}

	public static void preperDateForCalculatorActivity(Activity a) {
		spinner4 = (Spinner) a.findViewById(R.id.spinner4);
		editText2 = (EditText) a.findViewById(R.id.editText2);

		SpinnerUpdateAdapter adapter = new SpinnerUpdateAdapter(a,
				android.R.layout.simple_spinner_item, UPL);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner4.setAdapter(adapter);

	}
}
