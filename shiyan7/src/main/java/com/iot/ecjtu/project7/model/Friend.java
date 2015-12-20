package com.iot.ecjtu.project7.model;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 497908738@qq.com on 2015/12/18.
 */
public class Friend {
    private int mHeadImageId;
    private String mName;
    private int mRate;

    public int getHeadImageId() {
        return mHeadImageId;
    }

    public void setHeadImageId(int headImageId) {
        mHeadImageId = headImageId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getRate() {
        return mRate;
    }

    public void setRate(int rate) {
        mRate = rate;
    }

}
