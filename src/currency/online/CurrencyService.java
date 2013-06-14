/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

/**
 *
 * @author petroff
 */
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;

public class CurrencyService extends Service {
	int startId;
	final String LOG_TAG = "myLogs";

	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG, "onCreate");
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "onStartCommand" + startId);
		this.startId = startId;
		someTask();
		return START_STICKY;
	}

	public void onDestroy() {
		super.onDestroy();
		Log.d(LOG_TAG, "onDestroy");
	}

	public IBinder onBind(Intent intent) {
		Log.d(LOG_TAG, "onBind");
		return null;
	}

	void someTask() {
		Timer myTimer = new Timer();

		myTimer.schedule(new TimerTask() { 

			@Override
			public void run() {
				Log.d(LOG_TAG, "Timer" + startId);
			}
		 ;}, 0L, 60L * 100); // interval

	}
}
