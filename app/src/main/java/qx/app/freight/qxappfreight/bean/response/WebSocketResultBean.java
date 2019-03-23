package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

public class WebSocketResultBean {


    /**
     * chgData : [{"chargeFlag":"0","refrigeratedTemperature":"0","shipperCompanyId":"enbc55b2a4218a4261ac7083ccaa69c288","totalVolume":"100","spotFlag":"1","consigneePostcode":"jsows@foxmail.com","expectedDeliveryGate":"D口","virtualFlag":"0","delFlag":"0","totalNumberPackages":"100.0","taskType":"收验","consigneePhone":"13654988874","id":"12ed01f95297891a1531ff2d605abe6d","applyTime":"1553342848932","consigneeAddress":"四川省成都市双流机场1号","activitiId":"1932541","declareItem":[{"volume":100,"number":100,"cargoId":"b856f9932ec34a3bb2cd320c5ca136e3","cargoCn":"钛棒","waybillId":"12ed01f95297891a1531ff2d605abe6d","weight":100,"packagingType":["纸盒","编织袋"]}],"otherFlag":"0","specialCargoCode":"241872","consignee":"青宵科技","flightDate":"1553315440000","stepOrder":2,"taskTypeCode":"receive","flightNumber":"EU9564","coldStorage":"0","mailType":"C","billingWeight":"500.0","createTime":"1553343105594","totalWeight":"100.0","exceptionFlag":"0","waybillCode":"02810000028","createUser":"u90eab98bad7d407ca9ff7537f5e4367a","declareWaybillAddition":{"addtionInvoices":"[]","waybillId":"12ed01f95297891a1531ff2d605abe6d","id":"4bc92587e86924d1cfddec3e1fd45be7"},"expectedDeliveryTime":"1553343105594","taskId":"1932547","status":"2"}]
     * flag : N
     */

    private String flag;
    private List<ChgDataBean> chgData;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<ChgDataBean> getChgData() {
        return chgData;
    }

    public void setChgData(List<ChgDataBean> chgData) {
        this.chgData = chgData;
    }

    public static class ChgDataBean {
        /**
         * chargeFlag : 0
         * refrigeratedTemperature : 0
         * shipperCompanyId : enbc55b2a4218a4261ac7083ccaa69c288
         * totalVolume : 100
         * spotFlag : 1
         * consigneePostcode : jsows@foxmail.com
         * expectedDeliveryGate : D口
         * virtualFlag : 0
         * delFlag : 0
         * totalNumberPackages : 100.0
         * taskType : 收验
         * consigneePhone : 13654988874
         * id : 12ed01f95297891a1531ff2d605abe6d
         * applyTime : 1553342848932
         * consigneeAddress : 四川省成都市双流机场1号
         * activitiId : 1932541
         * declareItem : [{"volume":100,"number":100,"cargoId":"b856f9932ec34a3bb2cd320c5ca136e3","cargoCn":"钛棒","waybillId":"12ed01f95297891a1531ff2d605abe6d","weight":100,"packagingType":["纸盒","编织袋"]}]
         * otherFlag : 0
         * specialCargoCode : 241872
         * consignee : 青宵科技
         * flightDate : 1553315440000
         * stepOrder : 2
         * taskTypeCode : receive
         * flightNumber : EU9564
         * coldStorage : 0
         * mailType : C
         * billingWeight : 500.0
         * createTime : 1553343105594
         * totalWeight : 100.0
         * exceptionFlag : 0
         * waybillCode : 02810000028
         * createUser : u90eab98bad7d407ca9ff7537f5e4367a
         * declareWaybillAddition : {"addtionInvoices":"[]","waybillId":"12ed01f95297891a1531ff2d605abe6d","id":"4bc92587e86924d1cfddec3e1fd45be7"}
         * expectedDeliveryTime : 1553343105594
         * taskId : 1932547
         * status : 2
         */

