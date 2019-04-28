package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.UnloadPlaneAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.LocalBillBean;
import qx.app.freight.qxappfreight.bean.UnloadPlaneEntity;
import qx.app.freight.qxappfreight.bean.UnloadPlaneVersionEntity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.GetFlightCargoResBean;
import qx.app.freight.qxappfreight.contract.GetFlightCargoResContract;
import qx.app.freight.qxappfreight.dialog.UpdatePushDialog;
import qx.app.freight.qxappfreight.presenter.GetFlightCargoResPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 理货装机页面
 */
public class LoadPlaneActivity extends BaseActivity implements GetFlightCargoResContract.getFlightCargoResView {
    @BindView(R.id.rv_data)
    RecyclerView mRvData;
    @BindView(R.id.tv_plane_info)
    TextView mTvPlaneInfo;
    @BindView(R.id.tv_flight_craft)
    TextView mTvFlightType;
    @BindView(R.id.tv_start_place)
    TextView mTvStartPlace;
    @BindView(R.id.iv_two_place)
    ImageView mIvTwoPlace;
    @BindView(R.id.tv_middle_place)
    TextView mTvMiddlePlace;
    @BindView(R.id.tv_target_place)
    TextView mTvTargetPlace;
    @BindView(R.id.tv_seat)
    TextView mTvSeat;
    @BindView(R.id.tv_start_time)
    TextView mTvStartTime;
    @BindView(R.id.tv_pull_goods_report)
    TextView mTvPullGoodsReport;
    @BindView(R.id.tv_error_report)
    TextView mTvErrorReport;
    @BindView(R.id.tv_end_install_equip)
    TextView mTvEndInstall;
    private List<UnloadPlaneVersionEntity> mList = new ArrayList<>();
    private List<LocalBillBean> mBillList = new ArrayList<>();
    private String mFregihtSpace;//舱位名称
    private String mTargetPlace;
    private String mCurrentTaskId;
    private String mCurrentFlightId;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        if (result != null) {
            if (result.isCancelFlag()) {
                String taskId = result.getTaskId();
                if (taskId.equals(mCurrentTaskId)) {
                    ToastUtil.showToast("当前装机任务已取消");
                    Observable.timer(2, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()) // timer 默认在新线程，所以需要切换回主线程
                            .subscribe(aLong -> finish());
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result != null && result.equals(mCurrentFlightId)) {
            showCargoResUpdate(mCurrentFlightId);
        }
    }

    private void showCargoResUpdate(String flightId) {
        UpdatePushDialog updatePushDialog = new UpdatePushDialog(this, R.style.custom_dialog, flightId, s -> {
            ((GetFlightCargoResPresenter) mPresenter).getFlightCargoRes(mCurrentFlightId);
        });
        updatePushDialog.show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_load_plane;
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
        String[] info = flightInfo.split("\\*");
        mCurrentTaskId = info[12];
        toolbar.setMainTitle(Color.WHITE, info[0] + "  装机");
        mTvPlaneInfo.setText(info[0]);
        mTvFlightType.setText(info[1]);
        String start = info[2];
        String middle = info[3];
        String end = info[4];
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
        mTargetPlace = info[4];
        mTvSeat.setText(info[5]);
        mTvStartTime.setText(TimeUtils.getHMDay(Long.valueOf(info[6])));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //配置布局，默认为vertical（垂直布局），下边这句将布局改为水平布局
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvData.setLayoutManager(linearLayoutManager);
        mPresenter = new GetFlightCargoResPresenter(this);
        mCurrentFlightId = info[7];
        ((GetFlightCargoResPresenter) mPresenter).getFlightCargoRes(mCurrentFlightId);
        mTvPullGoodsReport.setOnClickListener(v -> {
            if (mBillList.size() == 0) {
                ToastUtil.showToast("当前航班无装机单数据，暂时无法进行下一步操作");
            } else {
                Intent intent = new Intent(LoadPlaneActivity.this, PullGoodsReportActivity.class);
                intent.putExtra("plane_info", flightInfo);
                intent.putExtra("fregiht_space", mFregihtSpace);
                intent.putParcelableArrayListExtra("bill_list", (ArrayList<? extends Parcelable>) mBillList);
                LoadPlaneActivity.this.startActivity(intent);
            }
        });
        mTvErrorReport.setOnClickListener(v -> {
            Intent intent = new Intent(LoadPlaneActivity.this, ErrorReportActivity.class);
            intent.putExtra("plane_info", flightInfo);
            intent.putExtra("error_type", 1);
            LoadPlaneActivity.this.startActivity(intent);
        });
        mTvEndInstall.setOnClickListener(v -> {
            if (mBillList.size() == 0) {
                ToastUtil.showToast("当前航班无装机单数据，暂时无法进行下一步操作");
            } else {
                GetFlightCargoResBean bean = new GetFlightCargoResBean();
                bean.setTpFlightId(info[7]);
                bean.setTaskId(info[11]);
                bean.setTpOperator(UserInfoSingle.getInstance().getUserId());
                ((GetFlightCargoResPresenter) mPresenter).flightDoneInstall(bean);
            }
        });
    }

    /**
     * 数据去重
     *
     * @param list 要去重的列表
     * @return 去重后的列表
     */
    private List<LocalBillBean> removeDuplicate(List<LocalBillBean> list) {
        List<LocalBillBean> result = new ArrayList<>();
        List<String> codes = new ArrayList<>();
        for (LocalBillBean bean : list) {
            if (!codes.contains(bean.getWayBillCode())) {
                codes.add(bean.getWayBillCode());
            }
        }
        for (String code : codes) {
            LocalBillBean billBean = new LocalBillBean();
            billBean.setWayBillCode(code);
            String id = "";
            int number = 0;
            double weight = 0d;
            double volume = 0d;
            String type = "";
            for (LocalBillBean bean : list) {
                if (code.equals(bean.getWayBillCode())) {
                    id = bean.getWaybillId();
                    type = bean.getCargoType();
                    number += bean.getMaxNumber();
                    weight += bean.getMaxWeight();
                    volume += bean.getMaxVolume();
                }
            }
            billBean.setCargoType(type);
            billBean.setWaybillId(id);
            billBean.setMaxNumber(number);
            billBean.setMaxWeight(weight);
            billBean.setMaxVolume(volume);
            billBean.setBillItemNumber(number);
            billBean.setBillItemWeight(weight);
            result.add(billBean);
        }
        return result;
    }

    @Override
    public void getFlightCargoResResult(List<GetFlightCargoResBean> getFlightCargoResBeanList) {
        if (getFlightCargoResBeanList == null || getFlightCargoResBeanList.size() == 0) return;
        List<LocalBillBean> list3 = new ArrayList<>();
        for (GetFlightCargoResBean.ContentObjectBean bean : getFlightCargoResBeanList.get(0).getContentObject()) {
            mFregihtSpace = bean.getSuggestRepository();
            if (bean.getGroupScooters() != null) {
                for (GetFlightCargoResBean.ContentObjectBean.GroupScootersBean groupCode : bean.getGroupScooters()) {
                    LocalBillBean billBean = new LocalBillBean();
                    billBean.setWayBillCode(groupCode.getWaybillCode());
                    billBean.setWaybillId(groupCode.getWaybillId());
                    billBean.setMaxNumber(groupCode.getNumber());
                    billBean.setMaxWeight(groupCode.getWeight());
                    billBean.setCargoType("C".equals(bean.getCargoType()) ? "cargo" : "mail");
                    billBean.setMaxVolume(groupCode.getVolume());
                    list3.add(billBean);
                }
            }
        }
        mBillList = removeDuplicate(list3);
        mList.clear();
        for (GetFlightCargoResBean bean : getFlightCargoResBeanList) {
            UnloadPlaneVersionEntity entity = new UnloadPlaneVersionEntity();
            entity.setVersion(Integer.valueOf(bean.getVersion()));
            List<UnloadPlaneEntity> list = new ArrayList<>();
            if (bean.getContentObject() != null) {
                for (int i = 0; i < bean.getContentObject().size(); i++) {
                    GetFlightCargoResBean.ContentObjectBean model = bean.getContentObject().get(i);
                    UnloadPlaneEntity item = new UnloadPlaneEntity();
                    item.setBerth(model.getSuggestRepository());
                    String boardNumber;
                    if (!TextUtils.isEmpty(model.getScooterCode())) {
                        boardNumber = model.getScooterCode();
                    } else {
                        boardNumber = (model.getGroupScooters() == null) ? "-" : model.getGroupScooters().get(0).getScooterCode();
                    }
                    item.setBoardNumber(boardNumber);
                    item.setUldNumber(TextUtils.isEmpty(model.getUldCode()) ? "-" : model.getUldCode());
                    item.setTarget(mTargetPlace);
                    item.setType(model.getCargoType());
                    item.setWeight(model.getWeight());
                    item.setGoodsPosition(model.getGoodsLocation());
                    item.setNumber(model.getTotal());
                    list.add(item);
                }
            }
            entity.setList(list);
            entity.setShowDetail(false);
            mList.add(entity);
        }
        Collections.reverse(mList);
        UnloadPlaneAdapter adapter = new UnloadPlaneAdapter(mList);
        mRvData.setAdapter(adapter);
    }

    @Override
    public void flightDoneInstallResult(String result) {
        ToastUtil.showToast("结束装机成功");
        Log.e("tagNet", "result=====" + result);
        EventBus.getDefault().post("InstallEquipFragment_refresh");
        finish();
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void showNetDialog() {
    }

    @Override
    public void dissMiss() {
    }
}
