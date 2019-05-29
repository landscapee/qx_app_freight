package com.qxkj.positionapp.observer;

import android.content.Context;

import com.qxkj.positionapp.GPSUtils;
import com.qxkj.positionapp.LocationEntity;

public class Test {

    Context context;
    public void test(){
        GPSUtils.getInstance().getLastKnownLocation(context, new MyLocation() {
            @Override
            public void receiveLocationUpdate(LocationEntity locationEntity) {

            }
        });
    }

}
