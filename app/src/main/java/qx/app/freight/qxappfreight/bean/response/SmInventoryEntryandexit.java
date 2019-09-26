package qx.app.freight.qxappfreight.bean.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SmInventoryEntryandexit {

        private static final long serialVersionUID = 1L;

        /**
         * id
         */
        private String id;

        /**
         * I 入库  O 出库
         */
        private String type;

        /**
         * 营业点
         */
        private String outletId;

        /**
         * 库区id
         */
        private String areaId;

        /**
         * 库区类型编码
         */
        private String areaTypeCode;

        /**
         * 库位id
         */
        private String binId;

        /**
         * 运单号
         */
        private String waybillCode;

        /**
         * 0待执行 1已执行
         */
        private Integer status;

        /**
         * 件数
         */
        private Integer number;

        /**
         * 重量
         */
        private Double weight;

        /**
         * 最低温度
         */
        private Integer temperatureFrom;

        /**
         * 最高温度
         */
        private Integer temperatureTo;

        /**
         * 申请人id
         */
        private String applyUserId;

        /**
         * 申请人姓名
         */
        private String applyUserName;

        /**
         * 入库时间
         */
        private Long applyTime;

        /**
         * 执行人id
         */
        private String execUserId;

        /**
         * 执行人姓名
         */
        private String execUserName;

        /**
         * 执行时间
         */
        private Long execTime;

        /**
         * 备注
         */
        private String remark;

        /**
         * 存储类型
         */
        private String storageType;


        /**
         * 特货代码
         */
        private String specialCode;

        /**
         * 品名
         */
        private String cargoCn;

        /**
         * 货邮类型  C代表货物,M代表邮件
         */
        private String mailType;

        /**
         * 0生成库存逻辑表 1不生成 出库不需生成;自动完成的不需要生成;
         */
        private Integer summaryFlag;
}
