/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

import android.widget.ListView;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.widget.TextView;
import android.util.Log;
import java.util.Calendar;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.widget.Spinner;
import android.widget.EditText;

/**
 *
 * @author petroff
 */
public class CurrencyModel {

	String url = "http://www.cbr.ru/scripts/XML_daily.asp?";
	//http://www.cbr.ru/scripts/XML_daily.asp?date_req=02/03/2002
	List<Currency> currency;
	List<Currency> currency_old;
	Activity activity;
	ListView lvMain;
	String CurrentDate = "";
	TaskPreperDate t;
	String defaultCurrency = "RUR";
	static Spinner spinner;
	static Spinner spinner2;
	static Spinner spinner3;
	static EditText edit;
	private static Double Spinner2_value;
	private static int Spinner2_nominal;
	private static Double Spinner3_value;
	private static int Spinner3_nominal;

	public CurrencyModel(Activity activity) {
		this.activity = activity;
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

	public Boolean threadPreDate() {

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

		return true;
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
			CurrencyAdapter adapter = new CurrencyAdapter(activity, this.currency, this.currency_old);
			lvMain.setAdapter(adapter);
		}
	}

	public void setupData() {
		setUpLV();
		setCurrentDate();
	}

	public void reload(Activity activity) {
		if (t != null) {
			String statusT = t.getStatus().toString();
			if (statusT.equals("RUNNING")) {
				CProgressBar.onCreateDialog(1, activity);
				CProgressBar.setProgress();
			}

		}
		this.activity = activity;
		setupData();
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
	
	public static Spinner getSpinner(){
		return spinner;
	}

	public static void setAdapterForSpiner(Activity a, List<Currency> l) {

		spinner = (Spinner) a.findViewById(R.id.spinner1);
		spinner2 = (Spinner) a.findViewById(R.id.spinner2);
		spinner3 = (Spinner) a.findViewById(R.id.spinner3);
		edit = (EditText) a.findViewById(R.id.editText1);
		if (l != null) {
			CurrencyAdapter adapter = new CurrencyAdapter(a, l);
			spinner.setAdapter(adapter);
			spinner.setSelection(listFindByArray(l, "SEK"));
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
	
	public static String currencyExchange(String str){
		String result = "";
		NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
		int cash = 0;
		try {
			Number number = format.parse(str);
			cash = number.intValue();
		} catch (ParseException p) {
			result = "Error, only numeric";
		}
		Double t = cash / (Spinner2_value/Spinner3_value);
		result = t.toString();
		return result;
	}
}
