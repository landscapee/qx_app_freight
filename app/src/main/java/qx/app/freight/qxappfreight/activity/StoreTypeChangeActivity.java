package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.ChangeStorageBean;
import qx.app.freight.qxappfreight.bean.response.DeclareApplyForRecords;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.contract.ChangeStorageContract;
import qx.app.freight.qxappfreight.contract.ChangeStorageListContract;
import qx.app.freight.qxappfreight.presenter.ChangeStorageListPresenter;
import qx.app.freight.qxappfreight.presenter.ChangeStoragePresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

public class StoreTypeChangeActivity extends BaseActivity implements ChangeStorageListContract.changeStorageListView, ChangeStorageContract.changeStorageView {

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


    private TransportDataBase data;
    private CustomToolbar toolbar;
    private DeclareApplyForRecords mEntity = new DeclareApplyForRecords();
    private List<String> storageList; //1：贵重  2：危险 3：活体 4：冷藏 0：普货
    private List<String> temperatureList; //温度

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
        toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "存储类型变更审核");
        initView();
        initData();
    }

    private void initView() {
        data = (TransportDataBase) getIntent().getSerializableExtra("TransportDataBase");
        if (data == null)
            return;
        storageList = new ArrayList<>();
        storageList.add("普货");
        storageList.add("贵重");
        storageList.add("危险");
        storageList.add("活体");
        storageList.add("冷藏");
        temperatureList = new ArrayList<>();
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
        mPresenter = new ChangeStoragePresenter(this);
        ChangeStorageBean entity = new ChangeStorageBean();
        entity.setDeclareApplyForRecords(mEntity);
        entity.setTaskId(data.getTaskId());
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
            switch (result.getOrgStorage()) {
                case 0:
                    mTvOldType.setText("普通");
                    break;
                case 1:
                    mTvOldType.setText("贵重");
                    break;
                case 2:
                    mTvOldType.setText("危险");
                    break;
                case 3:
                    mTvOldType.setText("活体");
                    break;
                case 4:
                    mTvOldType.setText("冷藏");
                    break;
            }
        }
    }

    @OnClick({R.id.ll_storage_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.btn_commit:
//                nextSteep();
//                break;
//            case R.id.ll_baozhuang:
//                showBaozhuangPickView();
//                break;
            case R.id.ll_storage_type:
                showStoragePickView();
                break;
//            case R.id.ll_temperature:
//                showtempretruePickView();
//                break;
        }
    }
    private void showStoragePickView() {
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mTvType.setText(storageList.get(options1));
                storageOption = options1;
                if (options1 == 4) {
                    llBaseTemperature.setVisibility(View.VISIBLE);
                } else {
                    llBaseTemperature.setVisibility(View.GONE);
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
                    mTvLengCang.setText(temperatureList.get(options1) + "*" + temperatureList.get(options2));
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

    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }

    @Override
    public void changeStorageResult(String result) {
        if (TextUtils.isEmpty(result)){
            EventBus.getDefault().post("collector_refresh");
            ToastUtil.showToast(result);
            finish();
        }
    }
}
