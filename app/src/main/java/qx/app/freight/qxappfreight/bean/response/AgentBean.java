package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class AgentBean  implements Serializable {
    public List<MyAgentListBean> records;

}
