package qx.app.freight.qxappfreight.bean.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PullGoodsEntity {
    private int  pullGoodsType; // 0
    private int  createUserType;//监装 传1
    private String flightInfoId;
    private String userId;
    private String userName;
    private List<String> scooterIds;
}
