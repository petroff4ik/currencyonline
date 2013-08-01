/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

import android.widget.ListView;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
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

/**
 * 
 * @author petroff
 */
public class CurrencyModel {

	String url = "http://www.cbr.ru/scripts/XML_daily_eng.asp?";
	// http://www.cbr.ru/scripts/XML_daily_eng.asp?date_req=02/03/2002
	List<Currency> currency;
	List<Currency> currency_old;
	Activity activity;
	Context context;
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
	private String currentID = "R00000";
	private Currency selectCurrency;
	private Boolean parserFlag;
	private Boolean lightsFlag = false;
	final String CURRENT_CURRENCY = "current_currency";
	final String CURRENT_NOMINAL = "current_nominal";
	final String CURRENT_VALUE = "current_value";
	final static String CURRENT_ALARAM = "current_alaram";
	final static String CURRENT_UPDATEPERIOD = "current_updateperiod";
	final String CURRENT_SELECT = "currentSelect";
	final String CURRENT_ID = "currentID";
	final String SELECT_CURRENCY = "selectCurrency";
	final String UPDATE_PERIOD = "updatePeriod";
	final String ALARAM = "alaram";
	final static String NAME_PREF = "currency_pref";
	static final List<SpinnerUpdateAdapterElement> UPL = new ArrayList<SpinnerUpdateAdapterElement>();
	private int updatePeriod;
	private Float alaram;
	private String textSystemBar;
	private String textEventInfo;
	private Integer img_id;

	static {
		UPL.add(new SpinnerUpdateAdapterElement(0, "never"));
		UPL.add(new SpinnerUpdateAdapterElement(1, "1 min"));
		UPL.add(new SpinnerUpdateAdapterElement(5, "5 min"));
		UPL.add(new SpinnerUpdateAdapterElement(10, "10 min"));
		UPL.add(new SpinnerUpdateAdapterElement(20, "20 min"));
		UPL.add(new SpinnerUpdateAdapterElement(30, "30 min"));
		UPL.add(new SpinnerUpdateAdapterElement(60, "1 hour"));
		UPL.add(new SpinnerUpdateAdapterElement(720, "12 hour"));
		UPL.add(new SpinnerUpdateAdapterElement(1420, "once a day"));
	}

	public Boolean getLightsFlag() {
		return lightsFlag;
	}

	public Integer getImg_id() {
		return img_id;
	}

	public String getTextEventInfo() {
		return textEventInfo;
	}

	public String getTextSystemBar() {
		return textSystemBar;
	}

	public Currency getSelectCurrency() {
		return selectCurrency;
	}

	public void setSelectCurrency(Currency selectCurrency) {
		this.selectCurrency = selectCurrency;
	}

	public Float getAlaram() {
		return alaram;
	}

	public void setAlaram(Float alaram) {
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
		this.context = activity.getBaseContext();
	}

