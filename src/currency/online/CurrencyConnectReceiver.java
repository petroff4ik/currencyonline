/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

/**
 *
 * @author petroff
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CurrencyConnectReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (Http.hasConnection(context)) {
			context.startService(new Intent(context, CurrencyService.class));
		} else {
			context.stopService(new Intent(context, CurrencyService.class));
		}

	}
}
