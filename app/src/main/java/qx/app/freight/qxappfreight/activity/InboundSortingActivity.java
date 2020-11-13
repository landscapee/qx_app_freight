package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import qx.app.freight.qxappfreight.adapter.InboundSortingAdapter;
import qx.app.freight.qxappfreight.adapter.PagedIndexAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.FlightLineBean;
import qx.app.freight.qxappfreight.bean.PagedIndexBean;
import qx.app.freight.qxappfreight.bean.ReservoirArea;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordGetEntity;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitNewEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.InWaybillRecordBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.WaybillQuickGetBean;
import qx.app.freight.qxappfreight.contract.InWaybillRecordContract;
import qx.app.freight.qxappfreight.contract.ListReservoirInfoContract;
import qx.app.freight.qxappfreight.dialog.ChooseStoreroomDialog2;
import qx.app.freight.qxappfreight.presenter.InWaybillRecordPresenter;
import qx.app.freight.qxappfreight.presenter.ListReservoirInfoPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.RecyclerViewPageHelper;

/**
 * 进港分拣提交页面
 */
public class InboundSortingActivity extends BaseActivity implements InWaybillRecordContract.inWaybillRecordView, ListReservoirInfoContract.listReservoirInfoView {
    @BindView(R.id.rv_flight_line)
    RecyclerView rvFlightLine;
    @BindView(R.id.tv_total_info)
    TextView tvTotalInfo;
    @BindView(R.id.ll_re_weight)
    LinearLayout llReWeight;
    @BindView(R.id.edt_re_weight)
    EditText etReWeight;
    @BindView(R.id.tv_data_empty)
    TextView tvEmpty;
    @BindView(R.id.rv_waybill_list)
    RecyclerView rvWaybillList;
    @BindView(R.id.rv_slide_index)
    RecyclerView rvSlideIndex;
    @BindView(R.id.btn_temp)
    Button btnTemp;
    @BindView(R.id.btn_done)
    Button btnDone;
    //从上一个页面传递过来的数据
    private TransportDataBase baseData;
    //网络请求返回的包含当前航班所有运单数据的实体bean
    private InWaybillRecordBean resultBean;
    //库区数据列表
    private List<ChooseStoreroomDialog2.TestBean> reservoirList = new ArrayList<>();
    //新增运单录入
    public static final String TYPE_ADD_WAYBILL = "ADD";
    //修改运单数据
    public static final String TYPE_UPDATE_WAYBILL = "UPDATE";
    //最终提交请求的实体
    private InWaybillRecordSubmitNewEntity requestEntity = new InWaybillRecordSubmitNewEntity();
    private String selectLine;
    private List<InWaybillRecordSubmitNewEntity.SingleLineBean> showWaybillList = new ArrayList<>();//展示用的运单列表数据
    private InboundSortingAdapter adapter;
    private List<PagedIndexBean> slideIndexList = new ArrayList<>();
    private PagedIndexAdapter indexAdapter;
    private String changedWaybillCode;
    private boolean showFinishActivity = false;
    private int currentPos = -1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_inbound_sorting;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        baseData = (TransportDataBase) intent.getSerializableExtra("TASK_INFO");
        setToolbar();
        setWaybillRvShow();
        setListener();
        getBaseData();
    }

    private boolean showStore = false;

    /**
     * 设置暂存和提交按钮监听
     */
    private void setListener() {
        //暂存，提交请求
        btnTemp.setOnClickListener(listener -> {
            //如果是包机L  货包 H  必须输入整机复重重量
            if ("L".equals(resultBean.getFlightType()) || "H".equals(resultBean.getFlightType())) {
                if (TextUtils.isEmpty(etReWeight.getText().toString())) {
                    ToastUtil.showToast("请输入整机复重重量");
                    return;
                }
                requestEntity.setCharterReWeight(etReWeight.getText().toString());
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
                            requestEntity.setFlag(0);
                            showFinishActivity = false;
                            ((InWaybillRecordPresenter) mPresenter).submit(requestEntity);
                        }
                    })
                    .show();
        });
        //提交请求
        btnDone.setOnClickListener(listener -> {
            //如果是包机L  货包 H  必须输入整机复重重量
            if ("L".equals(resultBean.getFlightType()) || "H".equals(resultBean.getFlightType())) {
                if (TextUtils.isEmpty(etReWeight.getText().toString())) {
                    ToastUtil.showToast("请输入整机复重重量");
                    return;
                }
                requestEntity.setCharterReWeight(etReWeight.getText().toString());
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
                            showStore = true;
                            requestEntity.setFlag(0);
                            showFinishActivity = true;
                            ((InWaybillRecordPresenter) mPresenter).submit(requestEntity);
                        }
                    })
                    .show();
        });
    }

    /**
     * 设置服务器返回的运单数据显示，主数据和下面的指示器显示
     */
    private void setWaybillRvShow() {
        adapter = new InboundSortingAdapter(showWaybillList);
        rvWaybillList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvWaybillList.setAdapter(adapter);
        setEmptyShow();
        adapter.setOnChildClickListener((pos, type) -> {
            switch (type) {
                case InboundSortingAdapter.CLICK_TYPE_COMMIT:
                    showMultiDialog(pos, InboundSortingAdapter.CLICK_TYPE_COMMIT);
                    break;
                case InboundSortingAdapter.CLICK_TYPE_DELETE:
                    showMultiDialog(pos, InboundSortingAdapter.CLICK_TYPE_DELETE);
                    break;
                case InboundSortingAdapter.CLICK_TYPE_MODIFY:
                default:
                    if (resultBean.getCloseFlag() == 1) {
                        ToastUtil.showToast("运单已经关闭，无法编辑！");
                        return;
                    }
                    Intent intent = new Intent(InboundSortingActivity.this, SortingAddActivity.class);
                    intent.putExtra("type", TYPE_UPDATE_WAYBILL);
                    intent.putExtra("flight_info_id", baseData.getFlightInfoId());
                    InWaybillRecordSubmitNewEntity.SingleLineBean updateInWaybillRecord = showWaybillList.get(pos);
                    changedWaybillCode = updateInWaybillRecord.getWaybillCode();
                    intent.putExtra("data", updateInWaybillRecord);
                    Bundle bundle = new Bundle();
                    if (reservoirList.size() > 0) {
                        bundle.putSerializable("reservoir_list", (Serializable) reservoirList);
                    } else {
                        ToastUtil.showToast("未获取到库区");
                        return;
                    }
                    intent.putExtras(bundle);
                    InboundSortingActivity.this.startActivityForResult(intent, 2);
                    break;
            }
        });
        for (int i = 1; i <= showWaybillList.size(); i++) {
            PagedIndexBean bean = new PagedIndexBean();
            bean.setIndex(String.valueOf(i));
            bean.setChecked(i == 1);
            slideIndexList.add(bean);
        }
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(rvWaybillList);
        rvWaybillList.postDelayed(() -> rvWaybillList.smoothScrollToPosition(0), 1000);
        rvSlideIndex.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        indexAdapter = new PagedIndexAdapter(slideIndexList);
        rvSlideIndex.setAdapter(indexAdapter);
        Tools.setRecyclerViewCenterHor(this, slideIndexList.size(), rvSlideIndex);
        rvWaybillList.addOnScrollListener(new RecyclerViewPageHelper(linearSnapHelper,
                position -> {
                    for (PagedIndexBean index : slideIndexList) {
                        index.setChecked(false);
                    }
                    slideIndexList.get(position).setChecked(true);
                    currentPos = position;
                    indexAdapter.notifyDataSetChanged();
                    Tools.scroll2Position(rvSlideIndex, position);
                }));
        indexAdapter.setOnItemClickListener((adapter, view, position) -> {
            for (PagedIndexBean index : slideIndexList) {
                index.setChecked(false);
            }
            slideIndexList.get(position).setChecked(true);
            indexAdapter.notifyDataSetChanged();
            Tools.scroll2Position(rvWaybillList, position);
        });
    }

    /**
     * 点击单票快提和删除按钮弹窗,接口返回弹窗
     *
     * @param pos  操作数据下标
     * @param type 操作的类型值
     */
    private void showMultiDialog(int pos, int type) {
        CommonDialog dialogCommit = new CommonDialog(InboundSortingActivity.this);
        String title = null;
        switch (type) {
            case InboundSortingAdapter.CLICK_TYPE_COMMIT:
                title = "确定单票快提吗？";
                break;
            case InboundSortingAdapter.CLICK_TYPE_DELETE:
                title = "确认删除？";
                break;
            case -1:
                title = "确认退出？";
                break;
        }
        dialogCommit.setTitle("提示")
                .setMessage(title)
                .setPositiveButton("确定")
                .setNegativeButton("取消")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener((dialog12, confirm) -> {
                    if (confirm) {
                        switch (type) {
                            case InboundSortingAdapter.CLICK_TYPE_COMMIT:
                                mPresenter = new InWaybillRecordPresenter(InboundSortingActivity.this);
                                InWaybillRecordSubmitNewEntity.SingleLineBean entity = showWaybillList.get(pos);
                                ((InWaybillRecordPresenter) mPresenter).allGoodsArrived(entity);
                                break;
                            case InboundSortingAdapter.CLICK_TYPE_DELETE:
                                InWaybillRecordSubmitNewEntity.SingleLineBean data = showWaybillList.remove(pos);
                                setTotalSelectShow(true, data);
                                List<InWaybillRecordSubmitNewEntity.SingleLineBean> changedList = requestEntity.getCargos().get(selectLine);
                                Objects.requireNonNull(changedList).remove(data);
                                setDataShow();
                                break;
                            case -1:
                            default:
                                InboundSortingActivity.this.finish();
                                break;
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //来自新增页面的结果返回
        if (data == null) {
            return;
        }
        InWaybillRecordSubmitNewEntity.SingleLineBean singleLineBean = (InWaybillRecordSubmitNewEntity.SingleLineBean) data.getSerializableExtra("data");
        singleLineBean.setSurplusNumber(0);
        singleLineBean.setSurplusWeight(0);
        singleLineBean.setStrayDisabled(false);
        singleLineBean.setOriginatingStationCn(selectLine);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {//新增
            singleLineBean.setCanModify(true);
            singleLineBean.setMailType("C");
            if (requestEntity.getCargos().containsKey(selectLine)) {//提交数据中有此航段的运单列表
                //此航段的运单列表非空
                HashMap<String, List<InWaybillRecordSubmitNewEntity.SingleLineBean>> allData = requestEntity.getCargos();
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
                    setTotalSelectShow(false, singleLineBean);
                    List<InWaybillRecordSubmitNewEntity.SingleLineBean> list = requestEntity.getCargos().get(selectLine);
                    if (list != null) {
                        list.add(singleLineBean);
                    }
                    setDataShow();
                }
            } else {
                setTotalSelectShow(false, singleLineBean);
                List<InWaybillRecordSubmitNewEntity.SingleLineBean> list = new ArrayList<>();
                list.add(singleLineBean);
                requestEntity.getCargos().put(selectLine, list);
                setDataShow();
            }
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {//修改
            List<InWaybillRecordSubmitNewEntity.SingleLineBean> list = requestEntity.getCargos().get(selectLine);
            if (list == null) {
                return;
            }
            setTotalSelectShow(false, singleLineBean);
            if (changedWaybillCode.equals(singleLineBean.getWaybillCode())) {//未修改运单号,该运单对应的实体更新数据
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
            setDataShow();
        }
    }

    /**
     * 控制是否显示暂无数据
     */
    private void setEmptyShow() {
        if (showWaybillList.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    /**
     * 获取基础数据信息
     */
    private void getBaseData() {
        mPresenter = new InWaybillRecordPresenter(this);
        InWaybillRecordGetEntity entity = new InWaybillRecordGetEntity();
        entity.setTaskFlag(0);
        entity.setFlightInfoId(baseData.getFlightInfoId());
        ((InWaybillRecordPresenter) mPresenter).getList(entity);
    }

    /**
     * 设置toolbar
     */
    private void setToolbar() {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setMainTitle(Color.WHITE, getString(R.string.format_inbound_sorting_title, baseData.getFlightNo(), TimeUtils.getTime2(baseData.getAta())), 16);
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", listener -> finish());
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
            Intent intentAdd = new Intent(InboundSortingActivity.this, SortingAddActivity.class);
            intentAdd.putExtra("type", TYPE_ADD_WAYBILL);
            intentAdd.putExtra("flight_number", baseData.getFlightNo());
            Bundle bundle = new Bundle();
            if (reservoirList.size() > 0) {
                bundle.putSerializable("reservoir_list", (Serializable) reservoirList);
            } else {
                ToastUtil.showToast("未获取到库区");
                return;
            }
            intentAdd.putExtras(bundle);
            startActivityForResult(intentAdd, 1);
        });
    }

    @Override
    public void resultGetList(InWaybillRecordBean bean) {
        if (bean == null) {
            resultBean = new InWaybillRecordBean();
        } else {
            resultBean = bean;
            //初始化提交实体类
            requestEntity.setFlightInfoId(baseData.getFlightInfoId());
            requestEntity.setFlightId(baseData.getFlightId());
            requestEntity.setTaskId(baseData.getTaskId());
            requestEntity.setUserId(UserInfoSingle.getInstance().getUserId());
            requestEntity.setUserName(UserInfoSingle.getInstance().getUsername());
            requestEntity.setFlightNo(baseData.getFlightNo());
            requestEntity.setCargos(resultBean.getCargos());
            if (bean.getRoute() != null) {//控制航线tab显示和监听设置
                List<FlightLineBean> list = new ArrayList<>();
                List<String> route = bean.getRoute();
                List<String> sortedList = StringUtil.sortByCnFirstLetter(route);
                for (int i = 0; i < sortedList.size(); i++) {
                    FlightLineBean lineBean = new FlightLineBean();
                    lineBean.setLine(bean.getRoute().get(i));
                    lineBean.setCheck((TextUtils.isEmpty(selectLine) ? (i == 0) : selectLine.equals(lineBean.getLine())));
                    list.add(lineBean);
                }
                rvFlightLine.setLayoutManager(new LinearLayoutManager(InboundSortingActivity.this, LinearLayoutManager.HORIZONTAL, false));
                FlightLineCheckAdapter adapter = new FlightLineCheckAdapter(list);
                rvFlightLine.setAdapter(adapter);
                if (TextUtils.isEmpty(selectLine)) {
                    selectLine = list.get(0).getLine();
                }
                adapter.setOnItemClickListener((adapter1, view, position) -> {
                    selectLine = list.get(position).getLine();
                    for (FlightLineBean bean1 : list) {
                        bean1.setCheck(false);
                    }
                    list.get(position).setCheck(true);
                    adapter1.notifyDataSetChanged();
                    setDataShow();
                });
            }
            if (resultBean.getList() != null && resultBean.getList().size() != 0) {//组装货物总计、邮件总计、总计等数据显示
                setTotalSelectShow(false, null);
            } else {//货物总计、邮件总计、总计等数据显示当list为空时全部显示默认值
                tvTotalInfo.setText(InboundSortingActivity.this.getString(R.string.format_inbound_sorting_total,
                        0, 0.0, 0, 0.0, 0, 0.0, 0, 0.0, 0, 0.0, 0, 0.0));
            }
        }
        setDataShow();
        getReservoirs();
    }

    /**
     * 控制数据显示
     */
    private void setDataShow() {
        showWaybillList.clear();
        HashMap<String, List<InWaybillRecordSubmitNewEntity.SingleLineBean>> listData = requestEntity.getCargos();
        if (listData != null && listData.get(selectLine) != null) {
            showWaybillList.addAll(Objects.requireNonNull(listData.get(selectLine)));
        }
        adapter.notifyDataSetChanged();
        slideIndexList.clear();
        for (int i = 1; i <= showWaybillList.size(); i++) {
            PagedIndexBean pageBean = new PagedIndexBean();
            pageBean.setIndex(String.valueOf(i));
            pageBean.setChecked((currentPos == -1) ? (i == 1) : (currentPos + 1 == i));
            slideIndexList.add(pageBean);
        }
        indexAdapter.notifyDataSetChanged();
        Tools.setRecyclerViewCenterHor(this, slideIndexList.size(), rvSlideIndex);
        setEmptyShow();
    }

    /**
     * 控制件数、重量等数据显示
     *
     * @param isDelete 是否是删除
     * @param bean     删除的数据
     */
    private void setTotalSelectShow(boolean isDelete, InWaybillRecordSubmitNewEntity.SingleLineBean bean) {
        int goodsTotalNum = 0, goodsSelectedNum = 0, mailTotalNum = 0, mailSelectedNum = 0;
        double goodsTotalWeight = 0, goodsSelectedWeight = 0, mailTotalWeight = 0, mailSelectedWeight = 0;
        HashMap<String, List<InWaybillRecordSubmitNewEntity.SingleLineBean>> cargo = requestEntity.getCargos();
        if (cargo != null) {
            for (String key : cargo.keySet()) {
                List<InWaybillRecordSubmitNewEntity.SingleLineBean> lineDataList = cargo.get(key);
                if (lineDataList != null) {
                    for (InWaybillRecordSubmitNewEntity.SingleLineBean record : lineDataList) {
                        if ("C".equals(record.getMailType()) || TextUtils.isEmpty(record.getMailType())) {
                            goodsTotalNum += record.getTotalNumber();
                            goodsTotalWeight += record.getTotalWeight();
                            goodsSelectedNum += record.getTallyingTotal();
                            goodsSelectedWeight += record.getTallyingWeight();
                        } else if ("M".equals(record.getMailType())) {
                            mailTotalNum += record.getTotalNumber();
                            mailTotalWeight += record.getTotalWeight();
                            mailSelectedNum += record.getTallyingTotal();
                            mailSelectedWeight += record.getTallyingWeight();
                        }
                    }
                }
            }
        }
        if (bean == null) {
            tvTotalInfo.setText(InboundSortingActivity.this.getString(R.string.format_inbound_sorting_total,
                    goodsTotalNum, goodsTotalWeight, goodsSelectedNum, goodsSelectedWeight,
                    mailTotalNum, mailTotalWeight, mailSelectedNum, mailSelectedWeight,
                    goodsTotalNum + mailTotalNum, goodsTotalWeight + mailTotalWeight, goodsSelectedNum + mailSelectedNum, goodsSelectedWeight + mailSelectedWeight));
        } else {
            int goodsNum = bean.getTallyingTotal();
            double goodsWeight = bean.getTallyingWeight();
            if (isDelete) {//是删除运单数据
                goodsSelectedNum -= goodsNum;
                goodsSelectedWeight -= goodsWeight;
            } else {//是添加运单数据
                goodsSelectedNum += goodsNum;
                goodsSelectedWeight += goodsWeight;
            }
            tvTotalInfo.setText(InboundSortingActivity.this.getString(R.string.format_inbound_sorting_total,
                    goodsTotalNum, goodsTotalWeight, goodsSelectedNum, goodsSelectedWeight,
                    mailTotalNum, mailTotalWeight, mailSelectedNum, mailSelectedWeight,
                    goodsTotalNum + mailTotalNum, goodsTotalWeight + mailTotalWeight, goodsSelectedNum + mailSelectedNum, goodsSelectedWeight + mailSelectedWeight));
        }
    }

    /**
     * 获取库区数据
     */
    private void getReservoirs() {
        mPresenter = new ListReservoirInfoPresenter(this);
        ((ListReservoirInfoPresenter) mPresenter).listReservoirInfoByCode(UserInfoSingle.getInstance().getDeptCode());
    }

    @Override
    public void resultSubmit(BaseEntity<Object> o) {
        Log.e("tagTest", "提交/暂存，返回值：" + o.toString());
        if (showStore) {
            requestEntity.setFlag(1);
            showStore = false;
            ((InWaybillRecordPresenter) mPresenter).submit(requestEntity);
        } else {
            if ("200".equals(o.getStatus())) {
                ToastUtil.showToast("成功");
                getBaseData();
                if (showFinishActivity) {
                    showMultiDialog(-1, -1);
                }
            } else {
                ToastUtil.showToast(o.getMessage());
                showMultiDialog(-1, -1);
            }
            EventBus.getDefault().post("InPortTallyFragment_refresh");//发送消息让前一个页面刷新
        }
    }

    @Override
    public void resultDeleteById(Object o) {

    }

    @Override
    public void allGoodsArrivedResult(WaybillQuickGetBean result) {
        if ("200".equals(result.getStatus())) {
            String notifyId = result.getData().getCargo().getId();
            List<InWaybillRecordSubmitNewEntity.SingleLineBean> changedList = requestEntity.getCargos().get(selectLine);
            int index = -1;
            for (int i = 0; i < Objects.requireNonNull(changedList).size(); i++) {
                InWaybillRecordSubmitNewEntity.SingleLineBean singleLineBean = changedList.get(i);
                if (notifyId.equals(singleLineBean.getId())) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                changedList.set(index, result.getData().getCargo());
                adapter.notifyDataSetChanged();
            }
        } else {
            ToastUtil.showToast(result.getMessage());
        }
    }

    @Override
    public void listReservoirInfoResult(List<ReservoirArea> bean) {
        reservoirList.clear();
        Log.e("tagTest", "库区信息\n" + bean.toString());
        //显示库区选择面板
        for (ReservoirArea item : bean) {
            ChooseStoreroomDialog2.TestBean testBean = new ChooseStoreroomDialog2.TestBean(item.getId(), item.getReservoirName());
            testBean.setName(item.getReservoirName());
            reservoirList.add(testBean);
        }
    }

    /**
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if ("InboundSortingActivity".equals(result.getFunctionFlag())) {
            String newCode;
            String code = result.getData();
            Log.e("tagTest", "运单号" + code);
            if (!TextUtils.isEmpty(code) && code.length() >= 10) {
                if (code.startsWith("DN")) {
                    newCode = "DN-" + code.substring(2, 10);
                } else {
                    if (code.length() >= 11) {
                        newCode = judgeWaybillCode(code);
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

    /**
     * 判断运单号后缀是否符合规则,并返回正确的运单数据
     *
     * @param ss 扫描到的运单号，有可能超长度
     * @return 正确的运单号码
     */
    private String judgeWaybillCode(String ss) {
        String s0 = ss.substring(0, 3); //前3位
        String s00 = ss.substring(3, 11); //后8位
        return s0 + "-" + s00;
    }

    /**
     * 跳转到新增界面
     */
    private void turnToAddActivity(String code) {
        boolean isEditChange;
        String[] parts = code.split("-");
        String ss = parts[1];
        String s1; //后8位的前7位
        String s2; //后8位的最后1位
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
        if (resultBean == null) {
            ToastUtil.showToast("运单信息出错，请重新请求");
            return;
        }
        if (resultBean.getCloseFlag() == 1) {
            ToastUtil.showToast("运单已经关闭，无法编辑！");
            return;
        }
        Intent intentAdd = new Intent(InboundSortingActivity.this, SortingAddActivity.class);
        intentAdd.putExtra("TYPE", TYPE_ADD_WAYBILL);
        intentAdd.putExtra("newCode", code);
        intentAdd.putExtra("FlightInfoId", baseData.getFlightInfoId());
        Bundle bundle = new Bundle();
        if (reservoirList.size() > 0) {
            bundle.putSerializable("mTestBeanList", (Serializable) reservoirList);
        } else {
            ToastUtil.showToast("未获取到库区");
            return;
        }
        intentAdd.putExtras(bundle);
        startActivityForResult(intentAdd, 1);
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
}