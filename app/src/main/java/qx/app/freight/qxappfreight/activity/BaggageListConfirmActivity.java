package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.BaggerListAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScooterMapSingle;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.BaggageAreaSubContract;
import qx.app.freight.qxappfreight.presenter.BaggageAreaSubPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

public class BaggageListConfirmActivity extends BaseActivity implements BaggageAreaSubContract.baggageAreaSubView {

    @BindView(R.id.tv_turntable_num)
    TextView tvTurntableNum;
    @BindView(R.id.mfrv_receive_good)
    SlideRecyclerView mSlideRV;
    @BindView(R.id.btn_next)
    Button btnNext;

    private BaggerListAdapter mAdapter;
    private List<TransportTodoListBean> mList;
    private CustomToolbar toolbar;
    private String flightNo;
    private String turntableId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_baggage_list_confirm;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        mList = (List<TransportTodoListBean>)getIntent().getSerializableExtra("listBean");
        flightNo = getIntent().getStringExtra("flightNo");
        turntableId = getIntent().getStringExtra("turntableId");

        tvTurntableNum.setText("行李转盘"+turntableId);
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE,flightNo);
        initView();

    }

    private void initView() {
        mPresenter = new BaggageAreaSubPresenter(this);
        mSlideRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        mList = new ArrayList<>();
        mAdapter = new BaggerListAdapter(mList);
//        mAdapter.setOnDeleteClickListener(new BaggerListAdapter.OnDeleteClickLister() {
//            @Override
//            public void onDeleteClick(View view, int position) {
//                mList.remove(position);
//                mAdapter.notifyDataSetChanged();
//            }
//        });
//        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//        });
        mSlideRV.setAdapter(mAdapter);

    }


    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        Gson mGson = new Gson();
        String json = mGson.toJson(mList);
        ((BaggageAreaSubPresenter)mPresenter).baggageAreaSub(json);
    }


    @Override
    public void scooterInfoListResult(List<ScooterInfoListBean> scooterInfoListBeans) {

    }

    @Override
    public void baggageAreaSubResult(String result) {
        ScooterMapSingle.getInstance().put(flightNo,null);
        ToastUtil.showToast("提交成功");
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    public void lookLUggageScannigFlightResult(String result) {

    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {
        showProgessDialog("");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }


}
