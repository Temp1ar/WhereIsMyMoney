package ru.spbau.WhereIsMyMoney.gui;

import android.widget.ListView;

import java.util.*;

/**
 * Show costs report grouped by place
 */

public class CostsReportByPlacesActivity extends AbstractCostsReportActivity{
    private Map<String, Map<String, Float>> places2costs = null;
    private List<String> places = null;
    private Date start = null;
    private Date end = null;

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
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Intent intent = new Intent(CostsReportByPlacesActivity.this, TransactionsListActivity.class);
//                intent.putExtra(TransactionsListActivity.PLACE, places.get(position));
//                if (start != null)
//                    intent.putExtra(CostsReportByPlacesActivity.START_DATE, start.getTime());
//                if (end != null)
//                    intent.putExtra(CostsReportByPlacesActivity.END_DATE, end.getTime());
//
//                startActivity(intent);
//            }
//        });
    }

    @Override
    protected List<List<Map<String, String>>> getDataForAdapter() {
        List<List<Map<String, String>>> dataForAdapter = new ArrayList<List<Map<String, String>>>();

        Map<String, Map<String, Float>> currency2place2costs = swapFirstAndSecondKeys(places2costs);
        for (String currency : currency2place2costs.keySet()) {
            List<Map<String, String>> places = new ArrayList<Map<String, String>>();

            Map<String, Float> place2costs = currency2place2costs.get(currency);
            for(String place : place2costs.keySet()) {
                Float costs = place2costs.get(place);
                Map<String, String> m = new HashMap<String, String>();
                m.put("place", place);
                m.put("amount", costs.toString());

                places.add(m);
            }
            dataForAdapter.add(places);
        }

        return dataForAdapter;
    }
}
