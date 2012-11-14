package ru.spbau.WhereIsMyMoney.gui;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import ru.spbau.WhereIsMyMoney.R;

import java.util.*;

/**
 * Show costs report grouped by place
 */

public class CostsReportByPlacesActivity extends AbstractCostsReportActivity {
    private Map<String, Map<String, Float>> places2costs = null;
    private Date start = null;
    private Date end = null;

    @Override
    protected void init(Date start, Date end) {
        this.places2costs = db.getCostsPerPlacesForPeriod(start, end);
        this.start = start;
        this.end = end;
    }

    @Override
    protected Map<String, Float> getTotalVals() {
        Map<String, Float> currency2costs4all = new HashMap<String, Float>();
        for (Map<String, Float> currency2costs : places2costs.values()) {
            for (String currency : currency2costs.keySet()) {
                Float val = currency2costs4all.get(currency);
                if (val == null)
                    val = 0f;
                currency2costs4all.put(currency, val + currency2costs.get(currency));
            }
        }

        return currency2costs4all;
    }

    @Override
    protected void customizeListView(ExpandableListView listView) {
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
                Intent intent = new Intent(CostsReportByPlacesActivity.this, TransactionsListActivity.class);

                TextView place = (TextView) view.findViewById(R.id.child_name);
                intent.putExtra(TransactionsListActivity.PLACE, place.getText());

                if (start != null)
                    intent.putExtra(CostsReportByPlacesActivity.START_DATE, start.getTime());
                if (end != null)
                    intent.putExtra(CostsReportByPlacesActivity.END_DATE, end.getTime());

                startActivity(intent);

                return true;
            }
        });
    }

    @Override
    protected List<List<Map<String, String>>> getDataForAdapter() {
        List<List<Map<String, String>>> dataForAdapter = new ArrayList<List<Map<String, String>>>();

        Map<String, Map<String, Float>> currency2place2costs = swapFirstAndSecondKeys(places2costs);
        for (String currency : currency2place2costs.keySet()) {
            List<Map<String, String>> places = new ArrayList<Map<String, String>>();

            Map<String, Float> place2costs = currency2place2costs.get(currency);
            for (String place : place2costs.keySet()) {
                Float costs = place2costs.get(place);
                Map<String, String> m = new HashMap<String, String>();
                m.put("name", place);
                m.put("amount", costs.toString());

                places.add(m);
            }
            dataForAdapter.add(places);
        }

        return dataForAdapter;
    }
}
