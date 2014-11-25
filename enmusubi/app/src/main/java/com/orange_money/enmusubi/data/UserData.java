package com.orange_money.enmusubi.data;

import java.io.Serializable;

/**
 * Created by admin on 14/11/25.
 */
public class UserData implements Serializable {

    private static final long serialVersionUID = 4385317968852498271L;
    private String mUserId = null;
    private String mUniv = null;

    public UserData(String userId, String univ) {
        mUserId = userId;
        mUniv = univ;
    }

    public String getmUserId() {
        return mUserId;
    }

    public String getmUniv() {
        return mUniv;
    }
}
