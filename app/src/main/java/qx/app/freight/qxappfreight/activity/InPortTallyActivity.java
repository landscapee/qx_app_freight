package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 进港理货页面
 */
public class InPortTallyActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_in_port_tally;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "上一步", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "进港理货");
    }
}
