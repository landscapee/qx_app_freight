package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.SmInventoryEntryandexit;

/**
 * TODO : xxx
 * Created by pr
 */
public class SubmitIOManiFestContract {

    public interface submitIOManiFestModel {
        void submitIOManiFest(SmInventoryEntryandexit entity, IResultLisenter lisenter);
    }

    public interface submitIOManiFestView extends IBaseView {
        void setSubmitIOManiFestResult(String result);
    }
}
