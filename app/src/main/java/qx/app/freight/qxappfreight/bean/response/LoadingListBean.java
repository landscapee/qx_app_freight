package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 单个装机单数据
 */
@Data
public class LoadingListBean implements Serializable {

    /**
     * status : 200
     * message : 正确
     * rowCount : null
     * data : [{"id":"d77b51e3a252a42faa823bff21e67ca8","flightInfoId":"c28b3b6c910c475884204a129afb02a9","version":1,"content":"[{\"cargoName\":\"1H\",\"scooters\":[{\"actWgt\":\"642\",\"cargoStatus\":0,\"dest\":\"CGO\",\"dst\":\"\",\"estWgt\":\"642\",\"flightInfoId\":\"c28b3b6c910c475884204a129afb02a9\",\"pos\":\"1H\",\"pri\":\"1\",\"restrictedCargo\":\"\",\"tailer\":\"24106\",\"type\":\"C\",\"waybillList\":[{\"cargoCn\":\"花生豆\",\"createTime\":1565577469277,\"createUser\":\"systemAuto\",\"exceptionFlag\":0,\"flightInfoId\":\"c28b3b6c910c475884204a129afb02a9\",\"id\":\"09946777b29a26ecc09f1192d647574e\",\"inflightnum\":0,\"mailType\":\"C\",\"manifestScooterId\":\"0503a2c299ac4f6a86511f3fe18d3e54\",\"number\":3100,\"receivecargoNumber\":3100,\"receivecargoVolumn\":0.40,\"receivecargoWeight\":300.00,\"runTimeScooterId\":\"0503a2c299ac4f6a86511f3fe18d3e54\",\"specialCode\":\"--\",\"totalnum\":0,\"volume\":0.40,\"waybillCode\":\"991-15235732\",\"waybillId\":\"832be037fa6847cdb326d8dd50d684f6\",\"weight\":300.00},{\"cargoCn\":\"可乐,豆奶\",\"createTime\":1565577469280,\"createUser\":\"systemAuto\",\"exceptionFlag\":0,\"flightInfoId\":\"c28b3b6c910c475884204a129afb02a9\",\"id\":\"91eccfa06dda05b7a9a2df8943fdf5c8\",\"inflightnum\":0,\"mailType\":\"C\",\"manifestScooterId\":\"0503a2c299ac4f6a86511f3fe18d3e54\",\"number\":3210,\"receivecargoNumber\":9520,\"receivecargoVolumn\":0.70,\"receivecargoWeight\":830.00,\"runTimeScooterId\":\"0503a2c299ac4f6a86511f3fe18d3e54\",\"specialCode\":\"--\",\"totalnum\":0,\"volume\":0.20,\"waybillCode\":\"991-79935553\",\"waybillId\":\"00514f84f387493ea7d19245a98f4697\",\"weight\":210.00},{\"cargoCn\":\"牛奶\",\"createTime\":1565577469283,\"createUser\":\"systemAuto\",\"exceptionFlag\":0,\"flightInfoId\":\"c28b3b6c910c475884204a129afb02a9\",\"id\":\"78784300a9a16267a8809f6610df170f\",\"inflightnum\":0,\"mailType\":\"C\",\"manifestScooterId\":\"0503a2c299ac4f6a86511f3fe18d3e54\",\"number\":1230,\"receivecargoNumber\":6740,\"receivecargoVolumn\":0.40,\"receivecargoWeight\":522.00,\"runTimeScooterId\":\"0503a2c299ac4f6a86511f3fe18d3e54\",\"specialCode\":\"PER\",\"totalnum\":0,\"volume\":0.10,\"waybillCode\":\"991-65651456\",\"waybillId\":\"e829dbf06d0544eba2b65446062867d4\",\"weight\":101.00},{\"cargoCn\":\"美年达\",\"createTime\":1565577469297,\"createUser\":\"systemAuto\",\"exceptionFlag\":0,\"flightInfoId\":\"c28b3b6c910c475884204a129afb02a9\",\"id\":\"2160ad82870a0a51f7d5956aab88a4a7\",\"inflightnum\":0,\"mailType\":\"C\",\"manifestScooterId\":\"0503a2c299ac4f6a86511f3fe18d3e54\",\"number\":1231,\"receivecargoNumber\":12230,\"receivecargoVolumn\":1.40,\"receivecargoWeight\":2496.00,\"runTimeScooterId\":\"0503a2c299ac4f6a86511f3fe18d3e54\",\"specialCode\":\"--\",\"totalnum\":0,\"volume\":0.20,\"waybillCode\":\"991-43233492\",\"waybillId\":\"29ef623ba29547f1b2c5f9b701a4fdb9\",\"weight\":31.00}]}]},{\"cargoName\":\"2H\",\"scooters\":[{\"actWgt\":\"1875\",\"cargoStatus\":0,\"dest\":\"CGO\",\"dst\":\"\",\"estWgt\":\"1875\",\"flightInfoId\":\"c28b3b6c910c475884204a129afb02a9\",\"pos\":\"2H\",\"pri\":\"2\",\"restrictedCargo\":\"\",\"tailer\":\"\",\"type\":\"C\"}]},{\"cargoName\":\"3H\",\"scooters\":[{\"actWgt\":\"1582\",\"cargoStatus\":0,\"dest\":\"CGO\",\"dst\":\"\",\"estWgt\":\"1582\",\"flightInfoId\":\"c28b3b6c910c475884204a129afb02a9\",\"pos\":\"3H\",\"pri\":\"3\",\"restrictedCargo\":\"\",\"tailer\":\"24100\",\"type\":\"M\",\"waybillList\":[{\"cargoCn\":\"美年达\",\"createTime\":1565577469320,\"createUser\":\"systemAuto\",\"exceptionFlag\":0,\"flightInfoId\":\"c28b3b6c910c475884204a129afb02a9\",\"id\":\"12eb2bf4bc21076395422d89476e1e23\",\"inflightnum\":0,\"mailType\":\"C\",\"manifestScooterId\":\"3404f92ef3cf4d8a8bac459b7bd535f6\",\"number\":1000,\"receivecargoNumber\":12230,\"receivecargoVolumn\":1.40,\"receivecargoWeight\":2496.00,\"runTimeScooterId\":\"3404f92ef3cf4d8a8bac459b7bd535f6\",\"specialCode\":\"--\",\"totalnum\":0,\"volume\":0.50,\"waybillCode\":\"991-43233492\",\"waybillId\":\"29ef623ba29547f1b2c5f9b701a4fdb9\",\"weight\":1231.00},{\"cargoCn\":\"可乐,豆奶\",\"createTime\":1565577469323,\"createUser\":\"systemAuto\",\"exceptionFlag\":0,\"flightInfoId\":\"c28b3b6c910c475884204a129afb02a9\",\"id\":\"963432bafc8270958462023a7d13e495\",\"inflightnum\":0,\"mailType\":\"C\",\"manifestScooterId\":\"3404f92ef3cf4d8a8bac459b7bd535f6\",\"number\":2100,\"receivecargoNumber\":9520,\"receivecargoVolumn\":0.70,\"receivecargoWeight\":830.00,\"runTimeScooterId\":\"3404f92ef3cf4d8a8bac459b7bd535f6\",\"specialCode\":\"--\",\"totalnum\":0,\"volume\":0.10,\"waybillCode\":\"991-79935553\",\"waybillId\":\"00514f84f387493ea7d19245a98f4697\",\"weight\":210.00},{\"cargoCn\":\"牛奶\",\"createTime\":1565577469325,\"createUser\":\"systemAuto\",\"exceptionFlag\":0,\"flightInfoId\":\"c28b3b6c910c475884204a129afb02a9\",\"id\":\"8652635c46471ef618fb7626804df287\",\"inflightnum\":0,\"mailType\":\"C\",\"manifestScooterId\":\"3404f92ef3cf4d8a8bac459b7bd535f6\",\"number\":2300,\"receivecargoNumber\":6740,\"receivecargoVolumn\":0.40,\"receivecargoWeight\":522.00,\"runTimeScooterId\":\"3404f92ef3cf4d8a8bac459b7bd535f6\",\"specialCode\":\"PER\",\"totalnum\":0,\"volume\":0.10,\"waybillCode\":\"991-65651456\",\"waybillId\":\"e829dbf06d0544eba2b65446062867d4\",\"weight\":120.00},{\"cargoCn\":\"茄子\",\"createTime\":1565577469328,\"createUser\":\"systemAuto\",\"exceptionFlag\":0,\"flightInfoId\":\"c28b3b6c910c475884204a129afb02a9\",\"id\":\"d7e4ea161ee3cbc0edfb23031da34f3b\",\"inflightnum\":0,\"mailType\":\"C\",\"manifestScooterId\":\"3404f92ef3cf4d8a8bac459b7bd535f6\",\"number\":3213,\"receivecargoNumber\":3213,\"receivecargoVolumn\":0.30,\"receivecargoWeight\":21.00,\"runTimeScooterId\":\"3404f92ef3cf4d8a8bac459b7bd535f6\",\"specialCode\":\"PER\",\"totalnum\":0,\"volume\":0.30,\"waybillCode\":\"991-40456345\",\"waybillId\":\"37cece61b8714f52b229bd4b2e709f69\",\"weight\":21.00}]}]},{\"cargoName\":\"4H\",\"scooters\":[{\"actWgt\":\"424\",\"cargoStatus\":0,\"dest\":\"CGO\",\"dst\":\"\",\"estWgt\":\"424\",\"flightInfoId\":\"c28b3b6c910c475884204a129afb02a9\",\"pos\":\"4H\",\"pri\":\"4\",\"restrictedCargo\":\"\",\"tailer\":\"24108\",\"type\":\"C\",\"waybillList\":[{\"cargoCn\":\"青椒\",\"createTime\":1565577469336,\"createUser\":\"systemAuto\",\"exceptionFlag\":0,\"flightInfoId\":\"c28b3b6c910c475884204a129afb02a9\",\"id\":\"5eac938b3b16dad483a80242367eac86\",\"inflightnum\":0,\"mailType\":\"C\",\"manifestScooterId\":\"b8891648dcb44e14ae691e65d109840a\",\"number\":3214,\"receivecargoNumber\":8466,\"receivecargoVolumn\":0.70,\"receivecargoWeight\":354.00,\"runTimeScooterId\":\"b8891648dcb44e14ae691e65d109840a\",\"specialCode\":\"PER\",\"totalnum\":0,\"volume\":0.30,\"waybillCode\":\"991-20264926\",\"waybillId\":\"0e6148fdeca2446697a73bd8647c8361\",\"weight\":123.00},{\"cargoCn\":\"牛奶\",\"createTime\":1565577469339,\"createUser\":\"systemAuto\",\"exceptionFlag\":0,\"flightInfoId\":\"c28b3b6c910c475884204a129afb02a9\",\"id\":\"0f43967e452db14eec47471d1c4fbef6\",\"inflightnum\":0,\"mailType\":\"C\",\"manifestScooterId\":\"b8891648dcb44e14ae691e65d109840a\",\"number\":3210,\"receivecargoNumber\":6740,\"receivecargoVolumn\":0.40,\"receivecargoWeight\":522.00,\"runTimeScooterId\":\"b8891648dcb44e14ae691e65d109840a\",\"specialCode\":\"PER\",\"totalnum\":0,\"volume\":0.20,\"waybillCode\":\"991-65651456\",\"waybillId\":\"e829dbf06d0544eba2b65446062867d4\",\"weight\":301.00}]}]}]","reviewStatus":0,"createTime":1565579288674,"createUser":"u547a3b69c78b49959580c7460b66c63b","updateTime":1565596203662,"updateUser":null,"documentType":2,"loadingAdvice":0,"loadingUser":null,"contentObject":null,"returnReason":null,"preContent":"","cgoContent":"","autoLoadInstalledSingle":0,"flightNo":"CZ3768","installedSingleConfirm":0}]
     * flag : null
     */

