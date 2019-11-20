package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CargoCabinData {

    /**
     * cargos : [{"id":"73a2f66f7bc34379bdb51cc9556072f9","aircraftNo":"B7185","iata":"CZ","cargoNo":"1","planType":"1","pos":"11L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":231.4,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"1"},{"id":"62b07510e9614647bf94ac7e18b9d06d","aircraftNo":"B7185","iata":"CZ","cargoNo":"1","planType":"1","pos":"11R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":231.4,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"2"},{"id":"1a1fbee6439f4a2da2a433dfab60304a","aircraftNo":"B7185","iata":"CZ","cargoNo":"1","planType":"2","pos":"11P","overlays":"[\"11L\",\"11R\",\"\",\"\",\"\"]","maxWgt":5102,"arm":249.2,"pr":"2","ul":"L","zone":"","validType":"[\"P6P\",\"\",\"\",\"PMC\",\"\"]","idx":"4"},{"id":"7114600072104270a13321aa965117e2","aircraftNo":"B7185","iata":"CZ","cargoNo":"1","planType":"1","pos":"12L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":329.6,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"6"},{"id":"54cedeafc7df4a979c7201eedbe818e1","aircraftNo":"B7185","iata":"CZ","cargoNo":"1","planType":"1","pos":"12R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":329.6,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"7"},{"id":"80c5ef0b062e437f899673ec42542ced","aircraftNo":"B7185","iata":"CZ","cargoNo":"1","planType":"1","pos":"13L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":406.5,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"9"},{"id":"6414dc54c5c64e33852972aa5bab9fbb","aircraftNo":"B7185","iata":"CZ","cargoNo":"1","planType":"1","pos":"13R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":406.5,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"10"},{"id":"ae330cb68be045f6bb055c71b2dfb381","aircraftNo":"B7185","iata":"CZ","cargoNo":"1","planType":"2","pos":"12P","overlays":"[\"12L\",\"12R\",\"13L\",\"13R\",\"\"]","maxWgt":5102,"arm":347.2,"pr":"2","ul":"L","zone":"","validType":"[\"P6P\",\"PMC\",\"\",\"\",\"\"]","idx":"12"},{"id":"22a7709e979d4aa6aedb453115b74f4d","aircraftNo":"B7185","iata":"CZ","cargoNo":"1","planType":"1","pos":"14L","overlays":"[\"13P\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":466.9,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"14"},{"id":"a9eece89dc81462eabce9c8097990c53","aircraftNo":"B7185","iata":"CZ","cargoNo":"1","planType":"1","pos":"14R","overlays":"[\"13P\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":466.9,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"15"},{"id":"8e119d69a5f94b50930e63a539374496","aircraftNo":"B7185","iata":"CZ","cargoNo":"1","planType":"2","pos":"13P","overlays":"[\"13L\",\"13R\",\"21\",\"\",\"\"]","maxWgt":5102,"arm":444.9,"pr":"2","ul":"L","zone":"","validType":"[\"P6P\",\"PMC\",\"\",\"\",\"\"]","idx":"17"},{"id":"7667bbcd1a5b46e99cf1066f8dc09787","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"21L","overlays":"[\"13P\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":527.3,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"19"},{"id":"38483aee745747689589b2ecbb1ddbbc","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"21R","overlays":"[\"13P\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":527.3,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"20"},{"id":"67cc44a91ed84cbeb56421242c4ac7af","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"22L","overlays":"[\"22P\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":587.7,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"22"},{"id":"4c7d90effead42628143bdcd84301639","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"22R","overlays":"[\"22P\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":587.7,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"23"},{"id":"f95171280e2646b4bd80565f42c1157c","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"2","pos":"21P","overlays":"[\"21L\",\"21R\",\"22L\",\"22R\",\"\"]","maxWgt":5102,"arm":542.6,"pr":"2","ul":"L","zone":"","validType":"[\"P6P\",\"PMC\",\"\",\"\",\"\"]","idx":"25"},{"id":"ec132acd891446b78e0bbb92a0f3e6ee","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"23L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":648.1,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"27"},{"id":"7c8ef50c5de4430abd9c49ff4d74fe7b","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"23R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":648.1,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"28"},{"id":"baae22fde196411c932808a2722d0fb2","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"24L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":708.6,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"30"},{"id":"63df7afb259b42fd92cce31045aea6cc","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"24R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":708.6,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"31"},{"id":"c3d6df54f29e4087aaf104d15a65d3f6","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"2","pos":"22P","overlays":"[\"23L\",\"23R\",\"24L\",\"24R\",\"\"]","maxWgt":5102,"arm":640.2,"pr":"2","ul":"L","zone":"","validType":"[\"P6P\",\"PMC\",\"\",\"\",\"\"]","idx":"33"},{"id":"45dc5898165b44c086b95329f5561ed5","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"25L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":769.2,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"35"},{"id":"6bb414bd23a44f12b9bfb57dcd84ebe2","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"25R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":769.2,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"36"},{"id":"862377b8f9154e43869b6b2f1013f738","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"2","pos":"23P","overlays":"[\"24L\",\"24R\",\"25L\",\"25R\",\"\"]","maxWgt":5102,"arm":737.9,"pr":"2","ul":"L","zone":"","validType":"[\"P6P\",\"PMC\",\"\",\"\",\"\"]","idx":"38"},{"id":"88154b008bf345438dc7aacf73e3f8b9","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"26L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":829.8,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"40"},{"id":"39961a8c55b340b0a846095ff8de3694","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"26R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":829.8,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"41"},{"id":"06e871b9099c4e1bb347b0343842fc2d","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"27L","overlays":"[\"24P\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":890.4,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"43"},{"id":"a82bb4872b5b44a1888cdeb62a6c8e92","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"27R","overlays":"[\"24P\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":890.4,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"44"},{"id":"4f06718578ed498793de12fcfa731dd9","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"2","pos":"24P","overlays":"[\"25L\",\"25R\",\"26L\",\"26R\",\"27\"]","maxWgt":5102,"arm":835.6,"pr":"2","ul":"L","zone":"","validType":"[\"P6P\",\"PMC\",\"\",\"\",\"\"]","idx":"46"},{"id":"a12c9ee1aaf34bdebb0e012fb113545a","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"28L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":951.1,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"48"},{"id":"ea35ea1117f844bf97a013d0df6bbe9b","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"1","pos":"28R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":951.1,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"49"},{"id":"7214688d5b8542d28146113afffc82d1","aircraftNo":"B7185","iata":"CZ","cargoNo":"2","planType":"2","pos":"25P","overlays":"[\"27L\",\"27R\",\"28L\",\"28R\",\"\"]","maxWgt":6350,"arm":933.3,"pr":"2","ul":"L","zone":"","validType":"[\"P6P\",\"PMC\",\"\",\"\",\"\"]","idx":"51"},{"id":"be10f4ff854b4d71b1be8e84e951ff2a","aircraftNo":"B7185","iata":"CZ","cargoNo":"3","planType":"1","pos":"31L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1480.3,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"53"},{"id":"088a02e02c9c4d0b836cec6dba0ff17f","aircraftNo":"B7185","iata":"CZ","cargoNo":"3","planType":"1","pos":"31R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1480.3,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"54"},{"id":"f8be7d1064134487a55019e0c6193567","aircraftNo":"B7185","iata":"CZ","cargoNo":"3","planType":"1","pos":"32L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1540.9,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"56"},{"id":"e0ad7abcfd7c4d91af00fb86990db2bd","aircraftNo":"B7185","iata":"CZ","cargoNo":"3","planType":"1","pos":"32R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1540.9,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"57"},{"id":"7c0db646da39456d998f9bbb52f486bd","aircraftNo":"B7185","iata":"CZ","cargoNo":"3","planType":"1","pos":"33L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1601.5,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"59"},{"id":"357de2e3555046dd95432b5feb54c2aa","aircraftNo":"B7185","iata":"CZ","cargoNo":"3","planType":"1","pos":"33R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1601.5,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"60"},{"id":"301a48119a79477b835e873a1bf248d4","aircraftNo":"B7185","iata":"CZ","cargoNo":"3","planType":"1","pos":"34L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1662.1,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"62"},{"id":"76d25b8f2eec4b86ac8e5d8a99d8e13e","aircraftNo":"B7185","iata":"CZ","cargoNo":"3","planType":"1","pos":"34R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1662.1,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"63"},{"id":"f7cf387e5eec43ef9f32bd4edd855658","aircraftNo":"B7185","iata":"CZ","cargoNo":"3","planType":"1","pos":"35L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1722.7,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"65"},{"id":"8a653cf94eb6496dbefddd92d16cbc96","aircraftNo":"B7185","iata":"CZ","cargoNo":"3","planType":"1","pos":"35R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1722.7,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"66"},{"id":"a1ee8f5c1c7347dd86a8876af3c0949e","aircraftNo":"B7185","iata":"CZ","cargoNo":"3","planType":"1","pos":"36L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1783.2,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"68"},{"id":"47128c84a588463f9d79f20d3c9b4e95","aircraftNo":"B7185","iata":"CZ","cargoNo":"3","planType":"1","pos":"36R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1783.2,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"69"},{"id":"88e95231221842afbe5cb10a288ace97","aircraftNo":"B7185","iata":"CZ","cargoNo":"4","planType":"1","pos":"41L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1843.4,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"71"},{"id":"9eac866738ee48ae848bdfc85921a0aa","aircraftNo":"B7185","iata":"CZ","cargoNo":"4","planType":"1","pos":"41R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1843.4,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"72"},{"id":"7a4bd2b05da445d0beac713f1f23131d","aircraftNo":"B7185","iata":"CZ","cargoNo":"4","planType":"1","pos":"42L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1905.6,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"74"},{"id":"afb613b718da44c2b57b1cb122c2dbdf","aircraftNo":"B7185","iata":"CZ","cargoNo":"4","planType":"1","pos":"42R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1905.6,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"75"},{"id":"b0b8f19c61e74792af677100ea8176ca","aircraftNo":"B7185","iata":"CZ","cargoNo":"4","planType":"1","pos":"43L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1968.2,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"77"},{"id":"a3c857bdede54e50933157d1d4420615","aircraftNo":"B7185","iata":"CZ","cargoNo":"4","planType":"1","pos":"43R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":1968.2,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"78"},{"id":"7421b9f4306640979570a882d2496974","aircraftNo":"B7185","iata":"CZ","cargoNo":"4","planType":"1","pos":"44L","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":2028.9,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"80"},{"id":"09e41f9f6cad411da56c6c2cb54756be","aircraftNo":"B7185","iata":"CZ","cargoNo":"4","planType":"1","pos":"44R","overlays":"[\"\",\"\",\"\",\"\",\"\"]","maxWgt":1587,"arm":2028.9,"pr":"1","ul":"L","zone":"","validType":"[\"LD3\",\"AKE\",\"\",\"\",\"\"]","idx":"81"}]
     * id : 29aa13263be4477384215af70bc2c8f9
     * aircraftNo : B7185
     * iata : CZ
     * creator : null
     * createTime : 1553672408075
     * updator : null
     * updateTime : null
     * hld1wgt : 15306
     * hld1arm : 347.1
     * hld1vol : 1949
     * hld1maxWgt : 15306
     * hld1identification : F
     * hld1FwdHldMaxWgt : 40823
     * hld2wgt : 26758
     * hld2arm : 737.9
     * hld2vol : 2929
     * hld2maxWgt : 26758
     * hld2identification : F
     * hld2AftHldMaxWgt : 35833
     * hld3wgt : 19050
     * hld3arm : 1631.8
     * hld3vol : 2368
     * hld3maxWgt : 19050
     * hld3identification : R
     * hld3BagCntMinWgt : 200
     * hld4wgt : 12700
     * hld4arm : 1936.5
     * hld4vol : 1510
     * hld4maxWgt : 12700
     * hld4identification : R
     * hld4ActTnkLmtWgt : 0
     * hld5wgt : 4082
     * hld5arm : 2153.5
     * hld5vol : 600
     * hld5maxWgt : 4082
     * hld5identification : R
     * bulkHld : 5
     * bagContType : LD3
     * bagDensity : 5
     * volumeOfBulkHolds : ["","","","",""]
     * mailDensity : 5
     * weightUnit : KG
     * lengthUnit : IN
     * containerPlate : [["LD3","90","1587","L1","159"],["P6P","150","6350","L4","407"],["P1P","131","5102","L4","381"],["LD6","159","3175","L2","322"],["PLA","83","3175","L2","256"],["FLA","83","3175","L2","256"],["ALF","159","3175","L2","322"],["AKE","90","1587","L1","159"],["PAG","106","5102","L4","381"],["PMC","116","6350","L4","407"]]
     * fwdZone : ["","","","","","","","","",""]
     * fmaxWgt : ["","","","","","","","","",""]
     * fcentroid : ["","","","","","","","","",""]
     * rwdZone : ["","","","","","","","","",""]
     * rmaxWgt : ["","","","","","","","","",""]
     * rcentroid : ["","","","","","","","","",""]
     * no : 1
     */

    private String id;
    private String aircraftNo;
    private String iata;
    private Object creator;
    private long createTime;
    private Object updator;
    private Object updateTime;
    private double hld1wgt;
    private double hld1arm;
    private double hld1vol;
    private double hld1maxWgt;
    private double already1Wgt;
    private double already1Vol;
    private String hld1identification;
    private double hld1FwdHldMaxWgt;
    private double hld2wgt;
    private double hld2arm;
    private double hld2vol;
    private double hld2maxWgt;
    private double already2Wgt;
    private double already2Vol;
    private String hld2identification;
    private double hld2AftHldMaxWgt;
    private double hld3wgt;
    private double hld3arm;
    private double hld3vol;
    private double hld3maxWgt;
    private double already3Wgt;
    private double already3Vol;
    private String hld3identification;
    private double hld3BagCntMinWgt;
    private double hld4wgt;
    private double hld4arm;
    private double hld4vol;
    private double hld4maxWgt;
    private double already4Wgt;
    private double already4Vol;
    private String hld4identification;
    private double hld4ActTnkLmtWgt;
    private double hld5wgt;
    private double hld5arm;
    private double hld5vol;
    private double hld5maxWgt;
    private double already5Wgt;
    private double already5Vol;
    private String hld5identification;
    private String bulkHld;
    private String bagContType;
    private double bagDensity;
    private String volumeOfBulkHolds;
    private double mailDensity;
    private String weightUnit;
    private String lengthUnit;
    private String containerPlate;
    private String fwdZone;
    private String fmaxWgt;
    private String fcentroid;
    private String rwdZone;
    private String rmaxWgt;
    private String rcentroid;
    private int no;
    private List <CargosBean> cargos;

    private boolean bodyType; //字段 机体类型  false 窄体机  true 宽体机
    private String  typeName;//飞机型号名称

    @Getter
    @Setter
    public static class CargosBean {
        /**
         * id : 73a2f66f7bc34379bdb51cc9556072f9
         * aircraftNo : B7185
         * iata : CZ
         * cargoNo : 1
         * planType : 1
         * pos : 11L
         * overlays : ["","","","",""]
         * maxWgt : 1587
         * arm : 231.4
         * pr : 1
         * ul : L
         * zone :
         * validType : ["LD3","AKE","","",""]
         * idx : 1
         */

        private String id;
        private String aircraftNo;
        private String iata;
        private String cargoNo;
        private String planType;
        private String pos;
        private String overlays;
        private int maxWgt;
        private double arm;
        private String pr;
        private String ul;
        private String zone;
        private String validType;
        private String idx;

    }
}
