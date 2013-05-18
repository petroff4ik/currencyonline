/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

/**
 *
 * @author petroff
 */
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.StringReader;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Date;
import java.text.ParseException;

public class XmlParser {
	
	static String currentDt = new java.util.Date().toString ();

	public static List<Currency> parserXml(String xml) {
		List<Currency> list = new ArrayList<Currency>();
		try {
			XmlPullParser parser = prepareXpp(xml);
			String date = new java.util.Date().toString();
			String currentTag = "";
			Currency currency = new Currency();
			while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (parser.getEventType() == XmlPullParser.START_TAG && !parser.getName().equals("ValCurs")) {
					currentTag = parser.getName();
				} else if (parser.getEventType() == XmlPullParser.TEXT) {

					if (currentTag.equals("CharCode")) {
						currency.setCharCode(parser.getText());
					} else if (currentTag.equals("Name")) {
						currency.setName(parser.getText());
					} else if (currentTag.equals("Value")) {
						NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
						double d = 0;
						try {
							Number number = format.parse(parser.getText());
							d = number.doubleValue();
						} catch (ParseException p) {
						}
						currency.setValue(d);
					} else if(currentTag.equals("Nominal")){
						NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
						int i = 0;
						try {
							Number number = format.parse(parser.getText());
							i = number.intValue();
						} catch (ParseException p) {
						}
						currency.setNominal(i);
					}
					currentTag = "";
				} else if (parser.getEventType() == XmlPullParser.START_TAG && !parser.getName().equals("ValCurs")) {
					date = parser.getAttributeValue(0);
				} else if (parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals("Valute")) {
					currency.setDate(date);
					list.add(currency);
					currency = new Currency();
				}else if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("ValCurs")) {
					currentDt = parser.getAttributeValue(0);
				}

				parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;

	}
	
	public static String getCDT(){
		return currentDt;
	}

	private static XmlPullParser prepareXpp(String xml) throws XmlPullParserException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(new StringReader(xml));
		return xpp;
	}
	
	
}
