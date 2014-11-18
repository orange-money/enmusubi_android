package com.orange_money.enmusubi.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.orange_money.enmusubi.R;
import com.orange_money.enmusubi.TabListener;


public class MainActivity extends Activity {

    private final static String TAG = "MainActivity";


    private UiLifecycleHelper uiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        uiHelper = new   UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        //FB session情報取得
        Session session = Session.getActiveSession();
        if(session == null){
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, callback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                //session.openForPublish(getOpenRequest());
                session.openForRead(new Session.OpenRequest(this));
            }
        }
            //ログインしていないならばログイン画面へ遷移
        if (!session.isOpened()) {
            Intent intent = new Intent(this,FBLoginActivity.class);
            startActivity(intent);
            finish();
        }



        //ActionBarをGetしてTabModeをセット
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.addTab(actionBar.newTab()
                .setIcon(R.drawable.search)
                .setTabListener(new TabListener<PurchaseTextFragment>(
                        this, "tag1", PurchaseTextFragment.class)));
        actionBar.addTab(actionBar.newTab()
                .setIcon(R.drawable.regist)
                .setTabListener(new TabListener<SellTextFragment>(
                        this, "tag2", SellTextFragment.class)));
        actionBar.addTab(actionBar.newTab()
                .setIcon(R.drawable.history)
                .setTabListener(new TabListener<HistoryFragment>(
                        this, "tag3", HistoryFragment.class)));

    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
            //ログインしていないならばログイン画面へ遷移
            Intent intent = new Intent(this,FBLoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
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

        if (id == R.id.action_logout){
            Log.d(TAG, "logout.");
            Session session = Session.getActiveSession();
            // セッションとトークン情報を廃棄する。
            session.closeAndClearTokenInformation();
            Intent intent = new Intent(this,FBLoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
