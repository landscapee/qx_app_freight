package qx.app.freight.qxappfreight.bean.response;


import java.util.List;

import lombok.Data;

@Data
public class SearchReservoirBean {

    private int total;
    private int size;
    private int current;
    private Object taskHandler;
    private boolean searchCount;
    private int pages;
    private List<RecordsBean> records;

    @Data
    public static class RecordsBean {
        /**
         * id : c2aef3d53f858e27fdf3acac29f182cb
         * depId : null
         * code : ctu_airport_cargo_00001
         * reservoirName : 普货区
         * reservoirType : ctu_airport_were_house_00001
         */

        private String id;
        private Object depId;
        private String code;
        private String reservoirName;
        private String reservoirType;
    }
}
