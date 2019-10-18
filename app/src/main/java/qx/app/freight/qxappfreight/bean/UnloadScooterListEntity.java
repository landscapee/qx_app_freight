package qx.app.freight.qxappfreight.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;

@Getter
@Setter
public class UnloadScooterListEntity implements Serializable {
    private List <ScooterInfoListBean> mListGoods;//货物板车列表
    private List<ScooterInfoListBean> mListBaggage;//行李板车列表
}
