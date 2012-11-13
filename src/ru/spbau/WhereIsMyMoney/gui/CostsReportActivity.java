package ru.spbau.WhereIsMyMoney.gui;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogSource;

import java.lang.Float;
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
        final Map<String, Float> cards2costs = db.getCostsPerCardsForPeriod(start, end);
        final List<String> cards = new ArrayList<String>(cards2costs.keySet());
//        final List<Double> costs = new ArrayList<Double>();
        final List<String> dataForAdapter = new ArrayList<String>();

        for (String key : cards) {
            dataForAdapter.add(key + "\n" + cards2costs.get(key));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.simple_list_item_2, R.id.text1, dataForAdapter);

        ListView listView = (ListView) findViewById(ru.spbau.WhereIsMyMoney.R.id.places);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(CostsReportActivity.this, TransactionsListActivity.class);
                String card = cards.get(position);
                intent.putExtra(TransactionsListActivity.ID_PARAM, card);
                startActivity(intent);
            }
        });

        TextView totalVal = (TextView) findViewById(ru.spbau.WhereIsMyMoney.R.id.totalVal);
        totalVal.setText(sum(cards2costs.values()).toString());
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

    private Float sum(Collection<Float> numbers) {
        float sum = 0;
        for(Float i : numbers) {
            sum += i;
        }
        return sum;
    }
}
