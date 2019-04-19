package qx.app.freight.qxappfreight.activity;

import android.os.Bundle;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.contract.GetWayBillInfoByIdContract;
import qx.app.freight.qxappfreight.presenter.GetWayBillInfoByIdPresenter;

public class CollectorDeclareActivity extends BaseActivity implements GetWayBillInfoByIdContract.getWayBillInfoByIdView {

    private String wayBillId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_collector_declare;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        wayBillId = getIntent().getStringExtra("watBillId");
        mPresenter = new GetWayBillInfoByIdPresenter(this);
        ((GetWayBillInfoByIdPresenter) mPresenter).getWayBillInfoById(wayBillId);
    }

    @Override
    public void getWayBillInfoByIdResult(DeclareWaybillBean result) {

    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }
}
