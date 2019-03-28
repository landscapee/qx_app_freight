package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;

public class DynamicDetailsAcitvity extends BaseActivity {

    public static void startActivity(Activity context, TransportListBean.DeclareWaybillAdditionBean declareWaybillAdditionBean, String taskId, String filePath, String spotFlag, int insCheck, String flightNumber, String shipperCompanyId) {
        Intent intent = new Intent(context, VerifyFileActivity.class);
        intent.putExtra("DeclareWaybillAdditionBean", declareWaybillAdditionBean);
        intent.putExtra("taskId", taskId);
        intent.putExtra("filePath", filePath);
        intent.putExtra("spotFlag", spotFlag);
        intent.putExtra("insCheck", insCheck);
        intent.putExtra("flightNumber", flightNumber);
        intent.putExtra("shipperCompanyId", shipperCompanyId);
        context.startActivityForResult(intent, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dynamic_details;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {

    }
}
