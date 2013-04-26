package currency.online;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import java.util.List;
import android.widget.ListView;

public class CurrencyActivity extends Activity {

	final String url = "http://www.cbr.ru/scripts/XML_daily.asp?";
	List<Currency> currency;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//startService(new Intent(this, CurrencyService.class));
		if(preperadate()){
		ListView lvMain = (ListView) findViewById(R.id.lvCurrency);
		CurrencyAdapter adapter = new CurrencyAdapter(this, this.currency);
		lvMain.setAdapter(adapter);
		}else{
			//TODO haven't data
		}
	}

	public Boolean preperadate() {
		FileOperation fo = new FileOperation(this);
		if (!Http.hasConnection(this)) {
			String data = fo.readFile();
			if (data == null || data.isEmpty()) {
				Log.v("test", "data is null");
				return false;
			} else {
				currency = XmlParser.parserXml(data);
			}
		} else {
			String data = Http.connect(url);
			currency = XmlParser.parserXml(data);
			fo.writeFile(data);
		}
		return true;
	}
}
