package amtt.epam.com.amtt.excel;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 02.07.2015
 */

public class XMLParser {

    private static XmlPullParserFactory xmlFactoryObject;

    public static void xmlParse(XmlPullParser resourse, Context context) {
        try {
            XmlPullParser parser = resourse;//getResources().getXml(R.xml.contacts);

            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                final String TAG = "ЛогКот";
                String tmp;

                switch (parser.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        Logger.d(TAG, "Начало документа");
                        break;
                    // начало тэга
                    case XmlPullParser.START_TAG:
                        Logger.d(TAG,
                                "START_TAG: имя тега = " + parser.getName()
                                        + ", уровень = " + parser.getDepth()
                                        + ", число атрибутов = "
                                        + parser.getAttributeCount());
                        tmp = "";
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            tmp = tmp + parser.getAttributeName(i) + " = "
                                    + parser.getAttributeValue(i) + ", ";
                        }
                        if (!TextUtils.isEmpty(tmp))
                            Logger.d(TAG, "Атрибуты: " + tmp);
                        break;
                    // конец тега
                    case XmlPullParser.END_TAG:
                        Logger.d(TAG, "END_TAG: имя тега = " + parser.getName());
                        break;
                    // содержимое тега
                    case XmlPullParser.TEXT:
                        Logger.d(TAG, "текст = " + parser.getText());
                        break;

                    default:
                        break;
                }
                parser.next();
            }
        } catch (Throwable t) {
            Toast.makeText(context,
                    "Ошибка при загрузке XML-документа: " + t.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }


    public static void fetchXML(final Context context){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL("https://spreadsheets.google.com/feeds/worksheets/1t9Yl39HHl4FjXkT9vGu__Q0glvdIk6SctGrVjeACt5k/public/full");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream stream = conn.getInputStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    xmlParse(myparser, context);
                    stream.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