	public CurrencyModel(Context context) {
		this.context = context;
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
	 * @param currentCurrency
	 *            the currentCurrency to set
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
	 * @param currentValue
	 *            the currentValue to set
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
	 * @param currentNominal
	 *            the currentNominal to set
	 */
	public void setCurrentNominal(int currentNominal) {
		this.currentNominal = currentNominal;
	}

	public Boolean threadPreDate(Boolean readFromFile) {
		FileOperation fo = new FileOperation(context);
		if (!Http.hasConnection(context)) {
			if(readFromFile == false){
				parserFlag = false;
				return false;
			}
			String data = fo.readFile();
			if (data == null || data.length() == 0) {
				parserFlag = false;
				return false;
			} else {
				currency = XmlParser.parserXml(data);
				fo.setFileName("data_old");
				CurrentDate = XmlParser.getCDT();
				data = fo.readFile();
				currency_old = XmlParser.parserXml(data);
			}
		} else {
			// create current list
			String data = Http.connect(url);
			if (data == null || data.equals("")) {
				parserFlag = false;
				return false;
			}
			currency = XmlParser.parserXml(data);
			fo.writeFile(data);
			CurrentDate = XmlParser.getCDT();
			// create old list
			String urlWithDat = "";
			urlWithDat = url + "date_req=" + getPrevData(CurrentDate);
			data = Http.connect(urlWithDat);
			currency_old = XmlParser.parserXml(data);
			fo.setFileName("data_old");
			fo.writeFile(data);
		}
		Currency rub = new Currency(currentCurrency, currentDescr,
				currentValue, getPrevData(CurrentDate).toString(),
				currentNominal, currentID);
		if (listFindByArray(this.currency, "RUB") == 0) {
			currency.add(rub);
			currency_old.add(rub);
		}
		parserFlag = true;
		selectCurrency = rub;
		return true;
	}
	
	

	public void restoreData() {
		sPref = context.getSharedPreferences(NAME_PREF, context.MODE_PRIVATE);
		String ts = sPref.getString(this.CURRENT_CURRENCY, null);
		if (ts != null) {
			this.currentCurrency = sPref.getString(this.CURRENT_CURRENCY, "");
			this.currentNominal = sPref.getInt(this.CURRENT_NOMINAL,
					currentNominal);
			Float t = sPref.getFloat(this.CURRENT_VALUE, 1);
			this.currentValue = t.doubleValue();
			restoreUpdateTime();
			this.alaram = sPref.getFloat(CURRENT_ALARAM, 0);
			this.currentID = sPref.getString(this.CURRENT_ID, "R00000");
			String tempCharCurrency = sPref.getString(this.CURRENT_SELECT,
					"RUB");
			int pos = listFindByArray(currency, tempCharCurrency);
			selectCurrency = currency.get(pos);
		}
	}

	public void restoreUpdateTime() {
		if (sPref == null) {
			sPref = context.getSharedPreferences(NAME_PREF, context.MODE_PRIVATE);
		}
		this.updatePeriod = sPref.getInt(CURRENT_UPDATEPERIOD, 0);
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
			adapter = new CurrencyAdapter(activity, this.currency,
					this.currency_old, this);
			lvMain.setAdapter(adapter);
			CurrencyActivity ca = (CurrencyActivity) activity;
			ca.connectAdapterListner();
		}
	}

