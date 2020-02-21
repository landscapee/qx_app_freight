package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;


@Data
public class ReturnBean {

        /**
         * cargoCn : 野生菌
         * number : 1.00
         * weight : 1.00
         * volume : 1.00
         * repName : 普货区
         * repPlaceNum : 库位0
         */

        private String cargoCn;
        private String number;
        private String weight;
        private String volume;
        private String repId;
        private String repName;
        private String repPlaceNum;
        private String scooterType;
        private String scooterCode;




}
