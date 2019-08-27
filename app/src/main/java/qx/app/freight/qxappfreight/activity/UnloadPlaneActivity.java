package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ScanInfoAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.request.UnLoadRequestEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.bean.response.UnLoadListBillBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.ArrivalDataSaveContract;
import qx.app.freight.qxappfreight.contract.GetUnLoadListBillContract;
import qx.app.freight.qxappfreight.contract.LoadAndUnloadTodoContract;
import qx.app.freight.qxappfreight.contract.ScanScooterCheckUsedContract;
import qx.app.freight.qxappfreight.contract.ScanScooterContract;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.dialog.ChoseFlightTypeDialog;
import qx.app.freight.qxappfreight.dialog.UnloadBillInfoDialog;
import qx.app.freight.qxappfreight.presenter.ArrivalDataSavePresenter;
import qx.app.freight.qxappfreight.presenter.GetUnLoadListBillPresenter;
import qx.app.freight.qxappfreight.presenter.LoadAndUnloadTodoPresenter;
import qx.app.freight.qxappfreight.presenter.ScanScooterCheckUsedPresenter;
import qx.app.freight.qxappfreight.presenter.ScanScooterPresenter;
import qx.app.freight.qxappfreight.presenter.ScooterInfoListPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.PushDataUtil;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 理货卸机页面
 */
