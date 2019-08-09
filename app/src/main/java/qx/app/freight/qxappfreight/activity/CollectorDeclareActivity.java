package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.BaseParamBean;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.bean.response.RecordsBean;
import qx.app.freight.qxappfreight.contract.BaseParamContract;
import qx.app.freight.qxappfreight.contract.GetWayBillInfoByIdContract;
import qx.app.freight.qxappfreight.presenter.BaseParamPresenter;
import qx.app.freight.qxappfreight.presenter.GetWayBillInfoByIdPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.SlideRecyclerView;

/**
 * 申报编辑信息
 */
public class CollectorDeclareActivity extends BaseActivity implements GetWayBillInfoByIdContract.getWayBillInfoByIdView, BaseParamContract.baseParamView {

    @BindView(R.id.waybill_id)
    TextView waybillId;
    @BindView(R.id.declare_type)
    TextView declareType;
    @BindView(R.id.flight_code)
    TextView flightCode;
    @BindView(R.id.flight_line_start)
    TextView flightLineStart;
    @BindView(R.id.flight_line_end)
    TextView flightLineEnd;
    @BindView(R.id.flight_company)
    TextView flightCompany;
    @BindView(R.id.tv_total_num)
    EditText tvTotalNum;
    @BindView(R.id.tv_total_weight)
    EditText tvTotalWeight;
    @BindView(R.id.tv_total_volume)
    EditText tvTotalVolume;
    //    @BindView(R.id.tv_weight) //计费重量录入删除
////    EditText tvWeight;
    @BindView(R.id.tv_goods_code)
    EditText tvGoodsCode;
    @BindView(R.id.tv_baozhuang)
    TextView tvBaozhuang;
    @BindView(R.id.tv_storage_type)
    TextView tvStorageType;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.rv_detail_list)
    SlideRecyclerView rvDetailList;
    @BindView(R.id.btn_commit)
    Button btnSubmit;
    @BindView(R.id.ll_baozhuang)
    LinearLayout llBaozhuang;
    @BindView(R.id.ll_storage_type)
    LinearLayout llStorageType;
    @BindView(R.id.ll_temperature)
    LinearLayout llTemperature;
    @BindView(R.id.ll_base_temperature)
    LinearLayout llBaseTemperature;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_volume)
    TextView tvVolume;
    @BindView(R.id.tv_type)
    TextView tvType;

    private String wayBillId;
    private String taskId;
    private String taskTypeCode;
    //    private CollectorDeclareAdapter mAdapter;
