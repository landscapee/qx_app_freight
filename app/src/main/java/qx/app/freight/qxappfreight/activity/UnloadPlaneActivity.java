package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ScanInfoAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.contract.ArrivalDataSaveContract;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.presenter.ArrivalDataSavePresenter;
import qx.app.freight.qxappfreight.presenter.ScooterInfoListPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 理货卸机页面
 */
public class UnloadPlaneActivity extends BaseActivity implements ScooterInfoListContract.scooterInfoListView, ArrivalDataSaveContract.arrivalDataSaveView{
    @BindView(R.id.tv_plane_info)
    TextView mTvPlaneInfo;//航班号
    @BindView(R.id.tv_flight_type)
    TextView mTvFlightType;//航班类型
    @BindView(R.id.tv_start_place)
    TextView mTvStartPlace;//航班起点
    @BindView(R.id.tv_middle_place)
    TextView mTvMiddlePlace;//航班中转点
    @BindView(R.id.tv_target_place)
    TextView mTvTargetPlace;//航班终点
    @BindView(R.id.tv_seat)
    TextView mTvSeat;//航班机位数
    @BindView(R.id.tv_start_time)
    TextView mTvStartTime;//航班起飞时间
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
    private String[] mInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_unload_plane;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        String flightInfo = getIntent().getStringExtra("plane_info");
        mInfo=flightInfo.split("\\*");
        toolbar.setMainTitle(Color.WHITE, mInfo[0] + "  卸机");
        mTvPlaneInfo.setText(mInfo[0]);
        mTvFlightType.setText(mInfo[1]);
        mTvStartPlace.setText(mInfo[2]);
        mTvMiddlePlace.setText(mInfo[3]);
        mTvTargetPlace.setText(mInfo[4]);
        mTvSeat.setText(mInfo[5]);
        long takeOff=Long.valueOf(mInfo[8]);
        SimpleDateFormat sdf=new SimpleDateFormat("dd HH:mm", Locale.CHINESE);
        String hourMinuteStart=sdf.format(new Date(takeOff)).substring(3);
        String dayStart=hourMinuteStart.substring(0,2);
        String startTime;
        if (Integer.valueOf(dayStart)<10){
            startTime=hourMinuteStart+"("+dayStart.substring(1)+")";
        }else {
            startTime=hourMinuteStart+"("+dayStart+")";
        }
        mTvStartTime.setText(startTime);
        long arrive=Long.valueOf(mInfo[9]);
        String hourMinuteArrive=sdf.format(new Date(arrive)).substring(3);
        String dayArrive=hourMinuteArrive.substring(0,2);
        String arriveTime;
        if (Integer.valueOf(dayArrive)<10){
            arriveTime=hourMinuteArrive+"("+dayArrive.substring(1)+")";
        }else {
            arriveTime=hourMinuteArrive+"("+dayArrive+")";
        }
        mTvArriveTime.setText(arriveTime);
        String scanGoods = "请扫描添加  <font color='#0000ff'>货物</font>  板车";
        mTvScanGoods.setText(Html.fromHtml(scanGoods));
        String scanPac = "请扫描添加  <font color='#0000ff'>行李</font>  板车";
        mTvScanPac.setText(Html.fromHtml(scanPac));
        mSlideRvGoods.setLayoutManager(new LinearLayoutManager(this));
        mScanGoodsAdapter = new ScanInfoAdapter(mListGoods,flightInfo);
        mSlideRvGoods.setAdapter(mScanGoodsAdapter);
        mScanGoodsAdapter.setOnDeleteClickListener((view, position) -> {
            mListGoods.remove(position);
            mSlideRvGoods.closeMenu();
            mTvGoodsNumber.setText(String.valueOf(mListGoods.size()));
            mScanGoodsAdapter.notifyDataSetChanged();
        });
        mSlideRvPac.setLayoutManager(new LinearLayoutManager(this));
        mScanPacAdapter = new ScanInfoAdapter(mListPac,flightInfo);
        mSlideRvPac.setAdapter(mScanPacAdapter);
        mScanPacAdapter.setOnDeleteClickListener((view, position) -> {
            mListPac.remove(position);
            mSlideRvPac.closeMenu();
            mTvPacNumber.setText(String.valueOf(mListPac.size()));
            mScanPacAdapter.notifyDataSetChanged();
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
                    intent.putExtra("plane_info", getIntent().getStringExtra("plane_info"));
                    intent.putExtra("error_type", 2);
                    UnloadPlaneActivity.this.startActivity(intent);
                });
        mTvEndUnload.setOnClickListener(v -> {
            TransportEndEntity model=new TransportEndEntity();
            model.setTaskId(mInfo[11]);
            Log.e("tag","卸机id======"+mInfo[11]);
            List<TransportTodoListBean> infos=new ArrayList<>();
            for (ScooterInfoListBean bean:mListGoods){
                TransportTodoListBean entity=new TransportTodoListBean();
                entity.setTpScooterType(String.valueOf(bean.getScooterType()));
                entity.setTpScooterCode(bean.getScooterCode());
                entity.setTpCargoType("cargo");
                entity.setTpFlightId(mInfo[7]);
                entity.setTpFlightNumber(mInfo[0]);
                entity.setTpFlightLocate(mInfo[5]);
                entity.setTpOperator(UserInfoSingle.getInstance().getUserId());
                entity.setDtoType(8);
                entity.setTpType(mInfo[10]);
                entity.setTaskId(mInfo[11]);//代办数据中的id
                infos.add(entity);
            }
            for (ScooterInfoListBean bean:mListPac){
                TransportTodoListBean entity=new TransportTodoListBean();
                entity.setTpScooterType(String.valueOf(bean.getScooterType()));
                entity.setTpScooterCode(bean.getScooterCode());
                entity.setTpCargoType("baggage");
                entity.setTpFlightId(mInfo[7]);
                entity.setTpFlightNumber(mInfo[0]);
                entity.setTpFlightLocate(mInfo[5]);
                entity.setTpOperator(UserInfoSingle.getInstance().getUserId());
                entity.setDtoType(8);
                entity.setTpType(mInfo[10]);
                entity.setTaskId(mInfo[11]);//代办数据中的id
                infos.add(entity);
            }
            model.setScooters(infos);
            mPresenter=new ArrivalDataSavePresenter(this);
            ((ArrivalDataSavePresenter)mPresenter).arrivalDataSave(model);
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if ("UnloadPlaneActivity".equals(result.getFunctionFlag())) {
            //根据扫一扫获取的板车信息查找板车内容
            addScooterInfo(result.getData());
        }
    }

    private void addScooterInfo(String scooterCode) {
        Log.e("tagCode", "scooterCode========"+scooterCode);
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
        Log.e("tagError","error========"+error);
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
}
