package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.PopupPrintAdapter;
import qx.app.freight.qxappfreight.adapter.ReceiveGoodsAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.SecurityCheckResult;
import qx.app.freight.qxappfreight.bean.request.TransportListCommitEntity;
import qx.app.freight.qxappfreight.bean.response.AgentBean;
import qx.app.freight.qxappfreight.bean.response.AutoReservoirBean;
import qx.app.freight.qxappfreight.bean.response.DeclareItem;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.AgentTransportationListContract;
import qx.app.freight.qxappfreight.contract.GetWayBillInfoByIdContract;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.contract.TransportListCommitContract;
import qx.app.freight.qxappfreight.presenter.AgentTransportationListPresent;
import qx.app.freight.qxappfreight.presenter.GetWayBillInfoByIdPresenter;
import qx.app.freight.qxappfreight.presenter.ScooterInfoListPresenter;
import qx.app.freight.qxappfreight.presenter.TransportListCommitPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CommonPopupWindow;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionSlideRecylerView;

/**
 * TODO :出港收货页面
 * Created by pr
 */
public class ReceiveGoodsActivity extends BaseActivity implements AgentTransportationListContract.agentTransportationListView, TransportListCommitContract.transportListCommitView, MultiFunctionSlideRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, ScooterInfoListContract.scooterInfoListView, GetWayBillInfoByIdContract.getWayBillInfoByIdView {
    @BindView(R.id.ll_not_transport)
    LinearLayout mLlNotTransport;//跳转到未收运记录查看页面的控件
    @BindView(R.id.tv_not_transport_number)
    TextView mTvNotTransport;//显示未收运记录条数的控件
    @BindView(R.id.mfrv_receive_good)
    MultiFunctionSlideRecylerView mMfrvData;
    @BindView(R.id.btn_receive_good)
    Button mBtnReceiveGood;         //提交
    @BindView(R.id.btn_printing)
    TextView mBtnPrinting;            //打印
    @BindView(R.id.tv_total_number)
    TextView mTvTotalNumber;        //总件数
    @BindView(R.id.tv_total_volume)
    TextView mTvTotalVolume;        //总体积
    @BindView(R.id.tv_total_weight)
    TextView mTvTotalWeight;        //总重量

    private ReceiveGoodsAdapter mAdapter;
    private List<MyAgentListBean> list;
    private String waybillId, taskId, reservoirType, waybillCode, taskTypeCode;
    private CustomToolbar toolbar;
    private List<DeclareItem> mDeclareItemBeans;
    private TransportListCommitEntity transportListCommitEntity;
    private String mScooterCode;
    private int pageCurrent = 1;
    private DeclareWaybillBean mDeclare = new DeclareWaybillBean();
    private List<SecurityCheckResult> mSecuriBean = new ArrayList<>();
    //库区
    private String reservoirName;
    //显示打印预览
    private CommonPopupWindow window;
    ViewPager vp;
    PopupPrintAdapter mPopupPrintAdapter;
    ImageView ivClose;
    ImageView ivLeft;
    ImageView ivRight;
    Button btnPrint;
    int vpPage = 0;
    boolean isPrint = false;
    private String id;
    private String wayBillId;
    private int mTag;
    private int listPostion;

