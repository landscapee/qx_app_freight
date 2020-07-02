package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.FlightLineCheckAdapter;
import qx.app.freight.qxappfreight.adapter.SortingInfoAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.FlightLineBean;
import qx.app.freight.qxappfreight.bean.InWaybillRecord;
import qx.app.freight.qxappfreight.bean.ReservoirArea;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordGetEntity;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitNewEntity;
import qx.app.freight.qxappfreight.bean.request.ScooterArriveNumChangeEntity;
import qx.app.freight.qxappfreight.bean.response.InWaybillRecordBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.contract.InWaybillRecordContract;
import qx.app.freight.qxappfreight.contract.ListReservoirInfoContract;
import qx.app.freight.qxappfreight.dialog.ChooseStoreroomDialog2;
import qx.app.freight.qxappfreight.presenter.InWaybillRecordPresenter;
import qx.app.freight.qxappfreight.presenter.ListReservoirInfoPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 进港分拣 - 显示列表界面
 * <p>
 * create by guohao - 2019/4/26
 */
public class SortingActivity extends BaseActivity implements InWaybillRecordContract.inWaybillRecordView, ListReservoirInfoContract.listReservoirInfoView {
    @BindView(R.id.recycler_view)
    SlideRecyclerView recyclerView;
    @BindView(R.id.ll_re_weight)
    LinearLayout llReWeight;
    @BindView(R.id.edt_re_weight)
    EditText edtReWeight;
    @BindView(R.id.tv_flight_no)
    TextView flightNoTv;
    @BindView(R.id.rv_flight_line)
    RecyclerView flightLineRv;
    @BindView(R.id.tv_arrive_time)
    TextView arriveTimeTv;
    @BindView(R.id.tv_handcar_num)
    TextView handCarNumTv;
    @BindView(R.id.tv_handcar_total)
    TextView handCarTotalTv;
    @BindView(R.id.btn_temp)
    Button tempBtn;
    @BindView(R.id.btn_done)
    Button doneBtn;
    //recycler 页脚
    private TextView tvFoot;
    private String flightNo = "";
    private String arriveTime = "";
    private String handCarNum = "";
    private String getHandCarNumTotal = "";
    //从上一个页面传递过来的数据
    private TransportDataBase transportListBean;
    //需要显示的数据delFlag == 0
    private List<InWaybillRecord> mList = new ArrayList<>();
    //删除的数据delFlag == 1
    private List<InWaybillRecord> mListDel = new ArrayList<>();
    //最终提交请求的实体
    private InWaybillRecordSubmitNewEntity entity = new InWaybillRecordSubmitNewEntity();
    //网络请求返回的bean
    private InWaybillRecordBean resultBean;
    private int CURRENT_DELETE_POSITION = -1;
    public static final String TYPE_ADD = "ADD";
    public static final String TYPE_UPDATE = "UPDATE";
    private SortingInfoAdapter mAdapter;
    private List<ChooseStoreroomDialog2.TestBean> mTestBeanList = new ArrayList<>();
    private int mConfirmPos = -1;
    private String selectLine;
    private String changedWaybillCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sorting;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        transportListBean = (TransportDataBase) intent.getSerializableExtra("TASK_INFO");
        //从前一个页面传递过来的数据
        flightNo = transportListBean.getFlightNo();
        arriveTime = TimeUtils.getTime2(transportListBean.getAta());
        handCarNum = transportListBean.getArriveWarehouseNum() + "";
        getHandCarNumTotal = transportListBean.getTotalScooterNum() + "";
        //显示传递的数据
        flightNoTv.setText(flightNo);
        arriveTimeTv.setText(arriveTime);
        handCarNumTv.setText(handCarNum);
        handCarTotalTv.setText("/" + getHandCarNumTotal);
        //toolbar
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setMainTitle(Color.WHITE, "进港理货");
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "上一步", listener -> {
            finish();
        });
        //右侧添加按钮
        toolbar.setRightIconView(View.VISIBLE, R.mipmap.add_bg, listener -> {
            //跳转到 ->新增页面
            if (resultBean == null) {
                return;
            }
            if (resultBean.getCloseFlag() == 1) {
                ToastUtil.showToast("运单已经关闭，无法编辑！");
                return;
            }
            Intent intentAdd = new Intent(SortingActivity.this, SortingAddActivity.class);
            intentAdd.putExtra("TYPE", TYPE_ADD);
            intentAdd.putExtra("FLIGHTNo", flightNo);
            Bundle bundle = new Bundle();
            if (mTestBeanList.size() > 0) {
                bundle.putSerializable("mTestBeanList", (Serializable) mTestBeanList);
            } else {
                ToastUtil.showToast("未获取到库区");
                return;
            }
            intentAdd.putExtras(bundle);
            startActivityForResult(intentAdd, 1);
        });
        //暂存，提交请求
        tempBtn.setOnClickListener(listener -> {
            //如果是包机L  货包 H  必须输入整机复重重量
            if (resultBean.getFlightType().equals("L") || resultBean.getFlightType().equals("H")) {
                if (TextUtils.isEmpty(edtReWeight.getText().toString())) {
                    ToastUtil.showToast("请输入整机复重重量");
                    return;
                }
                entity.setCharterReWeight(edtReWeight.getText().toString());
            }
            mPresenter = new InWaybillRecordPresenter(this);
            CommonDialog dialog = new CommonDialog(this);
            dialog.setTitle("提示")
                    .setMessage("确认暂存吗？")
                    .setPositiveButton("确定")
                    .setNegativeButton("取消")
                    .isCanceledOnTouchOutside(false)
                    .isCanceled(true)
                    .setOnClickListener((dialog1, confirm) -> {
                        if (confirm) {
                            if (mList == null || mList.size() == 0) {
                                ToastUtil.showToast("请添加分拣信息");
                                return;
                            }
                            entity.setFlag(0);
                            ((InWaybillRecordPresenter) mPresenter).submit(entity);
                        }
                    })
                    .show();
        });
        //提交请求
        doneBtn.setOnClickListener(listener -> {
            //如果是包机L  货包 H  必须输入整机复重重量
            if (resultBean.getFlightType().equals("L") || resultBean.getFlightType().equals("H")) {
                if (TextUtils.isEmpty(edtReWeight.getText().toString())) {
                    ToastUtil.showToast("请输入整机复重重量");
                    return;
                }
                entity.setCharterReWeight(edtReWeight.getText().toString());
            }
            mPresenter = new InWaybillRecordPresenter(this);
            CommonDialog dialog = new CommonDialog(this);
            dialog.setTitle("提示")
                    .setMessage("确认提交？")
                    .setPositiveButton("确定")
                    .setNegativeButton("取消")
                    .isCanceledOnTouchOutside(false)
                    .isCanceled(true)
                    .setOnClickListener((dialog12, confirm) -> {
                        if (confirm) {
                            if (mList == null || mList.size() == 0) {
                                ToastUtil.showToast("请添加分拣信息");
                                return;
                            }
                            entity.setFlag(1);
                            ((InWaybillRecordPresenter) mPresenter).submit(entity);
                        }
                    })
                    .show();
        });
        //获取数据
        getData();
    }

    private void getData() {
        //初始化数据
        mPresenter = new InWaybillRecordPresenter(this);
        InWaybillRecordGetEntity entity = new InWaybillRecordGetEntity();
        entity.setTaskFlag(0);
        entity.setFlightInfoId(transportListBean.getFlightInfoId());
        ((InWaybillRecordPresenter) mPresenter).getList(entity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("tagTest", "onActivityResult：" + requestCode + "-" + requestCode);
        //来自新增页面的结果返回
        if (data == null) {
            return;
        }
        InWaybillRecordSubmitNewEntity.SingleLineBean singleLineBean = new InWaybillRecordSubmitNewEntity.SingleLineBean();
        InWaybillRecord mInWaybillRecord = (InWaybillRecord) data.getSerializableExtra("DATA");
        singleLineBean.setDelFlag(mInWaybillRecord.getDelFlag());
        singleLineBean.setWaybillCode(mInWaybillRecord.getWaybillCode());
        String[] code = mInWaybillRecord.getWaybillCode().split("-");
        singleLineBean.setWaybillCodeFirst(code[0]);
        singleLineBean.setWaybillCodeSecond(code[1]);
        singleLineBean.setTallyingTotal(String.valueOf(mInWaybillRecord.getTallyingTotal()));
        singleLineBean.setTallyingWeight(String.valueOf(mInWaybillRecord.getTallyingWeight()));
        singleLineBean.setWarehouseArea(mInWaybillRecord.getWarehouseArea());
        singleLineBean.setWarehouseAreaDisplay(mInWaybillRecord.getWarehouseAreaDisplay());
        singleLineBean.setWarehouseAreaType(mInWaybillRecord.getWarehouseAreaType());
        singleLineBean.setWarehouseLocation(mInWaybillRecord.getWarehouseLocation());
        singleLineBean.setRemark(mInWaybillRecord.getRemark());
        singleLineBean.setTransit(String.valueOf(mInWaybillRecord.getTransit()));
        singleLineBean.setCounterUbnormalGoodsList(mInWaybillRecord.getCounterUbnormalGoodsList());
        singleLineBean.setMailType(mInWaybillRecord.getMailType());
        singleLineBean.setAllArrivedFlag(mInWaybillRecord.getAllArrivedFlag());
        singleLineBean.setStray(mInWaybillRecord.getStray());
        singleLineBean.setSurplusNumber("");
        singleLineBean.setSurplusWeight("");
        singleLineBean.setStrayDisabled(false);
        mInWaybillRecord.setOriginatingStationCn(selectLine);
        singleLineBean.setOriginatingStationCn(mInWaybillRecord.getOriginatingStationCn());
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //新增
            if (!TextUtils.isEmpty(mInWaybillRecord.getWaybillCode())) {
                for (InWaybillRecord item : mList) {
                    if (mInWaybillRecord.getWaybillCode().equals(item.getWaybillCode())) {
                        ToastUtil.showToast("不能添加相同运单号");
                        return;
                    }
                }
            }
            mList.add(mInWaybillRecord);
            List<InWaybillRecord> showList = new ArrayList<>();
            for (InWaybillRecord record : mList) {
                if (record.getOriginatingStationCn().equals(selectLine)) {
                    showList.add(record);
                }
            }
            mAdapter.setNewData(showList);
            //更新总运单数，总件数
            resultBean.setCount(resultBean.getCount() + 1);
            resultBean.setTotal(resultBean.getTotal() + mInWaybillRecord.getTallyingTotal());
            tvFoot.setText("总运单数：" + resultBean.getCount() + " 总件数：" + resultBean.getTotal());
            if (entity.getCargos().containsKey(selectLine)) {//提交数据中有此航段的运单列表
                //此航段的运单列表非空
                HashMap<String, List<InWaybillRecordSubmitNewEntity.SingleLineBean>> allData = entity.getCargos();
                List<String> waybillCodeList = new ArrayList<>();
                for (String key : allData.keySet()) {
                    if (allData.get(key) != null) {
                        for (InWaybillRecordSubmitNewEntity.SingleLineBean dataBean : Objects.requireNonNull(allData.get(key))) {
                            waybillCodeList.add(dataBean.getWaybillCode());
                        }
                    }
                }
                if (waybillCodeList.contains(singleLineBean.getWaybillCode())) {
                    ToastUtil.showToast("请不要添加重复的运单号");
                } else {
                    List<InWaybillRecordSubmitNewEntity.SingleLineBean> list = entity.getCargos().get(selectLine);
                    if (list != null) {
                        list.add(singleLineBean);
                    }
                }
            } else {
                List<InWaybillRecordSubmitNewEntity.SingleLineBean> list = new ArrayList<>();
                list.add(singleLineBean);
                entity.getCargos().put(selectLine, list);
            }
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {//修改
            int index = data.getIntExtra("INDEX", -1);
            if (index != -1) {
                //更新总运单数，总件数
                resultBean.setTotal(resultBean.getTotal() - mList.get(index).getTallyingTotal() + mInWaybillRecord.getTallyingTotal());
                tvFoot.setText("总运单数：" + resultBean.getCount() + " 总件数：" + resultBean.getTotal());
                mList.set(index, mInWaybillRecord);
                List<InWaybillRecord> showList = new ArrayList<>();
                for (InWaybillRecord record : mList) {
                    if (record.getOriginatingStationCn().equals(selectLine)) {
                        showList.add(record);
                    }
                }
                mAdapter.setNewData(showList);
                List<InWaybillRecordSubmitNewEntity.SingleLineBean> list = entity.getCargos().get(selectLine);
                if (list == null) {
                    return;
                }
                if (changedWaybillCode.equals(mInWaybillRecord.getWaybillCode())) {//未修改运单号,该运单对应的实体更新数据
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getWaybillCode().equals(singleLineBean.getWaybillCode())) {
                            singleLineBean.setId(list.get(i).getId());
                            list.set(i, singleLineBean);
                            break;
                        }
                    }
                } else {//修改了运单号,删除该运单号对应的实体，再添加新的实体数据
                    Iterator<InWaybillRecordSubmitNewEntity.SingleLineBean> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        InWaybillRecordSubmitNewEntity.SingleLineBean dataBean = (InWaybillRecordSubmitNewEntity.SingleLineBean) iterator.next();
                        if (dataBean.getWaybillCode().equals(singleLineBean.getWaybillCode())) {
                            iterator.remove();
                        }
                    }
                    list.add(singleLineBean);
                }
            }
        }
    }

    @Override
    public void resultGetList(InWaybillRecordBean bean) {
        if (bean == null) {
            resultBean = new InWaybillRecordBean();
        } else {
            resultBean = bean;
            if (bean.getRoute() != null) {
                List<FlightLineBean> list = new ArrayList<>();
                for (int i = 0; i < bean.getRoute().size(); i++) {
                    FlightLineBean lineBean = new FlightLineBean();
                    lineBean.setCheck(i == 0);
                    lineBean.setLine(bean.getRoute().get(i));
                    list.add(lineBean);
                }
                flightLineRv.setLayoutManager(new LinearLayoutManager(SortingActivity.this, LinearLayoutManager.HORIZONTAL, false));
                FlightLineCheckAdapter adapter = new FlightLineCheckAdapter(list);
                flightLineRv.setAdapter(adapter);
                selectLine = bean.getRoute().get(0);
                adapter.setOnItemClickListener((adapter1, view, position) -> {
                    selectLine = list.get(position).getLine();
                    for (FlightLineBean bean1 : list) {
                        bean1.setCheck(false);
                    }
                    list.get(position).setCheck(true);
                    adapter1.notifyDataSetChanged();
                    List<InWaybillRecord> showList = new ArrayList<>();
                    for (InWaybillRecord record : mList) {
                        if (record.getOriginatingStationCn().equals(selectLine)) {
                            showList.add(record);
                        }
                    }
                    mAdapter.setNewData(showList);
                });
            }
        }
        if (resultBean.getList() == null) {
            resultBean.setList(mList);
        } else {
            for (InWaybillRecord item : resultBean.getList()) {
                if (item.getDelFlag() == null || item.getDelFlag() == 0) {
                    mList.add(item);
                } else if (item.getDelFlag() == 1) {
                    mListDel.add(item);
                }
            }
            Log.e("tagTest", "服务器的数据，分拣信息长度 = " + mList.size());
        }
        //如果是包机L  货包 H  让输入框显示出来
        if (resultBean.getFlightType().equals("L") || resultBean.getFlightType().equals("H")) {
            llReWeight.setVisibility(View.VISIBLE);
            if (resultBean.getCharterReWeight() == 0) {
                edtReWeight.setText("");
            } else {
                edtReWeight.setText(resultBean.getCharterReWeight() + "");
            }
        } else {
            llReWeight.setVisibility(View.GONE);
        }
        //初始化提交实体类
        entity.setFlightInfoId(transportListBean.getFlightInfoId());
        entity.setFlightId(transportListBean.getFlightId());
        entity.setTaskId(transportListBean.getTaskId());
        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        entity.setUserName(UserInfoSingle.getInstance().getUsername());
        entity.setFlightNo(transportListBean.getFlightNo());
        entity.setCargos(resultBean.getCargos());
        //列表 -- 初始化
        List<InWaybillRecord> showList = new ArrayList<>();
        for (InWaybillRecord record : mList) {
            if (!TextUtils.isEmpty(record.getOriginatingStationCn()) && record.getOriginatingStationCn().equals(selectLine)) {
                showList.add(record);
            }
        }
        mAdapter = new SortingInfoAdapter(showList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //item点击事件，进入修改
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (resultBean.getCloseFlag() == 1) {
                ToastUtil.showToast("运单已经关闭，无法编辑！");
                return;
            }
            Intent intent = new Intent(SortingActivity.this, SortingAddActivity.class);
            intent.putExtra("TYPE", "UPDATE");
            intent.putExtra("FlightInfoId", transportListBean.getFlightInfoId());
            InWaybillRecord updateInWaybillRecord = null;
            int changeIndex = 0;
            try {
                updateInWaybillRecord = mAdapter.getData().get(position);
                for (int i = 0; i < mList.size(); i++) {
                    if (updateInWaybillRecord.getWaybillCode().equals(mList.get(i).getWaybillCode())) {
                        changeIndex = i;
                        break;
                    }
                }
                changedWaybillCode = updateInWaybillRecord.getWaybillCode();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("tagTest", "updateInWaybillRecord clone error：" + e.getMessage());
            }
            intent.putExtra("DATA", updateInWaybillRecord);
            intent.putExtra("INDEX", changeIndex);
            Bundle bundle = new Bundle();
            if (mTestBeanList.size() > 0) {
                bundle.putSerializable("mTestBeanList", (Serializable) mTestBeanList);
            } else {
                ToastUtil.showToast("未获取到库区");
                return;
            }
            intent.putExtras(bundle);
            //去修改
            SortingActivity.this.startActivityForResult(intent, 2);
        });
        //添加页脚
        tvFoot = new TextView(SortingActivity.this);
        tvFoot.setGravity(Gravity.CENTER);
        tvFoot.setTextColor(Color.parseColor("#FF2E81FD"));
        tvFoot.setTextSize(14);
        tvFoot.setText("总运单数：" + resultBean.getCount() + " 总件数：" + resultBean.getTotal());
        mAdapter.setFooterView(tvFoot);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnAllArriveNotifyListener((item, pos) -> {
            mConfirmPos = pos;
            mPresenter = new InWaybillRecordPresenter(SortingActivity.this);
            item.setFlightInfoId(transportListBean.getFlightInfoId());
            item.setCreateUserName(UserInfoSingle.getInstance().getUsername());
            ((InWaybillRecordPresenter) mPresenter).allGoodsArrived(item);
        });
        mAdapter.setOnInWaybillRecordDeleteListener(position -> {
            //数据被删除了
            CURRENT_DELETE_POSITION = position;
            //更新总运单数，总件数
            resultBean.setCount(resultBean.getCount() - 1);
            resultBean.setTotal(resultBean.getTotal() - mList.get(CURRENT_DELETE_POSITION).getTallyingTotal());
            tvFoot.setText("总运单数：" + resultBean.getCount() + " 总件数：" + resultBean.getTotal());
            //操作数据源
            if (mList.get(CURRENT_DELETE_POSITION).getId() == null || mList.get(CURRENT_DELETE_POSITION).getId() == "" || mList.get(CURRENT_DELETE_POSITION).getId().equals("")) {
                mList.remove(CURRENT_DELETE_POSITION);
            } else {
                //将数据放到删除列表里，delFlag = 1
                mList.get(position).setDelFlag(1);
                mListDel.add(mList.get(position));
                mList.remove(position);
                /*
                不再调用删除接口
                ((InWaybillRecordPresenter) mPresenter).deleteById(mList.get(CURRENT_DELETE_POSITION).getId());
                */
            }
            //刷新列表
            mAdapter.notifyDataSetChanged();
            recyclerView.closeMenu();
        });
        recyclerView.closeMenu();
        getReservoirAll();
    }

    /**
     * 选择库区方法
     */
    private void getReservoirAll() {
        mPresenter = new ListReservoirInfoPresenter(this);
        ((ListReservoirInfoPresenter) mPresenter).listReservoirInfoByCode(UserInfoSingle.getInstance().getDeptCode());
    }

    @Override
    public void resultSubmit(Object o) {
        Log.e("tagTest", "提交/暂存，返回值：" + o.toString());
        ToastUtil.showToast("成功");
        EventBus.getDefault().post("InPortTallyFragment_refresh");//发送消息让前一个页面刷新
        finish();
    }

    @Override
    public void resultDeleteById(Object o) {
        //删除成功，刷新数据
        Log.e("tagTest", "删除成功：" + o.toString());
        mList.remove(CURRENT_DELETE_POSITION);
        mAdapter.notifyDataSetChanged();
        CURRENT_DELETE_POSITION = -1;
    }

    @Override
    public void allGoodsArrivedResult(String o) {
        mList.get(mConfirmPos).setAllArrivedFlag(1);
        mList.get(mConfirmPos).setId(String.valueOf(o));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
        Log.e("tagTest", "错误信息：" + error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("请求中.......");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    /**
     * 激光扫码回调
     */
    private String newCode;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if (result.getFunctionFlag().equals("SortingActivity")) {
            newCode = "";
            String code = result.getData();
            Log.e("tagTest", "运单号" + code);
            if (!TextUtils.isEmpty(code) && code.length() >= 10) {
                if (code.startsWith("DN")) {
                    newCode = "DN-" + code.substring(2, 10);
                } else {
                    if (code.length() >= 11) {
                        newCode = editChange(code);
                    } else {
                        ToastUtil.showToast("无效的运单号");
                        return;
                    }
                }
            } else {
                ToastUtil.showToast("无效的运单号");
                return;
            }
            turnToAddActivity(newCode);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScooterArriveNumChangeEntity result) {
        if (result != null && result.getFlightInfoId().equals(transportListBean.getFlightInfoId())) {
            handCarNumTv.setText(result.getArriveWarehouseNum() + "");
            handCarTotalTv.setText("/" + result.getTotalScooterNum());
        }
    }

    /**
     * 跳转到新增界面
     */
    private void turnToAddActivity(String code) {
        recyclerView.closeMenu();
        boolean isEditChange;
        String[] parts = code.split("-");
        String ss = parts[1];
        String s1 = ""; //后8位的前7位
        String s2 = ""; //后8位的最后1位
        s1 = ss.substring(0, 7);
        s2 = ss.substring(7, 8);
        if (StringUtil.isDouble(s1) && StringUtil.isDouble(s2)) {
            isEditChange = Integer.parseInt(s1) % 7 == Integer.parseInt(s2);
        } else {
            isEditChange = false;
        }
        if (!isEditChange) {
            ToastUtil.showToast("无效的运单号");
            return;
        }
        //跳转到 ->新增页面
        if (resultBean == null) {
            ToastUtil.showToast("运单信息出错，请重新请求");
            return;
        }
        if (resultBean.getCloseFlag() == 1) {
            ToastUtil.showToast("运单已经关闭，无法编辑！");
            return;
        }
        Intent intentAdd = new Intent(SortingActivity.this, SortingAddActivity.class);
        intentAdd.putExtra("TYPE", TYPE_ADD);
        intentAdd.putExtra("newCode", code);
        intentAdd.putExtra("FlightInfoId", transportListBean.getFlightInfoId());
        Bundle bundle = new Bundle();
        if (mTestBeanList.size() > 0) {
            bundle.putSerializable("mTestBeanList", (Serializable) mTestBeanList);
        } else {
            ToastUtil.showToast("未获取到库区");
            return;
        }
        intentAdd.putExtras(bundle);
        startActivityForResult(intentAdd, 1);
    }

    //判断运单号后缀是否符合规则
    private String editChange(String ss) {
        String s0 = ss.substring(0, 3); //前3位
        String s00 = ss.substring(3, 11); //后8位
        return s0 + "-" + s00;
    }

    @Override
    public void listReservoirInfoResult(List<ReservoirArea> acceptTerminalTodoBeanList) {
        Log.e("tagTest", "库区信息\n" + acceptTerminalTodoBeanList.toString());
        //显示库区选择面板
        for (ReservoirArea item : acceptTerminalTodoBeanList) {
            ChooseStoreroomDialog2.TestBean testBean = new ChooseStoreroomDialog2.TestBean(item.getId(), item.getReservoirName());
            testBean.setName(item.getReservoirName());
            mTestBeanList.add(testBean);
        }
    }
}
