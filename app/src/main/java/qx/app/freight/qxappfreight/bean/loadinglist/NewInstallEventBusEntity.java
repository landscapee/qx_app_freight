package qx.app.freight.qxappfreight.bean.loadinglist;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;

@Setter
@Getter
public class NewInstallEventBusEntity {

    public final List <LoadingListBean.DataBean.ContentObjectBean> beans;

    public NewInstallEventBusEntity(List<LoadingListBean.DataBean.ContentObjectBean> beans) {
        this.beans = beans;
    }

}
