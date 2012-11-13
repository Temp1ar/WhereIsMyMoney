package ru.spbau.WhereIsMyMoney.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Setup and show report
 */
public class CostsReportSetupActivity extends Activity {
    private static final String TAG = CostsReportSetupActivity.class.getCanonicalName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(...)");
        super.onCreate(savedInstanceState);
        setContentView(ru.spbau.WhereIsMyMoney.R.layout.costs_report_setup);

        final DatePicker startDate = (DatePicker) findViewById(ru.spbau.WhereIsMyMoney.R.id.startDate);
        final DatePicker endDate = (DatePicker) findViewById(ru.spbau.WhereIsMyMoney.R.id.endDate);
        Button showReport = (Button) findViewById(ru.spbau.WhereIsMyMoney.R.id.showReport);

        showReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Calendar cal = new GregorianCalendar();
                cal.set(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
                Long start = cal.getTimeInMillis();
                cal.set(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth());
                Long end = cal.getTimeInMillis();

                Intent intent = new Intent(CostsReportSetupActivity.this, CostsReportActivity.class);
                intent.putExtra(CostsReportActivity.START_DATE, start);
                intent.putExtra(CostsReportActivity.END_DATE, end);
                startActivity(intent);
            }
        });
    }
}
