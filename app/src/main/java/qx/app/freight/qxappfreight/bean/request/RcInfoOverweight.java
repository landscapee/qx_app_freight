package qx.app.freight.qxappfreight.bean.request;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

import lombok.Data;

@Data
public class RcInfoOverweight implements MultiItemEntity, Serializable  {

    /**
     * 超重记录
     */
    private String id;

    /**
     * 收运记录id
     */
    private String rcId;

    /**
     * 品名
     */
    private String cargoCn;

    /**
     * 件数
     */
    private Integer count;

    /**
     * 重量
     */
    private Integer weight;

    /**
     * 体积
     */
    private Integer volume;

    /**
     * 超重重量
     */
    private Integer overWeight;

    /**
     * 1删除
     */
    private int delFlag;

    @Override
    public int getItemType() {
        if (delFlag==1){
            return 1;
        }else {
            return 0;
        }
    }
}
