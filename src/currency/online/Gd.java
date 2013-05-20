/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

import android.content.Context;
import java.util.Locale;
import junit.framework.Assert;

/**
 *
 * @author petroff
 */
public final class Gd {
	
	public static int getDrawable(String name,Context context) {
		Assert.assertNotNull(context);
		Assert.assertNotNull(name);

		return context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
	}
	
	public static String preName(String name){
		name = name.toLowerCase();
		name = name.substring(0, (name.length() - 1));
		return name;
	}
	
}
