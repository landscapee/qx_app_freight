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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import qx.app.freight.qxappfreight.bean.InWaybillRecord;
import qx.app.freight.qxappfreight.bean.RcInfoOverweight;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.bean.request.QueryWaybillInfoEntity;
import qx.app.freight.qxappfreight.bean.response.ArrivalCargoInfoBean;
import qx.app.freight.qxappfreight.bean.response.ReservoirBean;
import qx.app.freight.qxappfreight.contract.ReservoirContract;
import qx.app.freight.qxappfreight.contract.UploadsContract;
import qx.app.freight.qxappfreight.dialog.ChooseStoreroomDialog2;
import qx.app.freight.qxappfreight.dialog.ErrorTypeChooseDialog;
import qx.app.freight.qxappfreight.dialog.SortingReturnGoodsDialog;
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
public class SortingAddActivity extends BaseActivity implements ReservoirContract.reservoirView, UploadsContract.uploadsView {
    @BindView(R.id.edt_id)
    EditText idEdt;//运单号前半部
    @BindView(R.id.edt_id_1)
    EditText idEdt2;//运单号后半部
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
    InWaybillRecord mInWaybillRecord;//本页面的数据,这是最终生成的数据哦， 很关键
    int INDEX;
    String TYPE = "";
    String newCode; //传过来的运单号
    String flightNum; //传过来的航班号
    List<CounterUbnormalGoods> counterUbnormalGoodsList;//异常数组
    //是否转关 0 否 1 是
    int isTransit;//是否转关
    List<RcInfoOverweight> rcInfoOverweight; // 超重记录列表
    List<RcInfoOverweight> rcTempInfoOverweight; // 超重记录列表备份（用于dialog）
    List<ChooseStoreroomDialog2.TestBean> mTestBeanList = new ArrayList<>();
    private int maxTotal = -1;
    private double maxWeight = -1.0;

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
        TYPE = getIntent().getStringExtra("TYPE");
        INDEX = getIntent().getIntExtra("INDEX", 0);
        mTestBeanList = (List<ChooseStoreroomDialog2.TestBean>) getIntent().getSerializableExtra("mTestBeanList");
        if (TYPE.equals(SortingActivity.TYPE_ADD)) {
            Log.e("dime", "进入了addd");
            //如果是新增数据， 直接初始化
            newCode = getIntent().getStringExtra("newCode");
            flightNum = getIntent().getStringExtra("FLIGHTNo");
            //根据航班号查运单前缀
            if (!TextUtils.isEmpty(newCode)) {
                isEditChange = true;
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
            mInWaybillRecord = new InWaybillRecord();
            mInWaybillRecord.setDelFlag(0);
            rcInfoOverweight = new ArrayList<>();
            counterUbnormalGoodsList = new ArrayList<>();
        } else if (TYPE.equals(SortingActivity.TYPE_UPDATE)) {
            Log.e("dime", "进入了UPDATE");
            //如果是修改，数据从前一个页面来
            customToolbar.setMainTitle(Color.WHITE, "修改");
            mInWaybillRecord = (InWaybillRecord) getIntent().getSerializableExtra("DATA");
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
                isEditChange = true;
                String[] parts = mInWaybillRecord.getWaybillCode().split("-");
                idEdt.setText(parts[0]);
                idEdt2.setText(parts[1]);
            }
            sortingNumEdt.setText(mInWaybillRecord.getTallyingTotal() == null ? "" : mInWaybillRecord.getTallyingTotal() + "");
            sortingWeightEdt.setText(String.valueOf(mInWaybillRecord.getTallyingWeight().doubleValue()));
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
            remarkEdt.setText("" + mInWaybillRecord.getRemark() == null ? "" : mInWaybillRecord.getRemark());
        } else {
            Log.e("dime", "不知道进入了哪里");
        }
        //初始化recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(SortingAddActivity.this));
        mAdapter = new SortingAddAdapter(SortingAddActivity.this, counterUbnormalGoodsList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnSlectPicListener(position -> {
            Log.e("dime", "选择图片：" + position);
            CURRENT_PHOTO_INDEX = position;
            //将数组转为List
            List<String> originList = new ArrayList<>();
            if (counterUbnormalGoodsList.get(position).getLocalPath() != null) {
                for (String url : counterUbnormalGoodsList.get(position).getLocalPath()) {
                    originList.add(url);
                }
            }
            MultiImageSelector.create(SortingAddActivity.this)
                    .showCamera(true) // 是否显示相机. 默认为显示
                    .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                    .multi() // 多选模式, 默认模式;
                    .origin((ArrayList<String>) originList) // 默认已选择图片. 只有在选择模式为多选时有效
                    .start(SortingAddActivity.this, 1002);
        });
        //设置异常类型
        mAdapter.setOnExceptionTypeListener(posstion -> {
            Log.e("dime", "位置信息：" + posstion);
            ErrorTypeChooseDialog chooseExcetionDialog = new ErrorTypeChooseDialog();
            chooseExcetionDialog.setData(ExceptionUtils.testBeanList, SortingAddActivity.this);
            chooseExcetionDialog.setChooseDialogInterface(position2 -> {
                ///**
                //	 * 进港异常
                //	 */
                //	CHNM("CHNM",1,"更名类不正常"),
                //	DEAD("DEAD",2,"死亡"),
                //	DFLD("DFLD",3,"确已装机"),
                //	DMG("DMG",4,"货物破损"),
                //	FDAV("FDAV",5,"多收邮路单"),
                ////	FDAW("FDAW",6,"多收货运单"),			//有单无货
                //	FDAW("MSCA",6,"多收货运单"),			//有单无货
                //	FDCA("FDCA",7,"多收货物"),
                //	FDMB("FDMB",8,"多收邮袋"),
                //	GDAM("GDAM",9,"货物破损"),
                ////	HWFL("HWFL",10,"腐烂丢弃"),
                //	ROT("ROT",10,"腐烂"),
                //	KOUH("KOUH",11,"扣货"),
                //	MSAV("MSAV",12,"有邮袋无邮路单"),
                //	MSAW("MSAW",13,"少收货运单"),			//有货无单
                //	MSCA("S/L",14,"少收货物"),
                ////	MSCA("MSCA",14,"少收货物"),
                //	MSMB("MSMB",15,"有邮路单无邮袋"),
                //	NLAB("NLAB",16,"无标签"),
                //	OFLD("OFLD",17,"临时拉下货物"),
                //	OTHR("OTHR",18,"其他不正常"),
                //	OVCD("OVCD",19,"漏卸货物/运输文件"),
                //	SPLT("SPLT",20,"分批不正常"),
                //	SPSL("SPSL",21,"货物不齐"),
                //	SSPD("SSPD",22,"漏装货物/文件"),
                //	TDTH("TDTH",23,"退单退货"),
                //	WRTQ("WRTQ",24,"无人提取"),
                //	WLXFS("WLXFS",25,"无联系方式"),
                //	WET("WET",26,"受潮"),
                //	LEAK("WET",27,"泄漏"),
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
        sortingWeightEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (maxTotal != -1) {//系统中存在该运单号
                    int number = Integer.parseInt(s.toString().trim());
                    String weight = String.valueOf(maxWeight / maxTotal * number);
                    sortingWeightEdt.setText(weight);
                }
            }
        });
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
                    int number = Integer.parseInt(sortingNumEdt.toString().trim());
                    String weight = String.valueOf(maxWeight / maxTotal * number);
                    sortingWeightEdt.setText(weight);
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
            if (TextUtils.isEmpty(idEdt.getText().toString().trim()) && TextUtils.isEmpty(idEdt2.getText().toString().trim())) {
                mInWaybillRecord.setWaybillCode("");
            } else {
                if (!TextUtils.isEmpty(idEdt.getText().toString().trim()) && isEditChange) {
                    mInWaybillRecord.setWaybillCode(idEdt.getText().toString().trim() + "-" + idEdt2.getText().toString().trim());
                } else {
                    ToastUtil.showToast("请输入正确的运单号");
                    return;
                }
            }
            mInWaybillRecord.setWaybillCode(idEdt.getText().toString().trim() + "-" + idEdt2.getText().toString().trim());
            if (TextUtils.isEmpty(sortingNumEdt.getText().toString().trim())) {
                ToastUtil.showToast("请输入实际分拣数量！");
                return;
            } else if (maxTotal != -1 && Integer.parseInt(sortingNumEdt.getText().toString().trim()) > maxTotal) {
                ToastUtil.showToast("请输入正确的分拣数量！");
                return;
            }
            String weight = sortingWeightEdt.getText().toString().trim();
            if (TextUtils.isEmpty(weight)) {
                ToastUtil.showToast("请输入实际分拣重量！");
                return;
            }
            double weightFloat = Double.parseDouble(weight);
            boolean checkWeight = weightFloat == 0;
            if (checkWeight) {
                ToastUtil.showToast("请输入实际分拣重量！");
                return;
            }
            float diff = 1e-6f;
            boolean check1 = Math.abs(-1.0 - maxWeight) >= diff;
            boolean check2 = Double.parseDouble(sortingWeightEdt.getText().toString().trim()) > maxWeight;
            if (check1 && check2) {
                ToastUtil.showToast("请输入正确的分拣重量！");
                return;
            }
            if (TextUtils.isEmpty(remarkEdt.getText().toString().trim())) {
                mInWaybillRecord.setRemark("");
            } else {
                mInWaybillRecord.setRemark(remarkEdt.getText().toString().trim());
            }
            mInWaybillRecord.setTallyingTotal(Integer.valueOf(sortingNumEdt.getText().toString().trim()));
            mInWaybillRecord.setTallyingWeight(new BigDecimal(sortingWeightEdt.getText().toString().trim()));
            if (TextUtils.isEmpty(mInWaybillRecord.getWarehouseArea())) {
                ToastUtil.showToast("请选择库区");
                return;
            }
            if (!StringUtil.isEmpty(locationEt.getText().toString())) {
                mInWaybillRecord.setWarehouseLocation(locationEt.getText().toString());
            }
            mInWaybillRecord.setCounterUbnormalGoodsList(counterUbnormalGoodsList);
            mInWaybillRecord.setOverWeightList(rcInfoOverweight);
            Intent intent = new Intent(SortingAddActivity.this, SortingActivity.class);
            intent.putExtra("DATA", mInWaybillRecord);
            intent.putExtra("INDEX", INDEX);
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
                    entity.setFlightInfoId(getIntent().getStringExtra("FlightInfoId"));
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
                editChange(s.toString());
                if (s.toString().length() == 8 && isEditChange) {//运单号长度正确且符合规范，查询数据库中是否有此运单的信息
                    mPresenter = new ReservoirPresenter(SortingAddActivity.this);
                    QueryWaybillInfoEntity entity = new QueryWaybillInfoEntity();
                    entity.setWaybillCode(idEdt.getText().toString().trim() + "-" + idEdt2.getText().toString().trim());
                    entity.setFlightInfoId(getIntent().getStringExtra("FlightInfoId"));
                    ((ReservoirPresenter) mPresenter).getWaybillInfoByCode(entity);
                }
            }
        });
        //超重情况
        tvOverweight.setOnClickListener(v -> {
            showPopWindowList();
        });
    }

    //判断运单号后缀是否符合规则
    private boolean isEditChange = false;

    private void editChange(String ss) {
        if (!TextUtils.isEmpty(ss) && ss.length() == 8) {
            String s1 = ss.substring(0, 7);
            String s2 = ss.substring(7, 8);
            isEditChange = Integer.parseInt(s1) % 7 == Integer.parseInt(s2);
        } else {
            isEditChange = false;
        }
        if (isEditChange) {
            idEdt2.setTextColor(Color.parseColor("#888888"));
        } else {
            idEdt2.setTextColor(Color.parseColor("#ff0000"));
        }
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
        if (result.getFunctionFlag().equals("SortingAddActivity")) {
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
     * @param code
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
    public void getWaybillInfoByCodeResult(ArrivalCargoInfoBean result) {
        if ("200".equals(result.getStatus())) {
            if (result.getData() == null) {
                maxTotal = -1;
                maxWeight = -1.0;
            } else {
                maxTotal = result.getData().getTallyingTotal();
                maxWeight = result.getData().getTallyingWeight().doubleValue();
            }
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
                List<String> photoList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);//选择好的图片
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
    public void uploadsResult(Object result) {
        Map<String, String> map = (Map<String, String>) result;
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            counterUbnormalGoodsList.get(CURRENT_PHOTO_INDEX).getUbnormalPic().add(entry.getKey());
        }
        mAdapter.notifyItemChanged(CURRENT_PHOTO_INDEX);
        dismissProgessDialog();
    }
}
