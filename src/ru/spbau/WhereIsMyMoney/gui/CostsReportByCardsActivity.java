package ru.spbau.WhereIsMyMoney.gui;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import ru.spbau.WhereIsMyMoney.R;

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
    protected Map<String, Float> getTotalVals() {
        Map<String, Float> currency2costs4all = new HashMap<String, Float>();
        for (Map<String, Float> currency2costs : cards2costs.values()) {
            for(String currency : currency2costs.keySet()) {
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
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
                Intent intent = new Intent(CostsReportByCardsActivity.this, TransactionsListActivity.class);

                TextView card = (TextView) view.findViewById(R.id.child_name);
                intent.putExtra(TransactionsListActivity.ID_PARAM, card.getText());

                if (start != null)
                    intent.putExtra(AbstractCostsReportActivity.START_DATE, start.getTime());
                if (end != null)
                    intent.putExtra(AbstractCostsReportActivity.END_DATE, end.getTime());

                startActivity(intent);

                return true;
            }
        });
    }

    @Override
    protected List<List<Map<String, String>>> getDataForAdapter() {
        List<List<Map<String, String>>> dataForAdapter = new ArrayList<List<Map<String, String>>>();

        Map<String, Map<String, Float>> currency2card2costs = swapFirstAndSecondKeys(cards2costs);
        for (String currency : currency2card2costs.keySet()) {
            List<Map<String, String>> cards = new ArrayList<Map<String, String>>();

            Map<String, Float> card2costs = currency2card2costs.get(currency);
            for(String card : card2costs.keySet()) {
                Float costs = card2costs.get(card);
                Map<String, String> m = new HashMap<String, String>();
                m.put("name", card);
                m.put("amount", costs.toString());

                cards.add(m);
            }
            dataForAdapter.add(cards);
        }

        return dataForAdapter;
    }
}
