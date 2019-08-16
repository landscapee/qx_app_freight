package qx.app.freight.qxappfreight.bean.response;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class CargoReportHisBean  implements MultiItemEntity, Serializable {

    private String id;

    private String flightNo;

    private String flightId;

    private String reportOperator;

    private long createTime;

    private String flightIndicator;

    private List<TransportTodoListBean> data;

    /**
     * 上报类型,1行李上报,2国内货物上报,3国际货物上报
     */
    private int subType;

    @Override
    public int getItemType() {
        return 0;
    }
}
