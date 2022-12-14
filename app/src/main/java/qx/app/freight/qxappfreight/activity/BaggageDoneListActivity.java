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
import qx.app.freight.qxappfreight.bean.ScooterMapSingle;
import qx.app.freight.qxappfreight.bean.UnloadScooterListEntity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.CargoReportHisBean;
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
 * ???????????????
 */
public class BaggageDoneListActivity extends BaseActivity implements BaggageAreaSubContract.baggageAreaSubView, GetAllRemoteAreaContract.getAllRemoteAreaView, ScanScooterCheckUsedContract.ScanScooterCheckView {

    @BindView(R.id.mfrv_receive_good)
    SlideRecyclerView mSlideRV;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    @BindView(R.id.btn_next)
    Button btnNext;

    private BaggerListAdapter mAdapter;
    private List<TransportTodoListBean> mList = new ArrayList<>();
    private List<TransportTodoListBean> uploadList = new ArrayList<>();
    private CustomToolbar toolbar;

    private List<String> mAbnormalList; //???????????????

    TransportTodoListBean flightBean;
    CargoReportHisBean mData;
    private int flag = 0;
    private String mScooterCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_baggage_list;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mData = (CargoReportHisBean) intent.getSerializableExtra("flightBean");
        mAbnormalList = new ArrayList<>();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, mData.getFlightNo());
        if (mData.getMainInfos()!=null)
            mList.addAll(mData.getMainInfos());
        for (TransportTodoListBean transportTodoListBean:mList){
            transportTodoListBean.setNotCanDelete(true);
        }
        flightBean = mData.getMainInfos().get(0);
        initView();
    }

    private void initView() {
        mPresenter = new GetAllRemoteAreaPresenter(this);
        ((GetAllRemoteAreaPresenter) mPresenter).getAllRemoteArea();
//        mPresenter = new BaggageAreaSubPresenter(this);
        mSlideRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //????????????????????????
        if (flightBean!=null&&flightBean.getFlightNo() !=null){
            if (ScooterMapSingle.getInstance().get(flightBean.getFlightNo())!=null){
                if (ScooterMapSingle.getInstance().get(flightBean.getFlightNo()).getTransportTodoListBeans()!=null){
                    mList.addAll(ScooterMapSingle.getInstance().get(flightBean.getFlightNo()).getTransportTodoListBeans());
                }
            }
        }
        mAdapter = new BaggerListAdapter(mList);
        mAdapter.setOnDeleteClickListener(new BaggerListAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                if (mList.get(position).isNotCanDelete()){
                    ToastUtil.showToast("??????????????????????????????");
                    return;
                }
                mSlideRV.closeMenu();
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
        mSlideRV.setAdapter(mAdapter);
        //????????? ?????? ????????????
        setIsBack(true,()->{
            UnloadScooterListEntity unloadScooterListEntity = new UnloadScooterListEntity();
            for (TransportTodoListBean transportTodoListBean:mList){
                if (!transportTodoListBean.isNotCanDelete())
                    uploadList.add(transportTodoListBean);
            }
            unloadScooterListEntity.setTransportTodoListBeans(uploadList);
            ScooterMapSingle.getInstance().put(flightBean.getFlightNo(),unloadScooterListEntity);
            finish();
        });

    }

    @OnClick({R.id.ll_add, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_add:
                mSlideRV.closeMenu();
                flag = 0;
                ScanManagerActivity.startActivity(this,"BaggageDoneListActivity");
                break;
            case R.id.btn_next:
                mSlideRV.closeMenu();
                flag = 1;
                ScanManagerActivity.startActivity(this,"BaggageDoneListActivity_done");
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
            bean.setTpFlightLocate(flightBean.getTpFlightLocate());
            bean.setTpFlightTime(flightBean.getTpFlightTime());
            bean.setFlightInfoId(flightBean.getId());
            bean.setAsFlightId(flightBean.getAsFlightId());
            bean.setTpFlightType(flightBean.getTpFlightType());
/**
 *???????????? ????????????
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
                dialog.setData(this, "????????????????????????","??????","??????", isCheckRight -> {
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
            ToastUtil.showToast("???????????????");
        }
    }

    @Override
    public void baggageAreaSubResult(String result) {
        ToastUtil.showToast("????????????");
        finish();
    }

    @Override
    public void lookLUggageScannigFlightResult(String result) {
        ToastUtil.showToast("??????????????????");
    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {
        showProgessDialog("?????????????????????");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.SCAN_RESULT == resultCode) {
            mScooterCode = data.getStringExtra(Constants.SACN_DATA);
            if (flag == 1) {
                for (String item : mAbnormalList) {
                    if (mScooterCode.equals(item)) {
                        submitScooter(mScooterCode);
                        return;
                    }
                }
                ToastUtil.showToast("??????????????????");

            } else {
//                isIncludeScooterCode(mScooterCode);
                checkScooterCode(mScooterCode);
            }

        } else {
            Log.e("resultCode", "??????????????????200");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (result.getFunctionFlag().equals("BaggageDoneListActivity")) {
            if (result.getData() != null && result.getData().length() == Constants.SCOOTER_NO_LENGTH) {
                //?????????
                mScooterCode = result.getData();
                if (!"".equals(mScooterCode)) {
//                    isIncludeScooterCode(mScooterCode);
                    checkScooterCode(mScooterCode);
                } else {
                    ToastUtil.showToast("?????????????????????????????????");
                }
            } else {
                ToastUtil.showToast("????????????????????????????????????");
            }
        }
        if (result.getFunctionFlag().equals("BaggageDoneListActivity_done")) {
            if (flag == 1) {
                if ( result.getData()!=null){
                    for (String item : mAbnormalList) {
                        if (result.getData().equals(item)) {
                            submitScooter(result.getData());
                            return;
                        }
                    }
                    ToastUtil.showToast("??????????????????");
                }
            }
        }


    }

    /**
     * ?????????????????????
     * <p>
     * ?????????????????????
     */
    private void bindingUser() {
        if (mList.size() == 1) {
            if (TextUtils.isEmpty(flightBean.getTpOperator())) {
                BaseFilterEntity entity = new BaseFilterEntity();
                entity.setUserId(UserInfoSingle.getInstance().getUserId());
                entity.setFlightId(flightBean.getFlightId());
                mPresenter = new BaggageAreaSubPresenter(this);
                ((BaggageAreaSubPresenter) mPresenter).lookLUggageScannigFlight(entity);
            }
        }

    }

    /**
     * ????????????
     *
     * @param mScooterCode ?????????
     */
    private void isIncludeScooterCode(String mScooterCode) {
        if (mList.size() > 0) {
            for (TransportTodoListBean item : mList) {
                if (item.getTpScooterCode().equals(mScooterCode)) {
                    ToastUtil.showToast("???????????????");
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

    //????????????????????????????????????
    private void submitScooter(String turntableId) {
        for (TransportTodoListBean transportTodoListBean:mList){
            if (!transportTodoListBean.isNotCanDelete())
                uploadList.add(transportTodoListBean);
        }

        if (uploadList.size() == 0){
            ToastUtil.showToast("????????????????????????");
            return;
        }
        for (TransportTodoListBean item : uploadList) {
            item.setScSubCategory(1);
            item.setBaggageTurntable(turntableId);
            item.setBaggageSubOperator(UserInfoSingle.getInstance().getUserId());
            item.setBaggageSubTerminal(UserInfoSingle.getInstance().getUsername());
            item.setBaggageSubUserName(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
        }
//
//        BaseFilterEntity entity = new BaseFilterEntity();
//        entity.setFilter(mList);
//        List<TransportTodoListBean> mList2 = new ArrayList<>();
        Intent intent = new Intent(this, BaggageListConfirmActivity.class)
                .putExtra("listBean", (Serializable) uploadList)
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
     * ???????????????????????????
     *
     * @param scooterCode
     */
    private void checkScooterCode(String scooterCode) {
        mPresenter = new ScanScooterCheckUsedPresenter(this);
        ((ScanScooterCheckUsedPresenter) mPresenter).checkScooterCode(scooterCode,flightBean.getFlightId(),Constants.SCAN_XINGLI);
    }

    @Override
    public void checkScooterCodeResult(BaseEntity<Object> result) {
        if ("200".equals(result.getStatus())) {
            isIncludeScooterCode(mScooterCode);
        } else {
            ToastUtil.showToast("????????????????????????????????????");
        }
    }
}
