package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.DriverOutTaskDoingAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.FlightOfScooterBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskMyBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.ScanScooterContract;
import qx.app.freight.qxappfreight.contract.TransportBeginContract;
import qx.app.freight.qxappfreight.contract.TransportListContract;
import qx.app.freight.qxappfreight.presenter.ScanScooterPresenter;
import qx.app.freight.qxappfreight.presenter.TransportBeginPresenter;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

public class DriverOutDoingActivity extends BaseActivity implements TransportBeginContract.transportBeginView, ScanScooterContract.scanScooterView {
    @BindView(R.id.ll_add)
    LinearLayout llAdd;

    @BindView(R.id.rv_car_doing)
    RecyclerView doingRecyclerView;
    @BindView(R.id.btn_begin_end)
    Button btnBeginEnd;
    @BindView(R.id.image_scan)
    ImageView imageScan;

    private List <FlightOfScooterBean> list;
    private List <TransportTodoListBean> listScooter;
    private DriverOutTaskDoingAdapter mDriverOutTaskDoingAdapter;

    private int tpStatus = 1; // 0 运输中 1 运输结束
    private List <OutFieldTaskBean> mAcceptTerminalTodoBean;



    public static void startActivity(Context context, List <OutFieldTaskBean> mTasksBean) {
        Intent starter = new Intent(context, DriverOutDoingActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("acceptTerminalTodoBean", (Serializable) mTasksBean);
        starter.putExtras(mBundle);
        ((Activity) context).startActivityForResult(starter, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_driver_out_doing;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "正在执行");
        listScooter = new ArrayList <>();
        mAcceptTerminalTodoBean = (List <OutFieldTaskBean>) getIntent().getSerializableExtra("acceptTerminalTodoBean");
        doingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list = new ArrayList <>();
        mDriverOutTaskDoingAdapter = new DriverOutTaskDoingAdapter(list);
        doingRecyclerView.setAdapter(mDriverOutTaskDoingAdapter);
        mDriverOutTaskDoingAdapter.setOnDeleteClickListener((view, parentPosition, childPosition) ->
                {

                    deleteHandcar(list.get(parentPosition).getMTransportTodoListBeans().get(childPosition));

                }
        );
        mDriverOutTaskDoingAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
        imageScan.setOnClickListener(v -> {
            ScanManagerActivity.startActivity(this);
        });

        mDriverOutTaskDoingAdapter.setCheckBoxListener((view, position) -> {
            if (list.get(position).isSelect())
                list.get(position).setSelect(false);
            else
                list.get(position).setSelect(true);
        });
        upDataBtnStatus();
        getData();

    }

    private void deleteHandcar(TransportTodoListBean remove) {
        mPresenter = new TransportBeginPresenter(this);
        TransportEndEntity transportEndEntity = new TransportEndEntity();
        transportEndEntity.setId(remove.getId());
        ((TransportBeginPresenter) mPresenter).scanScooterDelete(transportEndEntity);
    }

    private void getData() {
        mPresenter = new ScanScooterPresenter(this);
        ((ScanScooterPresenter) mPresenter).scooterWithUser("u2c2f2a41101e4a14881b1d36337ff7bc");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.SCAN_RESULT == resultCode) {
            String str = data.getStringExtra(Constants.SACN_DATA);
            //根据扫一扫获取的板车信息查找板车内容
            addScooterInfo(str);
        }
    }

    private void addScooterInfo(String scooterCode) {
        Log.e("scooterCode", scooterCode);
        if (!"".equals(scooterCode)) {
            mPresenter = new ScanScooterPresenter(this);
            TransportTodoListBean mainIfos = new TransportTodoListBean();
            mainIfos.setTpScooterCode(scooterCode);
            mainIfos.setTpOperator("u2c2f2a41101e4a14881b1d36337ff7bc");
//            mainIfos.setTpOperator(UserInfoSingle.getInstance().getUserId());
            ((ScanScooterPresenter) mPresenter).scanScooter(mainIfos);
        } else
            ToastUtil.showToast(this, "扫描结果为空请重新扫描");
    }

    private int getMaxHandcarNum() {
        if (listScooter != null)
            return listScooter.size();
        return 0;
    }

    /**
     * 开始按钮是否可以点击
     */
    private void upDataBtnStatus() {
        if (getMaxHandcarNum() > 0) {
            btnBeginEnd.setClickable(true);
            btnBeginEnd.setBackgroundResource(R.drawable.btn_blue_press);
        } else {
            btnBeginEnd.setClickable(false);
            btnBeginEnd.setBackgroundColor(getResources().getColor(R.color.gray));
        }

    }