public class UnloadPlaneActivity extends BaseActivity implements ScooterInfoListContract.scooterInfoListView, ArrivalDataSaveContract.arrivalDataSaveView, ScanScooterCheckUsedContract.ScanScooterCheckView, ScanScooterContract.scanScooterView, GetUnLoadListBillContract.IView, LoadAndUnloadTodoContract.loadAndUnloadTodoView {
    @BindView(R.id.tv_plane_info)
    TextView mTvPlaneInfo;//航班号
    @BindView(R.id.tv_flight_type)
    TextView mTvFlightType;//航班类型
    @BindView(R.id.ll_flight_info_container)
    LinearLayout mLlInfo;//航班信息容器
    @BindView(R.id.tv_seat)
    TextView mTvSeat;//航班机位数
    @BindView(R.id.tv_arrive_time)
    TextView mTvArriveTime;//航班到达时间
    @BindView(R.id.tv_board_goods_number)
    TextView mTvGoodsNumber;//航班货物数量
    @BindView(R.id.tv_look_unload_bill_info)
    TextView mTvUnloadBillInfo;//显示卸机单数据
    @BindView(R.id.iv_cotro_1)
    ImageView mIvControl1;//控制显示隐藏货物列表
    @BindView(R.id.srv_goods_info)
    SlideRecyclerView mSlideRvGoods;//航班货物信息列表
    @BindView(R.id.ll_add_scan_goods)
    LinearLayout mLlScanGoods;//扫描货物数据
    @BindView(R.id.ll_scan)
    LinearLayout mLlScan;//扫板容器控件
    @BindView(R.id.tv_scan_goods)
    TextView mTvScanGoods;//扫描货物控件文字
    @BindView(R.id.tv_board_pac_number)
    TextView mTvPacNumber;//航班行李数量
    @BindView(R.id.iv_cotrol_2)
    ImageView mIvControl2;//控制显示隐藏行李列表
    @BindView(R.id.srv_pac_info)
    SlideRecyclerView mSlideRvPac;//航班行李信息列表
    @BindView(R.id.ll_add_scan_pac)
    LinearLayout mLlScanPac;//扫描行李数据
    @BindView(R.id.tv_scan_pac)
    TextView mTvScanPac;//扫描行李控件文字
    @BindView(R.id.tv_error_report)
    TextView mTvErrorReport;//偏离上报
    @BindView(R.id.tv_end_unload)
    TextView mTvEndUnload;//结束卸机
    @BindView(R.id.iv_notice_tp)
    ImageView ivNoticeTp;
    private boolean mIsScanGoods = true;
    private List<ScooterInfoListBean> mListGoods = new ArrayList<>();
    private List<ScooterInfoListBean> mListPac = new ArrayList<>();
    private ScanInfoAdapter mScanGoodsAdapter;//扫描货物适配器
    private ScanInfoAdapter mScanPacAdapter;//扫描行李适配器
    private String mCurrentTaskId;
    private List<String> mTpScooterCodeList = new ArrayList<>();
    private LoadAndUnloadTodoBean mData;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        PushDataUtil.handlePushInfo(result, mCurrentTaskId, this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_unload_plane;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        mData = (LoadAndUnloadTodoBean) getIntent().getSerializableExtra("plane_info");
        if (mData.getWidthAirFlag() == 0) {
            mLlScan.setVisibility(View.GONE);
        } else {
            mLlScan.setVisibility(View.VISIBLE);
        }
        mCurrentTaskId = mData.getTaskId();
        toolbar.setMainTitle(Color.WHITE, mData.getFlightNo() + "  卸机");
        mTvPlaneInfo.setText(mData.getFlightNo());
        mTvFlightType.setText(mData.getAircraftno());
        String route = mData.getRoute();
        List<String> resultList = new ArrayList<>();
        if (route != null) {
            String[] placeArray = route.split(",");
            for (String str : placeArray) {
                String temp = str.replaceAll("[^(a-zA-Z\\u4e00-\\u9fa5)]", "");
                resultList.add(temp);
            }
        }
        FlightInfoLayout layout = new FlightInfoLayout(this, resultList);
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLlInfo.removeAllViews();
        mLlInfo.addView(layout, paramsMain);
        mTvSeat.setText(mData.getSeat());
        String time;
        if (!StringUtil.isTimeNull(String.valueOf(mData.getAta()))) {//有实际到达显示实际到达时间
            time = TimeUtils.getHMDay(mData.getAta());
        } else if (!StringUtil.isTimeNull(String.valueOf(mData.getEta()))) {//预计到达时间
            time = TimeUtils.getHMDay(mData.getEta());
        } else {                                                    //计划到达时间
            time = TimeUtils.getHMDay(mData.getSta());
        }
        mTvArriveTime.setText(time);
        String scanGoods = "请扫描添加  <font color='#4791E5'>货物</font>  板车";
        mTvScanGoods.setText(Html.fromHtml(scanGoods));
        String scanPac = "请扫描添加  <font color='#4791E5'>行李</font>  板车";
        mTvScanPac.setText(Html.fromHtml(scanPac));
        mSlideRvGoods.setLayoutManager(new LinearLayoutManager(this));
        mScanGoodsAdapter = new ScanInfoAdapter(mListGoods, mData);
        mSlideRvGoods.setAdapter(mScanGoodsAdapter);
        mScanGoodsAdapter.setOnDeleteClickListener((view, position) -> {
            if (mListGoods.get(position).isNoticeTransport()) {
                ToastUtil.showToast("该板车已通知运输，无法删除");
            } else {
                mTpScooterCodeList.remove(mListGoods.get(position).getScooterCode());
                mListGoods.remove(position);
                mSlideRvGoods.closeMenu();
                mTvGoodsNumber.setText(String.valueOf(mListGoods.size()));
                mScanGoodsAdapter.notifyDataSetChanged();
            }
        });
        mScanGoodsAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
        mSlideRvPac.setLayoutManager(new LinearLayoutManager(this));
        mScanPacAdapter = new ScanInfoAdapter(mListPac, mData);
        mSlideRvPac.setAdapter(mScanPacAdapter);
        mScanPacAdapter.setOnDeleteClickListener((view, position) -> {
            if (mListPac.get(position).isNoticeTransport()) {
                ToastUtil.showToast("该板车已通知运输，无法删除");
            } else {
                mTpScooterCodeList.remove(mListPac.get(position).getScooterCode());
                mListPac.remove(position);
                mSlideRvPac.closeMenu();
                mTvPacNumber.setText(String.valueOf(mListPac.size()));
                mScanPacAdapter.notifyDataSetChanged();
            }
        });
        mScanPacAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
        setListeners();
    }

