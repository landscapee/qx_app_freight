package qx.app.freight.qxappfreight.bean;

import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
@Data
public class CargoUploadBean {
    /**
     * 航班ID
     */
    private long flightId;
    /**
     * 上报人ID
     */
    private String staffId;
    /**
     * 进出港
     */
    private String movement;
    /*
     * 行李重量
     */
    private double baggageWeight;
    /*
     * 货物重量
     */
    private double cargoWeight;
    /*
     * 邮件重量
     */
    private double mailWeight;
    private List<TransportTodoListBean> data;
}
