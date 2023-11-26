package com.example.cooclock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class add_ingredient_self_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient_self_page);
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.table);

        tableLayout.setShrinkAllColumns(true) ;
        for (int i = 0; i < 9; i++) {
            final TableRow tableRow = new TableRow(this);
            for(int j = 0 ; j < 9 ; j++) {
                final Button tb = new Button(this);
                tb.setText("d");
                tb.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tableRow.addView(tb);
            }
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT));
            tableLayout.addView(tableRow);
        } // Creation row
        // final TableRow tableRow = new TableRow(this) ;                

    }
}