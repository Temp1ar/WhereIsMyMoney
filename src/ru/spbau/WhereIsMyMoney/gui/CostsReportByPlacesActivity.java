package ru.spbau.WhereIsMyMoney.gui;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Show costs report grouped by place
 */

public class CostsReportByPlacesActivity extends AbstractCostsReportActivity{
    Map<String, Float> places2costs = null;
    List<String> places = null;

    @Override
    protected void init(Date start, Date end) {
        places2costs = db.getCostsPerPlacesForPeriod(start, end);
        places = new ArrayList<String>(places2costs.keySet());
    }

    @Override
    protected String getTotalVal() {
        return sum(places2costs.values()).toString();
    }

    @Override
    protected void customizeListView(ListView listView) {
    }

    @Override
    protected List<String> getDataForAdapter() {
        List<String> dataForAdapter = new ArrayList<String>();
        for (String key : places) {
            dataForAdapter.add(key + "\n" + places2costs.get(key));
        }

        return dataForAdapter;
    }
}
