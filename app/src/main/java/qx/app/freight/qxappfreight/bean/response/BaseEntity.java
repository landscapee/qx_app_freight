package qx.app.freight.qxappfreight.bean.response;

import lombok.Data;

@Data
public class BaseEntity<T> {
    private String status;
    private String message;
    private String rowCount;
    private T data;
}
