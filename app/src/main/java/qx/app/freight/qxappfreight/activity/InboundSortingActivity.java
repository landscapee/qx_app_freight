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
 * ????????????????????????
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
    //???????????????????????????????????????
    private TransportDataBase baseData;
    //??????????????????????????????????????????????????????????????????bean
    private InWaybillRecordBean resultBean;
    //??????????????????
    private List<ChooseStoreroomDialog2.TestBean> reservoirList = new ArrayList<>();
    //??????????????????
    public static final String TYPE_ADD_WAYBILL = "ADD";
    //??????????????????
    public static final String TYPE_UPDATE_WAYBILL = "UPDATE";
    //???????????????????????????
    private InWaybillRecordSubmitNewEntity requestEntity = new InWaybillRecordSubmitNewEntity();
    private String selectLine;
    private List<InWaybillRecordSubmitNewEntity.SingleLineBean> showWaybillList = new ArrayList<>();//??????????????????????????????
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
     * ?????????????????????????????????
     */
    private void setListener() {
        //?????????????????????
        btnTemp.setOnClickListener(listener -> {
            //???????????????L  ?????? H  ??????????????????????????????
            if (resultBean.getFlightType().equals("L") || resultBean.getFlightType().equals("H")) {
                if (TextUtils.isEmpty(etReWeight.getText().toString())) {
                    ToastUtil.showToast("???????????????????????????");
                    return;
                }
                requestEntity.setCharterReWeight(etReWeight.getText().toString());
            }
            mPresenter = new InWaybillRecordPresenter(this);
            CommonDialog dialog = new CommonDialog(this);
            dialog.setTitle("??????")
                    .setMessage("??????????????????")
                    .setPositiveButton("??????")
                    .setNegativeButton("??????")
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
        //????????????
        btnDone.setOnClickListener(listener -> {
            //???????????????L  ?????? H  ??????????????????????????????
            if (resultBean.getFlightType().equals("L") || resultBean.getFlightType().equals("H")) {
                if (TextUtils.isEmpty(etReWeight.getText().toString())) {
                    ToastUtil.showToast("???????????????????????????");
                    return;
                }
                requestEntity.setCharterReWeight(etReWeight.getText().toString());
            }
            mPresenter = new InWaybillRecordPresenter(this);
            CommonDialog dialog = new CommonDialog(this);
            dialog.setTitle("??????")
                    .setMessage("???????????????")
                    .setPositiveButton("??????")
                    .setNegativeButton("??????")
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
     * ?????????????????????????????????????????????????????????????????????????????????
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
                        ToastUtil.showToast("????????????????????????????????????");
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
                        ToastUtil.showToast("??????????????????");
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
     * ???????????????????????????????????????,??????????????????
     *
     * @param pos  ??????????????????
     * @param type ??????????????????
     */
    private void showMultiDialog(int pos, int type) {
        CommonDialog dialogCommit = new CommonDialog(InboundSortingActivity.this);
        String title = null;
        switch (type) {
            case InboundSortingAdapter.CLICK_TYPE_COMMIT:
                title = "????????????????????????";
                break;
            case InboundSortingAdapter.CLICK_TYPE_DELETE:
                title = "???????????????";
                break;
            case -1:
                title = "???????????????";
                break;
        }
        dialogCommit.setTitle("??????")
                .setMessage(title)
                .setPositiveButton("??????")
                .setNegativeButton("??????")
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
        //?????????????????????????????????
        if (data == null) {
            return;
        }
        InWaybillRecordSubmitNewEntity.SingleLineBean singleLineBean = (InWaybillRecordSubmitNewEntity.SingleLineBean) data.getSerializableExtra("data");
        singleLineBean.setSurplusNumber(0);
        singleLineBean.setSurplusWeight(0);
        singleLineBean.setStrayDisabled(false);
        singleLineBean.setOriginatingStationCn(selectLine);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {//??????
            singleLineBean.setCanModify(true);
            singleLineBean.setMailType("C");
            if (requestEntity.getCargos().containsKey(selectLine)) {//??????????????????????????????????????????
                //??????????????????????????????
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
                    ToastUtil.showToast("?????????????????????????????????");
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
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {//??????
            List<InWaybillRecordSubmitNewEntity.SingleLineBean> list = requestEntity.getCargos().get(selectLine);
            if (list == null) {
                return;
            }
            setTotalSelectShow(false, singleLineBean);
            if (changedWaybillCode.equals(singleLineBean.getWaybillCode())) {//??????????????????,????????????????????????????????????
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getWaybillCode().equals(singleLineBean.getWaybillCode())) {
                        singleLineBean.setId(list.get(i).getId());
                        list.set(i, singleLineBean);
                        break;
                    }
                }
            } else {//??????????????????,???????????????????????????????????????????????????????????????
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
     * ??????????????????????????????
     */
    private void setEmptyShow() {
        if (showWaybillList.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    /**
     * ????????????????????????
     */
    private void getBaseData() {
        mPresenter = new InWaybillRecordPresenter(this);
        InWaybillRecordGetEntity entity = new InWaybillRecordGetEntity();
        entity.setTaskFlag(0);
        entity.setFlightInfoId(baseData.getFlightInfoId());
        ((InWaybillRecordPresenter) mPresenter).getList(entity);
    }

    /**
     * ??????toolbar
     */
    private void setToolbar() {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setMainTitle(Color.WHITE, getString(R.string.format_inbound_sorting_title, baseData.getFlightNo(), TimeUtils.getTime2(baseData.getAta())), 16);
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "??????", listener -> finish());
        //??????????????????
        toolbar.setRightIconView(View.VISIBLE, R.mipmap.add_bg, listener -> {
            //????????? ->????????????
            if (resultBean == null) {
                return;
            }
            if (resultBean.getCloseFlag() == 1) {
                ToastUtil.showToast("????????????????????????????????????");
                return;
            }
            Intent intentAdd = new Intent(InboundSortingActivity.this, SortingAddActivity.class);
            intentAdd.putExtra("type", TYPE_ADD_WAYBILL);
            intentAdd.putExtra("flight_number", baseData.getFlightNo());
            Bundle bundle = new Bundle();
            if (reservoirList.size() > 0) {
                bundle.putSerializable("reservoir_list", (Serializable) reservoirList);
            } else {
                ToastUtil.showToast("??????????????????");
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
            //????????????????????????
            requestEntity.setFlightInfoId(baseData.getFlightInfoId());
            requestEntity.setFlightId(baseData.getFlightId());
            requestEntity.setTaskId(baseData.getTaskId());
            requestEntity.setUserId(UserInfoSingle.getInstance().getUserId());
            requestEntity.setUserName(UserInfoSingle.getInstance().getUsername());
            requestEntity.setFlightNo(baseData.getFlightNo());
            requestEntity.setCargos(resultBean.getCargos());
            if (bean.getRoute() != null) {//????????????tab?????????????????????
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
            if (resultBean.getList() != null && resultBean.getList().size() != 0) {//?????????????????????????????????????????????????????????
                setTotalSelectShow(false, null);
            } else {//??????????????????????????????????????????????????????list??????????????????????????????
                tvTotalInfo.setText(InboundSortingActivity.this.getString(R.string.format_inbound_sorting_total,
                        0, 0.0, 0, 0.0, 0, 0.0, 0, 0.0, 0, 0.0, 0, 0.0));
            }
        }
        setDataShow();
        getReservoirs();
    }

    /**
     * ??????????????????
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
     * ????????????????????????????????????
     *
     * @param isDelete ???????????????
     * @param bean     ???????????????
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
            if (isDelete) {//?????????????????????
                goodsSelectedNum -= goodsNum;
                goodsSelectedWeight -= goodsWeight;
            } else {//?????????????????????
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
     * ??????????????????
     */
    private void getReservoirs() {
        mPresenter = new ListReservoirInfoPresenter(this);
        ((ListReservoirInfoPresenter) mPresenter).listReservoirInfoByCode(UserInfoSingle.getInstance().getDeptCode());
    }

    @Override
    public void resultSubmit(BaseEntity<Object> o) {
        Log.e("tagTest", "??????/?????????????????????" + o.toString());
        if (showStore) {
            requestEntity.setFlag(1);
            showStore = false;
            ((InWaybillRecordPresenter) mPresenter).submit(requestEntity);
        } else {
            if ("200".equals(o.getStatus())) {
                ToastUtil.showToast("??????");
                getBaseData();
                if (showFinishActivity) {
                    showMultiDialog(-1, -1);
                }
            } else {
                ToastUtil.showToast(o.getMessage());
                showMultiDialog(-1, -1);
            }
            EventBus.getDefault().post("InPortTallyFragment_refresh");//????????????????????????????????????
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
        Log.e("tagTest", "????????????\n" + bean.toString());
        //????????????????????????
        for (ReservoirArea item : bean) {
            ChooseStoreroomDialog2.TestBean testBean = new ChooseStoreroomDialog2.TestBean(item.getId(), item.getReservoirName());
            testBean.setName(item.getReservoirName());
            reservoirList.add(testBean);
        }
    }

    /**
     * ??????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if ("InboundSortingActivity".equals(result.getFunctionFlag())) {
            String newCode;
            String code = result.getData();
            Log.e("tagTest", "?????????" + code);
            if (!TextUtils.isEmpty(code) && code.length() >= 10) {
                if (code.startsWith("DN")) {
                    newCode = "DN-" + code.substring(2, 10);
                } else {
                    if (code.length() >= 11) {
                        newCode = judgeWaybillCode(code);
                    } else {
                        ToastUtil.showToast("??????????????????");
                        return;
                    }
                }
            } else {
                ToastUtil.showToast("??????????????????");
                return;
            }
            turnToAddActivity(newCode);
        }
    }

    /**
     * ???????????????????????????????????????,??????????????????????????????
     *
     * @param ss ??????????????????????????????????????????
     * @return ?????????????????????
     */
    private String judgeWaybillCode(String ss) {
        String s0 = ss.substring(0, 3); //???3???
        String s00 = ss.substring(3, 11); //???8???
        return s0 + "-" + s00;
    }

    /**
     * ?????????????????????
     */
    private void turnToAddActivity(String code) {
        boolean isEditChange;
        String[] parts = code.split("-");
        String ss = parts[1];
        String s1; //???8?????????7???
        String s2; //???8????????????1???
        s1 = ss.substring(0, 7);
        s2 = ss.substring(7, 8);
        if (StringUtil.isDouble(s1) && StringUtil.isDouble(s2)) {
            isEditChange = Integer.parseInt(s1) % 7 == Integer.parseInt(s2);
        } else {
            isEditChange = false;
        }
        if (!isEditChange) {
            ToastUtil.showToast("??????????????????");
            return;
        }
        if (resultBean == null) {
            ToastUtil.showToast("????????????????????????????????????");
            return;
        }
        if (resultBean.getCloseFlag() == 1) {
            ToastUtil.showToast("????????????????????????????????????");
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
            ToastUtil.showToast("??????????????????");
            return;
        }
        intentAdd.putExtras(bundle);
        startActivityForResult(intentAdd, 1);
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(error);
        Log.e("tagTest", "???????????????" + error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("?????????.......");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }
}