    private void setListeners() {
        mTvUnloadBillInfo.setOnClickListener(v -> {
            mPresenter = new GetUnLoadListBillPresenter(this);
            UnLoadRequestEntity entity = new UnLoadRequestEntity();
            entity.setUnloadingUser(UserInfoSingle.getInstance().getUserId());
            entity.setFlightId(mData.getFlightId());
            String userName = UserInfoSingle.getInstance().getUsername();
            entity.setOperationUserName((userName.contains("-")) ? userName.substring(0, userName.indexOf("-")) : userName);
            ((GetUnLoadListBillPresenter) mPresenter).getUnLoadingList(entity);
        });
        mIvControl1.setOnClickListener(v -> {
            if (mSlideRvGoods.getVisibility() == View.GONE) {
                mSlideRvGoods.setVisibility(View.VISIBLE);
                mIvControl1.setImageResource(R.mipmap.up);
            } else {
                mSlideRvGoods.setVisibility(View.GONE);
                mIvControl1.setImageResource(R.mipmap.down);
            }
        });
        mIvControl2.setOnClickListener(v -> {
            if (mSlideRvPac.getVisibility() == View.GONE) {
                mSlideRvPac.setVisibility(View.VISIBLE);
                mIvControl2.setImageResource(R.mipmap.up);
            } else {
                mSlideRvPac.setVisibility(View.GONE);
                mIvControl2.setImageResource(R.mipmap.down);
            }
        });
        mLlScanGoods.setOnClickListener(v -> {
            mIsScanGoods = true;
            ScanManagerActivity.startActivity(UnloadPlaneActivity.this, "UnloadPlaneActivity");
        });
        mLlScanPac.setOnClickListener(v -> {
            mIsScanGoods = false;
            ScanManagerActivity.startActivity(UnloadPlaneActivity.this, "UnloadPlaneActivity");
        });
        mTvErrorReport.setOnClickListener(
                v -> {
                    Intent intent = new Intent(UnloadPlaneActivity.this, ErrorReportActivity.class);
                    intent.putExtra("flight_number", mData.getFlightNo());//航班号
                    intent.putExtra("task_id", mData.getTaskId());//任务id
                    intent.putExtra("flight_id", mData.getFlightId());//Flight id
                    intent.putExtra("error_type", 2);
                    intent.putExtra("area_id", mData.getSeat());//area_id
                    intent.putExtra("step_code", mData.getOperationStepObj().get(3).getOperationCode());//step_code
                    UnloadPlaneActivity.this.startActivity(intent);
                });
        mTvEndUnload.setOnClickListener(v -> {
            TransportEndEntity model = new TransportEndEntity();
            model.setTaskId(mData.getId());
            List<TransportTodoListBean> infos = new ArrayList<>();
            for (ScooterInfoListBean bean : mListGoods) {
                TransportTodoListBean entity = new TransportTodoListBean();
                entity.setTpScooterType(String.valueOf(bean.getScooterType()));
                entity.setFlightIndicator(bean.getFlightType());//添加板车 国际国内航班标记
                entity.setTpScooterCode(bean.getScooterCode());
                entity.setTpCargoType("cargo");
                entity.setFlightId(mData.getFlightId());
                entity.setFlightNo(mData.getFlightNo());
                entity.setTpFlightLocate(mData.getSeat());
                entity.setTpOperator(UserInfoSingle.getInstance().getUserId());
                entity.setDtoType(8);
                entity.setTpType(String.valueOf(mData.getMovement()));
                entity.setTaskId(mData.getTaskId());//代办数据中的id
                infos.add(entity);
            }
            for (ScooterInfoListBean bean : mListPac) {
                TransportTodoListBean entity = new TransportTodoListBean();
                entity.setTpScooterType(String.valueOf(bean.getScooterType()));
                entity.setFlightIndicator(bean.getFlightType());//添加板车 国际国内航班标记
                entity.setTpScooterCode(bean.getScooterCode());
                entity.setTpCargoType("baggage");
                entity.setFlightId(mData.getFlightId());
                entity.setFlightNo(mData.getFlightNo());
                entity.setTpFlightLocate(mData.getSeat());
                entity.setTpOperator(UserInfoSingle.getInstance().getUserId());
                entity.setDtoType(8);
                entity.setTpType(String.valueOf(mData.getMovement()));
                entity.setTaskId(mData.getTaskId());//代办数据中的id
                infos.add(entity);
            }
//            if (infos.size() == 0) {
//                ToastUtil.showToast("请选择上传板车信息再提交");
//            } else {
            model.setSeat(mData.getSeat());
            model.setScooters(infos);
            mPresenter = new ArrivalDataSavePresenter(this);
            ((ArrivalDataSavePresenter) mPresenter).arrivalDataSave(model);
//            }
        });
        ivNoticeTp.setOnClickListener(v -> {
            noticeTp();
        });
    }

