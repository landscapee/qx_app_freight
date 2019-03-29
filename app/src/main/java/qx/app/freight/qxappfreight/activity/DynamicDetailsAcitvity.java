package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;

public class DynamicDetailsAcitvity extends BaseActivity {

    public static void startActivity(Activity context, int flightId) {
        Intent intent = new Intent(context, DynamicDetailsAcitvity.class);
        intent.putExtra("flightId", flightId);
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
