package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.GeneralSpinnerAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.GeneralSpinnerBean;
import qx.app.freight.qxappfreight.bean.response.ExistBean;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetWeightContract;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.presenter.GetWeightPresenter;
import qx.app.freight.qxappfreight.presenter.ScooterInfoListPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * TODO : 新增收货页面
 * Created by pr
 */
public class AddReceiveGoodActivity extends BaseActivity implements GetWeightContract.getWeightView, ScooterInfoListContract.scooterInfoListView {

    @BindView(R.id.sp_product)
    Spinner mSpProduct; //品名
    @BindView(R.id.ll_product)
    LinearLayout llProduct;
    @BindView(R.id.edt_overweight)
    EditText mEdtOverWeight;    //超重重量
    @BindView(R.id.edt_dead_weight)
    EditText mEdtDeadWeight;   //ULD自重
    @BindView(R.id.et_uldnumber)
    EditText mEtUldNumber;     //Uld号
    @BindView(R.id.tv_cooter_weight)
    EditText mTvCooterWeight;  //板车重量
    @BindView(R.id.tv_scooter)
    EditText mTvScooter;         //板车号
    @BindView(R.id.btn_takeweight)
    Button mBtnTakeWeight;      //重量
    @BindView(R.id.tv_weight)
    TextView mTvWeight;        //输入重量
    @BindView(R.id.edt_volume)
    EditText mEdtVolume;        //体积
    @BindView(R.id.edt_number)
    EditText mEdtNumber;        //件数
    @BindView(R.id.btn_commit)
    Button mBtnCommit;          //提交

    private GeneralSpinnerAdapter mSpProductAdapter;
    private List<GeneralSpinnerBean.SpProductBean> mSpProductList;//品名
    private MyAgentListBean mMyAgentListBean;
    private String waybillId;
    private List<TransportListBean.DeclareItemBean> mDeclareItemBeans;
    private String mScooterCode;
    private ScooterInfoListBean scooterInfo;


