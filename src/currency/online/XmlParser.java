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
import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.StringReader;

public class XmlParser {

	public void parserXml(String xml) {

		try {
			XmlPullParser parser = prepareXpp(xml);
			Integer i = 0;
			while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
				Log.v("test",parser.getName());
				if (parser.getEventType() == XmlPullParser.START_TAG
						&& parser.getName().equals("Valute")) {
					parser.getAttributeValue(0);
				} else if (parser.getEventType() == XmlPullParser.START_TAG
						&& parser.getName().equals("link")) {
					parser.getAttributeValue(0);
				}



				parser.next();
			}
		} catch (Throwable t) {
		}

	}

	private XmlPullParser prepareXpp(String xml) throws XmlPullParserException {
		// получаем фабрику
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// включаем поддержку namespace (по умолчанию выключена)
		factory.setNamespaceAware(true);
		// создаем парсер
		XmlPullParser xpp = factory.newPullParser();
		// даем парсеру на вход Reader
		xpp.setInput(new StringReader(xml));
		return xpp;
	}
}
