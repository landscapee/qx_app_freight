package qx.app.freight.qxappfreight.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
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
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.TpScanInfoAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.ScooterMapSingle;
import qx.app.freight.qxappfreight.bean.UnloadScooterListEntity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PerformTaskStepsEntity;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.request.UnLoadRequestEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
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
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * ??????????????????
 */
public class TPUnloadPlaneActivity extends BaseActivity implements ScooterInfoListContract.scooterInfoListView, ArrivalDataSaveContract.arrivalDataSaveView, ScanScooterCheckUsedContract.ScanScooterCheckView, LoadAndUnloadTodoContract.loadAndUnloadTodoView, ScanScooterContract.scanScooterView, GetUnLoadListBillContract.IView {
    @BindView(R.id.tv_plane_info)
    TextView mTvPlaneInfo;//?????????
    @BindView(R.id.tv_flight_type)
    TextView mTvFlightType;//????????????
    @BindView(R.id.ll_flight_info_container)
    LinearLayout mLlInfo;//??????????????????
    @BindView(R.id.ll_scan_goods)
    LinearLayout mLlScanGoodsContainer;//????????????????????????
    @BindView(R.id.ll_scan_baggage)
    LinearLayout mLlScanBaggageContainer;//????????????????????????
    TextView mTvTargetPlace;//????????????
    @BindView(R.id.tv_seat)
    TextView mTvSeat;//???????????????
    @BindView(R.id.tv_arrive_time)
    TextView mTvArriveTime;//??????????????????
    @BindView(R.id.tv_board_goods_number)
    TextView mTvGoodsNumber;//??????????????????
    @BindView(R.id.iv_cotro_1)
    ImageView mIvControl1;//??????????????????????????????
    @BindView(R.id.srv_goods_info)
    SlideRecyclerView mSlideRvGoods;//????????????????????????
    @BindView(R.id.ll_add_scan_goods)
    LinearLayout mLlScanGoods;//??????????????????
    @BindView(R.id.tv_scan_goods)
    TextView mTvScanGoods;//????????????????????????
    @BindView(R.id.tv_board_pac_number)
    TextView mTvPacNumber;//??????????????????
    @BindView(R.id.iv_cotrol_2)
    ImageView mIvControl2;//??????????????????????????????
    @BindView(R.id.srv_pac_info)
    SlideRecyclerView mSlideRvPac;//????????????????????????
    @BindView(R.id.ll_add_scan_pac)
    LinearLayout mLlScanPac;//??????????????????
    @BindView(R.id.tv_scan_pac)
    TextView mTvScanPac;//????????????????????????
    @BindView(R.id.tv_error_report)
    TextView mTvErrorReport;//????????????
    @BindView(R.id.tv_end_unload)
    TextView mTvEndUnload;//????????????
    @BindView(R.id.iv_notice_tp)
    ImageView ivNoticeTp;
    @BindView(R.id.tv_look_unload_bill_info)
    TextView mTvUnloadBillInfo;//?????????????????????

    private boolean mIsScanGoods = true;
    private List<ScooterInfoListBean> mListGoods = new ArrayList<>();
    private List<ScooterInfoListBean> mListPac = new ArrayList<>();

    private List<ScooterInfoListBean> mListTempAlreadyNotify = new ArrayList<>();

    private TpScanInfoAdapter mScanGoodsAdapter;//?????????????????????
    private TpScanInfoAdapter mScanPacAdapter;//?????????????????????
    //    private String[] mInfo;
    private OutFieldTaskBean mOutFieldTaskBean;// ?????????????????????????????? ??? ????????????
    private String mCurrentTaskId;
    private List<String> mTpScooterCodeList = new ArrayList<>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        if (result != null) {
            if (result.isCancelFlag()) {
                String taskId = result.getTaskId();
                if (taskId.equals(mCurrentTaskId)) {
                    ToastUtil.showToast("???????????????????????????");
                    Observable.timer(2, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()) // timer ???????????????????????????????????????????????????
                            .subscribe(aLong -> finish());
                }
            }
        }
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
//        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
//        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "??????", v -> finish());
        mOutFieldTaskBean = (OutFieldTaskBean) getIntent().getSerializableExtra("plane_info");
        mCurrentTaskId = mOutFieldTaskBean.getTaskId();

