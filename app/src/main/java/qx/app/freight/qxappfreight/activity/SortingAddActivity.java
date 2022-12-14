package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnMultiCompressListener;
import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.SortingAddAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.CounterUbnormalGoods;
import qx.app.freight.qxappfreight.bean.RcInfoOverweight;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.request.InWaybillRecordSubmitNewEntity;
import qx.app.freight.qxappfreight.bean.request.QueryWaybillInfoEntity;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.ListWaybillCodeBean;
import qx.app.freight.qxappfreight.bean.response.ReservoirBean;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.contract.AddInventoryDetailContract;
import qx.app.freight.qxappfreight.contract.ReservoirContract;
import qx.app.freight.qxappfreight.contract.UploadsContract;
import qx.app.freight.qxappfreight.dialog.ChooseStoreroomDialog2;
import qx.app.freight.qxappfreight.dialog.ErrorTypeChooseDialog;
import qx.app.freight.qxappfreight.dialog.SortingReturnGoodsDialog;
import qx.app.freight.qxappfreight.presenter.AddInventoryDetailPresenter;
import qx.app.freight.qxappfreight.presenter.ReservoirPresenter;
import qx.app.freight.qxappfreight.presenter.UploadsPresenter;
import qx.app.freight.qxappfreight.utils.ExceptionUtils;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * ???????????? ??????
 * <p>
 * create by guohap - 2019/4/26
 */
