package ru.spbau.WhereIsMyMoney.gui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogSource;

import java.util.*;

/**
 * Base class for reports
 */
abstract class AbstractCostsReportActivity extends Activity {
    private static final String TAG = AbstractCostsReportActivity.class.getCanonicalName();
    static final String START_DATE = "startDate";
    static final String END_DATE = "endDate";

    protected  abstract void init(Date start, Date end);

    protected abstract List<String> getTotalVals();

    protected abstract void customizeListView(ListView listView);

    protected abstract List<List<Map<String, String>>> getDataForAdapter();

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

//        ArrayAdapter<String> transactions = new ArrayAdapter<String>(this,
//                R.layout.simple_list_item_2, R.id.text1, getDataForAdapter());
//
//        ListView transactionsView = (ListView) findViewById(ru.spbau.WhereIsMyMoney.R.id.transactions);
//        transactionsView.setAdapter(transactions);
//        customizeListView(transactionsView);
//
//        ArrayAdapter<String> totalVals = new ArrayAdapter<String>(this,
//                R.layout.simple_list_item_2, R.id.text1, getTotalVals());

//        ListView totalValsView = (ListView) findViewById(ru.spbau.WhereIsMyMoney.R.id.totalVals);
//        totalValsView.setAdapter(totalVals);

        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        for (String val : getTotalVals()) {
            Map<String, String> m = new HashMap<String, String>();
            m.put("groupName", val);
            groupData.add(m);
        }
        String groupFrom[] = new String[] {"groupName"};
        int groupTo[] = new int[] {android.R.id.text1};

        String childFrom[] = new String[] {"amount"};
        int childTo[] = new int[] {android.R.id.text1};

        List<List<Map<String, String>>> childData = getDataForAdapter();

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(this, groupData, android.R.layout.simple_expandable_list_item_1,
                groupFrom, groupTo, childData, android.R.layout.simple_list_item_1,
                childFrom, childTo);

        ExpandableListView transactionsView = (ExpandableListView) findViewById(ru.spbau.WhereIsMyMoney.R.id.transactions);
        transactionsView.setAdapter(adapter);
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

    Map<String, Map<String, Float>> swapFirstAndSecondKeys(Map<String, Map<String, Float>> input) {
        Map<String, Map<String, Float>> second2first2value = new HashMap<String, Map<String, Float>>();
        for (String card : input.keySet()) {
            Map<String, Float> second2value = input.get(card);
            for(String currency : second2value.keySet()) {
                Map<String, Float> first2value = second2first2value.get(currency);
                if (first2value == null)
                    first2value = new HashMap<String, Float>();

                first2value.put(card, second2value.get(currency));
                second2first2value.put(currency, first2value);
            }
        }
        return second2first2value;
    }

}
