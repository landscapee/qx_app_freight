package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;

/**
 * 检查板车二维码是否在数据库中处于被占用的状态
 */
public class ScanScooterCheckUsedContract {
    public interface ScanScooterCheckModel {
        void checkScooterCode(String scooterCode, IResultLisenter lisenter);
    }

    public interface ScanScooterCheckView extends IBaseView {
        void checkScooterCodeResult(BaseEntity<Object> result);
    }
}