public class SortingAddActivity extends BaseActivity implements ReservoirContract.reservoirView, UploadsContract.uploadsView, AddInventoryDetailContract.addInventoryDetailView {
    @BindView(R.id.edt_id)
    EditText idEdt;//??????????????????
    @BindView(R.id.edt_id_1)
    EditText idEdt2;//??????????????????
    @BindView(R.id.tv_add_nowaybill_goods)
    TextView tvAddNoBillGoods;//?????????????????????
    @BindView(R.id.edt_real_sort_num)
    EditText sortingNumEdt;//???????????????
    @BindView(R.id.edt_real_sort_weight)
    EditText sortingWeightEdt;//??????????????????
    @BindView(R.id.tv_reservoir)
    TextView reservoirTv;//??????
    @BindView(R.id.tv_location)
    TextView locationTv;//??????
    @BindView(R.id.et_location)
    EditText locationEt;//??????  ???????????? ?????????
    @BindView(R.id.tv_wrong_transport)
    TextView isWrongTransportTv;//????????????
    @BindView(R.id.tv_is_transit)
    TextView isTransitTv;//??????
    @BindView(R.id.btn_submit)
    Button submitBtn;//??????
    @BindView(R.id.btn_add_item)
    Button addItemBtn;//????????????
    @BindView(R.id.tv_remark)
    EditText remarkEdt;//??????
    @BindView(R.id.tv_overweight)
    TextView tvOverweight;//??????
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    CustomToolbar customToolbar;
    SortingAddAdapter mAdapter;//?????????
    int CURRENT_PHOTO_INDEX;
    private InWaybillRecordSubmitNewEntity.SingleLineBean mInWaybillRecord;//??????????????????,????????????????????????????????? ?????????
    private String type = "";
    private String newCode; //?????????????????????
    String flightNum; //?????????????????????
    List<CounterUbnormalGoods> counterUbnormalGoodsList;//????????????
    //???????????? 0 ??? 1 ???
    int isTransit;//????????????
    List<RcInfoOverweight> rcInfoOverweight; // ??????????????????
    List<RcInfoOverweight> rcTempInfoOverweight; // ?????????????????????????????????dialog???
    List<ChooseStoreroomDialog2.TestBean> mTestBeanList = new ArrayList<>();
    private int maxTotal = -1;
    private double maxWeight = -1.0;
    private boolean dataCanModify = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sorting_add;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        customToolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        customToolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "??????", listener -> {
            finish();
        });
        //???????????????????????? ???????????????????????? TYPE == ADD / UPDATE
        type = getIntent().getStringExtra("type");
        mTestBeanList = (List<ChooseStoreroomDialog2.TestBean>) getIntent().getSerializableExtra("reservoir_list");
        mInWaybillRecord = (InWaybillRecordSubmitNewEntity.SingleLineBean) getIntent().getSerializableExtra("data");
        if (mInWaybillRecord != null) {
            dataCanModify = mInWaybillRecord.isCanModify();
        }
        if (type.equals(InboundSortingActivity.TYPE_ADD_WAYBILL)) {
            newCode = getIntent().getStringExtra("newCode");//?????????????????????????????????
            flightNum = getIntent().getStringExtra("flight_number");
            //??????????????????????????????
            if (!TextUtils.isEmpty(newCode)) {
                String[] parts = newCode.split("-");
                idEdt.setText(parts[0]);
                idEdt2.setText(parts[1]);
            }
            //????????????????????????????????????????????????
            if (!TextUtils.isEmpty(flightNum)) {
                mPresenter = new ReservoirPresenter(this);
                ((ReservoirPresenter) mPresenter).getAirWaybillPrefix(flightNum.substring(0, 2));
            }
            customToolbar.setMainTitle(Color.WHITE, "??????");
            mInWaybillRecord = new InWaybillRecordSubmitNewEntity.SingleLineBean();
            mInWaybillRecord.setDelFlag(0);
            rcInfoOverweight = new ArrayList<>();
            counterUbnormalGoodsList = new ArrayList<>();
        } else if (type.equals(InboundSortingActivity.TYPE_UPDATE_WAYBILL)) {
            Log.e("dime", "?????????UPDATE");
            //?????????????????????????????????????????????
            customToolbar.setMainTitle(Color.WHITE, "??????");
            counterUbnormalGoodsList = mInWaybillRecord.getCounterUbnormalGoodsList();
            //?????????????????????
            rcInfoOverweight = mInWaybillRecord.getOverWeightList();
            int overweight = 0;
            for (RcInfoOverweight mRcInfoOverweight : rcInfoOverweight) {
                overweight += mRcInfoOverweight.getOverWeight();
            }
            tvOverweight.setText(overweight + "kg");
            //?????????????????? ?????????????????????????????????????????????????????????
            if (!TextUtils.isEmpty(mInWaybillRecord.getWaybillCode())) {
                String[] parts = mInWaybillRecord.getWaybillCode().split("-");
                idEdt.setText(parts[0]);
                idEdt2.setText(parts[1]);
            }
            sortingNumEdt.setText(mInWaybillRecord.getTallyingTotal() == 0 ? "" : mInWaybillRecord.getTallyingTotal() + "");
            sortingWeightEdt.setText(String.valueOf(mInWaybillRecord.getTallyingWeight()));
            //?????? id ????????????
            if (mInWaybillRecord.getWarehouseArea() != null) {
                for (ChooseStoreroomDialog2.TestBean mTestbean : mTestBeanList) {
                    if (mInWaybillRecord.getWarehouseArea().equals(mTestbean.getId())) {
                        reservoirTv.setText(mTestbean.getName());
                    }
                }
            }
            //textview??????edittext??????
            locationEt.setText(mInWaybillRecord.getWarehouseLocation());
            if (mInWaybillRecord.getTransit() != null) {
                locationEt.setText(mInWaybillRecord.getTransit() == 0 ? "???" : "???");
            }
            remarkEdt.setText(mInWaybillRecord.getRemark() == null ? "" : mInWaybillRecord.getRemark());
        }
        //?????????recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(SortingAddActivity.this));
        mAdapter = new SortingAddAdapter(SortingAddActivity.this, counterUbnormalGoodsList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnSlectPicListener(position -> {
            Log.e("dime", "???????????????" + position);
            CURRENT_PHOTO_INDEX = position;
            //???????????????List
            ArrayList<String> originList = new ArrayList<>();
            if (counterUbnormalGoodsList.get(position).getLocalPath() != null) {
                originList.addAll(counterUbnormalGoodsList.get(position).getLocalPath());
            }
            MultiImageSelector.create(SortingAddActivity.this)
                    .showCamera(true) // ??????????????????. ???????????????
                    .count(9) // ????????????????????????, ?????????9. ???????????????????????????????????????
                    .multi() // ????????????, ????????????;
                    .origin(originList) // ?????????????????????. ???????????????????????????????????????
                    .start(SortingAddActivity.this, 1002);
        });
        //??????????????????
        mAdapter.setOnExceptionTypeListener(posstion -> {
            Log.e("dime", "???????????????" + posstion);
            ErrorTypeChooseDialog chooseExcetionDialog = new ErrorTypeChooseDialog();
            chooseExcetionDialog.setData(ExceptionUtils.testBeanList, SortingAddActivity.this);
            chooseExcetionDialog.setChooseDialogInterface(position2 -> {
                int[] intTypes = {2, 4, 10, 27, 16, 26};
                Log.e("dime", "?????????type=" + position2);
                if (counterUbnormalGoodsList.get(posstion).getUbnormalType() == null) {
                    List<Integer> ubnormalType = new ArrayList<>(1);
                    ubnormalType.set(0, intTypes[position2]);
                    counterUbnormalGoodsList.get(posstion).setUbnormalType(ubnormalType);
                } else if (counterUbnormalGoodsList.get(posstion).getUbnormalType().size() == 0) {
                    counterUbnormalGoodsList.get(posstion).getUbnormalType().add(0, intTypes[position2]);
                } else {
                    counterUbnormalGoodsList.get(posstion).getUbnormalType().set(0, intTypes[position2]);
                }
                Log.e("dime", "????????????????????????" + counterUbnormalGoodsList.get(posstion).getUbnormalType().toString());
                Log.e("dime", "???????????????????????????????????????" + posstion);
                mAdapter.notifyItemChanged(posstion);
            });
            chooseExcetionDialog.show(getSupportFragmentManager(), "exception");
        });
        //???????????? ????????????
        reservoirTv.setOnClickListener(listener -> {
            ChooseStoreroomDialog2 chooseStoreroomDialog = new ChooseStoreroomDialog2();
            chooseStoreroomDialog.setData(mTestBeanList, SortingAddActivity.this);
            chooseStoreroomDialog.show(getSupportFragmentManager(), "guohao");
            chooseStoreroomDialog.setChooseDialogInterface(position -> {
                Log.e("dime", "??????????????????" + position);
                String reservoir = mTestBeanList.get(position).getId();//??????????????????
                reservoirTv.setText(mTestBeanList.get(position).getName());
                mInWaybillRecord.setWarehouseArea(reservoir);//?????????id
                mInWaybillRecord.setWarehouseAreaDisplay(mTestBeanList.get(position).getName());
                mInWaybillRecord.setWarehouseLocation("");
            });
        });
        //???????????? ????????????
        locationTv.setOnClickListener(listener -> {
            ToastUtil.showToast("??????????????????????????? ?????????0");
        });
        //???????????? ????????????
        isTransitTv.setOnClickListener(listener -> {
            if ("???".equals(isTransitTv.getText().toString())) {
                isTransitTv.setText("???");
                mInWaybillRecord.setTransit(0);
            } else {
                isTransitTv.setText("???");
                mInWaybillRecord.setTransit(1);
            }
        });
        //???????????? ????????????
        isWrongTransportTv.setOnClickListener(listener -> {
            if ("???".equals(isWrongTransportTv.getText().toString())) {
                isWrongTransportTv.setText("???");
                mInWaybillRecord.setStray(0);
            } else {
                isWrongTransportTv.setText("???");
                mInWaybillRecord.setStray(1);
            }
        });
        if (!dataCanModify) {
            idEdt.setEnabled(false);
            idEdt2.setEnabled(false);
            tvAddNoBillGoods.setVisibility(View.GONE);
        } else {
            idEdt.setEnabled(true);
            idEdt2.setEnabled(true);
            tvAddNoBillGoods.setVisibility(View.VISIBLE);
            tvAddNoBillGoods.setOnClickListener(v -> {
                mPresenter = new AddInventoryDetailPresenter(SortingAddActivity.this);
                ((AddInventoryDetailPresenter) mPresenter).getWaybillCode();
            });
        }
        mPresenter = new ReservoirPresenter(SortingAddActivity.this);
        QueryWaybillInfoEntity entity = new QueryWaybillInfoEntity();
        entity.setWaybillCode(idEdt.getText().toString().trim() + "-" + idEdt2.getText().toString().trim());
        entity.setFlightInfoId(getIntent().getStringExtra("flight_info_id"));
        ((ReservoirPresenter) mPresenter).getWaybillInfoByCode(entity);
        sortingNumEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (maxTotal != -1) {//???????????????????????????
                    String text = s.toString().trim();
                    if (!TextUtils.isEmpty(text) && !"0".equals(text.substring(0, 1))) {
                        int number = Integer.parseInt(text);
                        String weight = String.valueOf(maxWeight / maxTotal * number);
                        sortingWeightEdt.setText(weight);
                    }
                }
            }
        });
        //?????? ????????????
        addItemBtn.setOnClickListener(v -> {
            CounterUbnormalGoods newExceptionItem = new CounterUbnormalGoods();
            counterUbnormalGoodsList.add(newExceptionItem);
            mAdapter.notifyDataSetChanged();
        });
        //???????????? ????????????
        submitBtn.setOnClickListener(listen -> {
            //?????????????????????????????????????????????
            if (TextUtils.isEmpty(idEdt.getText().toString().trim()) || TextUtils.isEmpty(idEdt2.getText().toString().trim())) {
                ToastUtil.showToast("???????????????????????????");
                return;
            } else {
                if (idEdt.getText().toString().trim().length() == 3 && idEdt2.getText().toString().trim().length() == 8) {
                    mInWaybillRecord.setWaybillCode(idEdt.getText().toString().trim() + "-" + idEdt2.getText().toString().trim());
                } else {
                    ToastUtil.showToast("???????????????????????????");
                    return;
                }
            }
            String number=sortingNumEdt.getText().toString().trim();
            if (TextUtils.isEmpty(number)) {//???????????????
                ToastUtil.showToast("??????????????????????????????");
                return;
            } else if (maxTotal != -1 && Integer.parseInt(sortingNumEdt.getText().toString().trim()) > maxTotal) {//??????????????????????????????
                ToastUtil.showToast("????????????????????????????????????");
                return;
            }else if ("0".equals(number.substring(0,1))){//???0???????????????
                ToastUtil.showToast("?????????????????????????????????");
                return;
            }
            String weight = sortingWeightEdt.getText().toString().trim();
            if (TextUtils.isEmpty(weight)) {//???????????????
                ToastUtil.showToast("??????????????????????????????");
                return;
            }
            double weightFloat = Double.parseDouble(weight);
            boolean checkWeight = weightFloat == 0;
            if (checkWeight) {//??????????????????0
                ToastUtil.showToast("?????????????????????????????????");
                return;
            }
            float diff = 1e-6f;
            boolean check1 = Math.abs(-1.0 - maxWeight) >= diff;
            boolean check2 = Double.parseDouble(sortingWeightEdt.getText().toString().trim()) > maxWeight;
            if (check1 && check2) {//????????????????????????????????????
                ToastUtil.showToast("????????????????????????????????????");
                return;
            }
            if (TextUtils.isEmpty(remarkEdt.getText().toString().trim())) {
                mInWaybillRecord.setRemark("");
            } else {
                mInWaybillRecord.setRemark(remarkEdt.getText().toString().trim());
            }
            mInWaybillRecord.setTallyingTotal(Integer.parseInt(sortingNumEdt.getText().toString().trim()));
            mInWaybillRecord.setTallyingWeight(Double.parseDouble(sortingWeightEdt.getText().toString().trim()));
            if (TextUtils.isEmpty(mInWaybillRecord.getWarehouseArea())) {
                ToastUtil.showToast("???????????????");
                return;
            }
            if (!StringUtil.isEmpty(locationEt.getText().toString())) {
                mInWaybillRecord.setWarehouseLocation(locationEt.getText().toString());
            }
            mInWaybillRecord.setCounterUbnormalGoodsList(counterUbnormalGoodsList);
            mInWaybillRecord.setOverWeightList(rcInfoOverweight);
            Intent intent = new Intent(SortingAddActivity.this, InboundSortingActivity.class);
            intent.putExtra("data", (Serializable) mInWaybillRecord);
            setResult(RESULT_OK, intent);
            finish();
        });
        idEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 3) {
                    idEdt.setTextColor(Color.parseColor("#888888"));
                    mPresenter = new ReservoirPresenter(SortingAddActivity.this);
                    QueryWaybillInfoEntity entity = new QueryWaybillInfoEntity();
                    entity.setWaybillCode(idEdt.getText().toString().trim() + "-" + idEdt2.getText().toString().trim());
                    entity.setFlightInfoId(getIntent().getStringExtra("flight_info_id"));
                    ((ReservoirPresenter) mPresenter).getWaybillInfoByCode(entity);
                } else {
                    idEdt.setTextColor(Color.parseColor("#ff0000"));
                }
            }
        });
        //?????????????????????
        idEdt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 8) {//????????????????????????????????????????????????????????????????????????????????????
                    mPresenter = new ReservoirPresenter(SortingAddActivity.this);
                    QueryWaybillInfoEntity entity = new QueryWaybillInfoEntity();
                    entity.setWaybillCode(idEdt.getText().toString().trim() + "-" + idEdt2.getText().toString().trim());
                    entity.setFlightInfoId(getIntent().getStringExtra("flight_info_id"));
                    ((ReservoirPresenter) mPresenter).getWaybillInfoByCode(entity);
                    idEdt2.setTextColor(Color.parseColor("#888888"));
                } else {
                    idEdt2.setTextColor(Color.parseColor("#ff0000"));
                }
            }
        });
        //????????????
        tvOverweight.setOnClickListener(v -> {
            showPopWindowList();
        });
    }

    //?????????????????????
    private void showPopWindowList() {
        try {
            SortingReturnGoodsDialog dialog = new SortingReturnGoodsDialog(this);
            rcTempInfoOverweight = Tools.deepCopy((ArrayList<RcInfoOverweight>) rcInfoOverweight);
            dialog.setData(rcTempInfoOverweight)
                    .setOnClickListener(text -> {
                        tvOverweight.setText(text);
                        rcInfoOverweight.clear();
                        rcInfoOverweight.addAll(rcTempInfoOverweight);
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ??????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if ("SortingAddActivity".equals(result.getFunctionFlag())) {
            String code = result.getData();
            Log.e("22222", "?????????" + code);
            if (!TextUtils.isEmpty(code) && code.length() >= 10) {
                if (code.startsWith("DN")) {
                    newCode = "DN-" + code.substring(2, 10);
                } else {
                    if (code.length() >= 11) {
                        String s0 = code.substring(0, 3); //???3???
                        String s00 = code.substring(3, 11); //???8???
                        newCode = s0 + "-" + s00;
                    } else {
                        ToastUtil.showToast("??????????????????");
                        return;
                    }
                }
            } else {
                ToastUtil.showToast("??????????????????");
                return;
            }
            checkCode(newCode);
        }
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param code ?????????
     */
    private void checkCode(String code) {
        boolean isEditChange;
        String[] parts = code.split("-");
        String ss = parts[1];
        String s1 = ""; //???8?????????7???
        String s2 = ""; //???8????????????1???
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
        //???????????????????????????
        if (!TextUtils.isEmpty(code)) {
            String[] parts2 = code.split("-");
            idEdt.setText(parts2[0]);
            idEdt2.setText(parts2[1]);
        }
    }

    @Override
    public void reservoirResult(ReservoirBean acceptTerminalTodoBeanList) {
    }

    @Override
    public void getAirWaybillPrefixResult(String getAirWaybillPrefixBean) {
        idEdt.setText(getAirWaybillPrefixBean);
    }

    @Override
    public void getWaybillInfoByCodeResult(InWaybillRecordSubmitNewEntity.SingleLineBean result) {
        if (result == null) {
            maxTotal = -1;
            maxWeight = -1.0;
        } else {
            maxTotal = result.getTotalNumber();
            maxWeight = result.getTotalWeight();
        }
    }

    @Override
    public void toastView(String error) {
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1002) {//????????????????????????
                List<String> photoList = Objects.requireNonNull(data).getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);//??????????????????
                counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).setLocalPath(photoList);
                //??????????????????
                mPresenter = new UploadsPresenter(this);
                List<File> files = new ArrayList<>();
                for (String str : photoList) {
                    files.add(new File(str));
                }
                Luban.get(this).load(files)
                        .setMaxSize(150)
                        .setMaxHeight(1920)
                        .setMaxWidth(1080)
                        .putGear(Luban.CUSTOM_GEAR).launch(new OnMultiCompressListener() {
                    @Override
                    public void onStart() {
                        showProgessDialog("??????????????????????????????????????????");
                    }

                    @Override
                    public void onSuccess(List<File> fileList) {
                        List<MultipartBody.Part> upFiles = Tools.filesToMultipartBodyParts(fileList);
                        mPresenter = new UploadsPresenter(SortingAddActivity.this);
                        ((UploadsPresenter) mPresenter).uploads(upFiles);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
            }
        }
    }

    @Override
    public void addInventoryDetailResult(BaseEntity result) {

    }

    @Override
    public void uploadsResult(Object result) {
        Map<String, String> map = (Map<String, String>) result;
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).getUbnormalPic().add(entry.getKey());
        }
        mAdapter.notifyItemChanged(CURRENT_PHOTO_INDEX);
        dismissProgessDialog();
    }

    @Override
    public void listWaybillCodeResult(ListWaybillCodeBean result) {

    }

    @Override
    public void getWaybillCodeResult(String result) {
        String[] newCode = result.split("-");
        idEdt.setText(newCode[0]);
        idEdt2.setText(newCode[1]);
        tvAddNoBillGoods.setVisibility(View.GONE);
    }

    @Override
    public void getWaybillInfoByWaybillCodeResult(WaybillsBean result) {

    }

    @Override
    public void getWaybillInfoByWaybillCodeResultFail() {

    }
}
