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
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.TpFlightStep;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.response.AcceptTerminalTodoBean;
import qx.app.freight.qxappfreight.bean.response.FlightOfScooterBean;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.bean.response.TransportTodoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.ScanScooterContract;
import qx.app.freight.qxappfreight.contract.TransportBeginContract;
import qx.app.freight.qxappfreight.presenter.ScanScooterPresenter;
import qx.app.freight.qxappfreight.presenter.TransportBeginPresenter;
import qx.app.freight.qxappfreight.utils.DeviceInfoUtil;
import qx.app.freight.qxappfreight.utils.MapValue;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 外场押运界面
 */
public class DriverOutDoingActivity extends BaseActivity implements TransportBeginContract.transportBeginView, ScanScooterContract.scanScooterView {
    @BindView(R.id.ll_add)
    LinearLayout llAdd;

    @BindView(R.id.rv_car_doing)
    RecyclerView doingRecyclerView;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_error_report)
    TextView tvErrorReport;
    @BindView(R.id.image_scan)
    ImageView imageScan;

    @BindView(R.id.tv_tp_status)
    TextView tvTpStatus;

    private List <FlightOfScooterBean> list;
    private List <TransportTodoListBean> listScooter;
    private DriverOutTaskDoingAdapter mDriverOutTaskDoingAdapter;

    private List<String> flightNumList = new ArrayList<>();

    private int tpStatus = 1; // 0 运输中 1 运输结束
    private List <OutFieldTaskBean> mAcceptTerminalTodoBean;

    private int tpNum = 0; //这个人最多拉的板

    private String transfortType; //该任务 只能拉什么类型的板

    public static void startActivity(Context context, List <OutFieldTaskBean> mTasksBean,String transfortType) {
        Intent starter = new Intent(context, DriverOutDoingActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("acceptTerminalTodoBean", (Serializable) mTasksBean);
        starter.putExtra("transfortType",transfortType);
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
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        listScooter = new ArrayList <>();
        mAcceptTerminalTodoBean = (List <OutFieldTaskBean>) getIntent().getSerializableExtra("acceptTerminalTodoBean");

        transfortType = getIntent().getStringExtra("transfortType");

        if (mAcceptTerminalTodoBean != null){
           tpNum = 0;
           for (OutFieldTaskBean mOutFieldTaskBean:mAcceptTerminalTodoBean){
               tpNum += mOutFieldTaskBean.getNum();
           }

       }

        doingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list = new ArrayList <>();
        mDriverOutTaskDoingAdapter = new DriverOutTaskDoingAdapter(list);
        doingRecyclerView.setAdapter(mDriverOutTaskDoingAdapter);
        mDriverOutTaskDoingAdapter.setOnDeleteClickListener((view, parentPosition, childPosition) -> {

            deleteHandcar(list.get(parentPosition).getMTransportTodoListBeans().get(childPosition));

        });
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

            upDataBtnStatusEnd();
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
        ((ScanScooterPresenter) mPresenter).scooterWithUser(UserInfoSingle.getInstance().getUserId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (tpStatus == 0){
            ToastUtil.showToast("运输已经开始，无法再次扫版");
            return;
        }
        if (listScooter.size() >= tpNum){

            ToastUtil.showToast("任务只分配给你"+tpNum+"个板车");
            return;
        }
        if (getClass().getSimpleName().equals(result.getFunctionFlag())) {
            //根据扫一扫获取的板车信息查找板车内容
            addScooterInfo(result.getData());
        }
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
//            mainIfos.setTpOperator("u6911330e59ce46c288181ed11a48ee23");
            mainIfos.setTpOperator(UserInfoSingle.getInstance().getUserId());
            mainIfos.setTpScooterType(transfortType);
            mainIfos.setTpStartLocate(mAcceptTerminalTodoBean.get(0).getBeginAreaType());
            ((ScanScooterPresenter) mPresenter).scanScooter(mainIfos);
        } else
            ToastUtil.showToast("扫描结果为空请重新扫描");
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
        if (getMaxHandcarNum() == tpNum) {
            tvStart.setClickable(true);
            tvStart.setBackgroundResource(R.drawable.btn_blue_press);
        } else {
            tvStart.setClickable(false);
            tvStart.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }
    /**
     * 结束按钮是否可以点击
     */
    private void upDataBtnStatusEnd() {
        btnBeginEnd.setClickable(false);
        btnBeginEnd.setBackgroundColor(getResources().getColor(R.color.gray));
        for (FlightOfScooterBean mFlightOfScooterBean :list){
            if (mFlightOfScooterBean.isSelect()) {
                btnBeginEnd.setClickable(true);
                btnBeginEnd.setBackgroundResource(R.drawable.btn_blue_press);
                break;
            }
        }

    }

    @OnClick({R.id.ll_add, R.id.tv_start, R.id.tv_error_report})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_add:
                ScanManagerActivity.startActivity(this);
                break;
            case R.id.tv_error_report:
                flightNumList.clear();
                for (FlightOfScooterBean item: list) {
                    if (!flightNumList.contains(item.getFlightNo())){
                        flightNumList.add(item.getFlightNo());
                    }
                }
                if (!flightNumList.isEmpty()) {
                    Intent intent = new Intent(this, ErrorReportActivity.class);
                    intent.putStringArrayListExtra("plane_info_list", (ArrayList<String>) flightNumList);
                    intent.putExtra("error_type", 4);
                    startActivity(intent);
                }
                break;
            case R.id.tv_start:
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

        List<TpFlightStep> steps = new ArrayList <>();
        for (OutFieldTaskBean mOutFieldTaskBean : mAcceptTerminalTodoBean) {
            for (TransportTodoListBean tr : mListBeanBegin) {
                if (mOutFieldTaskBean.getFlightNo().equals(mOutFieldTaskBean.getFlightNo())){
                    tr.setTaskId(mOutFieldTaskBean.getId());
                    tr.setBeginAreaType(mOutFieldTaskBean.getBeginAreaType());
                    tr.setBeginAreaId(mOutFieldTaskBean.getBeginAreaId());
                    tr.setEndAreaType(mOutFieldTaskBean.getEndAreaType());
                    tr.setEndAreaId(mOutFieldTaskBean.getEndAreaId());
                    tr.setInSeat(1);
                }
            }
            TpFlightStep step = new TpFlightStep();
            step.setType(0);
            step.setLoadUnloadDataId(mOutFieldTaskBean.getId());
            step.setFlightId(Long.valueOf(mOutFieldTaskBean.getFlightId()));
            step.setFlightTaskId(mOutFieldTaskBean.getTaskId());
            step.setLatitude((Tools.getGPSPosition()==null)?"":Tools.getGPSPosition().getLatitude());
            step.setLongitude((Tools.getGPSPosition()==null)?"":Tools.getGPSPosition().getLongitude());
            step.setOperationCode("CargoOutTransportStart");
            step.setUserName(UserInfoSingle.getInstance().getUsername());
            step.setTerminalId(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
            step.setUserId(UserInfoSingle.getInstance().getUserId());
            step.setCreateTime(System.currentTimeMillis());
            step.setDeptId(UserInfoSingle.getInstance().getDepId());

            steps.add(step);
        }
        transportEndEntity.setScooters(mListBeanBegin);
        transportEndEntity.setSteps(steps);

        ((TransportBeginPresenter) mPresenter).transportBegin(transportEndEntity);
    }

    //
    private void doEnd(List <OutFieldTaskBean> OutFieldTaskBeans, List <TransportTodoListBean> mListBeanBegin) {
        mPresenter = new TransportBeginPresenter(this);
        TransportEndEntity transportEndEntity = new TransportEndEntity();
        for (TransportTodoListBean tr : mListBeanBegin) {
            tr.setInSeat(1);
            for (OutFieldTaskBean mOutFieldTaskBean : OutFieldTaskBeans) {
                if (mOutFieldTaskBean.getFlightNo().equals(tr.getTpFlightNumber())) {
                    tr.setBeginAreaType(mOutFieldTaskBean.getBeginAreaType());
                    tr.setBeginAreaId(mOutFieldTaskBean.getBeginAreaId());
                    tr.setEndAreaId(mOutFieldTaskBean.getEndAreaId());
                    tr.setEndAreaType(mOutFieldTaskBean.getEndAreaType());
                    tr.setTaskId(mOutFieldTaskBean.getId());
                    tr.setAcdmDtoId(mOutFieldTaskBean.getAcdmDtoId());
                }
            }
        }
        List<TpFlightStep> steps = new ArrayList <>();
        for (OutFieldTaskBean mOutFieldTaskBean : OutFieldTaskBeans) {

            TpFlightStep step = new TpFlightStep();
            step.setType(0);
            step.setLoadUnloadDataId(mOutFieldTaskBean.getId());
            step.setFlightId(Long.valueOf(mOutFieldTaskBean.getFlightId()));
            step.setFlightTaskId(mOutFieldTaskBean.getTaskId());
            step.setLatitude((Tools.getGPSPosition()==null)?"":Tools.getGPSPosition().getLatitude());
            step.setLongitude((Tools.getGPSPosition()==null)?"":Tools.getGPSPosition().getLongitude());
            step.setOperationCode("CargoOutTransportEnd");
            step.setUserName(UserInfoSingle.getInstance().getUsername());
            step.setTerminalId(DeviceInfoUtil.getDeviceInfo(this).get("deviceId"));
            step.setUserId(UserInfoSingle.getInstance().getUserId());
            step.setCreateTime(System.currentTimeMillis());
            step.setDeptId(UserInfoSingle.getInstance().getDepId());

            steps.add(step);
        }
        transportEndEntity.setScooters(mListBeanBegin);
        transportEndEntity.setSteps(steps);

        ((TransportBeginPresenter) mPresenter).transportEnd(transportEndEntity);
    }

    @Override
    public void transportBeginResult(String result) {
        if (!"".equals(result)) {
            setTpStatus(0);
            upDataBtnStatusEnd();
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
            tvStart.setText("结束");
            llAdd.setVisibility(View.GONE);
            mDriverOutTaskDoingAdapter.setCheckBoxEnable(true);
            mDriverOutTaskDoingAdapter.setIsmIsSlide(false);
            tvTpStatus.setVisibility(View.VISIBLE);
        } else {
            if (getMaxHandcarNum() == 0) {
                tvStart.setText("开始");
                llAdd.setVisibility(View.VISIBLE);
                mDriverOutTaskDoingAdapter.setCheckBoxEnable(false);
                mDriverOutTaskDoingAdapter.setIsmIsSlide(true);
                tvTpStatus.setVisibility(View.GONE);
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
                    mTransportTodoListBean.setTpDestinationLocate(mOutFieldTaskBean.getEndAreaType());
                    mTransportTodoListBean.setNum(mOutFieldTaskBean.getNum());
                    mTransportTodoListBean.setCarType(mOutFieldTaskBean.getCargoType());
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
                    mFlightOfScooterBean.setCarType(mTransportTodoListBean.getCarType());
                    mFlightOfScooterBean.setNum(mTransportTodoListBean.getNum());

                    List <TransportTodoListBean> listBeans = new ArrayList <>();
                    listBeans.add(mTransportTodoListBean);
                    mFlightOfScooterBean.setMTransportTodoListBeans(listBeans);
                    mapFlight.put(mTransportTodoListBean.getTpFlightNumber(), mFlightOfScooterBean);
                } else {
                    mapFlight.get(mTransportTodoListBean.getTpFlightNumber()).getMTransportTodoListBeans().add(mTransportTodoListBean);
                }
            }

            list.addAll(new ArrayList <FlightOfScooterBean>(mapFlight.values()));
            mapFlight.clear();

            mDriverOutTaskDoingAdapter.notifyDataSetChanged();
//            if (result.size() >= tpNum)
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
