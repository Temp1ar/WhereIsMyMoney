package ru.spbau.WhereIsMyMoney.gui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import ru.spbau.WhereIsMyMoney.Transaction;

import java.util.List;

/**
 * User: Alexander Opeykin alexander.opeykin@gmail.com
 * Date: 11/13/12
 * Time: 10:14 PM
 */
public class BalanceChartBuilder {
    private XYMultipleSeriesRenderer renderer;

    public BalanceChartBuilder() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
    }

    public Intent getIntent(Context context, List<Transaction> transactions) {
        setChartSettings(transactions);
        return ChartFactory.getBarChartIntent(context, getDataset(transactions), renderer, BarChart.Type.DEFAULT);
    }

    private void readSettings() {
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(0);
        renderer.setBarSpacing(0.05);
        renderer.setMargins(new int[] {20, 30, 15, 5});
        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        r.setColor(Color.BLUE);
    }

    private void setChartSettings(List<Transaction> transactions) {
        renderer.setChartTitle("Chart demo");
        renderer.setXTitle("transactions");
        renderer.setYTitle("balance ");
        renderer.setXAxisMin(0);
        renderer.setXAxisMax(transactions.size()); // additional 10%
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(getMaxBalance(transactions) * 1.1);
    }

    private float getMaxBalance(List<Transaction> transactions) {
        float max = 0;
        for (Transaction t : transactions) {
            float balance = t.getBalance();
            max = max >= balance ? max : balance;
        }
        return max;
    }

    private XYMultipleSeriesDataset getDataset(List<Transaction> transactions) {
        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
        CategorySeries series = new CategorySeries("Balance");

        for (Transaction t : transactions) {
            series.add(t.getBalance());
        }

        dataSet.addSeries(series.toXYSeries());
        return dataSet;
    }
}
