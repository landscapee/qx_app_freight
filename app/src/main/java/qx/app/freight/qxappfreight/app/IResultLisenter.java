package qx.app.freight.qxappfreight.app;

public interface IResultLisenter<T> {
    void onSuccess(T t);

    void onFail(String error);

}
