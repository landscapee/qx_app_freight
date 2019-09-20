package qx.app.freight.qxappfreight.bean.response;


import java.util.List;

import lombok.Data;

@Data
public class ScooterConfBean {
    private List<ScooterConf> data;

    @Data
    public static class ScooterConf {
        /**
         *  "id": "6874356dda876268f683c06901fe6f46",
         *         "name": "小滚筒",
         *         "value": "2",
         *         "type": 0
         */

        private String id;
        private String name;
        private String value;
        private int type;
    }
}
