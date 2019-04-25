package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

public class SortingActivity extends BaseActivity {

    String flightNo = "";
    String arriveTime = "";
    String handCarNum = "";
    String getHandCarNumTotal = "";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_flight_no)
    TextView flightNoTv;
    @BindView(R.id.tv_arrive_time)
    TextView arriveTimeTv;
    @BindView(R.id.tv_handcar_num)
    TextView handCarNumTv;
    @BindView(R.id.tv_handcar)
    TextView handCarTotalTv;

    @BindView(R.id.btn_temp)
    Button tempBtn;
    @BindView(R.id.btn_done)
    Button doneBtn;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sorting;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        Intent intent = getIntent();
        //从前一个页面传递过来的数据
        flightNo = intent.getStringExtra("FLIGHT_NO");
        arriveTime = intent.getStringExtra("ARRIVE_TIME");
        handCarNum = intent.getStringExtra("HANDCAR_NUM");
        handCarNum = intent.getStringExtra("HANDCAR_NUM_TOTAL");
        //显示传递的数据
        flightNoTv.setText(flightNo);
        arriveTimeTv.setText(arriveTime);
        handCarNumTv.setText(handCarNum);
        handCarTotalTv.setText(getHandCarNumTotal);
        //toolbar
        CustomToolbar customToolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        customToolbar.setMainTitle(R.color.white, "进港理货");
        customToolbar.setLeftTextView(View.VISIBLE, R.color.white, "上一步", null);
        //右侧添加按钮
        customToolbar.setRightIconView(View.VISIBLE, R.mipmap.add_bg, listener -> {
            //跳转到 ->新增页面
            startActivityForResult(new Intent(this, SortingAddActivity.class), 1);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //来自新增页面的结果返回
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {


        }
    }
}
