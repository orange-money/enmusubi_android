package com.orange_money.enmusubi.activity;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.orange_money.enmusubi.HistoryAdapter;
import com.orange_money.enmusubi.R;
import com.orange_money.enmusubi.TextAdapter;
import com.orange_money.enmusubi.TextData;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class HistoryFragment extends Fragment {


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        //この下にテキスト詳細を取得してviewにセットする処理を書く．

        //テキストデータの生成
        List<TextData> texts = new ArrayList<TextData>();

        //以下テストデータ
        TextData item1 = new TextData();
        item1.setTextId(1);
        item1.setTextTitle("huga");

        TextData item2 = new TextData();
        item2.setTextId(2);
        item2.setTextTitle("huga");


        texts.add(item1);
        texts.add(item2);
        //ここまで


        //viewにテキストデータをセット
        ListView textView = (ListView)v.findViewById(R.id.historyListView);

        HistoryAdapter historyAdapter = new HistoryAdapter(v.getContext(),texts);
        textView.setAdapter(historyAdapter);




        // Inflate the layout for this fragment
        return v;
    }


}
