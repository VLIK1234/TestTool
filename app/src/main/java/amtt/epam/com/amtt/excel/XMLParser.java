package amtt.epam.com.amtt.excel;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import amtt.epam.com.amtt.excel.bo.GoogleApiConst;
import amtt.epam.com.amtt.excel.bo.GoogleEntrySpreadshet;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 02.07.2015
 */

public class XMLParser {

    private static XmlPullParserFactory xmlFactoryObject;

    public static void xmlParse(XmlPullParser resourse, Context context) {
        try {
            while (resourse.getEventType() != XmlPullParser.END_DOCUMENT) {
                final String TAG = "ЛогКот";
                GoogleEntrySpreadshet spreadshet = new GoogleEntrySpreadshet();
                String tmp = "";
                switch (resourse.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        Logger.d(TAG, "Начало документа");
                        break;
                    case XmlPullParser.START_TAG:
                        if (resourse.getDepth() == 2) {
                            if(resourse.getName() == GoogleApiConst.ID_TAG){
                                spreadshet.setIdLink(resourse.getText());
                                Logger.d(TAG, "IDDD = " + spreadshet.getIdLink());
                            }else if(resourse.getName() == GoogleApiConst.UPDATED_TAG){

                            }
                            Logger.d(TAG, "START_TAG: имя тега = " + resourse.getName() + ", уровень = "
                                    + resourse.getDepth() + ", число атрибутов = " + resourse.getAttributeCount());
                            for (int i = 0; i < resourse.getAttributeCount(); i++) {
                                tmp = tmp + resourse.getAttributeName(i) + " = " + resourse.getAttributeValue(i) + ", ";
                            }
                            if (!TextUtils.isEmpty(tmp))
                                Logger.d(TAG, "Атрибуты: " + tmp);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        Logger.d(TAG, "END_TAG: имя тега = " + resourse.getName());
                        break;
                    case XmlPullParser.TEXT:
                        Logger.d(TAG, "текст = " + resourse.getText());
                        break;
                    default:
                        break;
                }
                resourse.next();
            }
        } catch (Throwable t) {
            Toast.makeText(context,
                    "Ошибка при загрузке XML-документа: " + t.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }


    public static void fetchXML(final Context context) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(GoogleApiConst.SPREADSHEET_PATH);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