//    private List<DeclareItem> mList;
    private DeclareWaybillBean mData;

    private List<String> baozhuangList;//1小件 2大件 3 超大件
    private List<String> storageList; //1：贵重  2：危险 3：活体 4：冷藏 0：普货
    private List<String> temperatureList; //温度
    private int baozhuangOption; //选中得包装大小
    private String storageOption;//选中得储存类型
    private List<String> resTypeList;

    private CustomToolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_collector_declare;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        initTitle();
        wayBillId = getIntent().getStringExtra("wayBillId");
        taskId = getIntent().getStringExtra("taskId");
        taskTypeCode = getIntent().getStringExtra("taskTypeCode");
        initVIew();
        mPresenter = new GetWayBillInfoByIdPresenter(this);
        ((GetWayBillInfoByIdPresenter) mPresenter).getWayBillInfoById(getIntent().getStringExtra("id"));
    }

    private void initTitle() {
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "修改申报信息");
    }

    private void initVIew() {
        baozhuangList = new ArrayList<>();
        baozhuangList.add("小件");
        baozhuangList.add("大件");
        baozhuangList.add("超大件");
        storageList = new ArrayList<>();
        resTypeList = new ArrayList<>();
//        storageList.add("CTU_GARGO_STORAGE_TYPE_001");
//        storageList.add("CTU_GARGO_STORAGE_TYPE_001");
//        storageList.add("CTU_GARGO_STORAGE_TYPE_001");
//        storageList.add("CTU_GARGO_STORAGE_TYPE_001");
//        storageList.add("CTU_GARGO_STORAGE_TYPE_001");

        temperatureList = new ArrayList<>();
        for (int i = -30; i < 31; i++) {
            temperatureList.add(i + "");
        }
//        mList = new ArrayList<>();
//        mAdapter = new CollectorDeclareAdapter(mList);
//        rvDetailList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        rvDetailList.setAdapter(mAdapter);

//        mAdapter.setOnDeleteClickListener(new CollectorDeclareAdapter.OnDeleteClickLister() {
//            @Override
//            public void onDeleteClick(View view, int position) {
//                rvDetailList.closeMenu();
//            }
//
//            @Override
//            public void onEditClick(View view, int position) {
//                rvDetailList.closeMenu();
//                Intent intent  = new Intent(CollectorDeclareActivity.this,AddCollectorDeclareActivity.class);
//                intent.putExtra("DeclareItemBean",mList.get(position));
//                startActivity(intent);
//            }
//        });

    }

    @OnClick({R.id.btn_commit, R.id.ll_baozhuang, R.id.ll_storage_type, R.id.ll_temperature})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                nextSteep();
                break;
            case R.id.ll_baozhuang:
                showBaozhuangPickView();
                break;
            case R.id.ll_storage_type:
                showStoragePickView();
                break;
            case R.id.ll_temperature:
                showtempretruePickView();
                break;
        }
    }

    @Override
    public void getWayBillInfoByIdResult(DeclareWaybillBean result) {
        mData = result;
//        mList.addAll(mData.getDeclareItems());
        getStoreInfo();
        refreshView();
//        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void sendPrintMessageResult(String result) {

    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {
        showProgessDialog("数据提交中……");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    private void showBaozhuangPickView() {
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvBaozhuang.setText(baozhuangList.get(options1));
                baozhuangOption = options1 + 1;
            }
        }).build();
        pickerView.setPicker(baozhuangList);
        pickerView.setTitleText("包装大小");
        pickerView.show();
    }

    private void showStoragePickView() {
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvStorageType.setText(storageList.get(options1));
                storageOption = resTypeList.get(options1);
                if (resTypeList.get(options1).equals("CTU_GARGO_STORAGE_TYPE_004")) {
                    llBaseTemperature.setVisibility(View.VISIBLE);
                } else {
                    llBaseTemperature.setVisibility(View.GONE);
                    tvTemperature.setText("");
                }
            }
        }).build();
        pickerView.setPicker(storageList);
        pickerView.setTitleText("储存类型");
        pickerView.show();
    }

    private void showtempretruePickView() {
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (options2 <= options1) {
                    ToastUtil.showToast("请选择正确的温度");
                } else {
                    tvTemperature.setText(temperatureList.get(options1) + "*" + temperatureList.get(options2));
                }


            }
        }).build();
        pickerView.setNPicker(temperatureList, temperatureList, null);
        pickerView.setTitleText("温度选择");
        pickerView.setSelectOptions(30, 30);
        pickerView.show();
    }

    /**
     * 提交到下一步
     */
    private void nextSteep() {
        if (TextUtils.isEmpty(tvTotalNum.getText().toString().trim())) {
            ToastUtil.showToast("总件数不能为空");
            return;
        }
        if (TextUtils.isEmpty(tvTotalWeight.getText().toString().trim())) {
            ToastUtil.showToast("总重量不能为空");
            return;
        }
        if (TextUtils.isEmpty(tvTotalVolume.getText().toString().trim())) {
            ToastUtil.showToast("总体积不能为空");
            return;
        }
//        if (TextUtils.isEmpty(tvWeight.getText().toString().trim())){
//            ToastUtil.showToast("计费重量不能为空");
//            return;
//        }
        if (!TextUtils.isEmpty(tvGoodsCode.getText().toString().trim())) {
//            mData.setSpecialCargoCode(tvGoodsCode.getText().toString().trim());
            mData.setSpecialCode(tvGoodsCode.getText().toString().trim());
        }
        try {
            int number = Integer.parseInt(tvTotalNum.getText().toString().trim());
            String weight = tvTotalWeight.getText().toString().trim();
            int volume = Integer.parseInt(tvTotalVolume.getText().toString().trim());
//            int jifeiWeight = Integer.parseInt(tvWeight.getText().toString().trim());

            mData.setTotalNumber(number);
            mData.setTotalWeight(Integer.valueOf(weight));
            mData.setTotalVolume(volume);
//            mData.setBillingWeight(jifeiWeight);
            mData.setStorageType(storageOption);
            mData.setStorageTypeName(tvStorageType.getText().toString());
            mData.setBigFlag(baozhuangOption);
            if (storageOption.equals("CTU_GARGO_STORAGE_TYPE_004")||storageOption.equals("冷藏")) {
                mData.setRefrigeratedTemperature(tvTemperature.getText().toString());
            }else
                mData.setRefrigeratedTemperature("");

            turnToReceiveGoodsActivity();
        } catch (Exception e) {
            ToastUtil.showToast("输入值格式不对，请重新输入");
        }

    }

    public void getStoreInfo() {
        mPresenter = new BaseParamPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setCurrent(1);
        entity.setSize(20);
        RecordsBean mBaseEntity = new RecordsBean();
        mBaseEntity.setOutletType("ctu_airport_cargo_00001");
        mBaseEntity.setType("11");
        entity.setFilter(mBaseEntity);
        ((BaseParamPresenter) mPresenter).baseParam(entity);
    }

    /**
     * 界面ui赋值
     */
    private void refreshView() {
        waybillId.setText(mData.getWaybillCode());
        declareType.setText(mData.getFreightName());
        flightCode.setText(mData.getFlightNo());
        if (TextUtils.isEmpty(mData.getOriginatingStation())) {
            flightLineStart.setText("--");
        } else {
            flightLineStart.setText(mData.getOriginatingStation());
        }
        if (TextUtils.isEmpty(mData.getDestinationStation())) {
            flightLineEnd.setText("--");
        } else {
            flightLineEnd.setText(mData.getDestinationStation());
        }
        flightCompany.setText(mData.getFlightName());
        tvTotalNum.setText(String.valueOf(mData.getTotalNumber()));
        tvTotalWeight.setText(String.valueOf(mData.getTotalWeight()));
        tvTotalVolume.setText(String.valueOf(mData.getTotalVolume()));
//        tvWeight.setText(String.valueOf(mData.getBillingWeight()));

        tvGoodsCode.setText(mData.getSpecialCode());
        if (mData.getBigFlag() == 1) {
            tvBaozhuang.setText("小件");
        } else if (mData.getBigFlag() == 2) {
            tvBaozhuang.setText("大件");
        } else if (mData.getBigFlag() == 3) {
            tvBaozhuang.setText("超大件");
        }
        String coldStorage;
        //货物类别  1：贵重  2：危险 3：活体 4：冷藏 0：普货
        baozhuangOption = mData.getBigFlag();
        storageOption = mData.getStorageType();

        if (storageOption.equals("CTU_GARGO_STORAGE_TYPE_004")) {
            llBaseTemperature.setVisibility(View.VISIBLE);
            tvTemperature.setText(mData.getRefrigeratedTemperature());
        }
        else
            tvTemperature.setText("");


        tvStorageType.setText(mData.getStorageTypeName());
        tvName.setText(mData.getCargoCn());
        tvNumber.setText(String.valueOf(mData.getTotalNumber()));
        tvWeight.setText(String.valueOf(mData.getTotalWeight()));
        tvVolume.setText(String.valueOf(mData.getTotalVolume()));
        tvType.setText(mData.getPackagingType());

    }

    /**
     * 跳转到代办详情
     *
     * @param
     */
    private void turnToReceiveGoodsActivity() {
        ReceiveGoodsActivity.startActivity(this, taskId, mData, taskTypeCode, getIntent().getStringExtra("id"), wayBillId);
    }


    @Override
    public void baseParamResult(BaseParamBean changeStorageBean) {
        if (null != changeStorageBean) {
            if (changeStorageBean.getRecords().size() > 0)
                for (int i = 0; i < changeStorageBean.getRecords().size(); i++) {
                    storageList.add(changeStorageBean.getRecords().get(i).getName());
                    resTypeList.add(changeStorageBean.getRecords().get(i).getValue());
                }
        }
    }
}