        private String chargeFlag;
        private String refrigeratedTemperature;
        private String shipperCompanyId;
        private String totalVolume;
        private String spotFlag;
        private String consigneePostcode;
        private String expectedDeliveryGate;
        private String virtualFlag;
        private String delFlag;
        private String totalNumberPackages;
        private String taskType;
        private String consigneePhone;
        private String id;
        private String applyTime;
        private String consigneeAddress;
        private String activitiId;
        private String otherFlag;
        private String specialCargoCode;
        private String consignee;
        private String flightDate;
        private int stepOrder;
        private String taskTypeCode;
        private String flightNumber;
        private String coldStorage;
        private String mailType;
        private String billingWeight;
        private String createTime;
        private String totalWeight;
        private String exceptionFlag;
        private String waybillCode;
        private String createUser;
        private DeclareWaybillAdditionBean declareWaybillAddition;
        private String expectedDeliveryTime;
        private String taskId;
        private String status;
        private List<DeclareItemBean> declareItem;

        public String getChargeFlag() {
            return chargeFlag;
        }

        public void setChargeFlag(String chargeFlag) {
            this.chargeFlag = chargeFlag;
        }

        public String getRefrigeratedTemperature() {
            return refrigeratedTemperature;
        }

        public void setRefrigeratedTemperature(String refrigeratedTemperature) {
            this.refrigeratedTemperature = refrigeratedTemperature;
        }

        public String getShipperCompanyId() {
            return shipperCompanyId;
        }

        public void setShipperCompanyId(String shipperCompanyId) {
            this.shipperCompanyId = shipperCompanyId;
        }

        public String getTotalVolume() {
            return totalVolume;
        }

        public void setTotalVolume(String totalVolume) {
            this.totalVolume = totalVolume;
        }

        public String getSpotFlag() {
            return spotFlag;
        }

        public void setSpotFlag(String spotFlag) {
            this.spotFlag = spotFlag;
        }

        public String getConsigneePostcode() {
            return consigneePostcode;
        }

        public void setConsigneePostcode(String consigneePostcode) {
            this.consigneePostcode = consigneePostcode;
        }

        public String getExpectedDeliveryGate() {
            return expectedDeliveryGate;
        }

        public void setExpectedDeliveryGate(String expectedDeliveryGate) {
            this.expectedDeliveryGate = expectedDeliveryGate;
        }

        public String getVirtualFlag() {
            return virtualFlag;
        }

