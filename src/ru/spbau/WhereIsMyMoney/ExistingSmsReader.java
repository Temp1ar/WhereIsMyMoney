package ru.spbau.WhereIsMyMoney;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingSmsReader {

//    http://androidforums.com/application-development/158592-querying-sms-content-provider.html

    public static ArrayList<SmsEvent> getAll(Context context) {
        ArrayList<SmsEvent> list = new ArrayList<SmsEvent>();
        Uri allMessage = Uri.parse("content://sms/inbox");
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(allMessage, new String[]{"address", "body", "date"}, null, null, null);
        while (c.moveToNext()) {
            list.add(new SmsEvent(c.getString(0), c.getString(1), c.getLong(2)));
        }
        return list;
    }

    public static List<SmsEvent> getAfterDate(Date date, Context context) {
        List<SmsEvent> filteredSmses = new ArrayList<SmsEvent>();

        for (SmsEvent smsEvent : getAll(context)) {
            if (smsEvent.getDate().before(date)) {
                filteredSmses.add(smsEvent);
            }
        }

        return filteredSmses;
    }
}
