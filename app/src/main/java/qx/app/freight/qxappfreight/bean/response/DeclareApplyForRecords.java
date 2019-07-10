package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

@Data
public class DeclareApplyForRecords {


        /**
         * 主动申报记录id
         */
        private String id;

        /**
         * 0,1,2,3 自定义
         */
        private Object applyForType;

        /**
         * 运单id
         */
        private String waybillId;

        /**
         * 申请人
         */
        private String userId;

        /**
         * 0通过 1不通过
         */
        private int applyForResult;

        /**
         * 变更存储类型
         */
        private String storage;

        /**
         * 原始存储类型
         */
        private String orgStorage;

        /**
         * 新存储温度
         */
        private String refrigeratedTemperature;

        /**
         * 原始存储温度
         */
        private String orgRefrigeratedTemperature;

        //-----------------库区相关--------------------
        private String repType;
        private String repName;
        private String repId;
        private String repPlaceNum;
}
