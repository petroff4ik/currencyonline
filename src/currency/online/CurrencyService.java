/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

/**
 *
 * @author petroff
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;

public class CurrencyService extends Service {
	int startId;
	final String LOG_TAG = "myLogs";
	NotificationManager nm;

	public void onCreate() {
		super.onCreate();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Log.d(LOG_TAG, "onCreate");
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "onStartCommand" + startId);
		this.startId = startId;
		CurrencyModel model = new CurrencyModel(this.getApplicationContext());
		model.serviceLoadAndPrepeData();
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

	private void someTask() {
		Timer myTimer = new Timer();

		myTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				Log.d(LOG_TAG, "Timer" + startId);
			};
		}, 0L, 60L * 100); // interval

	}

	private void sendNotif(String textSystemBar, String textEventInfo) {

		Notification notif = new Notification(R.drawable.graph16,
				textSystemBar, System.currentTimeMillis());
		Intent intent = new Intent(this, CurrencyActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		CharSequence titleForEvent = this.getText(R.string.title_notification);
		notif.setLatestEventInfo(this, titleForEvent, textEventInfo, pIntent);
		notif.flags |= Notification.FLAG_AUTO_CANCEL;
		nm.notify(1, notif);
	}
}
