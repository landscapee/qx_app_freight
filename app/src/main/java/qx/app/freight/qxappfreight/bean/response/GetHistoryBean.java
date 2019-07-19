package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class GetHistoryBean {


    private List<GetInfosByFlightIdBean> records;

    private int total;
    private int size;
    private int current;
    private String taskHandler;
    private boolean searchCount;
    private String pages;


}
