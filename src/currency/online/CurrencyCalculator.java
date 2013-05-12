package currency.online;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class CurrencyCalculator extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calc);
		MyParcelable myObj = (MyParcelable) getIntent().getParcelableExtra(
				MyParcelable.class.getCanonicalName());
		Log.v("test"," " + myObj);
	}
	//setResult(RESULT_OK, intent);
    //finish();


}
