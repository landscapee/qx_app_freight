package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.WeightWayBillBean;
import qx.app.freight.qxappfreight.utils.StringUtil;

/**
 * @ProjectName: qx_app_freight
 * @Package: qx.app.freight.qxappfreight.bean.response
 * @ClassName: UldInfo
 * @Description: java类作用描述
 * @Author: 张耀
 * @CreateDate: 2021/3/1 14:33
 * @UpdateUser: 更新者：
 * @UpdateDate: 2021/3/1 14:33
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
@Data
public class UldInfo implements Serializable {
    private String id;
    /**
     * 业务uld id
     */
    private String runTimeUldId;

    /**
     * 业务板车 id
     */
    private String businessScooterId;

    /**
     * 航司 code
     */
    private String iata;

    /**
     * uld 编码
     */
    private String uldCode;

    /**
     * uld 类型
     */
    private String uldType;

    /**
     * uld 重量
     */
    private double uldWeight;
    /**
     * uld 重量
     */
    private double uldVolume;


    /**
     * uld 上的货物记录
     */
    private List <WeightWayBillBean> groupScooters;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 删除标识: 0删除 , 1未删除. （default : 0）
     */
    private Integer delFlag;

    public String getUldName(){

        if (StringUtil.isEmpty(getUldType())){
            return "/";
        }
        if (StringUtil.isEmpty(getUldCode())){
            return "/";
        }
        if (StringUtil.isEmpty(getIata())){
            return "/";
        }

        return getUldType() + " " + getUldCode() + " " + getIata();

    }
}
