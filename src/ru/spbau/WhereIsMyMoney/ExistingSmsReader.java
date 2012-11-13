package ru.spbau.WhereIsMyMoney;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

public class ExistingSmsReader {

//    http://androidforums.com/application-development/158592-querying-sms-content-provider.html
//    [0] "_id" (id=830102691400)
//    [1] "thread_id" (id=830102691464)
//    [2] "toa" (id=830102691536)
//    [3] "address" (id=830102691600)
//    [4] "person" (id=830102691672)
//    [5] "date" (id=830102691736)
//    [6] "protocol" (id=830102691800)
//    [7] "read" (id=830102691872)
//    [8] "status" (id=830102691936)
//    [9] "type" (id=830102692000)
//    [10] "reply_path_present" (id=830102692064)
//    [11] "subject" (id=830102692152)
//    [12] "body" (id=830102692224)
//    [13] "sc_toa" (id=830102692288)
//    [14] "report_date" (id=830102692352)
//    [15] "service_center" (id=830102692432)
//    [16] "locked" (id=830102692512)
//    [17] "index_on_sim" (id=830102692576)
//    [18] "callback_number" (id=830102692656)
//    [19] "priority" (id=830102692744)
//    [20] "htc_category" (id=830102692816)
//    [21] "cs_timestamp" (id=830102692896)
//    [22] "cs_id" (id=830102692976)
//    [23] "cs_synced" (id=830102693040)
//    [24] "error_code" (id=830102693112)
//    [25] "seen" (id=830102693184)

    public static ArrayList<SmsEvent> getAll(Context context, String whereClause) {
        ArrayList<SmsEvent> list = new ArrayList<SmsEvent>();
        Uri allMessage = Uri.parse("content://sms/inbox");
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(allMessage, new String[] {"address", "body", "date"}, whereClause, null, null);
        while  (c.moveToNext()) {
           list.add(new SmsEvent(c.getString(0), c.getString(1), c.getLong(2)));
        }
        return list;
    }
}
