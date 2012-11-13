package ru.spbau.WhereIsMyMoney.gui;

import android.widget.ListView;

import java.lang.String;
import java.util.*;

/**
 * Show costs report grouped by place
 */

public class CostsReportByPlacesActivity extends AbstractCostsReportActivity{
    Map<String, Map<String, Float>> places2costs = null;
    List<String> places = null;

    @Override
    protected void init(Date start, Date end) {
        places2costs = db.getCostsPerPlacesForPeriod(start, end);
        places = new ArrayList<String>(places2costs.keySet());
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
            ret.add(currency2costs4all.get(currency).toString() + "  " + currency);
        }

        return ret;
    }

    @Override
    protected void customizeListView(ListView listView) {
    }

    @Override
    protected List<String> getDataForAdapter() {
        List<String> dataForAdapter = new ArrayList<String>();
        for (String place : places2costs.keySet()) {
            Map<String, Float> currency2costs = places2costs.get(place);
            for(String currency : currency2costs.keySet()) {
                dataForAdapter.add(place + "\n" + currency2costs.get(currency).toString() + currency);
            }
        }

        return dataForAdapter;
    }
}
