package ru.spbau.WhereIsMyMoney.gui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.String;
import java.util.*;

/**
 * Show costs report grouped by place
 */

public class CostsReportByPlacesActivity extends AbstractCostsReportActivity{
    Map<String, Map<String, Float>> places2costs = null;
    List<String> places = null;
    Date start = null;
    Date end = null;

    @Override
    protected void init(Date start, Date end) {
        this.places2costs = db.getCostsPerPlacesForPeriod(start, end);
        this.places = new ArrayList<String>(places2costs.keySet());
        this.start = start;
        this.end = end;
    }

    @Override
    protected List<String> getTotalVals() {
        Map<String, Float> currency2costs4all = new HashMap<String, Float>();
        for (Map<String, Float> currency2costs : places2costs.values()) {
            for(String currency : currency2costs.keySet()) {
                Float val = currency2costs4all.get(currency);
                if (val == null)
                    val = 0f;
                currency2costs4all.put(currency, val + currency2costs.get(currency));
            }
        }

        List<String> ret = new ArrayList<String>();
        for (String currency : currency2costs4all.keySet()) {
            ret.add(currency2costs4all.get(currency).toString() + " " + currency);
        }

        return ret;
    }

    @Override
    protected void customizeListView(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(CostsReportByPlacesActivity.this, TransactionsListActivity.class);
                intent.putExtra(TransactionsListActivity.PLACE, places.get(position));
                if (start != null)
                    intent.putExtra(CostsReportByPlacesActivity.START_DATE, start.getTime());
                if (end != null)
                    intent.putExtra(CostsReportByPlacesActivity.END_DATE, end.getTime());

                startActivity(intent);
            }
        });

    }

    @Override
    protected List<String> getDataForAdapter() {
        List<String> dataForAdapter = new ArrayList<String>();
        for (String place : places2costs.keySet()) {
            Map<String, Float> currency2costs = places2costs.get(place);
            for(String currency : currency2costs.keySet()) {
                dataForAdapter.add(place + "\n" + currency2costs.get(currency).toString() + " " + currency);
            }
        }

        return dataForAdapter;
    }
}
