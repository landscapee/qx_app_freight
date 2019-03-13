package qx.app.freight.qxappfreight.model;

public class TestBean {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
