package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.ReservoirArea;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.BaseParamBean;
import qx.app.freight.qxappfreight.bean.response.ChangeStorageBean;
import qx.app.freight.qxappfreight.bean.response.DeclareApplyForRecords;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.RecordsBean;
import qx.app.freight.qxappfreight.bean.response.SearchReservoirBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.contract.BaseParamContract;
import qx.app.freight.qxappfreight.contract.ChangeStorageContract;
import qx.app.freight.qxappfreight.contract.ChangeStorageListContract;
import qx.app.freight.qxappfreight.contract.SearchReservoirContract;
import qx.app.freight.qxappfreight.presenter.BaseParamPresenter;
import qx.app.freight.qxappfreight.presenter.ChangeStorageListPresenter;
import qx.app.freight.qxappfreight.presenter.ChangeStoragePresenter;
import qx.app.freight.qxappfreight.presenter.SearchReservoirPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

public class StoreTypeChangeActivity extends BaseActivity implements ChangeStorageListContract.changeStorageListView, ChangeStorageContract.changeStorageView, SearchReservoirContract.searchReservoirView, BaseParamContract.baseParamView {

    @BindView(R.id.tv_change)
    TextView mTvChange;
    @BindView(R.id.tv_old_type)
    TextView mTvOldType;
    @BindView(R.id.btn_accept)
    Button mBtnAccept;
    @BindView(R.id.btn_refuse)
    Button mBtnRefuse;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_storage)
    TextView mTvStorage;
    @BindView(R.id.tv_lengcang)
    TextView mTvLengCang;

    @BindView(R.id.ll_base_temperature)
    LinearLayout llBaseTemperature;


    private TransportDataBase data;
    private CustomToolbar toolbar;
    private DeclareApplyForRecords mEntity = new DeclareApplyForRecords();
    private List<String> storageList; //1：贵重  2：危险 3：活体 4：冷藏 0：普货
    private List<String> temperatureList; //温度
    private List<String> reservoirList;//库区
    private int storageOption;//选中得储存类型
    private List<String> resTypeList;

    public static void startActivity(Activity context, TransportDataBase transportDataBase) {
        Intent starter = new Intent(context, StoreTypeChangeActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("TransportDataBase", (Serializable) transportDataBase);
        starter.putExtras(mBundle);
        context.startActivityForResult(starter, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_store_type_change;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "存储类型变更审核");
        initView();

    }

    private void initView() {
        data = (TransportDataBase) getIntent().getSerializableExtra("TransportDataBase");
        if (data == null)
            return;
        storageList = new ArrayList<>();
        resTypeList = new ArrayList<>();
//        storageList.add("普货");
//        storageList.add("贵重");
//        storageList.add("危险");
//        storageList.add("活体");
//        storageList.add("冷藏");
        mPresenter = new BaseParamPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setCurrent(1);
        entity.setSize(20);
        RecordsBean mBaseEntity = new RecordsBean();
        mBaseEntity.setOutletType("ctu_airport_cargo_00001");
        mBaseEntity.setType("11");
        entity.setFilter(mBaseEntity);
        ((BaseParamPresenter) mPresenter).baseParam(entity);
//        resTypeList.add("ctu_airport_were_house_00001");
//        resTypeList.add("ctu_airport_were_house_00003");
//        resTypeList.add("ctu_airport_were_house_00005");
//        resTypeList.add("ctu_airport_were_house_00002");
//        resTypeList.add("ctu_airport_were_house_00004");
        temperatureList = new ArrayList<>();
        reservoirList = new ArrayList<>();
        for (int i = -30; i < 31; i++) {
            temperatureList.add(i + "");
        }
        //运单号
        mTvChange.setText("运单号:" + data.getWaybillCode());
        //接受申请
        mBtnAccept.setOnClickListener(v -> {
            if (mEntity != null) {
                commit(1);
            }
        });
        //拒绝申请
        mBtnRefuse.setOnClickListener(v -> {
            if (mEntity != null) {
                commit(0);
            }
        });
    }

    public void commit(int type) {
        if (TextUtils.isEmpty(mTvType.getText())) {
            ToastUtil.showToast("请先选择存储类型");
            return;
        }
        mPresenter = new ChangeStoragePresenter(this);
        ChangeStorageBean entity = new ChangeStorageBean();
        entity.setSpStorageChangeInfo(mEntity);
        entity.setTaskId(data.getTaskId());
        entity.setChargeFlag(data.getChargeFlag());
        entity.setUserId(UserInfoSingle.getInstance().getUserId());
        entity.setJudge(type); //1通过 0拒绝
        ((ChangeStoragePresenter) mPresenter).changeStorage(entity);
    }

    private void initData() {
        mPresenter = new ChangeStorageListPresenter(this);
        BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
        MyAgentListBean myAgentListBean = new MyAgentListBean();
        myAgentListBean.setWaybillId(data.getId());
        myAgentListBean.setTaskTypeCode(data.getTaskTypeCode());
        baseFilterEntity.setFilter(myAgentListBean);
        ((ChangeStorageListPresenter) mPresenter).changeStorageList(baseFilterEntity);
    }

    @Override
    public void changeStorageListResult(DeclareApplyForRecords result) {
        if (result != null) {
            mEntity = result;
            for (int i = 0; i < resTypeList.size(); i++) {
                if (result.getOrgStorage().equals(resTypeList.get(i))) {
                    mTvOldType.setText(storageList.get(i));
                    mTvType.setText(storageList.get(i));
                }
            }
            if (result.getOrgStorage().equals("CTU_GARGO_STORAGE_TYPE_004")) {
                llBaseTemperature.setVisibility(View.VISIBLE);
            }
            getShowStorage(result.getOrgStorage());
        }
    }

    @OnClick({R.id.ll_storage_type, R.id.ll_base_temperature, R.id.ll_baozhuang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.btn_commit:
//                nextSteep();
//                break;
            case R.id.ll_baozhuang:
                showBaozhuangPickView();
                break;
            case R.id.ll_storage_type:
                showStoragePickView();
                break;
            case R.id.ll_base_temperature:
                showtempretruePickView();
                break;
        }
    }

    //库区
    private void showBaozhuangPickView() {
        if (reservoirList.size() == 0) {
            ToastUtil.showToast("数据为空，不能选择");
            return;
        }
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mTvStorage.setText(reservoirList.get(options1));
                mEntity.setRepName(reservoirList.get(options1));
            }
        }).build();
        pickerView.setPicker(reservoirList);
        pickerView.setTitleText("库区选择");
        pickerView.show();
    }

    //存储类型
    private void showStoragePickView() {
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mTvType.setText(storageList.get(options1));
                getShowStorage(resTypeList.get(options1));
                storageOption = options1;
                if (resTypeList.get(options1).equals("CTU_GARGO_STORAGE_TYPE_004")) {
                    llBaseTemperature.setVisibility(View.VISIBLE);
                } else {
                    llBaseTemperature.setVisibility(View.GONE);
                    mTvLengCang.setText("");
                }
            }
        }).build();
        pickerView.setPicker(storageList);
        pickerView.setTitleText("储存类型");
        pickerView.show();
    }

    public void getShowStorage(String resType) {
        mEntity.setStorage(resType);
        mPresenter = new SearchReservoirPresenter(StoreTypeChangeActivity.this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setCurrent(1);
        entity.setSize(10);
        ReservoirArea mBean = new ReservoirArea();
        mBean.setCode("ctu_airport_cargo_00001");
        mBean.setReservoirSaveType(resType);
        entity.setFilter(mBean);
        ((SearchReservoirPresenter) mPresenter).searchReservoir(entity);
    }

    //温度
    private void showtempretruePickView() {
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (options2 <= options1) {
                    ToastUtil.showToast("请选择正确的温度");
                } else {
                    mTvLengCang.setText(temperatureList.get(options1) + "*" + temperatureList.get(options2));
                    mEntity.setRefrigeratedTemperature(mTvLengCang.getText().toString());
                }
            }
        }).build();
        pickerView.setNPicker(temperatureList, temperatureList, null);
        pickerView.setTitleText("温度选择");
        pickerView.setSelectOptions(30, 30);
        pickerView.show();
    }

    @Override
    public void toastView(String error) {
        Log.e("-----", error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("数据提交中……");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void changeStorageResult(String result) {
        if (!TextUtils.isEmpty(result)) {
            EventBus.getDefault().post("collector_refresh");
            ToastUtil.showToast(result);
            finish();
        }
    }

    @Override
    public void searchReservoirResult(SearchReservoirBean searchReservoirBeanList) {
        if (searchReservoirBeanList != null) {
            reservoirList.clear();
            if (searchReservoirBeanList.getRecords().size() > 0) {
                for (int i = 0; i < searchReservoirBeanList.getRecords().size(); i++) {
                    reservoirList.add(searchReservoirBeanList.getRecords().get(i).getReservoirName());
                }
                mTvStorage.setText(searchReservoirBeanList.getRecords().get(0).getReservoirName());
            } else
                mTvStorage.setText("");
        }
    }

    @Override
    public void baseParamResult(BaseParamBean changeStorageBean) {
        if (null != changeStorageBean) {
            if (changeStorageBean.getRecords().size() > 0)
                for (int i = 0; i < changeStorageBean.getRecords().size(); i++) {
                    storageList.add(changeStorageBean.getRecords().get(i).getName());
                    resTypeList.add(changeStorageBean.getRecords().get(i).getValue());
                }
            initData();
        }
    }
}
