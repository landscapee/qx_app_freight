package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.AddScooterBean;

public class AddScooterContract {
    public interface addScooterModel {
        void addScooter(IResultLisenter lisenter);
    }

    public interface addScooterView extends IBaseView {
        void addScooterResult(AddScooterBean addScooterBean);
    }
}
