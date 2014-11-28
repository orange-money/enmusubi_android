package com.orange_money.enmusubi.activity;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.orange_money.enmusubi.R;
import com.orange_money.enmusubi.adaptaer.TextAdapter;
import com.orange_money.enmusubi.data.TextData;
import com.orange_money.enmusubi.data.UserData;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class PurchaseTextFragment extends Fragment {

    private PullToRefreshListView mListView;
    private List<TextData> mTexts;
    private TextAdapter mTextAdapter;
    private UserData mUserData;
    private SearchView sView;
    private String sWord = "";



    public PurchaseTextFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTexts = new ArrayList<TextData>();
        mUserData = (UserData)getArguments().getSerializable("user_data");
        //項目初期化
        initializeItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_purchase_text, container, false);
        sView = (SearchView)v.findViewById(R.id.searchView);
        // 虫眼鏡アイコンを最初表示するかの設定
        sView.setIconifiedByDefault(true);

        // Submitボタンを表示するかどうか
        sView.setSubmitButtonEnabled(true);
        sView.setQueryHint("教科書を検索");

        if (!sWord.equals("")) {
            // TextView.setTextみたいなもの
            this.sView.setQuery(this.sWord, false);
        } else {
            String queryHint = "教科書を検索";
            // placeholderみたいなもの
            this.sView.setQueryHint(queryHint);
        }
        this.sView.setOnQueryTextListener(onQueryTextListener);




        //この下にテキスト詳細を取得してviewにセットする処理を書く．
        mListView = (PullToRefreshListView)v.findViewById(R.id.textListView);
        mTextAdapter = new TextAdapter(v.getContext(),mTexts);
        mListView.setAdapter(mTextAdapter);


        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            //上から引っ張った場合
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                initializeItems();
                new FinishRefresh().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new FinishRefresh().execute();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    //項目を初期化
    private void initializeItems(){
        mTexts.clear();
       //APIを投げてテキストの最新状態を取得
        //テキスト取得リクエストを投げる
        AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(false);
//        String url = getString(R.string.local) + "texts/" + mUserData.getmUniv();
        String url = getString(R.string.aws) + "texts/" + mUserData.getmUniv();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {

                    for (int i = 0; i < response.length(); i++) {

                        //取得
                        JSONObject member = response.getJSONObject(i);
                        TextData textData = new TextData();

                        textData.setTextId(member.getString("textinfo_id"));
                        textData.setUserId(member.getString("user_id"));
                        textData.setClassName(member.getString("lecture_name"));
                        textData.setTextTitle(member.getString("textbook_name"));
                        textData.setTextPrice(member.getString("price") + "円");
                        textData.setTeacherName(member.getString("teacher"));
                        textData.setComment(member.getString("comment"));
                        if(member.getString("file_name") != null) {
                            textData.setFileName(member.getString("file_name"));
                        }
                        textData.setLink(member.getJSONObject("user").getString("link").toString());
                        textData.setUniv(member.getString("univ"));
                        mTexts.add(textData);
                    }
                } catch (Exception e) {

                }
                mTextAdapter.notifyDataSetChanged();
            }
        });

    }

    //リスト更新終了
    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            mListView.onRefreshComplete();//更新アニメーション終了
        }
    }


    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String searchWord) {
            // SubmitボタンorEnterKeyを押されたら呼び出されるメソッド
            return setSearchWord(searchWord);
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // 入力される度に呼び出される
            return false;
        }
    };

    private boolean setSearchWord(String searchWord) {;
        if (searchWord != null && !searchWord.equals("")) {
            // searchWordがあることを確認
            this.sWord = searchWord;
        }
        // 虫眼鏡アイコンを隠す
        this.sView.setIconified(false);

        AsyncHttpClient client = new AsyncHttpClient();
//        String url = getString(R.string.local) + "texts/search";
        String url = getString(R.string.aws) + "texts/search";

        //jsonでpost
        JSONObject params = new JSONObject();
        try {
            params.put("textbook_lecture_name",searchWord);
            params.put("univ", mUserData.getmUniv());
            StringEntity entity = new StringEntity(params.toString(), HTTP.UTF_8);
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(sView.getContext(),url,entity,"application/json",new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    mTexts.clear();
                    try {

                        for (int i = 0; i < response.length(); i++) {

                            //取得
                            JSONObject member = response.getJSONObject(i);
                            TextData textData = new TextData();

                            textData.setTextId(member.getString("textinfo_id"));
                            textData.setUserId(member.getString("user_id"));
                            textData.setClassName(member.getString("lecture_name"));
                            textData.setTextTitle(member.getString("textbook_name"));
                            textData.setTextPrice(member.getString("price") + "円");
                            textData.setTeacherName(member.getString("teacher"));
                            textData.setComment(member.getString("comment"));
                            if(member.getString("file_name") != null) {
                                textData.setFileName(member.getString("file_name"));
                            }
                            textData.setLink(member.getJSONObject("user").getString("link").toString());
                            textData.setUniv(member.getString("univ"));
                            mTexts.add(textData);
                        }
                    } catch (Exception e) {

                    }
                    mTextAdapter.notifyDataSetChanged();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        // Focusを外す
        this.sView.clearFocus();
        return false;
    }


}


