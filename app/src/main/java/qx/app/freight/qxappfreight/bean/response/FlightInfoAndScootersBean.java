package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @ProjectName: qx_app_freight
 * @Package: qx.app.freight.qxappfreight.bean.response
 * @ClassName: FlightInfoAndScootersBean
 * @Description: java类作用描述
 * @Author: 张耀
 * @CreateDate: 2020/7/14 14:32
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/7/14 14:32
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
@Data
public class FlightInfoAndScootersBean implements Serializable {
    private boolean hasGroupScooterTask;
    private List <GetInfosByFlightIdBean> scooters;
}
