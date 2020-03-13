package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class BaseParamBean {

    private int total;
    private int size;
    private int current;
    private Object taskHandler;
    private boolean searchCount;
    private int pages;
    private List<RecordsBean> records;
}
