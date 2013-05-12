/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

import android.os.AsyncTask;
import android.util.Log;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author petroff
 */
public class TaskPreperDate extends AsyncTask<Void, Void, Void> {

	CurrencyModel model;

	TaskPreperDate(CurrencyModel currencyModel) {
		super();
		this.model = currencyModel;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		CProgressBar.onCreateDialog(1, model.activity);
		CProgressBar.setProgress();
	}

	@Override
	protected Void doInBackground(Void... params) {
		model.threadPreDate();
		return null;
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		model.setupData();
		CProgressBar.finishProgress();
		int i;
	}
}
