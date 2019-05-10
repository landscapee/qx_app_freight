package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;

public class InternationalCargoReportContract {
    public interface internationalCargoReportModel {
        void internationalCargoReport(String str,IResultLisenter lisenter);

        void scooterInfoList(BaseFilterEntity baseFilterEntity, IResultLisenter lisenter);
    }

    public interface internationalCargoReportView extends IBaseView {
        void internationalCargoReportResult(String result);

        void scooterInfoListResult(List<ScooterInfoListBean> scooterInfoListBeans);
    }
}
