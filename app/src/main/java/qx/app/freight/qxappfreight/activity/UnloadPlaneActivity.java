package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import qx.app.freight.qxappfreight.adapter.ScanInfoAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.ArrivalDataSaveContract;
import qx.app.freight.qxappfreight.contract.ScanScooterCheckUsedContract;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.dialog.ChoseFlightTypeDialog;
import qx.app.freight.qxappfreight.presenter.ArrivalDataSavePresenter;
import qx.app.freight.qxappfreight.presenter.ScanScooterCheckUsedPresenter;
import qx.app.freight.qxappfreight.presenter.ScooterInfoListPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 理货卸机页面
 */
public class UnloadPlaneActivity extends BaseActivity implements ScooterInfoListContract.scooterInfoListView, ArrivalDataSaveContract.arrivalDataSaveView, ScanScooterCheckUsedContract.ScanScooterCheckView {
    @BindView(R.id.tv_plane_info)
    TextView mTvPlaneInfo;//航班号
    @BindView(R.id.tv_flight_type)
    TextView mTvFlightType;//航班类型
    @BindView(R.id.tv_start_place)
    TextView mTvStartPlace;//航班起点
    @BindView(R.id.iv_two_place)
    ImageView mIvTwoPlace;
    @BindView(R.id.tv_middle_place)
    TextView mTvMiddlePlace;//航班中转点
    @BindView(R.id.tv_target_place)
    TextView mTvTargetPlace;//航班终点
    @BindView(R.id.tv_seat)
    TextView mTvSeat;//航班机位数
    @BindView(R.id.tv_arrive_time)
    TextView mTvArriveTime;//航班到达时间
    @BindView(R.id.tv_board_goods_number)
    TextView mTvGoodsNumber;//航班货物数量
    @BindView(R.id.iv_cotro_1)
    ImageView mIvControl1;//控制显示隐藏货物列表
    @BindView(R.id.srv_goods_info)
    SlideRecyclerView mSlideRvGoods;//航班货物信息列表
    @BindView(R.id.ll_add_scan_goods)
    LinearLayout mLlScanGoods;//扫描货物数据
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
        if (result != null) {
            if (result.isCancelFlag()) {
                String taskId = result.getTaskId();
                if (taskId.equals(mCurrentTaskId)) {
                    ToastUtil.showToast("当前卸机任务已取消");
                    Observable.timer(2, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()) // timer 默认在新线程，所以需要切换回主线程
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
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        String flightInfo = getIntent().getStringExtra("plane_info");
        mData = (LoadAndUnloadTodoBean) getIntent().getSerializableExtra("plane_info");
        mCurrentTaskId = mData.getTaskId();
        toolbar.setMainTitle(Color.WHITE, mData.getFlightNo() + "  卸机");
        mTvPlaneInfo.setText(mData.getFlightNo());
        mTvFlightType.setText(mData.getAircraftno());
        String route = mData.getRoute();
        String start = "", middle = "", end = "";
        if (route != null) {
            String[] placeArray = route.split(",");
            List<String> resultList = new ArrayList<>();
            for (String str : placeArray) {
                String temp = str.replaceAll("[^(a-zA-Z\\u4e00-\\u9fa5)]", "");
                resultList.add(temp);
            }
            if (resultList.size() == 3) {
                middle = resultList.get(1);
            }
            start = resultList.get(0);
            end = resultList.get(resultList.size() - 1);
        }
        if (TextUtils.isEmpty(start)) {//起点都没有，说明没有航线信息，全部隐藏
            mTvStartPlace.setVisibility(View.GONE);
            mIvTwoPlace.setVisibility(View.GONE);
            mTvMiddlePlace.setVisibility(View.GONE);
            mTvTargetPlace.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(middle)) {//没有中转站信息
                mTvStartPlace.setVisibility(View.VISIBLE);
                mTvStartPlace.setText(start);
                mIvTwoPlace.setVisibility(View.VISIBLE);
                mTvMiddlePlace.setVisibility(View.GONE);
                mTvTargetPlace.setVisibility(View.VISIBLE);
                mTvTargetPlace.setText(end);
            } else {
                mTvStartPlace.setVisibility(View.VISIBLE);
                mIvTwoPlace.setVisibility(View.GONE);
                mTvMiddlePlace.setVisibility(View.VISIBLE);
                mTvTargetPlace.setVisibility(View.VISIBLE);
                mTvStartPlace.setText(start);
                mTvMiddlePlace.setText(middle);
                mTvTargetPlace.setText(end);
            }
        }
        mTvSeat.setText(mData.getSeat());
        long arrive;
        if (mData.getActualArriveTime()!=0) {//有实际到达时间
            arrive = mData.getActualArriveTime();
        } else {
            arrive = mData.getScheduleTime();
        }
        mTvArriveTime.setText(TimeUtils.getHMDay(arrive));
        String scanGoods = "请扫描添加  <font color='#4791E5'>货物</font>  板车";
        mTvScanGoods.setText(Html.fromHtml(scanGoods));
        String scanPac = "请扫描添加  <font color='#4791E5'>行李</font>  板车";
        mTvScanPac.setText(Html.fromHtml(scanPac));
        mSlideRvGoods.setLayoutManager(new LinearLayoutManager(this));
        mScanGoodsAdapter = new ScanInfoAdapter(mListGoods, mData);
        mSlideRvGoods.setAdapter(mScanGoodsAdapter);
        mScanGoodsAdapter.setOnDeleteClickListener((view, position) -> {
            mTpScooterCodeList.remove(mListGoods.get(position).getScooterCode());
            mListGoods.remove(position);
            mSlideRvGoods.closeMenu();
            mTvGoodsNumber.setText(String.valueOf(mListGoods.size()));
            mScanGoodsAdapter.notifyDataSetChanged();
        });
        mScanGoodsAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
        mSlideRvPac.setLayoutManager(new LinearLayoutManager(this));
        mScanPacAdapter = new ScanInfoAdapter(mListPac, mData);
        mSlideRvPac.setAdapter(mScanPacAdapter);
        mScanPacAdapter.setOnDeleteClickListener((view, position) -> {
            mTpScooterCodeList.remove(mListPac.get(position).getScooterCode());
            mListPac.remove(position);
            mSlideRvPac.closeMenu();
            mTvPacNumber.setText(String.valueOf(mListPac.size()));
            mScanPacAdapter.notifyDataSetChanged();
        });
        mScanPacAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
        setListeners();
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
        mLlScanGoods.setOnClickListener(v -> ScanManagerActivity.startActivity(UnloadPlaneActivity.this, "UnloadPlaneActivity"));
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
                entity.setTpFlightId(mData.getFlightId());
                entity.setTpFlightNumber(mData.getFlightNo());
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
                entity.setTpFlightId(mData.getFlightId());
                entity.setTpFlightNumber(mData.getFlightNo());
                entity.setTpFlightLocate(mData.getSeat());
                entity.setTpOperator(UserInfoSingle.getInstance().getUserId());
                entity.setDtoType(8);
                entity.setTpType(String.valueOf(mData.getMovement()));
                entity.setTaskId(mData.getTaskId());//代办数据中的id
                infos.add(entity);
            }
            if (infos.size() == 0) {
                ToastUtil.showToast("请选择上传板车信息再提交");
            } else {
                model.setSeat(mData.getSeat());
                model.setScooters(infos);
                mPresenter = new ArrivalDataSavePresenter(this);
                ((ArrivalDataSavePresenter) mPresenter).arrivalDataSave(model);
            }
        });
    }

    private String mNowScooterCode;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if ("UnloadPlaneActivity".equals(result.getFunctionFlag())) {
            //根据扫一扫获取的板车信息查找板车内容
            if (!mTpScooterCodeList.contains(result.getData())) {
                mNowScooterCode = result.getData();
                mPresenter = new ScanScooterCheckUsedPresenter(this);
                ((ScanScooterCheckUsedPresenter) mPresenter).checkScooterCode(mNowScooterCode);
            } else {
                ToastUtil.showToast("操作不合法，不能重复扫描");
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

    }

    @Override
    public void dissMiss() {

    }

    @Override
    public void arrivalDataSaveResult(String result) {
        ToastUtil.showToast("结束卸机成功");
        EventBus.getDefault().post("InstallEquipFragment_refresh");
        finish();
    }

    @Override
    public void checkScooterCodeResult(BaseEntity<Object> result) {
        if ("200".equals(result.getStatus())) {
            addScooterInfo(mNowScooterCode);
        } else {
            ToastUtil.showToast("操作不合法，不能重复扫描");
        }
    }
}
