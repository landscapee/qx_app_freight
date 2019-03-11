package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideLeftExecuteView;

public class TestActivity extends BaseActivity {


    @BindView(R.id.slide_left)
    SlideLeftExecuteView mSlideLeftExecuteView;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, TestActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_test;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "test");

        toolbar.setRightTextViewImage(this,View.VISIBLE, R.color.flight_a, "新增", R.mipmap.new_2, v ->
                ToastUtil.showToast(TestActivity.this,"新增"));

        mSlideLeftExecuteView.setLockListener(() -> {
            ToastUtil.showToast(TestActivity.this,"执行任务");
        });

    }

}