    private String status;
    private String message;
    private Object rowCount;
    private Object flag;
    private List<DataBean> data;

    @Data
    public static class DataBean {
        /**
         * id : d77b51e3a252a42faa823bff21e67ca8
         * flightInfoId : c28b3b6c910c475884204a129afb02a9
         * version : 1
         * content : [{"cargoName":"1H","scooters":[{"actWgt":"642","cargoStatus":0,"dest":"CGO","dst":"","estWgt":"642","flightInfoId":"c28b3b6c910c475884204a129afb02a9","pos":"1H","pri":"1","restrictedCargo":"","tailer":"24106","type":"C","waybillList":[{"cargoCn":"花生豆","createTime":1565577469277,"createUser":"systemAuto","exceptionFlag":0,"flightInfoId":"c28b3b6c910c475884204a129afb02a9","id":"09946777b29a26ecc09f1192d647574e","inflightnum":0,"mailType":"C","manifestScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","number":3100,"receivecargoNumber":3100,"receivecargoVolumn":0.40,"receivecargoWeight":300.00,"runTimeScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","specialCode":"--","totalnum":0,"volume":0.40,"waybillCode":"991-15235732","waybillId":"832be037fa6847cdb326d8dd50d684f6","weight":300.00},{"cargoCn":"可乐,豆奶","createTime":1565577469280,"createUser":"systemAuto","exceptionFlag":0,"flightInfoId":"c28b3b6c910c475884204a129afb02a9","id":"91eccfa06dda05b7a9a2df8943fdf5c8","inflightnum":0,"mailType":"C","manifestScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","number":3210,"receivecargoNumber":9520,"receivecargoVolumn":0.70,"receivecargoWeight":830.00,"runTimeScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","specialCode":"--","totalnum":0,"volume":0.20,"waybillCode":"991-79935553","waybillId":"00514f84f387493ea7d19245a98f4697","weight":210.00},{"cargoCn":"牛奶","createTime":1565577469283,"createUser":"systemAuto","exceptionFlag":0,"flightInfoId":"c28b3b6c910c475884204a129afb02a9","id":"78784300a9a16267a8809f6610df170f","inflightnum":0,"mailType":"C","manifestScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","number":1230,"receivecargoNumber":6740,"receivecargoVolumn":0.40,"receivecargoWeight":522.00,"runTimeScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","specialCode":"PER","totalnum":0,"volume":0.10,"waybillCode":"991-65651456","waybillId":"e829dbf06d0544eba2b65446062867d4","weight":101.00},{"cargoCn":"美年达","createTime":1565577469297,"createUser":"systemAuto","exceptionFlag":0,"flightInfoId":"c28b3b6c910c475884204a129afb02a9","id":"2160ad82870a0a51f7d5956aab88a4a7","inflightnum":0,"mailType":"C","manifestScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","number":1231,"receivecargoNumber":12230,"receivecargoVolumn":1.40,"receivecargoWeight":2496.00,"runTimeScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","specialCode":"--","totalnum":0,"volume":0.20,"waybillCode":"991-43233492","waybillId":"29ef623ba29547f1b2c5f9b701a4fdb9","weight":31.00}]}]},{"cargoName":"2H","scooters":[{"actWgt":"1875","cargoStatus":0,"dest":"CGO","dst":"","estWgt":"1875","flightInfoId":"c28b3b6c910c475884204a129afb02a9","pos":"2H","pri":"2","restrictedCargo":"","tailer":"","type":"C"}]},{"cargoName":"3H","scooters":[{"actWgt":"1582","cargoStatus":0,"dest":"CGO","dst":"","estWgt":"1582","flightInfoId":"c28b3b6c910c475884204a129afb02a9","pos":"3H","pri":"3","restrictedCargo":"","tailer":"24100","type":"M","waybillList":[{"cargoCn":"美年达","createTime":1565577469320,"createUser":"systemAuto","exceptionFlag":0,"flightInfoId":"c28b3b6c910c475884204a129afb02a9","id":"12eb2bf4bc21076395422d89476e1e23","inflightnum":0,"mailType":"C","manifestScooterId":"3404f92ef3cf4d8a8bac459b7bd535f6","number":1000,"receivecargoNumber":12230,"receivecargoVolumn":1.40,"receivecargoWeight":2496.00,"runTimeScooterId":"3404f92ef3cf4d8a8bac459b7bd535f6","specialCode":"--","totalnum":0,"volume":0.50,"waybillCode":"991-43233492","waybillId":"29ef623ba29547f1b2c5f9b701a4fdb9","weight":1231.00},{"cargoCn":"可乐,豆奶","createTime":1565577469323,"createUser":"systemAuto","exceptionFlag":0,"flightInfoId":"c28b3b6c910c475884204a129afb02a9","id":"963432bafc8270958462023a7d13e495","inflightnum":0,"mailType":"C","manifestScooterId":"3404f92ef3cf4d8a8bac459b7bd535f6","number":2100,"receivecargoNumber":9520,"receivecargoVolumn":0.70,"receivecargoWeight":830.00,"runTimeScooterId":"3404f92ef3cf4d8a8bac459b7bd535f6","specialCode":"--","totalnum":0,"volume":0.10,"waybillCode":"991-79935553","waybillId":"00514f84f387493ea7d19245a98f4697","weight":210.00},{"cargoCn":"牛奶","createTime":1565577469325,"createUser":"systemAuto","exceptionFlag":0,"flightInfoId":"c28b3b6c910c475884204a129afb02a9","id":"8652635c46471ef618fb7626804df287","inflightnum":0,"mailType":"C","manifestScooterId":"3404f92ef3cf4d8a8bac459b7bd535f6","number":2300,"receivecargoNumber":6740,"receivecargoVolumn":0.40,"receivecargoWeight":522.00,"runTimeScooterId":"3404f92ef3cf4d8a8bac459b7bd535f6","specialCode":"PER","totalnum":0,"volume":0.10,"waybillCode":"991-65651456","waybillId":"e829dbf06d0544eba2b65446062867d4","weight":120.00},{"cargoCn":"茄子","createTime":1565577469328,"createUser":"systemAuto","exceptionFlag":0,"flightInfoId":"c28b3b6c910c475884204a129afb02a9","id":"d7e4ea161ee3cbc0edfb23031da34f3b","inflightnum":0,"mailType":"C","manifestScooterId":"3404f92ef3cf4d8a8bac459b7bd535f6","number":3213,"receivecargoNumber":3213,"receivecargoVolumn":0.30,"receivecargoWeight":21.00,"runTimeScooterId":"3404f92ef3cf4d8a8bac459b7bd535f6","specialCode":"PER","totalnum":0,"volume":0.30,"waybillCode":"991-40456345","waybillId":"37cece61b8714f52b229bd4b2e709f69","weight":21.00}]}]},{"cargoName":"4H","scooters":[{"actWgt":"424","cargoStatus":0,"dest":"CGO","dst":"","estWgt":"424","flightInfoId":"c28b3b6c910c475884204a129afb02a9","pos":"4H","pri":"4","restrictedCargo":"","tailer":"24108","type":"C","waybillList":[{"cargoCn":"青椒","createTime":1565577469336,"createUser":"systemAuto","exceptionFlag":0,"flightInfoId":"c28b3b6c910c475884204a129afb02a9","id":"5eac938b3b16dad483a80242367eac86","inflightnum":0,"mailType":"C","manifestScooterId":"b8891648dcb44e14ae691e65d109840a","number":3214,"receivecargoNumber":8466,"receivecargoVolumn":0.70,"receivecargoWeight":354.00,"runTimeScooterId":"b8891648dcb44e14ae691e65d109840a","specialCode":"PER","totalnum":0,"volume":0.30,"waybillCode":"991-20264926","waybillId":"0e6148fdeca2446697a73bd8647c8361","weight":123.00},{"cargoCn":"牛奶","createTime":1565577469339,"createUser":"systemAuto","exceptionFlag":0,"flightInfoId":"c28b3b6c910c475884204a129afb02a9","id":"0f43967e452db14eec47471d1c4fbef6","inflightnum":0,"mailType":"C","manifestScooterId":"b8891648dcb44e14ae691e65d109840a","number":3210,"receivecargoNumber":6740,"receivecargoVolumn":0.40,"receivecargoWeight":522.00,"runTimeScooterId":"b8891648dcb44e14ae691e65d109840a","specialCode":"PER","totalnum":0,"volume":0.20,"waybillCode":"991-65651456","waybillId":"e829dbf06d0544eba2b65446062867d4","weight":301.00}]}]}]
         * reviewStatus : 0
         * createTime : 1565579288674
         * createUser : u547a3b69c78b49959580c7460b66c63b
         * updateTime : 1565596203662
         * updateUser : null
         * documentType : 2
         * loadingAdvice : 0
         * loadingUser : null
         * contentObject : null
         * returnReason : null
         * preContent :
         * cgoContent :
         * autoLoadInstalledSingle : 0
         * flightNo : CZ3768
         * installedSingleConfirm : 0
         */
        private boolean showDetail;
        private String id;
        private String flightInfoId;
        private int version;
        private String content;
        private int reviewStatus;
        private long createTime;
        private String createUser;
        private long updateTime;
        private String updateUser;
        private int documentType;
        private int loadingAdvice;
        private String loadingUser;
        private List<ContentObjectBean> contentObject;
        private String returnReason;
        private String preContent;
        private String cgoContent;
        private int autoLoadInstalledSingle;
        private String flightNo;
        private int installedSingleConfirm;//1,监装确认，其他，默认版本

