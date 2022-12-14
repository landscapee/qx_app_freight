package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 单个装机单数据
 */
@Data
public class LoadingListBean implements Serializable {
    private String status;
    private String message;
    private Object rowCount;
    private Object flag;
    private List<DataBean> data;

    @Data
    public static class DataBean {
        private boolean showDetail;//是否全部展开
        private String id;
        private String flightInfoId;
        private int version;
        private String content;
        private int reviewStatus;
        private long createTime;
        private String createUser;
        private String createUserName;
        private long updateTime;
        private String updateUser;
        private int documentType;
        private int loadingAdvice;
        private String loadingUser;
        private List<ContentObjectBean> contentObject;// 不做展示 舱位 板车的关系
        private String returnReason;
        private String preContent;
        private String cgoContent;
        private int autoLoadInstalledSingle;
        private String flightNo;
        private int installedSingleConfirm;
        private String installedSingleConfirmUser;

        @Data
        public static class ContentObjectBean implements  Serializable{
            private String flightNo;
            private String cargoName;
            private List<ScooterBean> scooters;

            @Data
            public static class ScooterBean implements  Serializable{
                private String cargoName;
                private String oldCargoName;//原舱位
                private long createTime;
                private String createUser;
                private String destinationStation;//目的地
                private String flightInfoId;
                private String id;
                private String oldId;
                private String reportInfoId;
                private String scooterCode;
                private int total;
                private String type;
                private int version;
                private List<WaybillBean> waybillList;
                private double weight;
                private double volume;
                //添加数据
                private int lock;// 0 未锁定 1 已锁定 2
                private String location;//货位
                private String oldLocation;//原货位
                private String serialInd;//ULD号

                private boolean change;//是否修改
                private int exceptionFlag;//1建议拉
                private boolean split;//是否是被拆分 item

                private String specialCode;//特货代码
                private boolean show;

                @Data
                public static class WaybillBean  implements  Serializable{
                    private String cargoCn;
                    private long createTime;
                    private String createUser;
                    private int exceptionFlag;
                    private String flightInfoId;
                    private String id;
                    private int inflightnum;
                    private String mailType;
                    private String manifestScooterId;
                    private int receivecargoNumber;
                    private double receivecargoVolumn;
                    private double receivecargoWeight;
                    private String runTimeScooterId;
                    private int totalnum;
                    private double volume;
                    private boolean title;
                    private String waybillId;
                    private double weight;
                    private int number;
                    private String waybillCode;
                    private String specialCode;
                    private boolean hasLiveGoods;
                    private boolean hasGUNGoods;
                }
            }
        }
    }
}
