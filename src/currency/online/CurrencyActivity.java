package currency.online;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CurrencyActivity extends Activity {

	CurrencyModel model;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//startService(new Intent(this, CurrencyService.class));
		model = (CurrencyModel) getLastNonConfigurationInstance();
		if (model == null) {
			model = new CurrencyModel(this);
			model.preloadData();
		}else{
			model.reload(this);
		}

	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return model;
	}
	
	
}


