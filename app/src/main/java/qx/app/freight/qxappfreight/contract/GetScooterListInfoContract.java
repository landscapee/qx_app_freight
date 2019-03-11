package qx.app.freight.qxappfreight.contract;

import qx.app.freight.qxappfreight.app.IBaseView;
import qx.app.freight.qxappfreight.app.IResultLisenter;
import qx.app.freight.qxappfreight.bean.request.FightScooterSubmitEntity;
import qx.app.freight.qxappfreight.bean.request.GetScooterListInfoEntity;
import qx.app.freight.qxappfreight.bean.response.GetScooterListInfoBean;

/**
 * TODO : xxx
 * Created by pr
 */
public class GetScooterListInfoContract {
    public interface getScooterListInfoModel {
        void getScooterListInfo(GetScooterListInfoEntity scooterListInfoEntity, IResultLisenter lisenter);
        void fightScooterSubmit(FightScooterSubmitEntity fightScooterSubmitEntity, IResultLisenter lisenter);
    }

    public interface getScooterListInfoView extends IBaseView {
        void getScooterListInfoResult(GetScooterListInfoBean scooterListInfoBean);
        void fightScooterSubmitResult(String result);
    }

}