        public void setVirtualFlag(String virtualFlag) {
            this.virtualFlag = virtualFlag;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public String getTotalNumberPackages() {
            return totalNumberPackages;
        }

        public void setTotalNumberPackages(String totalNumberPackages) {
            this.totalNumberPackages = totalNumberPackages;
        }

        public String getTaskType() {
            return taskType;
        }

        public void setTaskType(String taskType) {
            this.taskType = taskType;
        }

        public String getConsigneePhone() {
            return consigneePhone;
        }

        public void setConsigneePhone(String consigneePhone) {
            this.consigneePhone = consigneePhone;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApplyTime() {
            return applyTime;
        }

        public void setApplyTime(String applyTime) {
            this.applyTime = applyTime;
        }

        public String getConsigneeAddress() {
            return consigneeAddress;
        }

        public void setConsigneeAddress(String consigneeAddress) {
            this.consigneeAddress = consigneeAddress;
        }

        public String getActivitiId() {
            return activitiId;
        }

        public void setActivitiId(String activitiId) {
            this.activitiId = activitiId;
        }

        public String getOtherFlag() {
            return otherFlag;
        }

        public void setOtherFlag(String otherFlag) {
            this.otherFlag = otherFlag;
        }

        public String getSpecialCargoCode() {
            return specialCargoCode;
        }

        public void setSpecialCargoCode(String specialCargoCode) {
            this.specialCargoCode = specialCargoCode;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getFlightDate() {
            return flightDate;
        }

        public void setFlightDate(String flightDate) {
            this.flightDate = flightDate;
        }

        public int getStepOrder() {
            return stepOrder;
        }

        public void setStepOrder(int stepOrder) {
            this.stepOrder = stepOrder;
        }

        public String getTaskTypeCode() {
            return taskTypeCode;
        }

        public void setTaskTypeCode(String taskTypeCode) {
            this.taskTypeCode = taskTypeCode;
        }

        public String getFlightNumber() {
            return flightNumber;
        }

        public void setFlightNumber(String flightNumber) {
            this.flightNumber = flightNumber;
        }

        public String getColdStorage() {
            return coldStorage;
        }

        public void setColdStorage(String coldStorage) {
            this.coldStorage = coldStorage;
        }

        public String getMailType() {
            return mailType;
        }

        public void setMailType(String mailType) {
            this.mailType = mailType;
        }

        public String getBillingWeight() {
            return billingWeight;
        }

        public void setBillingWeight(String billingWeight) {
            this.billingWeight = billingWeight;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTotalWeight() {
            return totalWeight;
        }

        public void setTotalWeight(String totalWeight) {
            this.totalWeight = totalWeight;
        }

        public String getExceptionFlag() {
            return exceptionFlag;
        }

        public void setExceptionFlag(String exceptionFlag) {
            this.exceptionFlag = exceptionFlag;
        }

        public String getWaybillCode() {
            return waybillCode;
        }

        public void setWaybillCode(String waybillCode) {
            this.waybillCode = waybillCode;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public DeclareWaybillAdditionBean getDeclareWaybillAddition() {
            return declareWaybillAddition;
        }

        public void setDeclareWaybillAddition(DeclareWaybillAdditionBean declareWaybillAddition) {
            this.declareWaybillAddition = declareWaybillAddition;
        }

        public String getExpectedDeliveryTime() {
            return expectedDeliveryTime;
        }

        public void setExpectedDeliveryTime(String expectedDeliveryTime) {
            this.expectedDeliveryTime = expectedDeliveryTime;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<DeclareItemBean> getDeclareItem() {
            return declareItem;
        }

        public void setDeclareItem(List<DeclareItemBean> declareItem) {
            this.declareItem = declareItem;
        }

        public static class DeclareWaybillAdditionBean {
            /**
             * addtionInvoices : []
             * waybillId : 12ed01f95297891a1531ff2d605abe6d
             * id : 4bc92587e86924d1cfddec3e1fd45be7
             */

            private String addtionInvoices;
            private String waybillId;
            private String id;

            public String getAddtionInvoices() {
                return addtionInvoices;
            }

            public void setAddtionInvoices(String addtionInvoices) {
                this.addtionInvoices = addtionInvoices;
            }

            public String getWaybillId() {
                return waybillId;
            }

            public void setWaybillId(String waybillId) {
                this.waybillId = waybillId;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public static class DeclareItemBean {
            /**
             * volume : 100.0
             * number : 100.0
             * cargoId : b856f9932ec34a3bb2cd320c5ca136e3
             * cargoCn : 钛棒
             * waybillId : 12ed01f95297891a1531ff2d605abe6d
             * weight : 100.0
             * packagingType : ["纸盒","编织袋"]
             */

            private double volume;
            private double number;
            private String cargoId;
            private String cargoCn;
            private String waybillId;
            private double weight;
            private List<String> packagingType;

            public double getVolume() {
                return volume;
            }

            public void setVolume(double volume) {
                this.volume = volume;
            }

            public double getNumber() {
                return number;
            }

            public void setNumber(double number) {
                this.number = number;
            }

            public String getCargoId() {
                return cargoId;
            }

            public void setCargoId(String cargoId) {
                this.cargoId = cargoId;
            }

            public String getCargoCn() {
                return cargoCn;
            }

            public void setCargoCn(String cargoCn) {
                this.cargoCn = cargoCn;
            }

            public String getWaybillId() {
                return waybillId;
            }

            public void setWaybillId(String waybillId) {
                this.waybillId = waybillId;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(double weight) {
                this.weight = weight;
            }

            public List<String> getPackagingType() {
                return packagingType;
            }

            public void setPackagingType(List<String> packagingType) {
                this.packagingType = packagingType;
            }
        }
    }
}
