package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.VerifyFileAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.StorageCommitEntity;
import qx.app.freight.qxappfreight.bean.response.AddtionInvoicesBean;
import qx.app.freight.qxappfreight.bean.response.AirlineRequireBean;
import qx.app.freight.qxappfreight.bean.response.ForwardInfoBean;
import qx.app.freight.qxappfreight.bean.response.HeChaBean;
import qx.app.freight.qxappfreight.constant.HttpConstant;
import qx.app.freight.qxappfreight.contract.AirlineRequireContract;
import qx.app.freight.qxappfreight.contract.SubmissionContract;
import qx.app.freight.qxappfreight.presenter.AirlineRequirePresenter;
import qx.app.freight.qxappfreight.presenter.SubmissionPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 核查证明文件页面
 */
public class VerifyFileActivity extends BaseActivity implements MultiFunctionRecylerView.OnRefreshListener, AirlineRequireContract.airlineRequireView, EmptyLayout.OnRetryLisenter, SubmissionContract.submissionView {
    @BindView(R.id.mfrv_verify)
    RecyclerView mMfrvData;
    @BindView(R.id.tv_collect_require)
    TextView mTvCollectRequire;


    private VerifyFileAdapter mAdapter;
    private List<AddtionInvoicesBean.AddtionInvoices> mList = new ArrayList<>();
    private String mTaskId;
    private String mFilePath;
    private String mSpotFlag;
    private String mFlightNumber;
    private int insCheck; //报检是否合格1合格 0不合格
    private String mShipperCompanyId;
    private String mAdditionTypeArr;
    private String mId;
    private String mWaybillId;
    private String mTaskTypeCode;
    private String mWaybillCode;
    private String Tid;
    private int mSpotResult;

    private String mJianYan;


    public static void startActivity(Activity context,
                                     String taskTypeCode,
                                     String waybillId,
                                     String id,
                                     String additionTypeArr,
                                     String taskId,
                                     String filePath,
                                     String spotFlag,
                                     int spotResult,
                                     int insCheck,
                                     String flightNumber,
                                     String shipperCompanyId,
                                     String waybillCode,
                                     String tid,
                                     String mJianYan) {
        Intent intent = new Intent(context, VerifyFileActivity.class);
//        intent.putExtra("DeclareWaybillAdditionBean", declareWaybillAdditionBean);
        intent.putExtra("taskTypeCode", taskTypeCode);
        intent.putExtra("waybillId", waybillId);
        intent.putExtra("id", id);
        intent.putExtra("additionTypeArr", additionTypeArr);
        intent.putExtra("taskId", taskId);
        intent.putExtra("filePath", filePath);
        intent.putExtra("spotFlag", spotFlag);
        intent.putExtra("spotResult", spotResult);
        intent.putExtra("insCheck", insCheck);
        intent.putExtra("flightNumber", flightNumber);
        intent.putExtra("shipperCompanyId", shipperCompanyId);
        intent.putExtra("waybillCode", waybillCode);
        intent.putExtra("tid", tid);
        intent.putExtra("mJianYan", mJianYan);
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
        initData();
        mPresenter = new AirlineRequirePresenter(this);
        ((AirlineRequirePresenter) mPresenter).forwardInfo(mShipperCompanyId);
    }

