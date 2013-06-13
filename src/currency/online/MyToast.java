/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

import android.app.Activity;
import android.widget.Toast;

/**
 *
 * @author petroff
 */
public class MyToast {

	public static void showWarning(Activity a, String str) {
		Toast.makeText(a.getApplicationContext(), "this is my Toast message!!! =)", Toast.LENGTH_LONG).show();
	}

	public static void showWarning(Activity a, int r) {
		Toast.makeText(a.getApplicationContext(), r, Toast.LENGTH_LONG).show();
	}
}