    /**
     * 通知开始运输
     */
    private void noticeTp() {
        TransportEndEntity model = new TransportEndEntity();
        model.setTaskId(mData.getTaskId());
        Log.e("tag", "卸机id======" + mData.getId());
        List<TransportTodoListBean> infos = new ArrayList<>();
        for (ScooterInfoListBean bean : mListGoods) {
            bean.setNoticeTransport(true);
            TransportTodoListBean entity = new TransportTodoListBean();
            entity.setTpScooterType(String.valueOf(bean.getScooterType()));
            entity.setTpScooterCode(bean.getScooterCode());
            entity.setFlightIndicator(bean.getFlightType());
            entity.setTpCargoType("cargo");
            entity.setFlightId(mData.getFlightId() + "");
            entity.setFlightNo(mData.getFlightNo());
            entity.setTpFlightLocate(mData.getSeat());
            entity.setTpStartLocate("seat");
            entity.setBeginAreaId(mData.getSeat());
//            entity.setTpOperator(UserInfoSingle.getInstance().getUserId()); //不需要锁定板车 所以不传userId
            entity.setDtoType(9);
            entity.setTpType("进");
            entity.setTpState(0);
            entity.setTaskId(mData.getTaskId());//代办数据中的id
            infos.add(entity);
        }
        for (ScooterInfoListBean bean : mListPac) {
            bean.setNoticeTransport(true);
            TransportTodoListBean entity = new TransportTodoListBean();
            entity.setTpScooterType(String.valueOf(bean.getScooterType()));
            entity.setTpScooterCode(bean.getScooterCode());
            entity.setFlightIndicator(bean.getFlightType());
            entity.setTpCargoType("baggage");
            entity.setFlightId(mData.getFlightId() + "");
            entity.setFlightNo(mData.getFlightNo());
            entity.setTpFlightLocate(mData.getSeat());
            entity.setTpStartLocate("seat");
            entity.setBeginAreaId(mData.getSeat());
//            entity.setTpOperator(UserInfoSingle.getInstance().getUserId()); //不需要锁定板车 所以不传userId
            entity.setDtoType(9);
            entity.setTpType("进");
            entity.setTpState(0);
            entity.setTaskId(mData.getTaskId());//代办数据中的id
            infos.add(entity);
        }
        model.setScooters(infos);
        model.setTaskType("2");
        if (model.getScooters().size() == 0) {
            ToastUtil.showToast("请选择上传板车信息再提交");
        } else {
            mPresenter = new ScanScooterPresenter(this);
            ((ScanScooterPresenter) mPresenter).scanLockScooter(model);
        }
    }

