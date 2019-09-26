package qx.app.freight.qxappfreight.bean.request;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.response.LoadingListBean;

@Data
public class LoadingListSendEntity implements Serializable {

    /**
     * flightId : 134c7d843a864fd6bab7ed7d3d62a035
     * content : [{"actWgt":"514","dest":"HAK","dst":"401","estWgt":"514","pos":"4H","pri":"A01","specialCode":"02M","tailer":"4540","type":"C"},{"actWgt":"238","dest":"HAK","dst":"401","estWgt":"238","pos":"4H","pri":"A01","specialCode":"02M","tailer":"4990","type":"C"}]
     * createDate : 1553396247913
     * createUser : 查询到的装机单中的createUser
     */

    private String flightInfoId;
    private String flightNo;
    private long createTime;
    private String createUser;
    private String loadingUser;
    private List<LoadingListBean.DataBean.ContentObjectBean> content;
}