    @OnClick({R.id.ll_add, R.id.btn_begin_end})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_add:

                break;
            case R.id.btn_begin_end:
                if (tpStatus == 1) {
                    doStart();

                } else {
                    List <OutFieldTaskBean> listEndTemp = new ArrayList <>();
                    List <TransportTodoListBean> mListBeanBegin = new ArrayList <>();
                    //把被选中的航班 下的板车 加入 结束列表 ，被选中的子任务 加入子任务结束列表
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isSelect()) {
                            for (TransportTodoListBean mTransportTodoListBean : list.get(i).getMTransportTodoListBeans()) {
                                if (list.get(i).getFlightNo().equals(mTransportTodoListBean.getTpFlightNumber()))
                                    mListBeanBegin.add(mTransportTodoListBean);
                            }
                            for (OutFieldTaskBean mOutFieldTaskBean : mAcceptTerminalTodoBean) {
                                if (list.get(i).getFlightNo().equals(mOutFieldTaskBean.getFlightNo()))
                                    listEndTemp.add(mOutFieldTaskBean);
                            }
                        }
                    }
                    if (listEndTemp != null && mListBeanBegin.size() > 0)
                        doEnd(listEndTemp, mListBeanBegin);
                }
                break;
        }
    }

    private void doStart() {
        mPresenter = new TransportBeginPresenter(this);
        TransportEndEntity transportEndEntity = new TransportEndEntity();

        List <TransportTodoListBean> mListBeanBegin = new ArrayList <>();
        mListBeanBegin.addAll(listScooter);
        for (OutFieldTaskBean mOutFieldTaskBean : mAcceptTerminalTodoBean) {
            for (TransportTodoListBean tr : mListBeanBegin) {
                if (mOutFieldTaskBean.getFlightNo().equals(mOutFieldTaskBean.getFlightNo())){
                    tr.setTaskId(mOutFieldTaskBean.getId());
                    tr.setBeginAreaType(mOutFieldTaskBean.getBeginAreaType());
                    tr.setBeginAreaId(mOutFieldTaskBean.getBeginAreaId());
                    tr.setEndAreaType(mOutFieldTaskBean.getEndAreaType());
                    tr.setEndAreaId(mOutFieldTaskBean.getEndAreaId());
                    tr.setInSeat(true);
                }
            }
        }
        transportEndEntity.setScooters(mListBeanBegin);

        ((TransportBeginPresenter) mPresenter).transportBegin(transportEndEntity);
    }

    //
    private void doEnd(List <OutFieldTaskBean> OutFieldTaskBeans, List <TransportTodoListBean> mListBeanBegin) {
        mPresenter = new TransportBeginPresenter(this);
        TransportEndEntity transportEndEntity = new TransportEndEntity();

        List<String> tasks = new ArrayList <>();
        for (TransportTodoListBean tr : mListBeanBegin) {
            tr.setInSeat(true);
            for (OutFieldTaskBean mOutFieldTaskBean : OutFieldTaskBeans) {
                if (mOutFieldTaskBean.getFlightNo().equals(tr.getTpFlightNumber())) {
                    tr.setBeginAreaType(mOutFieldTaskBean.getBeginAreaType());
                    tr.setBeginAreaId(mOutFieldTaskBean.getBeginAreaId());
                    tr.setEndAreaId(mOutFieldTaskBean.getEndAreaId());
                    tr.setEndAreaType(mOutFieldTaskBean.getEndAreaType());
                    tr.setTaskId(mOutFieldTaskBean.getId());
                }
            }
        }
        transportEndEntity.setScooters(mListBeanBegin);

        ((TransportBeginPresenter) mPresenter).transportEnd(transportEndEntity);
    }

    @Override
    public void transportBeginResult(String result) {
        if (!"".equals(result)) {
            setTpStatus(0);
        } else
            Log.e("开始失败", "开始失败");
    }

    /**
     * 通过flag 来设置 目前的运输状态
     * @param flag 0 运输中 1 运输结束（未开始运输）
     */
    private void setTpStatus(int flag) {
        tpStatus = flag;
        if (flag == 0) {
            btnBeginEnd.setText("结束");
            llAdd.setVisibility(View.GONE);
            mDriverOutTaskDoingAdapter.setCheckBoxEnable(true);
            mDriverOutTaskDoingAdapter.setIsmIsSlide(false);
        } else {
            if (getMaxHandcarNum() == 0) {
                btnBeginEnd.setText("开始");
                llAdd.setVisibility(View.VISIBLE);
                mDriverOutTaskDoingAdapter.setCheckBoxEnable(false);
                mDriverOutTaskDoingAdapter.setIsmIsSlide(true);
            }
        }
        upDataBtnStatus();
    }

    @Override
    public void transportEndResult(String result) {
        if (!"".equals(result)) {
            getData();
        } else
            Log.e("结束失败", "结束失败");
    }

    @Override
    public void scanScooterDeleteResult(String result) {

        if (!"".equals(result)) {
            if (getMaxHandcarNum() == 5) {
                llAdd.setVisibility(View.VISIBLE);
            }
            getData();
        }

    }

    //板车列表信息
    @Override
    public void scooterWithUserResult(List <TransportTodoListBean> result) {
        if (result != null) {
            listScooter.clear();
            listScooter.addAll(result);
            //所有板车运输结束  同一起点的任务结束
            if (tpStatus == 0){
                if (listScooter.size() < 1){
                    EventBus.getDefault().post("TaskDriverOutFragment_refresh");
                    finish();
                    return;
                }
            }
            // 把终点位置 设置到板车上 方便 下一级列表的 起点和终点显示 （起点已经存在 ）
            for (TransportTodoListBean mTransportTodoListBean: listScooter)
            {
                for (OutFieldTaskBean mOutFieldTaskBean:mAcceptTerminalTodoBean)
                {
                    mTransportTodoListBean.setPlanePlace(mOutFieldTaskBean.getFlights().getSeat());
                    mTransportTodoListBean.setPlaneType(mOutFieldTaskBean.getFlights().getAircraftType());
                    mTransportTodoListBean.setEtd(mOutFieldTaskBean.getFlights().getEtd());
                    mTransportTodoListBean.setFlightRoute(mOutFieldTaskBean.getFlights().getRouters());
                    mTransportTodoListBean.setTpDestinationLocate(MapValue.getLocationValue(mOutFieldTaskBean.getEndAreaType()));
                }

            }

            Map <String, FlightOfScooterBean> mapFlight = new HashMap <>();
            list.clear();
            // 把所有板车 通过 航班号 归类到航班下
            for (TransportTodoListBean mTransportTodoListBean : result) {
                if (mapFlight.get(mTransportTodoListBean.getTpFlightNumber()) == null) {
                    FlightOfScooterBean mFlightOfScooterBean = new FlightOfScooterBean();
                    //把上面绑定到板车上的航班数据 添加到 自己创建的航班对象里面
                    mFlightOfScooterBean.setFlightNo(mTransportTodoListBean.getTpFlightNumber());
                    mFlightOfScooterBean.setPlanePlace(mTransportTodoListBean.getPlanePlace());
                    mFlightOfScooterBean.setPlaneType(mTransportTodoListBean.getPlaneType());
                    mFlightOfScooterBean.setEtd(mTransportTodoListBean.getEtd());
                    mFlightOfScooterBean.setFlightRoute(mTransportTodoListBean.getFlightRoute());

                    List <TransportTodoListBean> listBeans = new ArrayList <>();
                    listBeans.add(mTransportTodoListBean);
                    mFlightOfScooterBean.setMTransportTodoListBeans(listBeans);
                    mapFlight.put(mTransportTodoListBean.getTpFlightNumber(), mFlightOfScooterBean);
                } else {
                    mapFlight.get(mTransportTodoListBean.getTpFlightNumber()).getMTransportTodoListBeans().add(mTransportTodoListBean);
                }
            }


            list.addAll(new ArrayList <FlightOfScooterBean>(mapFlight.values()));

            mDriverOutTaskDoingAdapter.notifyDataSetChanged();
            if (result.size() >= 5) {
                ToastUtil.showToast(this, "最多一次拉5板货");
                llAdd.setVisibility(View.GONE);
            }
            //通过 判断是否 拥有开始时间 来设置 运输的状态
            if (mAcceptTerminalTodoBean.get(0).getTaskBeginTime() > 0)
                setTpStatus(0);
            else
                setTpStatus(1);
        }

    }

    //扫描锁定（添加）
    @Override
    public void scanScooterResult(String result) {
        if (!"".equals(result)) {
            getData();
        }

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
