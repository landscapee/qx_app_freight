package qx.app.freight.qxappfreight.activity;

import android.os.Bundle;
import android.view.View;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

public class SortingAddActivity extends BaseActivity {

    CustomToolbar customToolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sorting_add;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        customToolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        customToolbar.setMainTitle(R.color.white, "新增");
        customToolbar.setLeftTextView(View.VISIBLE, R.color.white, "返回", null);
    }
}
