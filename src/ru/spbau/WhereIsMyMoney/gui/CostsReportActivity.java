package ru.spbau.WhereIsMyMoney.gui;

import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogSource;

import java.util.*;

/**
 * Show report grouped by cards
 */
public class CostsReportActivity extends Activity {
    private static final String TAG = CostsReportActivity.class.getCanonicalName();
    static final String START_DATE = "startDate";
    static final String END_DATE = "endDate";

    TransactionLogSource db;

    private void createListView() {
        Log.d(TAG, "createListView()");
        final Long startTime = this.getIntent().getLongExtra(START_DATE, -1);
        final Long endTime = this.getIntent().getLongExtra(END_DATE, -1);
        if (startTime == 0) {
            Log.d(TAG, START_DATE + "parameter not found");
            return;
        }
        if (endTime == 0) {
            Log.d(TAG, END_DATE + "parameter not found");
            return;
        }
        Date start = new Date(startTime);
        Date end = new Date(endTime);
        final Map<String, Double> costs = db.getCostsForPeriodPerCards(start,  end);

        ArrayAdapter<Double> adapter = new ArrayAdapter<Double>(this,
                R.layout.simple_list_item_2, R.id.text1, new ArrayList<Double>(costs.values()));

        ListView listView = (ListView) findViewById(ru.spbau.WhereIsMyMoney.R.id.places);
        listView.setAdapter(adapter);

        TextView totalVal = (TextView) findViewById(ru.spbau.WhereIsMyMoney.R.id.totalVal);
        totalVal.setText(sum(costs.values()).toString());
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(...)");
        super.onCreate(savedInstanceState);
        db = new TransactionLogSource(getApplicationContext());
        db.open();
        setContentView(ru.spbau.WhereIsMyMoney.R.layout.costs_report);
        createListView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    private Double sum(Collection<Double> numbers) {
        double sum = 0;
        for(Double i : numbers) {
            sum += i;
        }
        return sum;
    }
}
