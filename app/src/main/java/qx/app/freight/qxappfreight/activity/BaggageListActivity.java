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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.BaggerListAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.FlightLuggageBean;
import qx.app.freight.qxappfreight.bean.response.GetAllRemoteAreaBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.BaggageAreaSubContract;
import qx.app.freight.qxappfreight.contract.GetAllRemoteAreaContract;
import qx.app.freight.qxappfreight.contract.ScanScooterCheckUsedContract;
import qx.app.freight.qxappfreight.dialog.ChoseFlightTypeDialog;
import qx.app.freight.qxappfreight.presenter.BaggageAreaSubPresenter;
import qx.app.freight.qxappfreight.presenter.GetAllRemoteAreaPresenter;
import qx.app.freight.qxappfreight.presenter.ScanScooterCheckUsedPresenter;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 扫行李板车
 */
public class BaggageListActivity extends BaseActivity implements BaggageAreaSubContract.baggageAreaSubView, GetAllRemoteAreaContract.getAllRemoteAreaView, ScanScooterCheckUsedContract.ScanScooterCheckView {

    @BindView(R.id.mfrv_receive_good)
    SlideRecyclerView mSlideRV;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    @BindView(R.id.btn_next)
    Button btnNext;

    private BaggerListAdapter mAdapter;
    private List<TransportTodoListBean> mList;
    private CustomToolbar toolbar;

    private List<String> mAbnormalList; //行李区列表

