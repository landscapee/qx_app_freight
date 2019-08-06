package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;

public class AuditManifestContract {
    public interface auditManifestModel {
        void auditManifest(BaseFilterEntity entity,IResultLisenter lisenter);
    }

    public interface auditManifestView extends IBaseView {
        void auditManifestResult(String result);
    }
}
