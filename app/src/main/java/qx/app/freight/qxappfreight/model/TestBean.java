package qx.app.freight.qxappfreight.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestBean implements Serializable {
    private int type;
    private int number;
    private String name;
    private boolean isChoose;

    public TestBean(int type, int number) {
        this.type = type;
        this.number = number;
    }

    public TestBean(String name, boolean isChoose) {
        this.name = name;
        this.isChoose = isChoose;

    }
    public TestBean(int type,String name, boolean isChoose) {
        this.type = type;
        this.name = name;
        this.isChoose = isChoose;
    }
}
