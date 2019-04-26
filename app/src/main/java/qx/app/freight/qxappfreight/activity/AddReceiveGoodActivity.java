package qx.app.freight.qxappfreight.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.GeneralSpinnerAdapter;
import qx.app.freight.qxappfreight.adapter.OverweightRecordAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.RcInfoOverweight;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.GeneralSpinnerBean;
import qx.app.freight.qxappfreight.bean.response.DeclareItem;
import qx.app.freight.qxappfreight.bean.response.MyAgentListBean;
import qx.app.freight.qxappfreight.bean.response.ScooterInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.contract.GetWeightContract;
import qx.app.freight.qxappfreight.contract.ScooterInfoListContract;
import qx.app.freight.qxappfreight.dialog.ReturnGoodsDialog;
import qx.app.freight.qxappfreight.presenter.GetWeightPresenter;
import qx.app.freight.qxappfreight.presenter.ScooterInfoListPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CommonPopupWindow;
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
    @BindView(R.id.ll_overweight)
    LinearLayout llOverweight;
    @BindView(R.id.edt_overweight)
    TextView mEdtOverWeight;    //超重重量
    @BindView(R.id.tv_product_name)
    TextView tvProductName;    //品名

    @BindView(R.id.edt_dead_weight)
    EditText mEdtDeadWeight;   //ULD自重
    @BindView(R.id.et_uldnumber)
    EditText mEtUldNumber;     //Uld号
    @BindView(R.id.tv_cooter_weight)
    TextView mTvCooterWeight;  //板车重量
    @BindView(R.id.tv_scooter)
    TextView mTvScooter;         //板车号
    @BindView(R.id.btn_takeweight)
    Button mBtnTakeWeight;      //重量
    @BindView(R.id.tv_weight)
    EditText mTvWeight;        //输入重量
    @BindView(R.id.edt_volume)
    EditText mEdtVolume;        //体积
    @BindView(R.id.edt_number)
    EditText mEdtNumber;        //件数
    @BindView(R.id.btn_commit)
    Button mBtnCommit;          //提交
    @BindView(R.id.tv_info)
    TextView mTvInfo;

    private GeneralSpinnerAdapter mSpProductAdapter;
    private List<GeneralSpinnerBean.SpProductBean> mSpProductList;//品名
    private MyAgentListBean mMyAgentListBean;
    private String waybillId, waybillCode;
    private List<DeclareItem> mDeclareItemBeans;
    private String mScooterCode;
    private ScooterInfoListBean scooterInfo;

    List<RcInfoOverweight> rcInfoOverweight; // 超重记录列表

    public static void startActivity(Activity context, String waybillId, String mScooterCode, String waybillCode, List<DeclareItem> declareItemBean) {
        Intent starter = new Intent(context, AddReceiveGoodActivity.class);
        starter.putExtra("waybillId", waybillId);
        starter.putExtra("mScooterCode", mScooterCode);
        starter.putExtra("waybillCode", waybillCode);
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
            } else if ("".equals(mEdtNumber.getText().toString().trim())) {
                ToastUtil.showToast(this, "请输入重量");
            } else if ("".equals(mTvScooter.getText().toString().trim())) {
                ToastUtil.showToast(this, "请输入板车号");
            } else {
                finishForResult();
            }
        });
    }

    private void initView() {
        String content = "带<font color='#FF0000'>" + "*" + "</font>为必填项";
        mTvInfo.setText(Html.fromHtml(content));

        mMyAgentListBean = new MyAgentListBean();
        waybillId = getIntent().getStringExtra("waybillId");
        mScooterCode = getIntent().getStringExtra("mScooterCode");
        waybillCode = getIntent().getStringExtra("waybillCode");
        mDeclareItemBeans = (List<DeclareItem>) getIntent().getSerializableExtra("transportListBeans");
        tvProductName.setText(mDeclareItemBeans.get(0).getCargoCn());
        mMyAgentListBean.setCargoId(mDeclareItemBeans.get(0).getCargoId());
        //品名  coldStorage  deptCode
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

        rcInfoOverweight = new ArrayList <>();
        llOverweight.setOnClickListener(v->{

            showPopWindowList();

        });
        //板车号
        getNumberInfo();
    }


    private void finishForResult() {
        //id
        mMyAgentListBean.setId("");
        //运单id
        mMyAgentListBean.setWaybillId(waybillId);

        mMyAgentListBean.setWaybillCode(waybillCode);
        //件数
        mMyAgentListBean.setNumber(Integer.valueOf(mEdtNumber.getText().toString().trim()));
        //体积
        if ("".equals(mEdtVolume.getText().toString())) {
            mMyAgentListBean.setVolume(0);
        } else {
            mMyAgentListBean.setVolume(Double.valueOf(mEdtVolume.getText().toString().trim()));
        }
        //重量
        if ("".equals(mTvWeight.getText().toString())) {
            mMyAgentListBean.setWeight(0);
        } else {
            mMyAgentListBean.setWeight(Double.valueOf(mTvWeight.getText().toString().trim()));
        }
        //板车号
        mMyAgentListBean.setScooterCode(mScooterCode);
        //板车自重
        mMyAgentListBean.setScooterWeight(mTvCooterWeight.getText().toString().trim());
        //ULD号
        mMyAgentListBean.setUldCode(mEtUldNumber.getText().toString().trim());
        //ULD自重
        if (!TextUtils.isEmpty(mEdtDeadWeight.getText().toString().trim()))
            mMyAgentListBean.setUldWeight(Integer.valueOf(mEdtDeadWeight.getText().toString().trim()));
        else
            mMyAgentListBean.setUldWeight(0);
        //超重重量
//        if ("".equals(mEdtOverWeight.getText().toString())) {
//            mMyAgentListBean.setOverWeight(0);
//        } else {
//            mMyAgentListBean.setOverWeight(Integer.valueOf(mEdtOverWeight.getText().toString().trim()));
//        }
        mMyAgentListBean.setRcInfoOverweight(rcInfoOverweight);


        if (null == scooterInfo) {
            ToastUtil.showToast("id为空不能提交");
            return;
        } else {
            mMyAgentListBean.setScooterId(scooterInfo.getId());
            mMyAgentListBean.setScooterType(scooterInfo.getScooterType());
        }
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
            if (2 == scooterInfoListBeans.get(0).getScooterType()) {
                mEdtDeadWeight.setHint("平板车不能输入重量");
                mEtUldNumber.setHint("平板车不能输入ULD号");
                mEdtDeadWeight.setFocusable(false);
                mEdtDeadWeight.setEnabled(false);
                mEtUldNumber.setFocusable(false);
                mEtUldNumber.setEnabled(false);
            } else {
                //板车号
                mTvScooter.setText(scooterInfoListBeans.get(0).getScooterCode());
                //板车重量
                mTvCooterWeight.setText(scooterInfoListBeans.get(0).getScooterWeight() + "");
            }
            //如果有板车号和板车重量，就不能让用户输入
//            mTvScooter.setFocusable(false);
//            mTvScooter.setEnabled(false);
//            mTvCooterWeight.setFocusable(false);
//            mTvCooterWeight.setEnabled(false);
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
        mEtUldNumber.setText(existBean.getUldCode() == null ? "" : existBean.getUldCode());
        //uld重量
        if (deadWeight == 0)
            mEdtDeadWeight.setText("");
        else
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


    private void showPopWindowList() {

//        overweightRecordAdapter.notifyDataSetChanged();
//
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 0.3f;
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        getWindow().setAttributes(lp);
//        window.getPopupWindow().setAnimationStyle(R.style.animTranslate);
//        window.showAtLocation(tvProductName, Gravity.CENTER, 0, 0);
        ReturnGoodsDialog dialog = new ReturnGoodsDialog(this);
        dialog.setData(rcInfoOverweight)
                .setOnClickListener(new ReturnGoodsDialog.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                        mEdtOverWeight.setText(text);
                    }
                })
                .show();

    }

}
