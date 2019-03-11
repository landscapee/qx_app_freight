package qx.app.freight.qxappfreight.contract;

import java.util.List;
import java.util.Objects;

import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;

/**
 * TODO : xxx
 * Created by pr
 */
public class UploadsContract {
    public interface uploadsModel {
        void uploads(List<MultipartBody.Part> files, IResultLisenter lisenter);
    }

    public interface uploadsView extends IBaseView {
        void uploadsResult(Object result);
    }

}
