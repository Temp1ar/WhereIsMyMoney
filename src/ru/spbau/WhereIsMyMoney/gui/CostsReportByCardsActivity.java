package ru.spbau.WhereIsMyMoney.gui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.*;

/**
 * Show costs report grouped by cards
 */
public class CostsReportByCardsActivity extends AbstractCostsReportActivity {
    Map<String, Map<String,Float>> cards2costs = null;
    List<String> cards = null;
    Date start = null;
    Date end = null;

    @Override
    protected void init(Date start, Date end) {
        this.cards2costs = db.getCostsPerCardsForPeriod(start, end);
        this.cards = new ArrayList<String>(cards2costs.keySet());
        this.start = start;
        this.end = end;
    }

    @Override
    protected List<String> getTotalVals() {
        Map<String, Float> currency2costs4all = new HashMap<String, Float>();
        for (Map<String, Float> currency2costs : cards2costs.values()) {
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
                Intent intent = new Intent(CostsReportByCardsActivity.this, TransactionsListActivity.class);
                intent.putExtra(TransactionsListActivity.ID_PARAM, cards.get(position));
                if (start != null)
                    intent.putExtra(AbstractCostsReportActivity.START_DATE, start.getTime());
                if (end != null)
                    intent.putExtra(AbstractCostsReportActivity.END_DATE, end.getTime());

                startActivity(intent);
            }
        });
    }

    @Override
    protected List<String> getDataForAdapter() {
        List<String> dataForAdapter = new ArrayList<String>();

        for (String card : cards2costs.keySet()) {
            Map<String, Float> currency2costs = cards2costs.get(card);
            if (currency2costs.isEmpty()) {
                int i = cards.indexOf(card);
                if (i != -1) {
                    cards.remove(i);
                }
            }
            for(String currency : currency2costs.keySet()) {
                dataForAdapter.add(card + "\n" + currency2costs.get(currency).toString() +" " + currency);
            }
        }

        return dataForAdapter;
    }
}
