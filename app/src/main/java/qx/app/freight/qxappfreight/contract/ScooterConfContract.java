package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.ScooterConfBean;

/**
 * TODO : 板车基础数据获取
 * Created by zyy
 */
public class ScooterConfContract {

    public interface scooterConfModel {
        void getScooterConf(String type, IResultLisenter lisenter);
    }

    public interface scooterConfView extends IBaseView {
        void getScooterConfResult(List <ScooterConfBean.ScooterConf> result);
    }
}