        @Data
        public static class ContentObjectBean {

            /**
             * flightInfoId : c28b3b6c910c475884204a129afb02a9
             * cargoStatus : 0
             * dst :
             * pos : 1H
             * pri : 1
             * tailer : 24106
             * estWgt : 642
             * waybillList : [{"cargoCn":"花生豆","runTimeScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","manifestScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","weight":300,"volume":0.4,"flightInfoId":"c28b3b6c910c475884204a129afb02a9","inflightnum":0,"number":3100,"mailType":"C","receivecargoNumber":3100,"createTime":1565577469277,"totalnum":0,"receivecargoWeight":300,"exceptionFlag":0,"waybillCode":"991-15235732","waybillId":"832be037fa6847cdb326d8dd50d684f6","createUser":"systemAuto","id":"09946777b29a26ecc09f1192d647574e","receivecargoVolumn":0.4,"specialCode":"--"},{"cargoCn":"可乐,豆奶","runTimeScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","manifestScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","weight":210,"volume":0.2,"flightInfoId":"c28b3b6c910c475884204a129afb02a9","inflightnum":0,"number":3210,"mailType":"C","receivecargoNumber":9520,"createTime":1565577469280,"totalnum":0,"receivecargoWeight":830,"exceptionFlag":0,"waybillCode":"991-79935553","waybillId":"00514f84f387493ea7d19245a98f4697","createUser":"systemAuto","id":"91eccfa06dda05b7a9a2df8943fdf5c8","receivecargoVolumn":0.7,"specialCode":"--"},{"cargoCn":"牛奶","runTimeScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","manifestScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","weight":101,"volume":0.1,"flightInfoId":"c28b3b6c910c475884204a129afb02a9","inflightnum":0,"number":1230,"mailType":"C","receivecargoNumber":6740,"createTime":1565577469283,"totalnum":0,"receivecargoWeight":522,"exceptionFlag":0,"waybillCode":"991-65651456","waybillId":"e829dbf06d0544eba2b65446062867d4","createUser":"systemAuto","id":"78784300a9a16267a8809f6610df170f","receivecargoVolumn":0.4,"specialCode":"PER"},{"cargoCn":"美年达","runTimeScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","manifestScooterId":"0503a2c299ac4f6a86511f3fe18d3e54","weight":31,"volume":0.2,"flightInfoId":"c28b3b6c910c475884204a129afb02a9","inflightnum":0,"number":1231,"mailType":"C","receivecargoNumber":12230,"createTime":1565577469297,"totalnum":0,"receivecargoWeight":2496,"exceptionFlag":0,"waybillCode":"991-43233492","waybillId":"29ef623ba29547f1b2c5f9b701a4fdb9","createUser":"systemAuto","id":"2160ad82870a0a51f7d5956aab88a4a7","receivecargoVolumn":1.4,"specialCode":"--"}]
             * dest : CGO
             * type : C
             * actWgt : 642
             * restrictedCargo :
             */

