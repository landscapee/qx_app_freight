package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

@Data
public class DeclareItem {


        private String itemId;

        /**
         * 运单申报ID
         */
        private String waybillId;

        /**
         * 品名ID
         */
        private String cargoId;

        /**
         * 件数
         */
        private int number;

        /**
         * 重量
         */
        private int weight;

        /**
         * 体积
         */
        private int volume;

        /**
         * 包装类型
         */
        private String[] packagingType;

        /**
         * 品名父类
         * */
        private String cargoCn;

        private int type ;


}