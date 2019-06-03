package qx.app.freight.qxappfreight.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;

public class StoreTypeChangeActivity extends BaseActivity {

    private TransportDataBase data;

    @Override
    public int getLayoutId() {
        return R.layout.activity_store_type_change;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
//        data = getIntent().

    }
}
