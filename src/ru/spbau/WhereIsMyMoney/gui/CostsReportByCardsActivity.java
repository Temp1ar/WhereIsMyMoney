package ru.spbau.WhereIsMyMoney.gui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Show costs report grouped by cards
 */
public class CostsReportByCardsActivity extends AbstractCostsReportActivity {
    Map<String, Float> cards2costs = null;
    List<String> cards = null;

    @Override
    protected void init(Date start, Date end) {
        cards2costs = db.getCostsPerCardsForPeriod(start, end);
        cards = new ArrayList<String>(cards2costs.keySet());
    }

    @Override
    protected String getTotalVal() {
        return sum(cards2costs.values()).toString();
    }

    @Override
    protected void customizeListView(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(CostsReportByCardsActivity.this, TransactionsListActivity.class);
                String card = cards.get(position);
                intent.putExtra(TransactionsListActivity.ID_PARAM, card);
                startActivity(intent);
            }
        });
    }

    @Override
    protected List<String> getDataForAdapter() {
        List<String> dataForAdapter = new ArrayList<String>();
        for (String key : cards) {
            dataForAdapter.add(key + "\n" + cards2costs.get(key));
        }

        return dataForAdapter;
    }
}
