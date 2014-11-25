package com.orange_money.enmusubi.adaptaer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.orange_money.enmusubi.R;
import com.orange_money.enmusubi.data.TextData;

import java.util.List;

/**
 * Created by admin on 14/11/18.
 * 履歴表示のためのアダプター
 */
public class HistoryAdapter extends ArrayAdapter<TextData> {

    private LayoutInflater mInflater;
    private TextView mTextTitle;

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
            mTextTitle.setText(textData.getTextTitle());

            //トグルスイッチの処理を以下に書く

        }
        return convertView;
    }
}
