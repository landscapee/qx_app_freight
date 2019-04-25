package qx.app.freight.qxappfreight.bean.request;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

@Data
public class ReturnGoodsEntity implements Parcelable {
    private String goodsName;//品名
    private int returnNumber;//退回件数
    private String handleStyle = "退货";//处理方式
    private String returnReason;//退货原因

    public ReturnGoodsEntity() {
        super();
    }

    public ReturnGoodsEntity(Parcel in) {
        goodsName = in.readString();
        returnNumber = in.readInt();
        handleStyle = in.readString();
        returnReason = in.readString();
    }

    public static final Creator<ReturnGoodsEntity> CREATOR = new Creator<ReturnGoodsEntity>() {
        @Override
        public ReturnGoodsEntity createFromParcel(Parcel in) {
            return new ReturnGoodsEntity(in);
        }

        @Override
        public ReturnGoodsEntity[] newArray(int size) {
            return new ReturnGoodsEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goodsName);
        dest.writeInt(returnNumber);
        dest.writeString(handleStyle);
        dest.writeString(returnReason);
    }
}
