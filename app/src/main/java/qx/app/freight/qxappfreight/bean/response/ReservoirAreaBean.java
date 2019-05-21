package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.ReservoirArea;

/**
 * Created by guohao on 2019/5/21 22:54
 *
 * 库区 请求接口返回值
 */
@Data
public class ReservoirAreaBean implements Serializable {

    List<ReservoirArea> data;

}
