package ru.spbau.WhereIsMyMoney.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ru.spbau.WhereIsMyMoney.ExistingSmsReader;
import ru.spbau.WhereIsMyMoney.R;
import ru.spbau.WhereIsMyMoney.SmsEvent;

import java.util.ArrayList;

public class SmsViewActivity extends Activity {
    final static String BODY = "body";
    final static String SOURCE = "source";

    private void createSmsListView() {
        ListView listView = (ListView) findViewById(R.id.sms_list);

        final ArrayList<SmsEvent> smsList = ExistingSmsReader.getAll(getApplicationContext());

        SmsAdapter adapter = new SmsAdapter(this,
                smsList);


        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(0xFF00AA00));
        listView.setDividerHeight(2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(SmsViewActivity.this, ParserActivity.class);
                intent.putExtra(BODY, smsList.get(position).getBody());
                intent.putExtra(SOURCE, smsList.get(position).getSource());
                startActivity(intent);
            }


        });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_list);
        createSmsListView();
    }

    private class SmsAdapter extends ArrayAdapter<SmsEvent> {

        private final ArrayList<SmsEvent> items;

        public SmsAdapter(Context context, ArrayList<SmsEvent> items) {
            super(context, R.layout.list_item, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }
            SmsEvent o = items.get(position);
            if (o != null) {
                TextView tt = (TextView) v.findViewById(R.id.source);
                TextView bt = (TextView) v.findViewById(R.id.text);
                if (tt != null) {
                    tt.setText(o.getSource());
                }
                if(bt != null){
                    bt.setText(o.getBody());
                }
            }
            return v;
        }
    }

}