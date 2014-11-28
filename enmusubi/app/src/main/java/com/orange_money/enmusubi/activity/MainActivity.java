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
import com.orange_money.enmusubi.adaptaer.TabListener;
import com.orange_money.enmusubi.data.UserData;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class MainActivity extends Activity {

    private final static String TAG = "MainActivity";
    private final static String FILE_NAME = "user_data.dat";
    private UserData userData;

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
        }

        if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
            //session.openForPublish(getOpenRequest());
            session.openForRead(new Session.OpenRequest(this));
        }
            //ログインしていないならばログイン画面へ遷移
        if (!session.isOpened()) {
            Intent intent = new Intent(this,FBLoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        //ログイン画面からintentしてきた場合は,userdata取得
        Intent loginIntent = getIntent();
        if(loginIntent.getSerializableExtra("user_data") != null) {
             userData = (UserData)getIntent().getSerializableExtra("user_data");
        } else{
            try {
                FileInputStream fis = openFileInput(FILE_NAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                userData = (UserData)ois.readObject();
                ois.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        //ActionBarをGetしてTabModeをセット
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Bundle bundle = new Bundle();
        bundle.putSerializable("user_data", userData);


        actionBar.addTab(actionBar.newTab()
                .setIcon(R.drawable.search)
                .setTabListener(new TabListener<PurchaseTextFragment>(
                        this, "tag1", PurchaseTextFragment.class,bundle)));
        actionBar.addTab(actionBar.newTab()
                .setIcon(R.drawable.regist)
                .setTabListener(new TabListener<SellTextFragment>(
                        this, "tag2", SellTextFragment.class,bundle)));
        actionBar.addTab(actionBar.newTab()
                .setIcon(R.drawable.history)
                .setTabListener(new TabListener<HistoryFragment>(
                        this, "tag3", HistoryFragment.class,bundle)));

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
        if(userData != null) {
            //userDataの保存
            try {
                FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(userData);
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
        outState.putSerializable("user_data", userData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userData = (UserData)savedInstanceState.getSerializable("user_data");
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
