package ru.spbau.WhereIsMyMoney.gui;

import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogSource;

import java.lang.Float;
import java.util.*;

/**
 * Base class for reports
 */
public abstract class AbstractCostsReportActivity extends Activity {
    private static final String TAG = AbstractCostsReportActivity.class.getCanonicalName();
    static final String START_DATE = "startDate";
    static final String END_DATE = "endDate";

    protected  abstract void init(Date start, Date end);

    protected abstract String getTotalVal();

    protected abstract void customizeListView(ListView listView);

    protected abstract List<String> getDataForAdapter();

    TransactionLogSource db;

    private void createListView() {
        Log.d(TAG, "createListView()");
        final long startTime = this.getIntent().getLongExtra(START_DATE, -1);
        final long endTime = this.getIntent().getLongExtra(END_DATE, -1);
        if (startTime == -1) {
            Log.d(TAG, START_DATE + "parameter not found");
            return;
        }
        if (endTime == -1) {
            Log.d(TAG, END_DATE + "parameter not found");
            return;
        }
        Date start = new Date(startTime);
        Date end = new Date(endTime);

        Log.d(TAG, "Generate report from " + start.toString() + " to " + end.toString());

        init(start, end);
        List<String> dataForAdapter = getDataForAdapter();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.simple_list_item_2, R.id.text1, dataForAdapter);

        ListView listView = (ListView) findViewById(ru.spbau.WhereIsMyMoney.R.id.places);
        listView.setAdapter(adapter);
        customizeListView(listView);

        TextView totalVal = (TextView) findViewById(ru.spbau.WhereIsMyMoney.R.id.totalVal);
        totalVal.setText(getTotalVal());
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

    protected Float sum(Collection<Float> numbers) {
        float sum = 0;
        for(Float i : numbers) {
            sum += i;
        }
        return sum;
    }
}
