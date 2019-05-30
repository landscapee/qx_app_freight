package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

/**
 * TODO : 板车列表信息
 * Created by pr
 */
@Data
public class ScooterInfoListDataBean {

    private List<ScooterInfoListBean> records;
    private Integer total;
    private Integer size;
    private Integer current;
    private Integer pages;

}
