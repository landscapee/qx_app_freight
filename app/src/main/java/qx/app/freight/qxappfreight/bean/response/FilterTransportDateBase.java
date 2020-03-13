package qx.app.freight.qxappfreight.bean.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterTransportDateBase implements Serializable {
    private List <TransportDataBase> records;
    private Integer total;
    private Integer size;
    private Integer current;
    private Integer pages;

}
