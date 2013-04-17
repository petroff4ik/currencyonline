package currency.online;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class CurrencyActivity extends Activity {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		startService(new Intent(this, CurrencyService.class));
	}
}