            private String flightInfoId;
            private int cargoStatus;
            private String dst;
            private String pos;
            private String pri;
            private String tailer;
            private String estWgt;
            private String startBerth;
            private String location;
            private String dest;
            private String type;
            private String actWgt;
            private String restrictedCargo;
            private List<WaybillListBean> waybillList;
            /**
             * 拉货标记 0:正常   1: 拉货
             */
            private int exceptionFlag;
            /**
             * 缺少的数据
             */
            private boolean showPullDown = false;
            private String uldNumber = "-";
            private String goodsPosition = "";
            private int number = 0;
            private boolean locked;

            @Data
            public static class WaybillListBean {
                /**
                 * cargoCn : 花生豆
                 * runTimeScooterId : 0503a2c299ac4f6a86511f3fe18d3e54
                 * manifestScooterId : 0503a2c299ac4f6a86511f3fe18d3e54
                 * weight : 300.0
                 * volume : 0.4
                 * flightInfoId : c28b3b6c910c475884204a129afb02a9
                 * inflightnum : 0
                 * number : 3100
                 * mailType : C
                 * receivecargoNumber : 3100
                 * createTime : 1565577469277
                 * totalnum : 0
                 * receivecargoWeight : 300.0
                 * exceptionFlag : 0
                 * waybillCode : 991-15235732
                 * waybillId : 832be037fa6847cdb326d8dd50d684f6
                 * createUser : systemAuto
                 * id : 09946777b29a26ecc09f1192d647574e
                 * receivecargoVolumn : 0.4
                 * specialCode : --
                 */

                private String cargoCn;
                private String runTimeScooterId;
                private String manifestScooterId;
                private double weight;
                private double volume;
                private String flightInfoId;
                private int inflightnum;
                private int number;
                private String mailType;
                private int receivecargoNumber;
                private long createTime;
                private int totalnum;
                private double receivecargoWeight;
                private int exceptionFlag;
                private String waybillCode;
                private String waybillId;
                private String createUser;
                private String id;
                private double receivecargoVolumn;
                private String specialCode;
            }
        }
    }
}