	public boolean isMyServiceRunning() {
		CurrencyActivity ca = (CurrencyActivity) activity;
		ActivityManager manager = (ActivityManager) ca.getSystemService(ca.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("currency.online.CurrencyService".equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public void onClickByItem(Currency t) {
		setSelectCurrency(t);
		adapter.notifyDataSetChanged();
		sPref = context.getSharedPreferences(NAME_PREF, context.MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putString(CURRENT_SELECT, selectCurrency.getCharCode());
		ed.commit();
	}

	public void setupData() {
		if (parserFlag) {
			restoreData();
			setUpLV();
			setCurrentDate();
			setCurrentCurrency();
		} else {
			MyToast.showWarning(activity, R.string.warning);
		}
	}

	public void reload(Activity activity) {
		this.activity = activity;
		if (t != null) {
			String statusT = t.getStatus().toString();
			if (statusT.equals("RUNNING")) {
				CProgressBar.finish();
				CProgressBar.onCreateDialog(1, activity);
				CProgressBar.setProgress();
			} else {
				setupData();
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
			// TODO can't parse string
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

	public static void setAdapterForSpiner(Activity a, List<Currency> l,
			String currentCurrency) {

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
			// something here
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
		Double t = cash
				* ((Spinner2_value / Spinner2_nominal) / (Spinner3_value / Spinner3_nominal));
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
		this.alaram = data.getFloatExtra(CURRENT_ALARAM, 0);
		this.updatePeriod = data.getIntExtra(CURRENT_UPDATEPERIOD, 1420);
		setCurrentCurrency();
		adapter.notifyDataSetChanged();
		sPref = context.getSharedPreferences(NAME_PREF, context.MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putString(CURRENT_CURRENCY, currentCurrency);
		ed.putInt(CURRENT_NOMINAL, currentNominal);
		ed.putFloat(CURRENT_VALUE, currentValue.floatValue());
		ed.putFloat(CURRENT_ALARAM, alaram);
		ed.putInt(CURRENT_UPDATEPERIOD, updatePeriod);
		ed.putString(CURRENT_ID, currentID);
		ed.commit();
	}

	public static void preperDateForCalculatorActivity(Activity a) {
		spinner4 = (Spinner) a.findViewById(R.id.spinner4);
		editText2 = (EditText) a.findViewById(R.id.editText2);
		SharedPreferences sPref = a.getSharedPreferences(NAME_PREF, a.MODE_PRIVATE);
		Float alaram = sPref.getFloat(CURRENT_ALARAM, 0);
		Integer posObjectUpdatePeriod = sPref.getInt(CURRENT_UPDATEPERIOD, 0);
		editText2.setText(alaram.toString());
		SpinnerUpdateAdapter adapter = new SpinnerUpdateAdapter(a,
				android.R.layout.simple_spinner_item, UPL);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner4.setAdapter(adapter);
		spinner4.setSelection(posObjectUpdatePeriod);
	}

	public boolean serviceLoadAndPrepeData() {
		if (threadPreDate(false)) {
			restoreData();
			if (checkAlaramValue()) {
				return true;
			} else {
				int pos = listFindByArray(currency_old, selectCurrency.getCharCode());
				Currency c_old = currency_old.get(pos);
				Double t_now = ((selectCurrency.getValue() / getCurrentValue()) * getCurrentNominal());
				Double t_old = ((c_old.getValue() / getCurrentValue()) * getCurrentNominal());
				Double diff = t_now - t_old;
				diff = new BigDecimal(diff).setScale(3, RoundingMode.UP).doubleValue();
				if (selectCurrency.getValue() > c_old.getValue()) {
					img_id = Gd.getDrawable("up", context);
					textSystemBar = selectCurrency.getCharCode() + ": " + diff.toString();
					textEventInfo = selectCurrency.getCharCode() + ": " + diff.toString() + "(" + currentCurrency + ")";
				} else if (selectCurrency.getValue() < c_old.getValue()) {
					img_id = Gd.getDrawable("down", context);
					textSystemBar = selectCurrency.getCharCode() + ": " + diff.toString();
					textEventInfo = selectCurrency.getCharCode() + ": " + diff.toString() + "(" + currentCurrency + ")";
				} else {
					img_id = Gd.getDrawable("exactly", context);
					textSystemBar = selectCurrency.getCharCode() + ": " + diff.toString();
					textEventInfo = selectCurrency.getCharCode() + ": " + diff.toString() + "(" + currentCurrency + ")";
				}
				return true;
			}
		} else {
			setMessageNotConnect();
			return false;
		}
	}

	public boolean checkAlaramValue() {
		Double convert = ((selectCurrency.getValue() / getCurrentValue()) * getCurrentNominal());
		convert = new BigDecimal(convert).setScale(3, RoundingMode.UP).doubleValue();
		if (alaram != null && alaram != 0 && alaram >= convert) {
			img_id = Gd.getDrawable("graph16", context);
			textSystemBar = selectCurrency.getCharCode() + " = " + alaram;
			textEventInfo = selectCurrency.getCharCode() + " = " + alaram;
			lightsFlag = true;
			return true;
		} else {
			return false;
		}
	}

	public void setMessageNotConnect() {
		img_id = Gd.getDrawable("graph16", context);
		textSystemBar = context.getText(R.string.ndata).toString();
		textEventInfo = context.getText(R.string.ndata).toString();
		lightsFlag = true;
	}

	public int getUpdatePeriodMin() {
		SpinnerUpdateAdapterElement el = UPL.get(updatePeriod);
		return el.getValue();
	}
}
