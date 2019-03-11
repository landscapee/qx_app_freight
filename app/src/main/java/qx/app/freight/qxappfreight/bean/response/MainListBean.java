package qx.app.freight.qxappfreight.bean.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MainListBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        /**
         * orderNumber : 78-12345678
         * roadJunctionNumber : 009
         * useDate : 12:30-13:30
         * roadNumber : 19
         * totalNumber : 12
         * totalVolume : 5.2
         * totalWeight : 400
         * storeWay : 冷藏
         * storeTemp : 0
         * goodsType : 特货T899
         * chargingWeight : 300
         * singeData : [{"goodsName":"大豆","singleNumber":3,"singleWeight":300,"singleVolume":300,"packageType":"纸箱"},{"goodsName":"牛肉","singleNumber":2,"singleWeight":200,"singleVolume":125,"packageType":"纸箱"},{"goodsName":"狗肉","singleNumber":4,"singleWeight":400,"singleVolume":555,"packageType":"纸箱"}]
         * flightNumber : 3U9999
         * arriveTime : 13:12
         * realRoadNumber : 4
         * flightCompany : 四川航空
         * goodsCompany : 中外货运代理
         */

        private String orderNumber;
        private String roadJunctionNumber;
        private String useDate;
        private String roadNumber;
        private int totalNumber;
        private double totalVolume;
        private int totalWeight;
        private String storeWay;
        private int storeTemp;
        private String goodsType;
        private int chargingWeight;
        private String flightNumber;
        private String arriveTime;
        private String realRoadNumber;
        private String flightCompany;
        private String goodsCompany;
        private List<SingeDataBean> singeData;
        private String stepName;

        public String getStepName() {
            return stepName;
        }

        public void setStepName(String stepName) {
            this.stepName = stepName;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getRoadJunctionNumber() {
            return roadJunctionNumber;
        }

        public void setRoadJunctionNumber(String roadJunctionNumber) {
            this.roadJunctionNumber = roadJunctionNumber;
        }

        public String getUseDate() {
            return useDate;
        }

        public void setUseDate(String useDate) {
            this.useDate = useDate;
        }

        public String getRoadNumber() {
            return roadNumber;
        }

        public void setRoadNumber(String roadNumber) {
            this.roadNumber = roadNumber;
        }

        public int getTotalNumber() {
            return totalNumber;
        }

        public void setTotalNumber(int totalNumber) {
            this.totalNumber = totalNumber;
        }

        public double getTotalVolume() {
            return totalVolume;
        }

        public void setTotalVolume(double totalVolume) {
            this.totalVolume = totalVolume;
        }

        public int getTotalWeight() {
            return totalWeight;
        }

        public void setTotalWeight(int totalWeight) {
            this.totalWeight = totalWeight;
        }

        public String getStoreWay() {
            return storeWay;
        }

        public void setStoreWay(String storeWay) {
            this.storeWay = storeWay;
        }

        public int getStoreTemp() {
            return storeTemp;
        }

        public void setStoreTemp(int storeTemp) {
            this.storeTemp = storeTemp;
        }

        public String getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(String goodsType) {
            this.goodsType = goodsType;
        }

        public int getChargingWeight() {
            return chargingWeight;
        }

        public void setChargingWeight(int chargingWeight) {
            this.chargingWeight = chargingWeight;
        }

        public String getFlightNumber() {
            return flightNumber;
        }

        public void setFlightNumber(String flightNumber) {
            this.flightNumber = flightNumber;
        }

        public String getArriveTime() {
            return arriveTime;
        }

        public void setArriveTime(String arriveTime) {
            this.arriveTime = arriveTime;
        }

        public String getRealRoadNumber() {
            return realRoadNumber;
        }

        public void setRealRoadNumber(String realRoadNumber) {
            this.realRoadNumber = realRoadNumber;
        }

        public String getFlightCompany() {
            return flightCompany;
        }

        public void setFlightCompany(String flightCompany) {
            this.flightCompany = flightCompany;
        }

        public String getGoodsCompany() {
            return goodsCompany;
        }

        public void setGoodsCompany(String goodsCompany) {
            this.goodsCompany = goodsCompany;
        }

        public List<SingeDataBean> getSingeData() {
            return singeData;
        }

        public void setSingeData(List<SingeDataBean> singeData) {
            this.singeData = singeData;
        }

        public static class SingeDataBean implements Parcelable {
            /**
             * goodsName : 大豆
             * singleNumber : 3
             * singleWeight : 300
             * singleVolume : 300
             * packageType : 纸箱
             */

            private String goodsName;
            private int singleNumber;
            private int singleWeight;
            private double singleVolume;
            private String packageType;
            private String storeInfo;//入库存储信息

            SingeDataBean(Parcel in) {
                goodsName = in.readString();
                singleNumber = in.readInt();
                singleWeight = in.readInt();
                singleVolume = in.readDouble();
                packageType = in.readString();
                storeInfo = in.readString();
            }

            public static final Creator<SingeDataBean> CREATOR = new Creator<SingeDataBean>() {
                @Override
                public SingeDataBean createFromParcel(Parcel in) {
                    return new SingeDataBean(in);
                }

                @Override
                public SingeDataBean[] newArray(int size) {
                    return new SingeDataBean[size];
                }
            };

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public int getSingleNumber() {
                return singleNumber;
            }

            public void setSingleNumber(int singleNumber) {
                this.singleNumber = singleNumber;
            }

            public int getSingleWeight() {
                return singleWeight;
            }

            public void setSingleWeight(int singleWeight) {
                this.singleWeight = singleWeight;
            }

            public double getSingleVolume() {
                return singleVolume;
            }

            public void setSingleVolume(int singleVolume) {
                this.singleVolume = singleVolume;
            }

            public String getPackageType() {
                return packageType;
            }

            public void setPackageType(String packageType) {
                this.packageType = packageType;
            }

            public String getStoreInfo() {
                return storeInfo;
            }

            public void setStoreInfo(String storeInfo) {
                this.storeInfo = storeInfo;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(goodsName);
                dest.writeInt(singleNumber);
                dest.writeInt(singleWeight);
                dest.writeDouble(singleVolume);
                dest.writeString(packageType);
                dest.writeString(storeInfo);
            }
        }
    }

    public static class MainListCheckBean extends DataBean {
    }

    public static class MainListTranSportBean extends DataBean {
    }

    public static class MainListStoreBean extends DataBean {
    }
}