    FlightLuggageBean flightBean;
    private int flag = 0;
    private String mScooterCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_baggage_list;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        Intent intent = getIntent();
        flightBean = (FlightLuggageBean) intent.getSerializableExtra("flightBean");
        mAbnormalList = new ArrayList<>();
        EventBus.getDefault().register(this);
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, flightBean.getFlightNo());

        initView();
    }

    private void initView() {
        mPresenter = new GetAllRemoteAreaPresenter(this);
        ((GetAllRemoteAreaPresenter) mPresenter).getAllRemoteArea();
//        mPresenter = new BaggageAreaSubPresenter(this);
        mSlideRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mList = new ArrayList<>();
        mAdapter = new BaggerListAdapter(mList);
        mAdapter.setOnDeleteClickListener(new BaggerListAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                mSlideRV.closeMenu();
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
                mSlideRV.closeMenu();
                flag = 0;
                ScanManagerActivity.startActivity(this,"BaggageListActivity");
                break;
            case R.id.btn_next:
                mSlideRV.closeMenu();
                flag = 1;
                ScanManagerActivity.startActivity(this,"BaggageListActivity_done");
                break;
        }
    }

    @Override
    public void scooterInfoListResult(List<ScooterInfoListBean> scooterInfoListBeans) {
        if (scooterInfoListBeans != null && scooterInfoListBeans.size() > 0) {
            TransportTodoListBean bean = new TransportTodoListBean();
            bean.setTpScooterCode(scooterInfoListBeans.get(0).getScooterCode());
            bean.setTpScooterType(scooterInfoListBeans.get(0).getScooterType() + "");
            bean.setHeadingFlag(scooterInfoListBeans.get(0).getHeadingFlag());

            bean.setFlightId(flightBean.getFlightId());
            bean.setFlightNo(flightBean.getFlightNo());
            bean.setTpFlightLocate(flightBean.getSeat());
            bean.setTpFlightTime(flightBean.getScheduleTime());
            bean.setFlightInfoId(flightBean.getId());
            bean.setAsFlightId(flightBean.getSuccessionId());
            bean.setTpFlightType(flightBean.getTpFlightType());


/**
 *屏蔽行李 信息补充
 */
//            BaggerInputDialog dialog = new BaggerInputDialog();
//            dialog.setData(this,bean,flightBean.getFlightIndicator());
//            dialog.setBaggerInoutListener(new BaggerInputDialog.OnBaggerInoutListener() {
//                @Override
//                public void onConfirm(TransportTodoListBean data) {
//                    mList.add(data);
//                    mAdapter.notifyDataSetChanged();
////                    bindingUser();
//                }
//            });
//            dialog.show(getSupportFragmentManager(),"321");
            if ("D".equals(flightBean.getFlightIndicator()) || "I".equals(flightBean.getFlightIndicator())) {
                bean.setFlightIndicator(flightBean.getFlightIndicator());
                mList.add(bean);
                mAdapter.notifyDataSetChanged();
            } else {
                ChoseFlightTypeDialog dialog = new ChoseFlightTypeDialog();
                dialog.setData(this, "请选择国际或国内","国际","国内", isCheckRight -> {
                    if (isCheckRight) {
                        bean.setFlightIndicator("D");
                    } else {
                        bean.setFlightIndicator("I");
                    }
                    mList.add(bean);
                    mAdapter.notifyDataSetChanged();
                });
                dialog.setCancelable(false);
                dialog.show(getSupportFragmentManager(), "111");

            }

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
        showProgessDialog("数据提交中……");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (result.getFunctionFlag().equals("BaggageListActivity")) {
            if (result.getData() != null ) {
                //板车号
                mScooterCode = result.getData();
                if (!"".equals(mScooterCode)) {
                    checkScooterCode(mScooterCode);
                } else {
                    ToastUtil.showToast("扫码数据为空请重新扫码");
                }
            } else {
                ToastUtil.showToast("请扫描或输入正确的板车号");
            }
        }
        if (result.getFunctionFlag().equals("BaggageListActivity_done")) {
            if (flag == 1) {
                if (result.getData()!=null){
                    for (String item : mAbnormalList) {
                        if (result.getData().equals(item)) {
                            submitScooter(result.getData());
                            return;
                        }
                    }
                    ToastUtil.showToast("无该行李转盘");
                }
            }
        }
    }

    /**
     * 操作者绑定航班
     * <p>
     * 暂时取消该功能
     */
    private void bindingUser() {
        if (mList.size() == 1) {
            if (TextUtils.isEmpty(flightBean.getLuggageScanningUser())) {
                BaseFilterEntity entity = new BaseFilterEntity();
                entity.setUserId(UserInfoSingle.getInstance().getUserId());
                entity.setFlightId(flightBean.getFlightId());
                mPresenter = new BaggageAreaSubPresenter(this);
                ((BaggageAreaSubPresenter) mPresenter).lookLUggageScannigFlight(entity);
            }
        }

    }

    /**
     * 添加板车
     *
     * @param mScooterCode 板车号
     */
    private void isIncludeScooterCode(String mScooterCode) {
        if (mList.size() > 0) {
            for (TransportTodoListBean item : mList) {
                if (item.getTpScooterCode().equals(mScooterCode)) {
                    ToastUtil.showToast("该板已扫！");
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
        mPresenter = new BaggageAreaSubPresenter(this);
        ((BaggageAreaSubPresenter) mPresenter).ScooterInfoList(entity);
    }

    //提交数据，现在改成下一步
    private void submitScooter(String turntableId) {
        if (mList.size() == 0){
            ToastUtil.showToast("请先扫码添加板车");
            return;
        }
        for (TransportTodoListBean item : mList) {
            item.setBaggageTurntable(turntableId);
            item.setBaggageSubOperator(UserInfoSingle.getInstance().getUserId());
            item.setBaggageSubTerminal(UserInfoSingle.getInstance().getUsername());
            item.setBaggageSubUserName(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
        }
//        BaseFilterEntity entity = new BaseFilterEntity();
//        entity.setFilter(mList);
//        List<TransportTodoListBean> mList2 = new ArrayList<>();
        Intent intent = new Intent(this, BaggageListConfirmActivity.class)
                .putExtra("listBean", (Serializable) mList)
                .putExtra("flightNo", flightBean.getFlightNo())
                .putExtra("turntableId", turntableId);
        startActivity(intent);

//        String json = JSON.toJSONString(mList);
//        ((BaggageAreaSubPresenter)mPresenter).baggageAreaSub(json);
    }

    @Override
    public void getAllRemoteAreaResult(List<GetAllRemoteAreaBean> getAllRemoteAreaBean) {

        for (GetAllRemoteAreaBean item : getAllRemoteAreaBean) {
            if (item.getAreaType() == Constants.BAGGAGE_AREA) {
                mAbnormalList.add(item.getAreaId());
            }
        }
    }

    /**
     * 检测板车号是否可用
     *
     * @param scooterCode
     */
    private void checkScooterCode(String scooterCode) {
        mPresenter = new ScanScooterCheckUsedPresenter(this);
        ((ScanScooterCheckUsedPresenter) mPresenter).checkScooterCode(scooterCode);
    }

    @Override
    public void checkScooterCodeResult(BaseEntity<Object> result) {
        if ("200".equals(result.getStatus())) {
            isIncludeScooterCode(mScooterCode);
        } else {
            ToastUtil.showToast("操作不合法，不能重复扫描");
        }
    }
}
