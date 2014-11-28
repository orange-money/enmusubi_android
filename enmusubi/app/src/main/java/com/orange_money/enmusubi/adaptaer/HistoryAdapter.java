package com.orange_money.enmusubi.adaptaer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.orange_money.enmusubi.R;
import com.orange_money.enmusubi.data.TextData;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by admin on 14/11/18.
 * 履歴表示のためのアダプター
 */
public class HistoryAdapter extends ArrayAdapter<TextData> {

    private LayoutInflater mInflater;
    private TextView mTextTitle;
    private ToggleButton mStatusBottun;

    public HistoryAdapter(Context context, List<TextData> objects) {
        super(context, 0, objects);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.history_row,null);
        }

        final TextData textData = this.getItem(position);

        if(textData != null) {
            mTextTitle = (TextView) convertView.findViewById(R.id.history_text);
            mStatusBottun = (ToggleButton)convertView.findViewById(R.id.status_toggle);
            mTextTitle.setText(textData.getTextTitle());

            //トグルの状態設定
            if(textData.getStatus().equals("1")){
                mStatusBottun.setChecked(true);
            }
            if(textData.getStatus().equals("2")){
                mStatusBottun.setChecked(false);
            }

            //教科書の販売状態の変更
            mStatusBottun.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    AsyncHttpClient client = new AsyncHttpClient();
//                    String url = getContext().getString(R.string.local) + "texts/" + textData.getTextId() + "/detail";
                    String url = getContext().getString(R.string.aws) + "texts/" + textData.getTextId() + "/detail";

                    //jsonでpost
                    JSONObject params = new JSONObject();
                    try {
                        //教科書が販売中の場合の処理
                        if (textData.getStatus().equals("1")) {
                            params.put("status",2);
                            textData.setStatus("2");
                        }
                        //教科書が販売完了時の処理
                        else if (textData.getStatus().equals("2")) {
                            params.put("status",1);
                            textData.setStatus("1");
                        }
                        StringEntity entity = new StringEntity(params.toString(), HTTP.UTF_8);
                        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                        client.put(getContext(),url,entity,"application/json", new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Toast.makeText(getContext(),"教科書販売状態が変更されました",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(getContext(),"教科書販売状態が変更失敗．．．",Toast.LENGTH_LONG).show();
                            }
                        });
                    }catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        return convertView;
    }
}
