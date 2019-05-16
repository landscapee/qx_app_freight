package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.response.ListWaybillCodeBean;

public class ListWaybillCodeContract {
    public interface listWaybillCodeModel {
        void listWaybillCode(String code, IResultLisenter lisenter);
    }

    public interface listWaybillCodeView extends IBaseView {
        void listWaybillCodeResult(ListWaybillCodeBean listWaybillCodeBean);
    }
}
