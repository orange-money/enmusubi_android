package com.orange_money.enmusubi.layouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.orange_money.enmusubi.R;

/**
 * Created by admin on 14/11/14.
 */
public class EnmusubiTextView extends TextView {

    private static final String TAG = "EnmusubiTextView";

    public EnmusubiTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs,defStyle);
        SetCustomFont(context, attrs);
    }

    public EnmusubiTextView(Context context, AttributeSet attrs) {
        super(context,attrs);
        SetCustomFont(context, attrs);
    }

    public EnmusubiTextView(Context context) {
        super(context);
    }


    //フォントスタイルを読み込む
    private void SetCustomFont(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EnmusubiTextView);
        String customFont = a.getString(R.styleable.EnmusubiTextView_font);
        setCustomFont(context,customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }

}
