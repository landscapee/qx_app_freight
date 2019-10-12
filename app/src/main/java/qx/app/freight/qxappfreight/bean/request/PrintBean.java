package qx.app.freight.qxappfreight.bean.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PrintBean {
    private String printCode;
    private String printName;
    public PrintBean(String printCode,String printName){
        this.printCode = printCode;
        this.printName = printName;
    }

}
