package qx.app.freight.qxappfreight.contract;


import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.UpdateVersionBean;

public class UpdateVersionContract {
    public interface updateModel {
        void updateVersion(IResultLisenter lisenter);
    }

    public interface updateView extends IBaseView {
        void updateVersionResult(UpdateVersionBean updataBean);
    }
}
