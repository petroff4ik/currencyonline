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

/**
 *
 * @author petroff
 */
public class CurrencyModel {

	final String url = "http://www.cbr.ru/scripts/XML_daily.asp?";
	List<Currency> currency;
	Activity activity;
	ListView lvMain;
	String CurrentDate = "Connect...";

	public CurrencyModel(Activity activity) {
		this.activity = activity;
	}

	public Boolean threadPreDate() {
		
		FileOperation fo = new FileOperation(activity);
		if (!Http.hasConnection(activity)) {
			String data = fo.readFile();
			if (data == null || data.isEmpty()) {
				return false;
			} else {
				currency = XmlParser.parserXml(data);
			}
		} else {
			String data = Http.connect(url);
			currency = XmlParser.parserXml(data);
			fo.writeFile(data);
		}
		CurrentDate = XmlParser.getCDT();
		return true;
	}

	public void preloadData() {
		TaskPreperDate t = new TaskPreperDate(this);
		t.execute();
	}

	private void setCurrentDate() {
		TextView textview = (TextView) activity.findViewById(R.id.dateCurrency);
		textview.setText(CurrentDate);
	}

	private void setUpLV() {
		lvMain = (ListView) activity.findViewById(R.id.lvCurrency);
		if (currency != null) {
			CurrencyAdapter adapter = new CurrencyAdapter(activity, this.currency);
			lvMain.setAdapter(adapter);
		}
	}

	public void setupData() {
		setUpLV();
		setCurrentDate();
	}

	public void reload(Activity activity) {
		this.activity = activity;
		setupData();
	}
}
