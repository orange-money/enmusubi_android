package com.orange_money.enmusubi;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //ActionBarをGetしてTabModeをセット
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.addTab(actionBar.newTab()
                .setText("教科書を買う")
                .setTabListener(new TabListener<PurchaseTextFragment>(
                        this, "tag1", PurchaseTextFragment.class)));
        actionBar.addTab(actionBar.newTab()
                .setText("教科書を売る")
                .setTabListener(new TabListener<SellTextFragment>(
                        this, "tag2", SellTextFragment.class)));
        actionBar.addTab(actionBar.newTab()
                .setText("販売履歴")
                .setTabListener(new TabListener<HistoryFragment>(
                        this, "tag3", HistoryFragment.class)));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
