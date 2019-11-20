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
public class IOManiFestContract {

    public interface ioManiFestModel {
        void getManifestInfo(BaseFilterEntity entity, IResultLisenter lisenter);
    }

    public interface ioManiFestView extends IBaseView {
        void setManifestResult(List<SmInventoryEntryandexit> result);
    }
}
