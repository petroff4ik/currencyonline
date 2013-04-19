package currency.online;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CurrencyActivity extends Activity {

	final String url = "http://www.cbr.ru/scripts/XML_daily.asp?";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//startService(new Intent(this, CurrencyService.class));
		if (!Http.hasConnection(this)) {
		} else {
			parserXml(Http.connect(url));
		}
	}

	
}