    private String mNowScooterCode;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if ("UnloadPlaneActivity".equals(result.getFunctionFlag())) {
            if (result.getData() != null && result.getData().length() == Constants.SCOOTER_NO_LENGTH) {
                //根据扫一扫获取的板车信息查找板车内容
                if (!mTpScooterCodeList.contains(result.getData())) {
                    mNowScooterCode = result.getData();
                    mPresenter = new ScanScooterCheckUsedPresenter(this);
                    ((ScanScooterCheckUsedPresenter) mPresenter).checkScooterCode(mNowScooterCode);
                } else {
                    ToastUtil.showToast("操作不合法，不能重复扫描");
                }
            } else {
                ToastUtil.showToast("请扫描或输入正确的板车号");
            }
        }
    }

    private void addScooterInfo(String scooterCode) {
        Log.e("tagCode", "scooterCode========" + scooterCode);
        if (!"".equals(scooterCode)) {
            mPresenter = new ScooterInfoListPresenter(this);
            BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
            MyAgentListBean myAgentListBean = new MyAgentListBean();
            baseFilterEntity.setSize(10);
            baseFilterEntity.setCurrent(1);
            myAgentListBean.setScooterCode(scooterCode);
            baseFilterEntity.setFilter(myAgentListBean);
            ((ScooterInfoListPresenter) mPresenter).ScooterInfoList(baseFilterEntity);
        } else
            ToastUtil.showToast(this, "扫描结果为空请重新扫描");
    }

    @Override
    public void scooterInfoListResult(List<ScooterInfoListBean> result) {
        if (result.size() == 0) {
            ToastUtil.showToast("板车扫描错误，请检查");
        } else {
            String flightType = getIntent().getStringExtra("flight_type");
            if ("D".equals(flightType) || "I".equals(flightType)) {
                for (ScooterInfoListBean bean : result) {
                    bean.setFlightType(flightType);
                }
                showBoardInfos(result);
            } else {
                ChoseFlightTypeDialog dialog = new ChoseFlightTypeDialog();
                dialog.setData(this, isLocal -> {
                    if (isLocal) {
                        for (ScooterInfoListBean bean : result) {
                            bean.setFlightType("D");
                        }
                    } else {
                        for (ScooterInfoListBean bean : result) {
                            bean.setFlightType("I");
                        }
                    }
                    showBoardInfos(result);
                });
                dialog.setCancelable(false);
                dialog.show(getSupportFragmentManager(), "111");
            }
        }
    }

    @Override
    public void scooterInfoListForReceiveResult(List<ScooterInfoListBean> scooterInfoListBeans) {

    }

    /**
     * 显示最后生成的板车信息列表
     *
     * @param result 板车信息
     */
    private void showBoardInfos(List<ScooterInfoListBean> result) {
        for (ScooterInfoListBean entity : result) {
            mTpScooterCodeList.add(entity.getScooterCode());
        }
        if (mIsScanGoods) {
            mSlideRvGoods.setVisibility(View.VISIBLE);
            mListGoods.addAll(result);
            mIvControl1.setImageResource(R.mipmap.up);
            mTvGoodsNumber.setText(String.valueOf(mListGoods.size()));
            mScanGoodsAdapter.notifyDataSetChanged();
        } else {
            mSlideRvPac.setVisibility(View.VISIBLE);
            mListPac.addAll(result);
            mIvControl2.setImageResource(R.mipmap.up);
            mTvPacNumber.setText(String.valueOf(mListPac.size()));
            mScanPacAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void existResult(MyAgentListBean result) {

    }

    @Override
    public void addInfoResult(MyAgentListBean result) {

    }

    @Override
    public void toastView(String error) {
        Log.e("tagError", "error========" + error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("请求中......");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void arrivalDataSaveResult(String result) {
        PerformTaskStepsEntity entity = new PerformTaskStepsEntity();
        entity.setType(1);
        entity.setLoadUnloadDataId(mData.getId());
        entity.setFlightId(Long.valueOf(mData.getFlightId()));
        entity.setFlightTaskId(mData.getTaskId());
        entity.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude());
        entity.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude());
        entity.setOperationCode("FreightUnloadFinish");
        entity.setTerminalId(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        entity.setUserName(mData.getWorkerName());
        entity.setCreateTime(System.currentTimeMillis());
        mPresenter = new LoadAndUnloadTodoPresenter(this);
        ((LoadAndUnloadTodoPresenter) mPresenter).slideTask(entity);
    }

    @Override
    public void checkScooterCodeResult(BaseEntity<Object> result) {
        if ("200".equals(result.getStatus())) {
            addScooterInfo(mNowScooterCode);
        } else {
            ToastUtil.showToast("操作不合法，不能重复扫描");
        }
    }

    @Override
    public void scanScooterResult(String result) {

    }

    @Override
    public void scanLockScooterResult(String result) {
        ToastUtil.showToast("已通知运输");
    }

    @Override
    public void scooterWithUserResult(List<TransportTodoListBean> result) {

    }

    @Override
    public void scooterWithUserTaskResult(List<TransportTodoListBean> result) {

    }

    @Override
    public void getUnLoadingListResult(UnLoadListBillBean result) {
        if (result != null) {
            if (result.getData() != null) {
                List<UnLoadListBillBean.DataBean.ContentObjectBean> list = result.getData().getContentObject();
                UnloadBillInfoDialog unloadBillInfoDialog = new UnloadBillInfoDialog();
                unloadBillInfoDialog.setData(list, this);
                unloadBillInfoDialog.show(getSupportFragmentManager(), "unload_bill");
            } else {
                ToastUtil.showToast(result.getMessage());
            }
        }
    }

    @Override
    public void loadAndUnloadTodoResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {

    }

    @Override
    public void slideTaskResult(String result) {
        ToastUtil.showToast("结束卸机成功");
        EventBus.getDefault().post("InstallEquipFragment_refresh" + "@" + mCurrentTaskId);
        finish();
    }

    @Override
    public void startClearTaskResult(String result) {

    }

}
