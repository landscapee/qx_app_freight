package qx.app.freight.qxappfreight.contract;


import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.FindAirlineAllBean;

public class FindAirlineAllContract {

    public interface findAirlineAllModel {
        void findAirlineAll(IResultLisenter lisenter);
    }

    public interface findAirlineAllView extends IBaseView {
        void findAirlineAllResult(List<FindAirlineAllBean> result);
    }
}
