/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

import android.widget.ListView;
import java.util.List;
import android.app.Activity;
import android.widget.TextView;
import android.util.Log;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author petroff
 */
public class CurrencyModel  {

	String url = "http://www.cbr.ru/scripts/XML_daily.asp?";
	//http://www.cbr.ru/scripts/XML_daily.asp?date_req=02/03/2002
	List<Currency> currency;
	List<Currency> currency_old;
	Activity activity;
	ListView lvMain;
	String CurrentDate = "";
	TaskPreperDate t;
	String defaultCurrency = "RUR";

	public CurrencyModel(Activity activity) {
		this.activity = activity;
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
		if(t != null){
			String statusT = t.getStatus().toString();
			if(statusT.equals("RUNNING")){
				CProgressBar.onCreateDialog(1,activity);
				CProgressBar.setProgress();
			}
			
		}
		this.activity = activity;
		setupData();
	}
	
	public String getPrevData(String dt){
		  Date dt2 = new Date();
		  SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		  try {
		   dt2 = sdf.parse(dt);
		  }catch(Exception e) {
			  //TODO can't parse string
		  } 
	      Calendar c=Calendar.getInstance();
	      c.setTime(dt2);
	      c.add(Calendar.DATE,-1);
	      sdf.applyPattern("dd/MM/yyyy");
	      return sdf.format(c.getTime());
	}
}
