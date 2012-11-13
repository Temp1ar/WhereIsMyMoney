package ru.spbau.WhereIsMyMoney.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import ru.spbau.WhereIsMyMoney.R;

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
        Button showReportByCards = (Button) findViewById(R.id.showReportByCards);
        Button showReportByPlaces = (Button) findViewById(R.id.showReportByPlaces);

        showReportByPlaces.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "Try to show report by places");

                long start = unixTime(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
                long end = unixTime(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth());

                Intent intent = new Intent(CostsReportSetupActivity.this, CostsReportByPlacesActivity.class);
                intent.putExtra(AbstractCostsReportActivity.START_DATE, start);
                intent.putExtra(AbstractCostsReportActivity.END_DATE, end);
                startActivity(intent);
            }
        });

        showReportByCards.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "Try to show report by cards");

                long start = unixTime(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
                long end = unixTime(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth());

                Intent intent = new Intent(CostsReportSetupActivity.this, CostsReportByCardsActivity.class);
                intent.putExtra(AbstractCostsReportActivity.START_DATE, start);
                intent.putExtra(AbstractCostsReportActivity.END_DATE, end);
                startActivity(intent);
            }
        });
    }

    long unixTime(int year, int month, int day) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, month, day);
        return cal.getTimeInMillis();
    }

}