        toolbar.setMainTitle(Color.WHITE, mOutFieldTaskBean.getFlights().getFlightNo() + "  ??????");
        mTvPlaneInfo.setText(mOutFieldTaskBean.getFlights().getFlightNo());
        mTvFlightType.setText(mOutFieldTaskBean.getFlights().getAircraftNo());
        List<String> routes = mOutFieldTaskBean.getFlights().getRoute();
        FlightInfoLayout layout = new FlightInfoLayout(this, routes);
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLlInfo.removeAllViews();
        mLlInfo.addView(layout, paramsMain);
        mTvSeat.setText(mOutFieldTaskBean.getFlights().getSeat());
        long arrive;
        //??????????????????
        //??????????????????
        //??????????????????
        //??????????????????
        if (!TextUtils.isEmpty(mOutFieldTaskBean.getFlights().getActualTime() + "") && !"0".equals(mOutFieldTaskBean.getFlights().getActualTime() + "")) {//?????????????????????
            arrive = Long.valueOf(mOutFieldTaskBean.getFlights().getActualTime() + "");
        } else {
            arrive = Long.valueOf(mOutFieldTaskBean.getFlights().getScheduleTime() + "");
        }
        mTvArriveTime.setText(TimeUtils.getHMDay(arrive));
        String scanGoods = "???????????????  <font color='#4791E5'>??????</font>  ??????";
        mTvScanGoods.setText(Html.fromHtml(scanGoods));
        String scanPac = "???????????????  <font color='#4791E5'>??????</font>  ??????";
        mTvScanPac.setText(Html.fromHtml(scanPac));
        mSlideRvGoods.setLayoutManager(new LinearLayoutManager(this));
        mScanGoodsAdapter = new TpScanInfoAdapter(mListGoods, mOutFieldTaskBean.getFlights());
        mSlideRvGoods.setAdapter(mScanGoodsAdapter);
        mScanGoodsAdapter.setOnDeleteClickListener((view, position) -> {
            if (mListGoods.get(position).isNoticeTransport()) {
                ToastUtil.showToast("???????????????????????????????????????");
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
        mScanPacAdapter = new TpScanInfoAdapter(mListPac, mOutFieldTaskBean.getFlights());
        mSlideRvPac.setAdapter(mScanPacAdapter);
        mScanPacAdapter.setOnDeleteClickListener((view, position) -> {
            if (mListPac.get(position).isNoticeTransport()) {
                ToastUtil.showToast("???????????????????????????????????????");
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

        ivNoticeTp.setOnClickListener(v -> {
            showYesOrNoDialog("","????????????????????????????????????????????????????",1);

        });
        mTvUnloadBillInfo.setOnClickListener(v -> {
            mPresenter = new GetUnLoadListBillPresenter(this);
            UnLoadRequestEntity entity = new UnLoadRequestEntity();
            entity.setUnloadingUser(UserInfoSingle.getInstance().getUserId());
            entity.setFlightId(mOutFieldTaskBean.getFlightId());
            ((GetUnLoadListBillPresenter) mPresenter).getUnLoadingList(entity);
        });
        setListeners();
        //????????????????????????
        if (mCurrentTaskId !=null){
            if (ScooterMapSingle.getInstance().get(mCurrentTaskId)!=null){
                if (ScooterMapSingle.getInstance().get(mCurrentTaskId).getMListGoods()!=null){
                    for (ScooterInfoListBean scooterInfoListBean:ScooterMapSingle.getInstance().get(mCurrentTaskId).getMListGoods()){
                        mTpScooterCodeList.add(scooterInfoListBean.getScooterCode());
                    }
                    mListGoods.addAll(ScooterMapSingle.getInstance().get(mCurrentTaskId).getMListGoods());
                    mSlideRvGoods.setVisibility(View.VISIBLE);
                    mScanGoodsAdapter.notifyDataSetChanged();
                }
                if (ScooterMapSingle.getInstance().get(mCurrentTaskId).getMListBaggage()!=null){
                    for (ScooterInfoListBean scooterInfoListBean:ScooterMapSingle.getInstance().get(mCurrentTaskId).getMListBaggage()){
                        mTpScooterCodeList.add(scooterInfoListBean.getScooterCode());
                    }
                    mListPac.addAll(ScooterMapSingle.getInstance().get(mCurrentTaskId).getMListBaggage());
                    mSlideRvPac.setVisibility(View.VISIBLE);
                    mScanPacAdapter.notifyDataSetChanged();
                }
            }
        }
        //????????? ?????? ????????????
        setIsBack(true,()->{
            UnloadScooterListEntity unloadScooterListEntity = new UnloadScooterListEntity();
            unloadScooterListEntity.setMListGoods(mListGoods);
            unloadScooterListEntity.setMListBaggage(mListPac);
            ScooterMapSingle.getInstance().put(mCurrentTaskId,unloadScooterListEntity);
            finish();
        });
    }
    private void endUnload() {
        TransportEndEntity model = new TransportEndEntity();
        model.setTaskId(mOutFieldTaskBean.getTaskId());
        Log.e("tag", "??????id======" + mOutFieldTaskBean.getId());
        List<TransportTodoListBean> infos = new ArrayList<>();
        for (ScooterInfoListBean bean : mListGoods) {
            TransportTodoListBean entity = new TransportTodoListBean();
            entity.setTpScooterType(String.valueOf(bean.getScooterType()));
            entity.setTpScooterCode(bean.getScooterCode());
            entity.setFlightIndicator(bean.getFlightType());
            entity.setHeadingFlag(bean.getHeadingFlag());
            entity.setTpCargoType("cargo");
            entity.setFlightId(mOutFieldTaskBean.getFlights().getFlightId() + "");
            entity.setFlightNo(mOutFieldTaskBean.getFlights().getFlightNo());
            entity.setTpFlightLocate(mOutFieldTaskBean.getFlights().getSeat());
            entity.setTpOperator(UserInfoSingle.getInstance().getUserId());
            entity.setDtoType(8);
            entity.setTpType(mOutFieldTaskBean.getFlights().getMovement());
            entity.setTaskId(mOutFieldTaskBean.getTaskId());//??????????????????id
            entity.setTpStartLocate("seat");
            entity.setBeginAreaType(mOutFieldTaskBean.getBeginAreaType());
            entity.setBeginAreaId(mOutFieldTaskBean.getBeginAreaId());
            entity.setEndAreaType(mOutFieldTaskBean.getEndAreaType());
            entity.setEndAreaId(mOutFieldTaskBean.getEndAreaId());
            entity.setScSubCategory(3);
            if (!bean.isNoticeTransport())//???????????????????????? ??????????????????
            {
                infos.add(entity);
            }
        }
        for (ScooterInfoListBean bean : mListPac) {
            TransportTodoListBean entity = new TransportTodoListBean();
            entity.setTpScooterType(String.valueOf(bean.getScooterType()));
            entity.setTpScooterCode(bean.getScooterCode());
            entity.setFlightIndicator(bean.getFlightType());
            entity.setHeadingFlag(bean.getHeadingFlag());
            entity.setTpCargoType("baggage");
            entity.setFlightId(mOutFieldTaskBean.getFlights().getFlightId() + "");
            entity.setFlightNo(mOutFieldTaskBean.getFlights().getFlightNo());
            entity.setTpFlightLocate(mOutFieldTaskBean.getFlights().getSeat());
            entity.setTpOperator(UserInfoSingle.getInstance().getUserId());
            entity.setDtoType(8);
            entity.setTpType(mOutFieldTaskBean.getFlights().getMovement());
            entity.setTaskId(mOutFieldTaskBean.getTaskId());//??????????????????id
            entity.setTpStartLocate("seat");
            entity.setBeginAreaType(mOutFieldTaskBean.getBeginAreaType());
            entity.setBeginAreaId(mOutFieldTaskBean.getBeginAreaId());
            entity.setEndAreaType(mOutFieldTaskBean.getEndAreaType());
            entity.setEndAreaId(mOutFieldTaskBean.getEndAreaId());
            entity.setScSubCategory(3);
            if (!bean.isNoticeTransport())//???????????????????????? ??????????????????
            {
                infos.add(entity);
            }
        }
//        if (infos.size() == 0) {
//            ToastUtil.showToast("????????????????????????????????????????????????");
//        } else {
            model.setSeat(mOutFieldTaskBean.getFlights().getSeat());
            model.setScooters(infos);
            model.setEndUnloadTask(false);
            mPresenter = new ArrivalDataSavePresenter(this);
            ((ArrivalDataSavePresenter) mPresenter).arrivalDataSave(model);
//        }
    }
    /**
     * ??????????????????
     */
    private void noticeTp() {
        TransportEndEntity model = new TransportEndEntity();
        model.setTaskId(mOutFieldTaskBean.getTaskId());
        Log.e("tag", "??????id======" + mOutFieldTaskBean.getId());
        List<TransportTodoListBean> infos = new ArrayList<>();
        for (ScooterInfoListBean bean : mListGoods) {
            TransportTodoListBean entity = new TransportTodoListBean();
            entity.setTpScooterType(String.valueOf(bean.getScooterType()));
            entity.setHeadingFlag(bean.getHeadingFlag());
            entity.setTpScooterCode(bean.getScooterCode());
            entity.setFlightIndicator(bean.getFlightType());
            entity.setTpCargoType("cargo");
            entity.setFlightId(mOutFieldTaskBean.getFlights().getFlightId() + "");
            entity.setFlightNo(mOutFieldTaskBean.getFlights().getFlightNo());
            entity.setTpFlightLocate(mOutFieldTaskBean.getFlights().getSeat());
            entity.setTpStartLocate("seat");
            entity.setBeginAreaId(mOutFieldTaskBean.getFlights().getSeat());
//            entity.setTpOperator(UserInfoSingle.getInstance().getUserId()); //????????????????????? ????????????userId
            entity.setDtoType(9);
            entity.setTpType("???");
            entity.setTpState(0);
            entity.setTaskId(mOutFieldTaskBean.getTaskId());//??????????????????id
            entity.setScSubCategory(3);
            if (!bean.isNoticeTransport()){//???????????????????????????????????????
                infos.add(entity);
                mListTempAlreadyNotify.add(bean);
            }
        }
        for (ScooterInfoListBean bean : mListPac) {
            TransportTodoListBean entity = new TransportTodoListBean();
            entity.setTpScooterType(String.valueOf(bean.getScooterType()));
            entity.setHeadingFlag(bean.getHeadingFlag());
            entity.setTpScooterCode(bean.getScooterCode());
            entity.setFlightIndicator(bean.getFlightType());
            entity.setTpCargoType("baggage");
            entity.setFlightId(mOutFieldTaskBean.getFlights().getFlightId() + "");
            entity.setFlightNo(mOutFieldTaskBean.getFlights().getFlightNo());
            entity.setTpFlightLocate(mOutFieldTaskBean.getFlights().getSeat());
            entity.setTpStartLocate("seat");
            entity.setBeginAreaId(mOutFieldTaskBean.getFlights().getSeat());
//            entity.setTpOperator(UserInfoSingle.getInstance().getUserId()); //????????????????????? ????????????userId
            entity.setDtoType(9);
            entity.setTpType("???");
            entity.setTpState(0);
            entity.setTaskId(mOutFieldTaskBean.getTaskId());//??????????????????id
            entity.setScSubCategory(3);
            if (!bean.isNoticeTransport()){//???????????????????????????????????????
                infos.add(entity);
                mListTempAlreadyNotify.add(bean);
            }
        }
        model.setScooters(infos);
        model.setTaskType("2");
        if (model.getScooters().size() == 0) {
            ToastUtil.showToast("????????????????????????????????????????????????");
        } else {
            mPresenter = new ScanScooterPresenter(this);
            ((ScanScooterPresenter) mPresenter).scanLockScooter(model);
        }
    }

    private void setListeners() {
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
            ScanManagerActivity.startActivity(TPUnloadPlaneActivity.this, "TPUnloadPlaneActivity");
        });
        mLlScanPac.setOnClickListener(v -> {
            mIsScanGoods = false;
            ScanManagerActivity.startActivity(TPUnloadPlaneActivity.this, "TPUnloadPlaneActivity");
        });
        mTvErrorReport.setOnClickListener(
                v -> {
                    Intent intent = new Intent(TPUnloadPlaneActivity.this, ErrorReportActivity.class);
                    intent.putExtra("area_id", mOutFieldTaskBean.getFlights().getSeat());//area_id
                    intent.putExtra("step_code", Constants.TP_START);//step_code
                    intent.putExtra("flight_number", mOutFieldTaskBean.getFlights().getFlightNo());//?????????
                    intent.putExtra("task_id", mOutFieldTaskBean.getTaskId());//??????id
                    intent.putExtra("flight_id", String.valueOf(mOutFieldTaskBean.getFlights().getFlightId()));//Flight id
                    intent.putExtra("error_type", 2);
                    TPUnloadPlaneActivity.this.startActivity(intent);
                });
        mTvEndUnload.setOnClickListener(v -> {
            showYesOrNoDialog("","???????????????????????????????",2);
        });
    }
    /**
     * ?????????????????????
     *
     * @param title
     * @param msg
     * @param flag
     */
    private void showYesOrNoDialog(String title, String msg, int flag) {
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("??????")
                .setNegativeButton("??????")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            switch (flag) {
                                case 1:
                                    noticeTp();
                                    break;
                                case 2:
                                    endUnload();
                                    break;
                            }
                        } else {
//                            ToastUtil.showToast("????????????????????????");
                        }
                    }
                })
                .show();
    }
    private String mNowScooterCode;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if ("TPUnloadPlaneActivity".equals(result.getFunctionFlag())) {
            if (result.getData() != null && result.getData().length() == Constants.SCOOTER_NO_LENGTH) {
                //??????????????????????????????????????????????????????
                if (!mTpScooterCodeList.contains(result.getData())) {
                    boolean isLaser=result.isLaser();
                    if (isLaser) {
                        ChoseFlightTypeDialog dialog = new ChoseFlightTypeDialog();
                        dialog.setData(this, "????????????????????????", "??????", "??????", isCheckRight -> {
                            mIsScanGoods= !isCheckRight;
                            mNowScooterCode = result.getData();
//                            addScooterInfo(mNowScooterCode);
                            mPresenter = new ScanScooterCheckUsedPresenter(this);
                            ((ScanScooterCheckUsedPresenter) mPresenter).checkScooterCode(mNowScooterCode,mOutFieldTaskBean.getFlightId(),Constants.SCAN_UNLOAD);
                        });
                        dialog.setCancelable(false);
                        dialog.show(getSupportFragmentManager(), "222");
                    } else {
                        mNowScooterCode = result.getData();
//                        addScooterInfo(mNowScooterCode);
                        mPresenter = new ScanScooterCheckUsedPresenter(this);
                        ((ScanScooterCheckUsedPresenter) mPresenter).checkScooterCode(mNowScooterCode,mOutFieldTaskBean.getFlightId(),Constants.SCAN_UNLOAD);
                    }
                } else {
                    ToastUtil.showToast("????????????????????????????????????");
                }
            } else {
                ToastUtil.showToast("????????????????????????????????????");
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
            ToastUtil.showToast(this, "?????????????????????????????????");
    }

    @Override
    public void scooterInfoListResult(List<ScooterInfoListBean> result) {
        if (result.size() == 0) {
            ToastUtil.showToast("??????????????????????????????");
        } else {
            String flightType = getIntent().getStringExtra("flight_type");
            if ("D".equals(flightType) || "I".equals(flightType)) {
                for (ScooterInfoListBean bean : result) {
                    bean.setFlightType(flightType);
                }
                showBoardInfos(result);
            } else {
                ChoseFlightTypeDialog dialog = new ChoseFlightTypeDialog();
                dialog.setData(this, "????????????????????????", "??????", "??????", isCheckRight -> {
                    if (isCheckRight) {
                        for (ScooterInfoListBean bean : result) {
                            bean.setFlightType("D");
                        }
                        showBoardInfos(result);
                    } else {
                        for (ScooterInfoListBean bean : result) {
                            bean.setFlightType("I");
                        }
                        showBoardInfos(result);
                    }
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
     * ???????????????????????????????????????
     *
     * @param result ????????????
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

    /**
     * x???????????? ??????????????????????????? ???????????? ??????
     *
     * @param mOutFieldTaskBean
     * @param step
     */
    private void submitStep(OutFieldTaskBean mOutFieldTaskBean, int step) {
        mPresenter = new LoadAndUnloadTodoPresenter(this);
        PerformTaskStepsEntity entity = new PerformTaskStepsEntity();
        entity.setType(0);
        entity.setLoadUnloadDataId(mOutFieldTaskBean.getId());
        entity.setFlightId(Long.valueOf(mOutFieldTaskBean.getFlightId()));
        entity.setFlightTaskId(mOutFieldTaskBean.getTaskId());
        entity.setLatitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLatitude()+"");
        entity.setLongitude((Tools.getGPSPosition() == null) ? "" : Tools.getGPSPosition().getLongitude()+"");

        if (step == 0)
            entity.setOperationCode(Constants.TP_START);//????????????
        else if (step == 1)
            entity.setOperationCode(Constants.TP_END);//????????????
        else
            entity.setOperationCode(Constants.TP_ACCEPT);//????????????

        entity.setUserName(UserInfoSingle.getInstance().getUsername());

        entity.setTerminalId(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        entity.setCreateTime(System.currentTimeMillis());
        ((LoadAndUnloadTodoPresenter) mPresenter).slideTask(entity);

    }

    @Override
    public void existResult(MyAgentListBean result) {

    }

    @Override
    public void addInfoResult(MyAgentListBean result) {

    }

    @Override
    public void toastView(String error) {
        if (error!=null)
            ToastUtil.showToast(error);
        mListTempAlreadyNotify.clear();
        Log.e("tagError", "error========" + error);
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
    public void arrivalDataSaveResult(String result) {
        ScooterMapSingle.getInstance().put(mCurrentTaskId,null);
        submitStep(mOutFieldTaskBean, 1);
    }

    @Override
    public void checkScooterCodeResult(BaseEntity<Object> result) {
        if ("200".equals(result.getStatus())) {
            addScooterInfo(mNowScooterCode);
        } else {
            ToastUtil.showToast("????????????????????????????????????");
        }
    }

    @Override
    public void loadAndUnloadTodoResult(List<LoadAndUnloadTodoBean> loadAndUnloadTodoBean) {

    }

    @Override
    public void slideTaskResult(String result) {
        ToastUtil.showToast("????????????");
        EventBus.getDefault().post("TaskDriverOutFragment_refresh");
        finish();
    }

    @Override
    public void startClearTaskResult(String result) {

    }


    @Override
    public void scanScooterResult(String result) {

    }

    @Override
    public void scanLockScooterResult(String result) {
//        ScooterMapSingle.getInstance().put(mCurrentTaskId,null);
        //?????????????????????????????????
         for (ScooterInfoListBean bean:mListTempAlreadyNotify){
             bean.setNoticeTransport(true);
         }
        ToastUtil.showToast("???????????????");

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
    public void getUnLoadDoneScooterResult(List <TransportTodoListBean> result) {

    }
}
