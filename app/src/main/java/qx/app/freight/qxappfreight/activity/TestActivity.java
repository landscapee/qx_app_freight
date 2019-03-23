package qx.app.freight.qxappfreight.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideLeftExecuteView;

public class TestActivity extends BaseActivity {


    @BindView(R.id.slide_left)
    SlideLeftExecuteView mSlideLeftExecuteView;
    @BindView(R.id.tv_flight_info)
    TextView tvFlightInfo;
    @BindView(R.id.sp_filight_num)
    Spinner mSpinner;
    @BindView(R.id.ll_spinner)
    LinearLayout llSpinner;

    private ArrayList<String> mFlightNumberList; //传过来的航班号列表
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
        mFlightNumberList = new ArrayList<>();
        mFlightNumberList.add("11111");
        mFlightNumberList.add("22222");
        mFlightNumberList.add("333333");
        mFlightNumberList.add("444444");
        toolbar.setRightTextViewImage(this, View.VISIBLE, R.color.flight_a, "新增", R.mipmap.new_2, v ->
                ToastUtil.showToast("新增"));

        mSlideLeftExecuteView.setLockListener(() -> {
            ToastUtil.showToast("执行任务");
        });
        mSlideLeftExecuteView.setLockListener(new SlideLeftExecuteView.OnLockListener() {
            @Override
            public void onOpenLockSuccess() {
            }
        });

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,R.layout.item_spinner_general, mFlightNumberList);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