    public static void startActivity(Activity context, String taskId, DeclareWaybillBean declareWaybillBean, String taskTypeCode, String id, String wayBillId) {
        Intent starter = new Intent(context, ReceiveGoodsActivity.class);
        starter.putExtra("taskId", taskId);
        starter.putExtra("DeclareWaybillBean", declareWaybillBean);
        starter.putExtra("taskTypeCode", taskTypeCode);
        starter.putExtra("id", id);
        starter.putExtra("wayBillId", wayBillId);
        context.startActivityForResult(starter, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_receive_goods;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        EventBus.getDefault().register(this);
        mDeclare = (DeclareWaybillBean) getIntent().getSerializableExtra("DeclareWaybillBean");
        taskId = getIntent().getStringExtra("taskId");
        taskTypeCode = getIntent().getStringExtra("taskTypeCode");
        wayBillId = getIntent().getStringExtra("wayBillId");
        id = getIntent().getStringExtra("id");
        waybillId = mDeclare.getId();
        waybillCode = mDeclare.getWaybillCode();
        reservoirType = mDeclare.getColdStorage() + "";
        toolbar = getToolbar();
        mDeclareItemBeans = mDeclare.getDeclareItems();
        toolbar.setRightTextViewImage(this, View.VISIBLE, R.color.flight_a, "新增", R.mipmap.new_2, v -> {
            mTag = 1;
            startAct(new MyAgentListBean(), 1);
        });
        mTvNotTransport.setText(String.valueOf(mSecuriBean.size()));
        mLlNotTransport.setOnClickListener(v -> {
            String goodsName = mDeclare.getCargoCn();
            Intent intent = new Intent(ReceiveGoodsActivity.this, NotTransportListActivity.class);
            intent.putExtra("goods_name", goodsName);
            Bundle bundle = new Bundle();
            bundle.putSerializable("start_data", (ArrayList<SecurityCheckResult>) mSecuriBean);
            intent.putExtras(bundle);
            ReceiveGoodsActivity.this.startActivityForResult(intent, 123);
        });
        //提交
        mBtnReceiveGood.setOnClickListener(v -> commit());
        getAutoReservoir();
        initView();
        initPopupWindow();
    }

    public void getAutoReservoir() {
        mPresenter = new AgentTransportationListPresent(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setDeptCode(UserInfoSingle.getInstance().getDeptCode());
        entity.setReservoirType(Integer.valueOf(reservoirType));
        ((AgentTransportationListPresent) mPresenter).autoReservoirv(entity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (result.getFunctionFlag().equals("ReceiveGoodsActivity")) {
            mTag = 1;
            startAct(new MyAgentListBean(), 1);
        } else {
            Log.e("resultCode", "收货页面不是200");
        }
    }

    public void startAct(MyAgentListBean myAgentListBean, int tag) {
        AddReceiveGoodActivity.startActivity(ReceiveGoodsActivity.this, waybillId, waybillCode, mDeclare.getCargoCn(), myAgentListBean, tag,wayBillId,taskTypeCode,id);
    }


    //提交
    public void commit() {
        mPresenter = new TransportListCommitPresenter(this);
        if (list.size() != 0) {
            transportListCommitEntity = new TransportListCommitEntity();
            transportListCommitEntity.setType("1");//1提交
            transportListCommitEntity.setTaskId(taskId);//当前任务id
            transportListCommitEntity.setUserId(UserInfoSingle.getInstance().getUserId());//当前操作人
            transportListCommitEntity.setUserName(UserInfoSingle.getInstance().getUsername());
            transportListCommitEntity.setWaybillId(wayBillId);
            transportListCommitEntity.setWaybillInfo(mDeclare);
            transportListCommitEntity.setSecurityResultList(mSecuriBean);
            List<TransportListCommitEntity.RcInfosEntity> mListRcInfosEntity = new ArrayList<>();
            for (MyAgentListBean mMyAgentListBean : list) {
                TransportListCommitEntity.RcInfosEntity rcInfosEntity = new TransportListCommitEntity.RcInfosEntity();
                rcInfosEntity.setAddOrderId(id);
                rcInfosEntity.setTaskTypeCode(taskTypeCode);
                rcInfosEntity.setId(mMyAgentListBean.getId());
                rcInfosEntity.setCargoId(mMyAgentListBean.getCargoId());
                rcInfosEntity.setCargoCn(mMyAgentListBean.getCargoCn());
                rcInfosEntity.setReservoirName(mMyAgentListBean.getReservoirName());
                rcInfosEntity.setReservoirType(reservoirType);
                rcInfosEntity.setWaybillId(mMyAgentListBean.getWaybillId());
                rcInfosEntity.setRepName(reservoirName);
                rcInfosEntity.setWaybillCode(mMyAgentListBean.getWaybillCode());
                rcInfosEntity.setNumber(mMyAgentListBean.getNumber());
                rcInfosEntity.setWeight((int) mMyAgentListBean.getWeight());
                rcInfosEntity.setVolume(mMyAgentListBean.getVolume());
                rcInfosEntity.setPackagingType(mMyAgentListBean.getPackagingType());
                rcInfosEntity.setScooterId(mMyAgentListBean.getScooterId());
                rcInfosEntity.setUldId(mMyAgentListBean.getUldId());
                rcInfosEntity.setOverWeight(mMyAgentListBean.getOverWeight());
                rcInfosEntity.setRepType(mMyAgentListBean.getRepType());
                rcInfosEntity.setRepPlaceId(mMyAgentListBean.getRepPlaceId());

                rcInfosEntity.setId(id);
                rcInfosEntity.setCargoId(mMyAgentListBean.getCargoId());
                rcInfosEntity.setCargoCn(mMyAgentListBean.getCargoCn());
                rcInfosEntity.setNumber(mMyAgentListBean.getNumber());
                rcInfosEntity.setWeight(mMyAgentListBean.getWeight());
                rcInfosEntity.setVolume(mMyAgentListBean.getVolume());
                rcInfosEntity.setScooterId(mMyAgentListBean.getScooterId());
                rcInfosEntity.setScooterType(mMyAgentListBean.getScooterType());
                rcInfosEntity.setScooterCode(mMyAgentListBean.getScooterCode());
                rcInfosEntity.setScooterWeight(mMyAgentListBean.getScooterWeight());
                rcInfosEntity.setRepName(mMyAgentListBean.getRepName());
                rcInfosEntity.setRepPlaceNum(mMyAgentListBean.getRepPlaceNum());
                rcInfosEntity.setUldId(mMyAgentListBean.getUldId());
                rcInfosEntity.setUldCode(mMyAgentListBean.getUldCode());
                rcInfosEntity.setUldType(mMyAgentListBean.getUldType());
                rcInfosEntity.setUldWeight(mMyAgentListBean.getUldWeight());
                rcInfosEntity.setOverWeight(mMyAgentListBean.getOverWeight());
                rcInfosEntity.setDelFlag(mMyAgentListBean.getDelFlag());
                mListRcInfosEntity.add(rcInfosEntity);
            }
            transportListCommitEntity.setRcInfos(mListRcInfosEntity);
            transportListCommitEntity.setTaskTypeCode(taskTypeCode);
            transportListCommitEntity.setAddOrderId(id);
            ((TransportListCommitPresenter) mPresenter).transportListCommit(transportListCommitEntity);
        } else {
            ToastUtil.showToast(this, "请先添加数据");
        }
    }

    public void upData() {
        mPresenter = new AgentTransportationListPresent(this);
        BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
        MyAgentListBean myAgentListBean = new MyAgentListBean();
        myAgentListBean.setWaybillId(waybillId);
        myAgentListBean.setTaskTypeCode(taskTypeCode);
        baseFilterEntity.setSize(10);
        baseFilterEntity.setCurrent(pageCurrent);
        baseFilterEntity.setFilter(myAgentListBean);
        ((AgentTransportationListPresent) mPresenter).agentTransportationList(baseFilterEntity);
    }

    public void initView() {
        mTvTotalNumber.setText("总件数:" + 0 + "");
        mTvTotalVolume.setText("总体积:" + 0 + "m³");
        mTvTotalWeight.setText("总重量" + 0 + "kg");
        mMfrvData.setLayoutManager(new LinearLayoutManager(this));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        list = new ArrayList<>();
        mAdapter = new ReceiveGoodsAdapter(list);
        mMfrvData.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            mTag = 2;
            listPostion = position;
            startAct(list.get(position), 2);
        });
        mAdapter.setOnDeleteClickListener((view, position) -> {
            if (list.size() != 0) {
                mPresenter = new TransportListCommitPresenter(this);
                mMfrvData.closeMenu();
                ((TransportListCommitPresenter) mPresenter).deleteCollectionInfo(list.get(position).getId());
//                Toast.makeText(ReceiveGoodsActivity.this, "当前删除：" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mBtnPrinting.setOnClickListener(v -> {
            if (list.size() > 0)
                showPopWindowList();
            else
                ToastUtil.showToast("请先添加收运记录");
        });
        upData();
    }

    @Override
    public void onRefresh() {
        pageCurrent = 1;
        upData();
    }

    @Override
    public void onLoadMore() {
        upData();
    }

    @Override
    public void agentTransportationListResult(AgentBean myAgentListBean) {
        toolbar.setMainTitle(Color.WHITE, "出港收货" + "(" + myAgentListBean.getRcInfo().size() + ")");
        if (myAgentListBean != null) {
            if (pageCurrent == 1) {
                list.clear();
                mMfrvData.finishRefresh();
            } else {
                pageCurrent++;
                mMfrvData.finishLoadMore();
            }
            list.addAll(myAgentListBean.getRcInfo());
            int number = 0;
            int weight = 0;
            int volume = 0;
            for (MyAgentListBean myAgentListBean1 : list) {
                number += myAgentListBean1.getNumber();
                weight += myAgentListBean1.getWeight();
                volume += myAgentListBean1.getVolume();
                if (!"".equals(reservoirName))
                    myAgentListBean1.setReservoirName(reservoirName);
            }
            mTvTotalNumber.setText("总件数:" + number + "");
            mTvTotalVolume.setText("总体积:" + volume + "m³");
            mTvTotalWeight.setText("总重量" + weight + "kg");
            mMfrvData.notifyForAdapter(mAdapter);
        } else {
            ToastUtil.showToast(this, "数据错误");
        }
    }

    @Override
    public void autoReservoirvResult(AutoReservoirBean myAgentListBean) {
        if (null != myAgentListBean) {
            reservoirName = myAgentListBean.getReservoirName();

        } else
            ToastUtil.showToast("数据为空");
    }

    @Override
    public void transportListCommitResult(String result) {
        if (!"".equals(result)) {
            EventBus.getDefault().post("collector_refresh");
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    //删除板车
    @Override
    public void deleteCollectionInfoResult(String result) {
        upData();
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void toastView(String error) {
        Log.e("refresh", error);
        if (pageCurrent == 1) {
            mMfrvData.finishRefresh();
        } else {
            mMfrvData.finishLoadMore();
        }
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 123 && resultCode == RESULT_OK) {
            if (data != null && ((ArrayList<SecurityCheckResult>) data.getSerializableExtra("not_transport_list")).size() != 0) {
                mSecuriBean.clear();
                mSecuriBean.addAll((ArrayList<SecurityCheckResult>) data.getSerializableExtra("not_transport_list"));
                mTvNotTransport.setText(String.valueOf(mSecuriBean.size()));
            }
        }
        if (resultCode == 200) {
            MyAgentListBean mMyAgentListBean = (MyAgentListBean) data.getSerializableExtra("mMyAgentListBean");
            int number = 0;
            int weight = 0;
            int volume = 0;
            if (mTag == 2) {
                //总重量，总体积，总件数 计算
                if (mMyAgentListBean != null) {
                    list.set(listPostion, mMyAgentListBean);
                }
            } else if (mTag == 1) {
//                for (MyAgentListBean item : list) {
//                    if (mMyAgentListBean.getScooterCode().equals(item.getScooterCode())) {
//                        ToastUtil.showToast("请勿添加重复的板车");
//                        return;
//                    }
//                }
                list.add(mMyAgentListBean);
            }
            toolbar.setMainTitle(Color.WHITE, "出港收货" + "(" + list.size() + ")");
            for (int i = 0; i < list.size(); i++) {
                number += list.get(i).getNumber();
                weight += list.get(i).getWeight();
                volume += list.get(i).getVolume();
                if (!"".equals(reservoirName))
                    mMyAgentListBean.setReservoirName(reservoirName);
            }
            mTvTotalNumber.setText("总件数:" + number + "");
            mTvTotalVolume.setText("总体积:" + volume + "m³");
            mTvTotalWeight.setText("总重量" + weight + "kg");
            mMfrvData.notifyForAdapter(mAdapter);
        } else if (Constants.SCAN_RESULT == resultCode) {
            mScooterCode = data.getStringExtra(Constants.SACN_DATA);
            if (!"".equals(mScooterCode)) {
                getNumberInfo(mScooterCode);
            } else {
                ToastUtil.showToast(ReceiveGoodsActivity.this, "扫码数据为空请重新扫码");
            }
        } else {
            Log.e("resultCode", "收货页面不是200");
        }
    }

    //获取板车信息
    public void getNumberInfo(String str) {
        mPresenter = new ScooterInfoListPresenter(this);
        BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
        MyAgentListBean myAgentListBean = new MyAgentListBean();
        baseFilterEntity.setSize(10);
        baseFilterEntity.setCurrent(1);
        myAgentListBean.setScooterCode(str);
        baseFilterEntity.setFilter(myAgentListBean);
        ((ScooterInfoListPresenter) mPresenter).ScooterInfoList(baseFilterEntity);
    }

    @Override
    public void onRetry() {
        showProgessDialog("加载数据中……");
        new Handler().postDelayed(() -> {
            upData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void scooterInfoListResult(List<ScooterInfoListBean> scooterInfoListBeans) {
//        if (scooterInfoListBeans.size() != 0)
        mTag = 1;
        startAct(new MyAgentListBean(), 1);
//        else
//            ToastUtil.showToast("当前板车号不正确请重新输入");

    }

    @Override
    public void existResult(MyAgentListBean existBean) {
    }

    @Override
    public void addInfoResult(MyAgentListBean result) {

    }

    @Override
    public void getWayBillInfoByIdResult(DeclareWaybillBean result) {

    }

    //打印成功
    @Override
    public void sendPrintMessageResult(String result) {
        isPrint = true;
        ToastUtil.showToast(result);
    }

    private void printWayBill() {
        mPresenter = new GetWayBillInfoByIdPresenter(this);
        ((GetWayBillInfoByIdPresenter) mPresenter).sendPrintMessage(waybillId);
    }

    /**
     * 初始化popWindow
     */
    private void initPopupWindow() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        window = new CommonPopupWindow(this, R.layout.popup_print_fr, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) {
            @Override
            protected void initView() {

                View view = getContentView();
                ivClose = view.findViewById(R.id.iv_close);
                ivLeft = view.findViewById(R.id.iv_left);
                ivRight = view.findViewById(R.id.iv_right);
                btnPrint = view.findViewById(R.id.btn_print);
                vp = (ViewPager) view.findViewById(R.id.view_pager);
                mPopupPrintAdapter = new PopupPrintAdapter(ReceiveGoodsActivity.this, list);
                vp.setAdapter(mPopupPrintAdapter);
                vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {

                    }

                    @Override
                    public void onPageSelected(int i) {
                        vpPage = i;
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {

                    }
                });
                ivClose.setOnClickListener((v) -> {
                    dismissPopWindows();
                });
                ivLeft.setOnClickListener((v) -> {
                    if (vpPage != 0) {
                        vpPage--;
                        vp.setCurrentItem(vpPage);
                    }

                });
                ivRight.setOnClickListener((v) -> {
                    if (vpPage < list.size()) {
                        vpPage++;
                        vp.setCurrentItem(vpPage);
                    }
                });
                btnPrint.setOnClickListener((v) -> {
                    printWayBill();
                });
            }

            @Override
            protected void initEvent() {

            }

            @Override
            protected void initWindow() {
                super.initWindow();
                PopupWindow instance = getPopupWindow();
                instance.setOnDismissListener(() -> {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1.0f;
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getWindow().setAttributes(lp);
                });
            }
        };
    }


    private void showPopWindowList() {
        mPopupPrintAdapter.notifyDataSetChanged();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        window.getPopupWindow().setAnimationStyle(R.style.animTranslate);
        window.showAtLocation(mBtnPrinting, Gravity.BOTTOM, 0, 0);
    }

    private void dismissPopWindows() {

        window.getPopupWindow().dismiss();

    }
}
