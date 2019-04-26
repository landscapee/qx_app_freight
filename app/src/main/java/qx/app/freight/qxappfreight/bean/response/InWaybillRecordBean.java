package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;

/**
 * 进港分拣 - 获取数据的返回值实体类
 *
 * create by guohao - 2019/4/25
 *
 */
@Data
public class InWaybillRecordBean implements Serializable {

    List<InWaybillRecord> list;//分拣运单实体类集合
    int count;//运单总数
    int total;//总数量
    int closeFlag;//关闭标识(0开启;1关闭;)
}
