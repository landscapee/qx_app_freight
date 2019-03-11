package qx.app.freight.qxappfreight.contract;

import java.util.List;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;

/**
 * TODO : xxx
 * Created by pr
 */
public class TransportBeginContract {
    public interface transportBeginModel {
        void transportBegin(TransportEndEntity transportEndEntity, IResultLisenter lisenter);
        void transportEnd(TransportEndEntity transportEndEntity, IResultLisenter lisenter);
        void scanScooterDelete(TransportEndEntity transportEndEntity, IResultLisenter lisenter);
    }

    public interface transportBeginView extends IBaseView {
        void transportBeginResult(String result);
        void transportEndResult(String result);
        void scanScooterDeleteResult(String result);
    }
}
