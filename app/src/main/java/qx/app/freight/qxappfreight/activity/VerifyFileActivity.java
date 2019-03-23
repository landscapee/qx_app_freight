package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.zxing.common.StringUtils;
import com.ouyben.empty.EmptyLayout;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.VerifyFileAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.AddtionInvoicesBean;
import qx.app.freight.qxappfreight.bean.response.AirlineRequireBean;
import qx.app.freight.qxappfreight.bean.response.ForwardInfoBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.contract.AirlineRequireContract;
import qx.app.freight.qxappfreight.presenter.AirlineRequirePresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 核查证明文件页面
 */
public class VerifyFileActivity extends BaseActivity implements MultiFunctionRecylerView.OnRefreshListener, AirlineRequireContract.airlineRequireView, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_verify)
    RecyclerView mMfrvData;
    @BindView(R.id.tv_collect_require)
    TextView mTvCollectRequire;

    private TransportListBean.DeclareWaybillAdditionBean mDeclareData;

    private VerifyFileAdapter mAdapter;
    private List<AddtionInvoicesBean.AddtionInvoices> mList;
    private String mTaskId;
    private String mFilePath;
    private String mSpotFlag;
    private String mFlightNumber;
    private int insCheck; //报检是否合格1合格 0不合格
    private String mShipperCompanyId;


    public static void startActivity(Activity context, TransportListBean.DeclareWaybillAdditionBean declareWaybillAdditionBean, String taskId, String filePath, String spotFlag, int insCheck, String flightNumber, String shipperCompanyId) {
        Intent intent = new Intent(context, VerifyFileActivity.class);
        intent.putExtra("DeclareWaybillAdditionBean", declareWaybillAdditionBean);
        intent.putExtra("taskId", taskId);
        intent.putExtra("filePath", filePath);
        intent.putExtra("spotFlag", spotFlag);
        intent.putExtra("insCheck", insCheck);
        intent.putExtra("flightNumber", flightNumber);
        intent.putExtra("shipperCompanyId", shipperCompanyId);
        context.startActivityForResult(intent, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_verify_file;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "核查证明文件");
        //获取航司资质   shipperCompanyId
//        BaseFilterEntity baseFilterEntity = new BaseFilterEntity();
//        MyAgentListBean myAgentListBean = new MyAgentListBean();
//        myAgentListBean.setIata("");
//        baseFilterEntity.setSize(10);
//        baseFilterEntity.setCurrent(1);
//        baseFilterEntity.setFilter(myAgentListBean);

        initData();
        mPresenter = new AirlineRequirePresenter(this);
        ((AirlineRequirePresenter) mPresenter).forwardInfo(mShipperCompanyId);
    }

    private void initData() {
        mDeclareData = (TransportListBean.DeclareWaybillAdditionBean) getIntent().getSerializableExtra("DeclareWaybillAdditionBean");
        mTaskId = getIntent().getStringExtra("taskId");
        mFilePath = getIntent().getStringExtra("filePath");
        mSpotFlag = getIntent().getStringExtra("spotFlag");
        mFlightNumber = getIntent().getStringExtra("flightNumber");
        mShipperCompanyId = getIntent().getStringExtra("shipperCompanyId");
        insCheck = getIntent().getIntExtra("insCheck", 0);
        AddtionInvoicesBean addtionInvoicesBean = new AddtionInvoicesBean();
        if (mDeclareData != null) {
            String str = mDeclareData.getAddtionInvoices();
            mList = JSON.parseArray(str, AddtionInvoicesBean.AddtionInvoices.class);
            mMfrvData.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new VerifyFileAdapter(mList);
            mMfrvData.setAdapter(mAdapter);
        } else
            ToastUtil.showToast(this, addtionInvoicesBean.getId());
    }

    @OnClick({R.id.agree_tv, R.id.refuse_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agree_tv:
                ToastUtil.showToast(this, "合格");
                VerifyCargoActivity.startActivity(this,
                        mDeclareData.getWaybillId(),
                        mDeclareData.getId(),
                        mFilePath,//图片路径
                        insCheck,//报检是否合格0合格 1不合格
                        0,//资质是否合格0合格 1不合格
                        mTaskId, //当前任务id
                        mSpotFlag,
                        UserInfoSingle.getInstance().getUserId(), //当前提交人id
                        mFlightNumber
                );
                break;
            case R.id.refuse_tv:
                ToastUtil.showToast(this, "不合格");
                ToastUtil.showToast(this, "合格");
                VerifyCargoActivity.startActivity(this,
                        mDeclareData.getWaybillId(),
                        mDeclareData.getId(),
                        mFilePath,//图片路径
                        insCheck,//报检是否合格0合格 1不合格
                        1,//资质是否合格0合格 1不合格
                        mTaskId, //当前任务id
                        mSpotFlag,
                        UserInfoSingle.getInstance().getUserId(), //当前提交人id
                        mFlightNumber
                );
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 404) {
            setResult(404);
            finish();
        }
    }


    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据。。。。。。");
        new Handler().postDelayed(() -> {
            initData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMore() {
        initData();
    }


    @Override
    public void airlineRequireResult(List<AirlineRequireBean> airlineRequireBeans) {
        if (airlineRequireBeans != null) {
            for (int i = 0; i < airlineRequireBeans.size(); i++) {
                mTvCollectRequire.setText(airlineRequireBeans.get(i).getRequire());
            }
        } else {
            Log.e("airlineRequireBeans", "airlineRequireBeans为空");
        }
    }

    @Override
    public void forwardInfoResult(ForwardInfoBean forwardInfoBean) {
        if (null != forwardInfoBean) {
            String str = Arrays.toString(forwardInfoBean.getFreightAptitudeName());
            mTvCollectRequire.setText(str);
        } else
            Log.e("核查证明文件空", "");
    }

    @Override
    public void toastView(String error) {
        Log.e(error, error);
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }
}
