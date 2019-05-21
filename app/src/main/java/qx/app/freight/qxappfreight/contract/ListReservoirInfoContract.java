package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.ReservoirArea;
import qx.app.freight.qxappfreight.bean.response.ReservoirAreaBean;

public class ListReservoirInfoContract {

    public interface listReservoirInfoModel{
        void listReservoirInfo(String deptCode, IResultLisenter lisenter);
    }

    public interface listReservoirInfoView extends IBaseView{
        void listReservoirInfoResult(List<ReservoirArea> bean);
    }
}
