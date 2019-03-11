package qx.app.freight.qxappfreight.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.DeliveryDetailAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;

public class InportDeliveryDetailActivity extends BaseActivity {

    @BindView(R.id.r_view)
    RecyclerView rView;

    DeliveryDetailAdapter mAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_inport_delivery_detail;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {

    }

}
