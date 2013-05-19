package currency.online;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;

public class CurrencyCalculator extends Activity implements
		OnItemSelectedListener, OnKeyListener {

	String selectCurrency;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calc);
		MyParcelable myObj = (MyParcelable) getIntent().getParcelableExtra(
				MyParcelable.class.getCanonicalName());
		CurrencyModel.setAdapterForSpiner(this, myObj.currency);
		CurrencyModel.spinner.setOnItemSelectedListener(this);
		CurrencyModel.spinner2.setOnItemSelectedListener(this);
		CurrencyModel.spinner3.setOnItemSelectedListener(this);
		CurrencyModel.edit.setOnKeyListener(this);
	}

	// setResult(RESULT_OK, intent);
	// finish();

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Currency t = (Currency) parent.getItemAtPosition(pos);
		if (parent.getId() == R.id.spinner1) {
			selectCurrency = t.getCharCode();
		} else if (parent.getId() == R.id.spinner2) {
			CurrencyModel.setSpinner2_value(t.getValue());
			CurrencyModel.setSpinner2_nominal(t.getNominal());
		} else if (parent.getId() == R.id.spinner3) {
			CurrencyModel.setSpinner3_value(t.getValue());
			CurrencyModel.setSpinner3_nominal(t.getNominal());
			calc();
		}

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}

	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
			calc();
			return true;
		}
		return false;
	}

	public void calc() {
		String result = CurrencyModel.currencyExchange(CurrencyModel.edit
				.getText().toString());
		TextView text = (TextView) this.findViewById(R.id.res);
		text.setText(result);
	}
}
