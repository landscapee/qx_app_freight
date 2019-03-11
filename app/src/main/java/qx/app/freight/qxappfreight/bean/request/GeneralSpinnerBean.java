package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

public class GeneralSpinnerBean {
    private List<WorkTypeBean> workType;//违规单位
    private List<PositionBean> position;//违规职位
    private List<StaffCheckInfo> staffCheckInfo;//报检员身份信息
    private List<SpAreaBean> spArea;//库区
    private List<SpScooterBean> spScooter;//板车号
    private List<SpProductBean> spProduct; //品名
    private List<SpRemarksBean> spRemarks; //备注

    public List<SpRemarksBean> getSpRemarks() {
        return spRemarks;
    }

    public void setSpRemarks(List<SpRemarksBean> spRemarks) {
        this.spRemarks = spRemarks;
    }

    public List<SpProductBean> getSpProduct() {
        return spProduct;
    }

    public void setSpProduct(List<SpProductBean> spProduct) {
        this.spProduct = spProduct;
    }

    public List<SpAreaBean> getSpArea() {
        return spArea;
    }

    public void setSpArea(List<SpAreaBean> spArea) {
        this.spArea = spArea;
    }

    public List<SpScooterBean> getSpScooter() {
        return spScooter;
    }

    public void setSpScooter(List<SpScooterBean> spScooter) {
        this.spScooter = spScooter;
    }

    public static class BaseViolationT {
        private String id;
        private String value;
        private String type;
        private int order_num;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getOrder_num() {
            return order_num;
        }

        public void setOrder_num(int order_num) {
            this.order_num = order_num;
        }
    }


    public List<WorkTypeBean> getWorkType() {
        return workType;
    }

    public void setWorkType(List<WorkTypeBean> workType) {
        this.workType = workType;
    }


    public List<PositionBean> getPosition() {
        return position;
    }

    public void setPosition(List<PositionBean> position) {
        this.position = position;
    }


    public List<StaffCheckInfo> getStaffCheckInfo() {
        return staffCheckInfo;
    }

    public void setStaffCheckInfo(List<StaffCheckInfo> staffCheckInfo) {
        this.staffCheckInfo = staffCheckInfo;
    }

    /**
     * 报检员身份信息
     */
    public static class StaffCheckInfo extends BaseViolationT {

    }

    /**
     *
     */
    public static class WorkTypeBean extends BaseViolationT {

    }

    /**
     *
     */
    public static class PositionBean extends BaseViolationT {

    }

    /**
     * 库区
     */
    public static class SpAreaBean extends BaseViolationT {

    }

    /**
     * 板车号
     */

    public static class SpScooterBean extends BaseViolationT {

    }


    /**
     * 品名
     */
    public static class SpProductBean extends BaseViolationT {

    }

    /***
     * 备注
     */
    public static class SpRemarksBean extends BaseViolationT {

    }

}
