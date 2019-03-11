package qx.app.freight.qxappfreight.app;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class BaseModel {
    protected List<Disposable> mDisposableList = new ArrayList<>();

    void interruptRequest() {
        for (Disposable disposable : mDisposableList) {
            disposable.dispose();
        }
    }
}
