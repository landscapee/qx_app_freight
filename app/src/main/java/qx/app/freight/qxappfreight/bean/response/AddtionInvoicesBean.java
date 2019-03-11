package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO : xxx
 * Created by pr
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddtionInvoicesBean {
    private String id;
    private String waybillId;
    private List<AddtionInvoices> addtionInvoices;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddtionInvoices {
        private String fileName;
        private String filePath;
        private String fileType;
        private String fileTypeName;
    }
}
