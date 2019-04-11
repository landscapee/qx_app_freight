package qx.app.freight.qxappfreight.bean.response;

import android.content.Intent;

import java.io.Serializable;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author nacol
 * @since 2019-01-22
 */
@Data
public class FtGroupScooter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 运单申报ID
     */
    private String waybillId;

    /**
     * 运单申报号
     */
    private String waybillCode;

    /**
     * 件数
     */
    private Integer number;

    /**
     * 重量
     */
    private Double weight;

    /**
     * 体积
     */
    private Double volume;

    /**
     * 目的站
     */
    private String destinationStation;

//--------------------------------
    /**
     * 板车
     */
    private String runTimeScooterId;//运行时板车id(业务板车id)
    private String scooterId;//基础信息板车id
    private Short scooterType;
    private String scooterCode;
    private Double scooterWeight;

    /**
     * 库位id
     */
    private String repPlaceId;
    private String repName;
    private String repPlaceNum;
    private Integer repPlaceStatus;

//--------------------------------

    /**
     * 1删除
     */
    private Integer delFlag;


    private String createUser;

    private Long createDate;

    private String updateUser;

    private Long updateDate;

    //nacol-------------------
    /**
     *
     * 1:update
     * 2:insert
     * 3:delete
     * 0:unchange
     */
    private Short updateStatus = 0;

    /**
     * 1: false
     * 0: true
     */
    private Short inFlight = 0;
    /**
     * 组板记录状态:
     * 0.正常
     * 1.拉回
     *  2.转运
     */
    private Integer groupScooterStatus;


}
