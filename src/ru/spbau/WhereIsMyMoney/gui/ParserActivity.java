package ru.spbau.WhereIsMyMoney.gui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import ru.spbau.WhereIsMyMoney.R;
import ru.spbau.WhereIsMyMoney.Transaction;
import ru.spbau.WhereIsMyMoney.parser.Template;
import ru.spbau.WhereIsMyMoney.storage.TemplatesSource;

public class ParserActivity extends Activity {
    private String smsText;
    private int transactionType = 0;
    private int fieldType = 0;
    private final String[] fields = {"{{card}}", "{{place}}", "{{delta}}", "{{balance}}", "{{date}}"};

    public void saveParser(View view) {
        EditText sms = (EditText) findViewById(R.id.sms);
        smsText = sms.getText().toString();
        TemplatesSource templatesSource = new TemplatesSource(getApplicationContext());
        templatesSource.open();
        Template template = new Template(smsText, transactionType);
        templatesSource.addTemplate(template);
        templatesSource.close();
        finish();
    }

    public void changeType(View view) {
        final EditText sms = (EditText) findViewById(R.id.sms);
        int startSelection = sms.getSelectionStart();
        int endSelection = sms.getSelectionEnd();
        if (endSelection - startSelection == 0) return;
        Editable text = sms.getText().replace(startSelection, endSelection, fields[fieldType]);
        sms.setText(text);
    }

    private void createFieldAdapter() {
        Resources res = getResources();
        String[] localizedFields = res.getStringArray(R.array.field_name);

        ArrayAdapter<String> fieldAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, localizedFields);
        fieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner field = (Spinner) findViewById(R.id.field);
        field.setAdapter(fieldAdapter);
        field.setSelection(0);

        field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fieldType = position;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                fieldType = 0;
            }
        });
    }

    private void createTypeAdapter() {
        Resources res = getResources();
        String[] localizedTypes = res.getStringArray(R.array.type_name);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, localizedTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner type = (Spinner) findViewById(R.id.type);
        type.setAdapter(adapter);
        type.setSelection(Transaction.NONE);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transactionType = position;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                transactionType = Transaction.NONE;
            }
        });

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parser);

        Intent intent = getIntent();
        smsText = intent.getStringExtra(SmsViewActivity.BODY);
        final EditText sms = (EditText) findViewById(R.id.sms);
        sms.setText(smsText);

        createTypeAdapter();
        createFieldAdapter();
    }
}