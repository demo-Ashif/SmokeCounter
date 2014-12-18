package de.schipplock.smokecounter.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.schipplock.smokecounter.app.database.Database;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.updateInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent aboutActivity = new Intent(this, AboutActivity.class);
            startActivity(aboutActivity);
            return true;
        }

        if (id == R.id.action_reset) {
            Database database = new Database("cigstats", this);
            database.destroy();
            this.updateInfo();
            new AlertDialog.Builder(this)
                    .setTitle("Info")
                    .setMessage(getString(R.string.msg_reset))
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                Database database = new Database("cigstats", this);
                String timeStamp = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());

                // increment the total cigarette counter
                if (database.keyExists("total")) {
                    int total = database.getInt("total");
                    total = total + 1;
                    database.add("total", total);
                } else {
                    database.add("total", 1);
                }

                // count the cigarettes for today and add it
                if (database.keyExists(timeStamp)) {
                    int todayTotal = database.getInt(timeStamp);
                    todayTotal = todayTotal + 1;
                    database.add(timeStamp, todayTotal);
                } else {
                    database.add(timeStamp, 1);
                }

                this.updateInfo();

                break;
            default:
                return;
        }
    }

    /**
     * update the ui
     */
    public void updateInfo() {
        Database database = new Database("cigstats", this);
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(Html.fromHtml("<b>Today:</b> " + Integer.toString(database.getInt(timeStamp))));

        GridView gridView = (GridView) findViewById(R.id.gridView);
        List items = new ArrayList<String>();
        Map<String, ?> map = database.getKeys();
        Iterator it = map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            items.add(pairs.getKey());
            items.add(pairs.getValue());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);

        gridView.setAdapter(adapter);
    }

}
