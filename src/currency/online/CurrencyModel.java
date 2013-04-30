/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

import android.widget.ListView;
import java.util.List;
import android.app.Activity;
import android.widget.TextView;

/**
 *
 * @author petroff
 */
public class CurrencyModel {

	final String url = "http://www.cbr.ru/scripts/XML_daily.asp?";
	List<Currency> currency;
	Activity activity;
	ListView lvMain;
	String CurrentDate;

	public CurrencyModel(Activity activity) {
		this.activity = activity;
	}

	public Boolean preperadate() {
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
		if (preperadate()) {
			setUpLV();
			setCurrentDate();
		} else {
			//TODO haven't data
		}
	}

	private void setCurrentDate() {
		TextView textview = (TextView) activity.findViewById(R.id.dateCurrency);
		textview.setText(CurrentDate);
	}

	private void setUpLV() {
		lvMain = (ListView) activity.findViewById(R.id.lvCurrency);
		CurrencyAdapter adapter = new CurrencyAdapter(activity, this.currency);
		lvMain.setAdapter(adapter);
	}

	public void reload(Activity activity) {
		this.activity = activity;
		setUpLV();
		setCurrentDate();
	}
}
