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
	CurrencyModel model;

	public void onCreate() {
		super.onCreate();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Log.d(LOG_TAG, "onCreate");
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "onStartCommand" + startId);
		this.startId = startId;
		model = new CurrencyModel(this.getApplicationContext());
		model.restoreUpdateTime();
		int period = model.getUpdatePeriodMin();
		if (period > 0) {
			someTask((long) (period * 60 * 1000));
		} else {
			stopSelf();
		}
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

	private void someTask(Long period) {
		Timer myTimer = new Timer();

		myTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (model.serviceLoadAndPrepeData()) {
					if (model.checkAlaramValue()) {
						sendNotif(model.getTextSystemBar(), model.getTextEventInfo(), model.getImg_id(), true);
					} else {
						sendNotif(model.getTextSystemBar(), model.getTextEventInfo(), model.getImg_id(), false);
					}
				} else {
					//todo haven't data
					model.setNotHaveData();
					sendNotif(model.getTextSystemBar(), model.getTextEventInfo(), model.getImg_id(), true);
				}
			}
		 ;
		}, period, period); // interval
	}

	private void sendNotif(String textSystemBar, String textEventInfo, int r_id, Boolean m) {

		Notification notif = new Notification(r_id,
				textSystemBar, System.currentTimeMillis());
		notif.defaults = Notification.DEFAULT_LIGHTS;
		Intent intent = new Intent(this, CurrencyActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		CharSequence titleForEvent = this.getText(R.string.title_notification);
		notif.setLatestEventInfo(this, titleForEvent, textEventInfo, pIntent);
		notif.flags |= Notification.FLAG_AUTO_CANCEL;
		notif.defaults |= Notification.DEFAULT_SOUND;
		notif.defaults |= Notification.DEFAULT_LIGHTS;
		notif.defaults |= Notification.DEFAULT_VIBRATE;

		if (m) {
			notif.flags |= Notification.FLAG_SHOW_LIGHTS;
			notif.ledARGB = 0xff00ff00;
			notif.ledOnMS = 300;
			notif.ledOffMS = 1000;
		}

		nm.notify(1, notif);
	}
}
