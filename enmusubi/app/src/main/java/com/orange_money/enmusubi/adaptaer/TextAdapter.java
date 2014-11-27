package com.orange_money.enmusubi.adaptaer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orange_money.enmusubi.R;
import com.orange_money.enmusubi.activity.TextDetailActivity;
import com.orange_money.enmusubi.data.TextData;

import java.util.List;

/**
 * Created by admin on 14/11/13.
 */
public class TextAdapter extends ArrayAdapter<TextData> {

    private LayoutInflater mInflater;
    private TextView mTextTitle;
    private TextView mClassName;
    private TextView mTextPrice;
    private ImageView mDetailImage;


    public TextAdapter(Context context, List<TextData> objects) {
        super(context,0,objects);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.text_row,null);
        }

        final TextData textData = this.getItem(position);

        if(textData != null) {
            mTextTitle = (TextView) convertView.findViewById(R.id.textTitle);
            mClassName = (TextView) convertView.findViewById(R.id.className);
            mTextPrice = (TextView) convertView.findViewById(R.id.textPrice);
            mDetailImage = (ImageView) convertView.findViewById(R.id.detailImg);

            mTextTitle.setText(textData.getTextTitle());
            mClassName.setText(textData.getClassName());
            mTextPrice.setText(textData.getTextPrice());

            mDetailImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //教科書詳細画面へインテント
                    Intent intent = new Intent(view.getContext(), TextDetailActivity.class);
                    intent.putExtra("text_data",textData);
                    view.getContext().startActivity(intent);
                }
            });
        }
        return convertView;
    }
}
