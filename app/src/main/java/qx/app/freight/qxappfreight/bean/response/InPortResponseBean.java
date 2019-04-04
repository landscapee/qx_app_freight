package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;
import qx.app.freight.qxappfreight.bean.InportTallyBean;
@Data
public class InPortResponseBean {

    /**
     * hasError : false
     * list : []
     */

    private boolean hasError;
    private List<InportTallyBean> list;
}
