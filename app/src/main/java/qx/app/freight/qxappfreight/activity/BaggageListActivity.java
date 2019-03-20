package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.BaggerListAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.FlightLuggageBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.BaggageAreaSubContract;
import qx.app.freight.qxappfreight.presenter.BaggageAreaSubPresenter;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionSlideRecylerView;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 扫行李板车
 */
public class BaggageListActivity extends BaseActivity implements BaggageAreaSubContract.baggageAreaSubView {

    @BindView(R.id.mfrv_receive_good)
    SlideRecyclerView mSlideRV;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    @BindView(R.id.btn_next)
    Button btnNext;

    private BaggerListAdapter mAdapter;
    private List<TransportTodoListBean> mList;
    private CustomToolbar toolbar;

    FlightLuggageBean flightBean;
    private int flag = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_baggage_list;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        Intent intent =  getIntent();
        flightBean =(FlightLuggageBean)intent.getSerializableExtra("flightBean");

        EventBus.getDefault().register(this);
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, flightBean.getFlightNo());

        initView();
    }

    private void initView() {
        mPresenter = new BaggageAreaSubPresenter(this);
        mSlideRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mList = new ArrayList<>();
        mAdapter = new BaggerListAdapter(mList);
        mAdapter.setOnDeleteClickListener(new BaggerListAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
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
                flag =0;
                ScanManagerActivity.startActivity(this);
                break;
            case R.id.btn_next:
                flag =1;
                ScanManagerActivity.startActivity(this);
                break;
        }
    }

    @Override
    public void scooterInfoListResult(List<ScooterInfoListBean> scooterInfoListBeans) {
        if (scooterInfoListBeans != null && scooterInfoListBeans.size() > 0) {
            TransportTodoListBean bean = new TransportTodoListBean();
            bean.setTpScooterCode(scooterInfoListBeans.get(0).getScooterCode());
            bean.setTpScooterType(scooterInfoListBeans.get(0).getScooterType() + "");

            bean.setTpFlightId(flightBean.getFlightId());
            bean.setTpFlightNumber(flightBean.getFlightNo());
            bean.setTpFlightLocate(flightBean.getSeat());
            bean.setTpFlightTime(flightBean.getScheduleTime());
            bean.setTpFlightBusId(flightBean.getId());
            bean.setTpAsFlightId(flightBean.getSuccessionId());
            bean.setTpFlightType(flightBean.getTpFlightType());
            mList.add(bean);
            mAdapter.notifyDataSetChanged();
            bindingUser();
        } else {
            ToastUtil.showToast("无该板信息");
        }
    }

    @Override
    public void baggageAreaSubResult(String result) {
        ToastUtil.showToast("提交成功");
        finish();
    }

    @Override
    public void lookLUggageScannigFlightResult(String result) {
        ToastUtil.showToast("绑定航班成功");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.SCAN_RESULT == resultCode) {
            String mScooterCode = data.getStringExtra(Constants.SACN_DATA);
            if (flag ==1){
                submitScooter(mScooterCode);

            }else {
                isIncludeScooterCode(mScooterCode);

            }

        } else {
            Log.e("resultCode", "收货页面不是200");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (result.getFunctionFlag().equals("BaggageListActivity")) {
            //板车号
            String mScooterCode = result.getData();
            if (!"".equals(mScooterCode)) {
                isIncludeScooterCode(mScooterCode);
            } else {
                ToastUtil.showToast("扫码数据为空请重新扫码");
            }
        } else {
            Log.e("resultCode", "收货页面不是200");
        }
    }

    private void bindingUser(){
        if (mList.size()==1){
            if (TextUtils.isEmpty(flightBean.getLuggageScanningUser())){
                BaseFilterEntity entity = new BaseFilterEntity();
                entity.setUserId(UserInfoSingle.getInstance().getUserId());
                entity.setFlightId(flightBean.getFlightId());
                ((BaggageAreaSubPresenter)mPresenter).lookLUggageScannigFlight(entity);
            }
        }

    }

    /**添加板车
     *
     * @param mScooterCode  板车号
     */
    private void isIncludeScooterCode(String mScooterCode) {
        if (mList.size() > 0) {
            for (TransportTodoListBean item : mList) {
                if (item.getTpScooterCode().equals(mScooterCode)) {
                    ToastUtil.showToast("改板已扫！");
                    return;
                }
            }
        }
        BaseFilterEntity entity = new BaseFilterEntity();
        MyAgentListBean myAgentListBean = new MyAgentListBean();
        myAgentListBean.setScooterCode(mScooterCode);
        entity.setCurrent(1);
        entity.setSize(10);
        entity.setFilter(myAgentListBean);
        ((BaggageAreaSubPresenter)mPresenter).ScooterInfoList(entity);
    }

    private void submitScooter(String turntableId){

        for (TransportTodoListBean item:mList) {
            item.setBaggageTurntable(turntableId);
            item.setBaggageSubOperator(UserInfoSingle.getInstance().getUserId());
            item.setBaggageSubTerminal(UserInfoSingle.getInstance().getUsername());
            item.setBaggageSubUserName(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
        }
//
//        BaseFilterEntity entity = new BaseFilterEntity();
//        entity.setFilter(mList);
//        List<TransportTodoListBean> mList2 = new ArrayList<>();
        String json = JSON.toJSONString(mList);


        ((BaggageAreaSubPresenter)mPresenter).baggageAreaSub(json);
    }
}
