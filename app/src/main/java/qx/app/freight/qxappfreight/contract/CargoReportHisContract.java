package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.CargoReportHisBean;

public class CargoReportHisContract {
    public interface cargoReportHisModel {
        void cargoReportHis(String operatorId, IResultLisenter lisenter);
    }

    public interface cargoReportHisView extends IBaseView {
        void cargoReportHisResult(List<CargoReportHisBean> cargoReportHisBeans);
    }
}
