package currency.online;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;

public class CurrencyCalculator extends Activity implements OnItemSelectedListener, OnKeyListener {

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
	//setResult(RESULT_OK, intent);
	//finish();

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Currency t = (Currency)parent.getItemAtPosition(pos);
		if(parent.getId() == R.id.spinner1){
			selectCurrency = t.getCharCode();
		}else{
			
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event)
	{
		String t = event.toString();;
	    if(event.getAction() == KeyEvent.ACTION_DOWN && 
		    (keyCode == KeyEvent.KEYCODE_ENTER))
			{
			    // сохраняем текст, введенный до нажатия Enter в переменн  
				return true;
			}
		return false;
	}
}
