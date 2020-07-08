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
import qx.app.freight.qxappfreight.bean.response.ListWaybillCodeBean;
import qx.app.freight.qxappfreight.bean.response.ReservoirBean;
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
 * 进港分拣 新增
 * <p>
 * create by guohap - 2019/4/26
 */
public class SortingAddActivity extends BaseActivity implements ReservoirContract.reservoirView, UploadsContract.uploadsView, AddInventoryDetailContract.addInventoryDetailView {
    @BindView(R.id.edt_id)
    EditText idEdt;//运单号前半部
    @BindView(R.id.edt_id_1)
    EditText idEdt2;//运单号后半部
    @BindView(R.id.tv_add_nowaybill_goods)
    TextView tvAddNoBillGoods;//新增无运单货物
    @BindView(R.id.edt_real_sort_num)
    EditText sortingNumEdt;//实际分拣数
    @BindView(R.id.edt_real_sort_weight)
    EditText sortingWeightEdt;//实际分拣重量
    @BindView(R.id.tv_reservoir)
    TextView reservoirTv;//库区
    @BindView(R.id.tv_location)
    TextView locationTv;//库位
    @BindView(R.id.et_location)
    EditText locationEt;//库位  暂修改为 输入框
    @BindView(R.id.tv_wrong_transport)
    TextView isWrongTransportTv;//是否错运
    @BindView(R.id.tv_is_transit)
    TextView isTransitTv;//转关
    @BindView(R.id.btn_submit)
    Button submitBtn;//提交
    @BindView(R.id.btn_add_item)
    Button addItemBtn;//新增异常
    @BindView(R.id.tv_remark)
    EditText remarkEdt;//备注
    @BindView(R.id.tv_overweight)
    TextView tvOverweight;//超重
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    CustomToolbar customToolbar;
    SortingAddAdapter mAdapter;//适配器
    int CURRENT_PHOTO_INDEX;
    private InWaybillRecordSubmitNewEntity.SingleLineBean mInWaybillRecord;//本页面的数据,这是最终生成的数据哦， 很关键
    private String type = "";
    private String newCode; //传过来的运单号
    String flightNum; //传过来的航班号
    List<CounterUbnormalGoods> counterUbnormalGoodsList;//异常数组
    //是否转关 0 否 1 是
    int isTransit;//是否转关
    List<RcInfoOverweight> rcInfoOverweight; // 超重记录列表
    List<RcInfoOverweight> rcTempInfoOverweight; // 超重记录列表备份（用于dialog）
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
        customToolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", listener -> {
            finish();
        });
        //新增页面的逻辑， 是修改还是新增？ TYPE == ADD / UPDATE
        type = getIntent().getStringExtra("type");
        mTestBeanList = (List<ChooseStoreroomDialog2.TestBean>) getIntent().getSerializableExtra("reservoir_list");
        mInWaybillRecord = (InWaybillRecordSubmitNewEntity.SingleLineBean) getIntent().getSerializableExtra("data");
        if (mInWaybillRecord != null) {
            dataCanModify = mInWaybillRecord.isCanModify();
        }
        if (type.equals(InboundSortingActivity.TYPE_ADD_WAYBILL)) {
            newCode = getIntent().getStringExtra("newCode");//激光扫码获取到的运单号
            flightNum = getIntent().getStringExtra("flight_number");
            //根据航班号查运单前缀
            if (!TextUtils.isEmpty(newCode)) {
                String[] parts = newCode.split("-");
                idEdt.setText(parts[0]);
                idEdt2.setText(parts[1]);
            }
            //根据航班号航司码搜索运单号前三位
            if (!TextUtils.isEmpty(flightNum)) {
                mPresenter = new ReservoirPresenter(this);
                ((ReservoirPresenter) mPresenter).getAirWaybillPrefix(flightNum.substring(0, 2));
            }
            customToolbar.setMainTitle(Color.WHITE, "新增");
            mInWaybillRecord = new InWaybillRecordSubmitNewEntity.SingleLineBean();
            mInWaybillRecord.setDelFlag(0);
            rcInfoOverweight = new ArrayList<>();
            counterUbnormalGoodsList = new ArrayList<>();
        } else if (type.equals(InboundSortingActivity.TYPE_UPDATE_WAYBILL)) {
            Log.e("dime", "进入了UPDATE");
            //如果是修改，数据从前一个页面来
            customToolbar.setMainTitle(Color.WHITE, "修改");
            counterUbnormalGoodsList = mInWaybillRecord.getCounterUbnormalGoodsList();
            //初始化超重重量
            rcInfoOverweight = mInWaybillRecord.getOverWeightList();
            int overweight = 0;
            for (RcInfoOverweight mRcInfoOverweight : rcInfoOverweight) {
                overweight += mRcInfoOverweight.getOverWeight();
            }
            tvOverweight.setText(overweight + "kg");
            //显示运单号， 实际分拣数，库区，库位，是否转关，备注
            if (!TextUtils.isEmpty(mInWaybillRecord.getWaybillCode())) {
                String[] parts = mInWaybillRecord.getWaybillCode().split("-");
                idEdt.setText(parts[0]);
                idEdt2.setText(parts[1]);
            }
            sortingNumEdt.setText(mInWaybillRecord.getTallyingTotal() == 0 ? "" : mInWaybillRecord.getTallyingTotal() + "");
            sortingWeightEdt.setText(String.valueOf(mInWaybillRecord.getTallyingWeight()));
            //根据 id 获取库区
            if (mInWaybillRecord.getWarehouseArea() != null) {
                for (ChooseStoreroomDialog2.TestBean mTestbean : mTestBeanList) {
                    if (mInWaybillRecord.getWarehouseArea().equals(mTestbean.getId())) {
                        reservoirTv.setText(mTestbean.getName());
                    }
                }
            }
            //textview改成edittext显示
            locationEt.setText(mInWaybillRecord.getWarehouseLocation());
            if (mInWaybillRecord.getTransit() != null) {
                locationEt.setText(mInWaybillRecord.getTransit() == 0 ? "否" : "是");
            }
            remarkEdt.setText(mInWaybillRecord.getRemark() == null ? "" : mInWaybillRecord.getRemark());
        }
        //初始化recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(SortingAddActivity.this));
        mAdapter = new SortingAddAdapter(SortingAddActivity.this, counterUbnormalGoodsList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnSlectPicListener(position -> {
            Log.e("dime", "选择图片：" + position);
            CURRENT_PHOTO_INDEX = position;
            //将数组转为List
            ArrayList<String> originList = new ArrayList<>();
            if (counterUbnormalGoodsList.get(position).getLocalPath() != null) {
                originList.addAll(counterUbnormalGoodsList.get(position).getLocalPath());
            }
            MultiImageSelector.create(SortingAddActivity.this)
                    .showCamera(true) // 是否显示相机. 默认为显示
                    .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                    .multi() // 多选模式, 默认模式;
                    .origin(originList) // 默认已选择图片. 只有在选择模式为多选时有效
                    .start(SortingAddActivity.this, 1002);
        });
        //设置异常类型
        mAdapter.setOnExceptionTypeListener(posstion -> {
            Log.e("dime", "位置信息：" + posstion);
            ErrorTypeChooseDialog chooseExcetionDialog = new ErrorTypeChooseDialog();
            chooseExcetionDialog.setData(ExceptionUtils.testBeanList, SortingAddActivity.this);
            chooseExcetionDialog.setChooseDialogInterface(position2 -> {
                int[] intTypes = {2, 4, 10, 27, 16, 26};
                Log.e("dime", "异常：type=" + position2);
                if (counterUbnormalGoodsList.get(posstion).getUbnormalType() == null) {
                    List<Integer> ubnormalType = new ArrayList<>(1);
                    ubnormalType.set(0, intTypes[position2]);
                    counterUbnormalGoodsList.get(posstion).setUbnormalType(ubnormalType);
                } else if (counterUbnormalGoodsList.get(posstion).getUbnormalType().size() == 0) {
                    counterUbnormalGoodsList.get(posstion).getUbnormalType().add(0, intTypes[position2]);
                } else {
                    counterUbnormalGoodsList.get(posstion).getUbnormalType().set(0, intTypes[position2]);
                }
                Log.e("dime", "组装好的数据呢：" + counterUbnormalGoodsList.get(posstion).getUbnormalType().toString());
                Log.e("dime", "组装好的数据呢：位置信息：" + posstion);
                mAdapter.notifyItemChanged(posstion);
            });
            chooseExcetionDialog.show(getSupportFragmentManager(), "exception");
        });
        //库区按钮 点击事件
        reservoirTv.setOnClickListener(listener -> {
            ChooseStoreroomDialog2 chooseStoreroomDialog = new ChooseStoreroomDialog2();
            chooseStoreroomDialog.setData(mTestBeanList, SortingAddActivity.this);
            chooseStoreroomDialog.show(getSupportFragmentManager(), "guohao");
            chooseStoreroomDialog.setChooseDialogInterface(position -> {
                Log.e("dime", "选择了库区：" + position);
                String reservoir = mTestBeanList.get(position).getId();//库区已经选择
                reservoirTv.setText(mTestBeanList.get(position).getName());
                mInWaybillRecord.setWarehouseArea(reservoir);//库区的id
                mInWaybillRecord.setWarehouseAreaDisplay(mTestBeanList.get(position).getName());
                mInWaybillRecord.setWarehouseLocation("");
            });
        });
        //库位按钮 点击事件
        locationTv.setOnClickListener(listener -> {
            ToastUtil.showToast("库位操作暂不支持， 统一为0");
        });
        //是否转关 点击事件
        isTransitTv.setOnClickListener(listener -> {
            if ("是".equals(isTransitTv.getText().toString())) {
                isTransitTv.setText("否");
                mInWaybillRecord.setTransit(0);
            } else {
                isTransitTv.setText("是");
                mInWaybillRecord.setTransit(1);
            }
        });
        //是否错运 点击事件
        isWrongTransportTv.setOnClickListener(listener -> {
            if ("是".equals(isWrongTransportTv.getText().toString())) {
                isWrongTransportTv.setText("否");
                mInWaybillRecord.setStray(0);
            } else {
                isWrongTransportTv.setText("是");
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
                if (maxTotal != -1) {//系统中存在该运单号
                    String text = s.toString().trim();
                    if (!TextUtils.isEmpty(text) && !"0".equals(text.substring(0, 1))) {
                        int number = Integer.parseInt(text);
                        String weight = String.valueOf(maxWeight / maxTotal * number);
                        sortingWeightEdt.setText(weight);
                    }
                }
            }
        });
        //新增 异常数据
        addItemBtn.setOnClickListener(v -> {
            CounterUbnormalGoods newExceptionItem = new CounterUbnormalGoods();
            counterUbnormalGoodsList.add(newExceptionItem);
            mAdapter.notifyDataSetChanged();
        });
        //提交按钮 点击事件
        submitBtn.setOnClickListener(listen -> {
            //将组装好的数据返回给前一个页面
            if (TextUtils.isEmpty(idEdt.getText().toString().trim()) || TextUtils.isEmpty(idEdt2.getText().toString().trim())) {
                ToastUtil.showToast("请输入正确的运单号");
                return;
            } else {
                if (idEdt.getText().toString().trim().length() == 3 && idEdt2.getText().toString().trim().length() == 8) {
                    mInWaybillRecord.setWaybillCode(idEdt.getText().toString().trim() + "-" + idEdt2.getText().toString().trim());
                } else {
                    ToastUtil.showToast("请输入正确的运单号");
                    return;
                }
            }
            String number=sortingNumEdt.getText().toString().trim();
            if (TextUtils.isEmpty(number)) {//未输入数量
                ToastUtil.showToast("请输入实际分拣数量！");
                return;
            } else if (maxTotal != -1 && Integer.parseInt(sortingNumEdt.getText().toString().trim()) > maxTotal) {//超过录单带过来的数量
                ToastUtil.showToast("分拣数量大于录单总件数！");
                return;
            }else if ("0".equals(number.substring(0,1))){//以0开头的输入
                ToastUtil.showToast("请输入正确的分拣数量！");
                return;
            }
            String weight = sortingWeightEdt.getText().toString().trim();
            if (TextUtils.isEmpty(weight)) {//未输入重量
                ToastUtil.showToast("请输入实际分拣重量！");
                return;
            }
            double weightFloat = Double.parseDouble(weight);
            boolean checkWeight = weightFloat == 0;
            if (checkWeight) {//输入的重量为0
                ToastUtil.showToast("请输入正确的分拣重量！");
                return;
            }
            float diff = 1e-6f;
            boolean check1 = Math.abs(-1.0 - maxWeight) >= diff;
            boolean check2 = Double.parseDouble(sortingWeightEdt.getText().toString().trim()) > maxWeight;
            if (check1 && check2) {//超过录单带过来的最大重量
                ToastUtil.showToast("分拣重量大于录单总重量！");
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
                ToastUtil.showToast("请选择库区");
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
        //运单号后缀事件
        idEdt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 8) {//运单号长度正确且符合规范，查询数据库中是否有此运单的信息
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
        //超重情况
        tvOverweight.setOnClickListener(v -> {
            showPopWindowList();
        });
    }

    //超重情况的弹窗
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
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        if ("SortingAddActivity".equals(result.getFunctionFlag())) {
            String code = result.getData();
            Log.e("22222", "运单号" + code);
            if (!TextUtils.isEmpty(code) && code.length() >= 10) {
                if (code.startsWith("DN")) {
                    newCode = "DN-" + code.substring(2, 10);
                } else {
                    if (code.length() >= 11) {
                        String s0 = code.substring(0, 3); //前3位
                        String s00 = code.substring(3, 11); //后8位
                        newCode = s0 + "-" + s00;
                    } else {
                        ToastUtil.showToast("无效的运单号");
                        return;
                    }
                }
            } else {
                ToastUtil.showToast("无效的运单号");
                return;
            }
            checkCode(newCode);
        }
    }

    /**
     * 检查运单号是否符合规则，并填入控件
     *
     * @param code 运单号
     */
    private void checkCode(String code) {
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
        //向控件里面填入数据
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
            if (requestCode == 1002) {//异常上报相机事件
                List<String> photoList = Objects.requireNonNull(data).getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);//选择好的图片
                counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).setLocalPath(photoList);
                //开始上传图片
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
                        showProgessDialog("正在上传，请稍候。。。。。。");
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
    public void addInventoryDetailResult(String result) {

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
}
