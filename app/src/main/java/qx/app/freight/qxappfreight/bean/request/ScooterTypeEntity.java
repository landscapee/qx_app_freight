package qx.app.freight.qxappfreight.bean.request;

import lombok.Data;

/**
 * @ProjectName: qx_app_pad
 * @Package: com.qxkj.smartservice.bean.local
 * @ClassName: ScooterTypeEntity
 * @Description: java类作用描述
 * @Author: 张耀
 * @CreateDate: 2020/7/29 17:02
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/7/29 17:02
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
@Data
public class ScooterTypeEntity {

    public ScooterTypeEntity(int scooterType, String scooterTypeName) {
        this.scooterType = scooterType;
        this.scooterTypeName = scooterTypeName;
    }

    private int scooterType;
    private String scooterTypeName;
}
