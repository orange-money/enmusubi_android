package com.orange_money.enmusubi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.orange_money.enmusubi.data.UserData;
import com.orange_money.enmusubi.layouts.EnmusubiTextView;
import com.orange_money.enmusubi.R;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public class FBLoginActivity extends Activity {

    private final static String TAG = "FBLoginActivity";


    private UiLifecycleHelper uiHelper;
    private GraphUser mGraphUser;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fblogin);

        EnmusubiTextView enmusubiText =(EnmusubiTextView)findViewById(R.id.appTitle);
        LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
        //学歴取得のパーミッションの設定
        authButton.setPublishPermissions(Arrays.asList("user_education_history"));

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
    }


    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");

            //ユーザー情報を取得(id,name,link,education)
            Request request = Request.newMeRequest(session, new Request.GraphUserCallback(){
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if(session == Session.getActiveSession()){
                        if(user != null){
                            mGraphUser = user;
                        }
                    }
                }
            });
            Request.executeBatchAsync(request);

            Request.executeMeRequestAsync(session, new Request.GraphUserCallback(){

                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            //ユーザー登録リクエストを投げる
                            AsyncHttpClient client = new AsyncHttpClient();
//                            String url = getString(R.string.local) + "users";
                            String url = getString(R.string.aws) + "users";

                            //jsonでpost
                            JSONObject params = new JSONObject();
                            try {
                                params.put("name",user.getName());
                                params.put("link",user.getLink());
                                //デバッグ用 education_listから大学情報を抽出する処理が必要
                                params.put("univ","京都大学");
                                params.put("user_id",user.getId());
                                StringEntity entity = new StringEntity(params.toString(),HTTP.UTF_8);
                                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                                client.post(getApplicationContext(),url,entity,"application/json",new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            UserData userData = new UserData(user.getId(),"京都大学");

                            //ログインに成功するとMain画面へ遷移,userIdと大学名を渡す
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("user_data", userData);
                            startActivity(intent);
                            finish();
                        }
                    });
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
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
        getMenuInflater().inflate(R.menu.fblogin, menu);
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
