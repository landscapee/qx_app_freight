package com.qxkj.positionapp.observer;

import com.qxkj.positionapp.LocationEntity;

/**
 * Created by guohao On 2019/5/29 16:55
 *  订阅者
 */
public interface LocationObservable {

    public void receiveLocationUpdate(LocationEntity locationEntity);
}