    public static void startActivity(Activity context, String waybillId, String mScooterCode, List<TransportListBean.DeclareItemBean> declareItemBean) {
        Intent starter = new Intent(context, AddReceiveGoodActivity.class);
        starter.putExtra("waybillId", waybillId);
        starter.putExtra("mScooterCode", mScooterCode);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("transportListBeans", (Serializable) declareItemBean);
        starter.putExtras(mBundle);
        context.startActivityForResult(starter, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_receive_good;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setMainTitle(Color.WHITE, "新增");
        //点击清空
        toolbar.setRightTextViewImage(this, View.VISIBLE, Color.RED, "清空", R.mipmap.delete_all,
                v -> {
                    mEdtOverWeight.setText("");
                    mEdtDeadWeight.setText("");
                    mEtUldNumber.setText("");
                    mTvCooterWeight.setText("");
                    mTvScooter.setText("");
                    mTvWeight.setText("");
                    mEdtVolume.setText("");
                    mEdtNumber.setText("");
                });
        initView();
        //提交
        mBtnCommit.setOnClickListener(v -> {
            if ("".equals(mEdtNumber.getText().toString().trim())) {
                ToastUtil.showToast(this, "请输入件数");
            } else if ("".equals(mEdtVolume.getText().toString().trim())) {
                ToastUtil.showToast(this, "请输入体积");
            } else if ("".equals(mEdtNumber.getText().toString().trim())) {
                ToastUtil.showToast(this, "请输入重量");
            } else if ("".equals(mEtUldNumber.getText().toString().trim())) {
                ToastUtil.showToast(this, "请输入ULD重量");
            } else if ("".equals(mEdtDeadWeight.getText().toString().trim())) {
                ToastUtil.showToast(this, "请输入ULD自重");
            } else if ("".equals(mEdtOverWeight.getText().toString().trim())) {
                ToastUtil.showToast(this, "请输入超重重量");
            } else {
                finishForResult();
            }
        });
    }

    private void initView() {
        mMyAgentListBean = new MyAgentListBean();
        waybillId = getIntent().getStringExtra("waybillId");
        mScooterCode = getIntent().getStringExtra("mScooterCode");
        mDeclareItemBeans = (List<TransportListBean.DeclareItemBean>) getIntent().getSerializableExtra("transportListBeans");
        //品名
        mSpProductList = new ArrayList<>();
        if (mDeclareItemBeans != null) {
            for (int i = 0; i < mDeclareItemBeans.size(); i++) {
                GeneralSpinnerBean.SpProductBean mSpProductBean = new GeneralSpinnerBean.SpProductBean();
                mSpProductBean.setId(i + "");
                mSpProductBean.setValue(mDeclareItemBeans.get(i).getCargoCn());
                mSpProductList.add(mSpProductBean);
            }
        }
        mSpProductAdapter = new GeneralSpinnerAdapter(this, mSpProductList);
        mSpProduct.setAdapter(mSpProductAdapter);
        mSpProduct.setDropDownVerticalOffset(30);
        mSpProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //品名id
                mMyAgentListBean.setCargoId(mDeclareItemBeans.get(position).getCargoId());
                //品名
                mMyAgentListBean.setCargoCn(mDeclareItemBeans.get(position).getCargoCn());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //取重
        mBtnTakeWeight.setOnClickListener(v -> {
            mPresenter = new GetWeightPresenter(this);
            ((GetWeightPresenter) mPresenter).getWeight("pb1");
        });
        //板车号
        getNumberInfo();
    }


    private void finishForResult() {
        //id
        mMyAgentListBean.setId("");
        //运单id
        mMyAgentListBean.setWaybillId(waybillId);
        //件数
        mMyAgentListBean.setNumber(Integer.valueOf(mEdtNumber.getText().toString().trim()));
        //体积
        mMyAgentListBean.setVolume(Double.valueOf(mEdtVolume.getText().toString().trim()));
        //重量
        mMyAgentListBean.setWeight(Double.valueOf(mTvWeight.getText().toString().trim()));
        //板车号
        mMyAgentListBean.setScooterCode(mScooterCode);
        //板车自重
        mMyAgentListBean.setScooterWeight(mTvCooterWeight.getText().toString().trim());
        //ULD号
        mMyAgentListBean.setUldCode(mEtUldNumber.getText().toString().trim());
        //ULD自重
        mMyAgentListBean.setUldWeight(Integer.valueOf(mEdtDeadWeight.getText().toString().trim()));
        //超重重量
        mMyAgentListBean.setOverWeight(Integer.valueOf(mEdtOverWeight.getText().toString().trim()));
        if (null == scooterInfo) {
            Toast.makeText(this, "id为空不能提交", Toast.LENGTH_LONG).show();
            return;
        } else {
            mMyAgentListBean.setScooterId(scooterInfo.getId());
            mMyAgentListBean.setScooterType(scooterInfo.getScooterType());
        }
//        mMyAgentListBean.setRepPlaceId("2");
//        mMyAgentListBean.setRepName("普货01");
//        mMyAgentListBean.setRepPlaceNum("001");
//        mMyAgentListBean.setRepPlaceStatus("3");
//        mMyAgentListBean.setUldCode("4");
//        mMyAgentListBean.setUldType("5");
//        mMyAgentListBean.setIata("3U");
//        mMyAgentListBean.setPackagingType(mDeclareItemBeans.get(0).getPackagingType());
//            mAddInfoEntity.setCreateUser(UserInfoSingle.getInstance().getUsername());
//            mAddInfoEntity.setUpdateUser(UserInfoSingle.getInstance().getUsername());
        mPresenter = new ScooterInfoListPresenter(this);
        ((ScooterInfoListPresenter) mPresenter).addInfo(mMyAgentListBean);
    }

    //获取板车信息
    public void getNumberInfo() {
        mPresenter = new ScooterInfoListPresenter(this);
        BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
        MyAgentListBean myAgentListBean = new MyAgentListBean();
        baseFilterEntity.setSize(10);
        baseFilterEntity.setCurrent(1);
        myAgentListBean.setScooterCode(mScooterCode);
        baseFilterEntity.setFilter(myAgentListBean);
        ((ScooterInfoListPresenter) mPresenter).ScooterInfoList(baseFilterEntity);
    }

    //取重
    @Override
    public void getWeightResult(String result) {
        if (!"".equals(result))
            mTvWeight.setText(result);
        else
            ToastUtil.showToast("未拿到取重数据，请重新点击取重");
    }

    private void finishAndToast() {
        ToastUtil.showToast("未获取到板车信息，请手动输入");
    }

    //根据扫一扫获取到的板车号查询获得数据
    @Override
    public void scooterInfoListResult(List<ScooterInfoListBean> scooterInfoListBeans) {
        if (scooterInfoListBeans != null && scooterInfoListBeans.size() > 0) {
            scooterInfo = scooterInfoListBeans.get(0);
            //板车号
            mTvScooter.setText(scooterInfoListBeans.get(0).getScooterCode());
            //板车重量
            mTvCooterWeight.setText(scooterInfoListBeans.get(0).getScooterWeight() + "");
            //如果有板车号和板车重量，就不能让用户输入
            mTvScooter.setFocusable(false);
            mTvScooter.setEnabled(false);
            mTvCooterWeight.setFocusable(false);
            mTvCooterWeight.setEnabled(false);
            ((ScooterInfoListPresenter) mPresenter).exist(scooterInfoListBeans.get(0).getId());
        } else {
            finishAndToast();
        }
    }

    @Override
    public void existResult(MyAgentListBean existBean) {
        //如果返回为空就让用户输入
        int deadWeight = existBean.getUldWeight() == 0 ? 0 : existBean.getUldWeight();
        //uld号
        mEtUldNumber.setText(existBean.getUldCode() == null ? "" : existBean.getUldType() + existBean.getUldCode() + existBean.getIata());
        //uld重量
        mEdtDeadWeight.setText(deadWeight + "");
    }

    //提交
    @Override
    public void addInfoResult(MyAgentListBean result) {
        if (null != result) {
            Intent intent = new Intent();
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("mMyAgentListBean", result);
            intent.putExtras(mBundle);
            setResult(Constants.FINISH_REFRESH, intent);
            finish();
        } else
            Log.e("123", "123");
    }


    @Override
    public void toastView(String error) {
        ToastUtil.showToast(this, error);
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }


}
