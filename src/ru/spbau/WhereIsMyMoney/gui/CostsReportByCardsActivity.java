package ru.spbau.WhereIsMyMoney.gui;

import android.widget.ListView;

import java.util.*;

/**
 * Show costs report grouped by cards
 */
public class CostsReportByCardsActivity extends AbstractCostsReportActivity {
    private Map<String, Map<String,Float>> cards2costs = null;
    private List<String> cards = null;
    private Date start = null;
    private Date end = null;

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
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Intent intent = new Intent(CostsReportByCardsActivity.this, TransactionsListActivity.class);
//                intent.putExtra(TransactionsListActivity.ID_PARAM, cards.get(position));
//                if (start != null)
//                    intent.putExtra(AbstractCostsReportActivity.START_DATE, start.getTime());
//                if (end != null)
//                    intent.putExtra(AbstractCostsReportActivity.END_DATE, end.getTime());
//
//                startActivity(intent);
//            }
//        });
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
                m.put("card", card);
                m.put("amount", costs.toString());

                cards.add(m);
            }
            dataForAdapter.add(cards);
        }

        return dataForAdapter;
    }
}
