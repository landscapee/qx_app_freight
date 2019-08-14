package qx.app.freight.qxappfreight.bean.response;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverweightBean implements MultiItemEntity, Serializable {

    /**
     * id : e55bca4a2d5b3445d4c041a40881ff68
     * count : 2
     * weight : 3
     * volume : 0
     * overWeight : 0
     * delFlag : 0
     * stepName : DA_outbound
     * inWaybillRecordId : 93f4329d804bd967e6aafd6d68b3b277
     */

    private String id;
    private int count;
    private int weight;
    private int volume;
    private int overWeight;
    private int delFlag;
    private String stepName;
    private String inWaybillRecordId;

    private String overweightInfoId;

    @Override
    public int getItemType() {
        if (delFlag==1){
            return 1;
        }else {
            return 0;
        }
    }
}
