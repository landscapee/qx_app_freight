package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.BaggerListAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionSlideRecylerView;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 扫行李板车
 */
public class BaggageListActivity extends BaseActivity {

    @BindView(R.id.mfrv_receive_good)
    SlideRecyclerView mSlideRV;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    @BindView(R.id.btn_next)
    Button btnNext;

    private BaggerListAdapter mAdapter;
    private List<String> mList;
    private CustomToolbar toolbar;

    private String flightId;
    @Override
    public int getLayoutId() {
        return R.layout.activity_baggage_list;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        flightId = getIntent().getStringExtra("flightId");

        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, flightId);

        initView();
    }

    private void initView() {
        mSlideRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mList = new ArrayList<>();
        mAdapter = new BaggerListAdapter(mList);
        mAdapter.setOnDeleteClickListener(new BaggerListAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {

            }
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
        mSlideRV.setAdapter(mAdapter);


    }

    @OnClick({R.id.ll_add, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_add:
                ScanManagerActivity.startActivity(this);
                break;
            case R.id.btn_next:
                break;
        }
    }
}
