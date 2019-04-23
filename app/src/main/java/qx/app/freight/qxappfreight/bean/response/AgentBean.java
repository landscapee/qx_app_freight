package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class AgentBean implements Serializable {

    public List<MyAgentListBean> rcInfo;

    private List<SecurityResultListBean> securityResultList;
    @Data
    public static class SecurityResultListBean {
        /**
         * 已检查id
         */
        private String id;

        /**
         * 安检id
         */
        private String securityId;

        /**
         * 件数
         */
        private long piece;

        /**
         * 品名
         */
        private String commodity;

        /**
         * 0退运 1扣货 2移交公安
         */
        private int processMode;

        /**
         * 原因
         */
        private String reason;

        /**
         * 0删除 1正常
         */
        private int delFlag;

        /**
         * 品名id
         */
        private String cargoId;
        /**
         * 0收验 1收运
         */
        private int reType;
    }
}
