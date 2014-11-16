package com.orange_money.enmusubi;

import android.content.Intent;

/**
 * Created by Asuka Shimada on 14/11/14.
 * 教科書一覧表示表示画面のアイテムの情報を保持するクラス
 */
public class TextData {
    private int textId;
    private String textTitle;
    private String textPrice;
    private String className;

    public TextData() {
    }

    public int getTextId() {
        return textId;
    }

    public void setTextId(int textId) {
        this.textId = textId;
    }

    public String getTextTitle() {
        return textTitle;
    }

    public void setTextTitle(String textTitle) {
        this.textTitle = textTitle;
    }

    public String getTextPrice() {
        return textPrice;
    }

    public void setTextPrice(String textPrice) {
        this.textPrice = textPrice;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