    private void initData() {
//        mDeclareData = (TransportListBean.DeclareWaybillAdditionBean) getIntent().getSerializableExtra("DeclareWaybillAdditionBean");
        mWaybillId = getIntent().getStringExtra("waybillId");
        mTaskTypeCode = getIntent().getStringExtra("taskTypeCode");
        mTaskId = getIntent().getStringExtra("taskId");
        mId = getIntent().getStringExtra("id");
        mAdditionTypeArr = getIntent().getStringExtra("additionTypeArr");
        mJianYan = getIntent().getStringExtra("mJianYan");
        mFilePath = getIntent().getStringExtra("filePath");
        mSpotFlag = getIntent().getStringExtra("spotFlag");
        mSpotResult = getIntent().getIntExtra("spotResult", -1);
        mFlightNumber = getIntent().getStringExtra("flightNumber");
        mShipperCompanyId = getIntent().getStringExtra("shipperCompanyId");
        mWaybillCode = getIntent().getStringExtra("waybillCode");
        Tid = getIntent().getStringExtra("tid");
        insCheck = getIntent().getIntExtra("insCheck", 0);
        if (!mAdditionTypeArr.equals("[]")&&!TextUtils.isEmpty(mAdditionTypeArr)) {
            Gson mGson = new Gson();
            AddtionInvoicesBean.AddtionInvoices[] addtionInvoices = mGson.fromJson(mAdditionTypeArr, AddtionInvoicesBean.AddtionInvoices[].class);
            List<AddtionInvoicesBean.AddtionInvoices> addtionInvoices1 = Arrays.asList(addtionInvoices);
            mList.clear();
            mList.addAll(addtionInvoices1);
            mMfrvData.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new VerifyFileAdapter(mList);
            mMfrvData.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                        List<String> array = new ArrayList<>();
                        array.add(HttpConstant.IMAGEURL + mList.get(position).getFilePath());
                        ImgPreviewAct.startPreview(VerifyFileActivity.this, array, position);
                    }
            );
        } else if (!TextUtils.isEmpty(mJianYan)) {
            Gson mGson = new Gson();
            HeChaBean[] addtionInvoices = mGson.fromJson(mJianYan, HeChaBean[].class);
            List<HeChaBean> heChaBeanList = Arrays.asList(addtionInvoices);
            List<AddtionInvoicesBean.AddtionInvoices> addList = new ArrayList<>();
            for (int i = 0; i < heChaBeanList.size(); i++) {
                AddtionInvoicesBean.AddtionInvoices add = new AddtionInvoicesBean.AddtionInvoices();
                add.setFileTypeName(heChaBeanList.get(i).getName());
                addList.add(add);
            }
            mList.addAll(addList);
            mMfrvData.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new VerifyFileAdapter(mList);
            mMfrvData.setAdapter(mAdapter);
        } else
            ToastUtil.showToast("数据为空");


    }

    @OnClick({R.id.agree_tv, R.id.refuse_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agree_tv:
                ToastUtil.showToast(this, "合格");
                VerifyCargoActivity.startActivity(this,
                        mTaskTypeCode,
                        mWaybillId,
                        mId,
                        mFilePath,//图片路径
                        insCheck,//报检是否合格0合格 1不合格
                        0,//资质是否合格0合格 1不合格
                        mTaskId, //当前任务id
                        mSpotFlag,
                        mSpotResult,
                        UserInfoSingle.getInstance().getUserId(), //当前提交人id
                        mFlightNumber,
                        mWaybillCode,
                        Tid
                );
                break;
            case R.id.refuse_tv:
                mPresenter = new SubmissionPresenter(this);
                StorageCommitEntity mStorageCommitEntity = new StorageCommitEntity();
                mStorageCommitEntity.setWaybillId(Tid);
                mStorageCommitEntity.setWaybillCode(mWaybillCode);
                mStorageCommitEntity.setInsUserId(UserInfoSingle.getInstance().getUserId());
                mStorageCommitEntity.setInsFile(mFilePath);
                mStorageCommitEntity.setInsCheck(1);
                mStorageCommitEntity.setFileCheck(1);
                mStorageCommitEntity.setTaskTypeCode(mTaskTypeCode);
                mStorageCommitEntity.setType(1);
                mStorageCommitEntity.setTaskId(mSpotFlag);
                mStorageCommitEntity.setUserId(mFlightNumber);
                //新加
                mStorageCommitEntity.setInsUserName("");
                mStorageCommitEntity.setInsDangerEnd(123);
                mStorageCommitEntity.setInsDangerStart(123);
                mStorageCommitEntity.setInsStartTime(123);
                mStorageCommitEntity.setInsEndTime(123);
                mStorageCommitEntity.setInsUserHead("");
                ((SubmissionPresenter) mPresenter).submission(mStorageCommitEntity);
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
            str = str.replaceAll(",", "\n");
            mTvCollectRequire.setText(str);
        } else
            Log.e("核查证明文件空", "");
    }

    @Override
    public void toastView(String error) {
        mTvCollectRequire.setText(error);
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }

    @Override
    public void submissionResult(String result) {
        if (!TextUtils.isEmpty(result)) {
            ToastUtil.showToast(result);
            setResult(404);
            finish();
        }
    }
}
