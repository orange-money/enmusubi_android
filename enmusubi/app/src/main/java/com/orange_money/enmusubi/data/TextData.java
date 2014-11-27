package com.orange_money.enmusubi.data;

import android.content.Intent;

import java.io.Serializable;

/**
 * Created by Asuka Shimada on 14/11/14.
 * 教科書一覧表示表示画面のアイテムの情報を保持するクラス
 */
public class TextData implements Serializable {
    private static final long serialVersionUID = 4555528694944002884L;
    private String textId = "";
    private String userId = "";
    private String textTitle = "";
    private String textPrice = "";
    private String className = "";
    private String comment = "";
    private String univ = "";
    private String teacherName = "";
    private String fileName = "";
    private String link = "";



    public TextData() {
    }

    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUniv() {
        return univ;
    }

    public void setUniv(String univ) {
        this.univ = univ;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

