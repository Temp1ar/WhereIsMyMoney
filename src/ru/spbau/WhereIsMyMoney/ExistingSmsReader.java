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
        Cursor cursor = cr.query(allMessage, new String[]{"address", "body", "date"}, null, null, null);
        while (cursor.moveToNext()) {
            list.add(new SmsEvent(cursor.getString(0), cursor.getString(1), cursor.getLong(2)));
        }
        cursor.close();
        return list;
    }

    public static List<SmsEvent> getAfterDate(Date date, Context context) {
        List<SmsEvent> filteredSmses = new ArrayList<SmsEvent>();

        for (SmsEvent smsEvent : getAll(context)) {
            Date curSmsDate = smsEvent.getDate();
            if (curSmsDate.after(date)) {
                filteredSmses.add(smsEvent);
            }
        }

        return filteredSmses;
    }
}
