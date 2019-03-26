package qx.app.freight.qxappfreight.bean;

import android.os.Parcel;
import android.os.Parcelable;


import lombok.Data;

@Data
public class LocalBillBean implements Parcelable {
    private String waybillId;
    private String wayBillCode;
    public LocalBillBean(){
        super();
    }
    private LocalBillBean(Parcel in) {
        waybillId = in.readString();
        wayBillCode = in.readString();
    }

    public static final Creator<LocalBillBean> CREATOR = new Creator<LocalBillBean>() {
        @Override
        public LocalBillBean createFromParcel(Parcel in) {
            return new LocalBillBean(in);
        }

        @Override
        public LocalBillBean[] newArray(int size) {
            return new LocalBillBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(waybillId);
        dest.writeString(wayBillCode);
    }
}