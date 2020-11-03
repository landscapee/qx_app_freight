package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitNewEntity;

/**
 * 进港分拣 - 获取数据的返回值实体类
 * <p>
 * create by guohao - 2019/4/25
 */
@Data
public class InWaybillRecordBean implements Serializable {
    private List<InWaybillRecord> list;//分拣运单实体类集合
    private int count;//运单总数
    private int total;//总数量
    private int closeFlag;//关闭标识(0开启;1关闭;)
    private double charterReWeight; //包机重量
    private String flightType;// 包机类型 (L包机 / H货包)
    private List<String> route;
    private HashMap<String, List<InWaybillRecordSubmitNewEntity.SingleLineBean>> cargos = new HashMap<>();
}
