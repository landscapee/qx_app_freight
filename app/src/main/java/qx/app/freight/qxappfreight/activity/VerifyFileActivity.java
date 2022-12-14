package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import qx.app.freight.qxappfreight.bean.GoodsIdEntity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.StorageCommitEntity;
import qx.app.freight.qxappfreight.bean.response.AddtionInvoicesBean;
import qx.app.freight.qxappfreight.bean.response.AirlineRequireBean;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.bean.response.DocumentsBean;
import qx.app.freight.qxappfreight.bean.response.ForwardInfoBean;
import qx.app.freight.qxappfreight.bean.response.HeChaBean;
import qx.app.freight.qxappfreight.bean.response.TestInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.bean.response.WaybillCommodities;
import qx.app.freight.qxappfreight.constant.HttpConstant;
import qx.app.freight.qxappfreight.contract.AirlineRequireContract;
import qx.app.freight.qxappfreight.contract.SubmissionContract;
import qx.app.freight.qxappfreight.presenter.AirlineRequirePresenter;
import qx.app.freight.qxappfreight.presenter.SubmissionPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * ????????????????????????
 */
public class VerifyFileActivity extends BaseActivity implements MultiFunctionRecylerView.OnRefreshListener, AirlineRequireContract.airlineRequireView, EmptyLayout.OnRetryLisenter, SubmissionContract.submissionView {
    @BindView(R.id.mfrv_verify)
    RecyclerView mMfrvData;
    @BindView(R.id.tv_collect_require)
    TextView mTvCollectRequire;
    @BindView(R.id.tv_wenjian)
    TextView mTvWenjian;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_waybill_code)
    TextView tvWaybillCode;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_special_code)
    TextView tvSpecialCode;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_weight)
    TextView tvWeight;

    @BindView(R.id.et_remark)
    EditText etRemark;


    private VerifyFileAdapter mAdapter;
    private List <AddtionInvoicesBean.AddtionInvoices> mList = new ArrayList <>();
    private String mFilePath;
    private int insCheck; //??????????????????1?????? 0?????????
    private int mSpotResult;
    private TransportDataBase mBean;
    private DeclareWaybillBean mDecBean;
    private TestInfoListBean mAcTestInfoListBean;

    public static void startActivity(Activity context,
                                     TransportDataBase mBean,
                                     DeclareWaybillBean mDecBean,
                                     String filePath,
                                     int spotResult,
                                     int insCheck,
                                     TestInfoListBean mAcTestInfoListBean
    ) {
        Intent intent = new Intent(context, VerifyFileActivity.class);
        intent.putExtra("mBean", mBean);
        intent.putExtra("mDecBean", mDecBean);
        intent.putExtra("filePath", filePath);
        intent.putExtra("spotResult", spotResult);
        intent.putExtra("insCheck", insCheck);
        intent.putExtra("mAcTestInfoListBean", mAcTestInfoListBean);
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
        toolbar.setMainTitle(Color.WHITE, "??????????????????");
        initData();
        mPresenter = new AirlineRequirePresenter(this);
        ((AirlineRequirePresenter) mPresenter).forwardInfo(mBean.getShipperCompanyId());
    }

    private void initData() {
        mBean = (TransportDataBase) getIntent().getSerializableExtra("mBean");
        setWaybillInfo(mBean);
        mDecBean = (DeclareWaybillBean) getIntent().getSerializableExtra("mDecBean");
        mAcTestInfoListBean = (TestInfoListBean) getIntent().getSerializableExtra("mAcTestInfoListBean");
        mFilePath = getIntent().getStringExtra("filePath");
        mSpotResult = getIntent().getIntExtra("spotResult", -1);
        insCheck = getIntent().getIntExtra("insCheck", 0);

        etRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtil.isEmpty(etRemark.getText().toString())){
                    mBean.setRemark(etRemark.getText().toString());
                }
                else {
                    mBean.setRemark("");
                }
            }
        });

        if (null != mDecBean.getSpWaybillFile()) {

            if ("[]".equals(mDecBean.getSpWaybillFile().getAddtionInvoices()) && !TextUtils.isEmpty(mDecBean.getSpWaybillFile().getAddtionInvoices())) {
                Gson mGson = new Gson();
                AddtionInvoicesBean.AddtionInvoices[] addtionInvoices = mGson.fromJson(mDecBean.getSpWaybillFile().getAddtionInvoices(), AddtionInvoicesBean.AddtionInvoices[].class);
                List <AddtionInvoicesBean.AddtionInvoices> addtionInvoices1 = Arrays.asList(addtionInvoices);
                mList.clear();
                mList.addAll(addtionInvoices1);
                mMfrvData.setLayoutManager(new LinearLayoutManager(this));
                mAdapter = new VerifyFileAdapter(mList);
                mMfrvData.setAdapter(mAdapter);
            } else if (!TextUtils.isEmpty(mDecBean.getAdditionTypeArr())) {
                Gson mGson = new Gson();
                HeChaBean[] addtionInvoices = mGson.fromJson(mDecBean.getSpWaybillFile().getAddtionInvoices(), HeChaBean[].class);
                List <HeChaBean> heChaBeanList = Arrays.asList(addtionInvoices);
                List <AddtionInvoicesBean.AddtionInvoices> addList = new ArrayList <>();
                for (int i = 0; i < heChaBeanList.size(); i++) {
                    AddtionInvoicesBean.AddtionInvoices add = new AddtionInvoicesBean.AddtionInvoices();
                    add.setFileTypeName(heChaBeanList.get(i).getFileTypeName());
                    add.setFilePath(heChaBeanList.get(i).getFilePath());
                    addList.add(add);
                }
                mList.addAll(addList);
                mMfrvData.setLayoutManager(new LinearLayoutManager(this));
                mAdapter = new VerifyFileAdapter(mList);
                mMfrvData.setAdapter(mAdapter);
            } else
                ToastUtil.showToast("????????????");

            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                if (!StringUtil.isEmpty(mList.get(position).getFilePath())) {
                    List <String> array = new ArrayList <>();
                    array.add(HttpConstant.IMAGEURL + mList.get(position).getFilePath());
                    ImgPreviewAct.startPreview(VerifyFileActivity.this, array, 0);
                }
            });
        }
        getCommdityById(mBean);
    }

    private void getCommdityById(TransportDataBase transportDataBase) {
        if (transportDataBase != null && transportDataBase.getSpWaybillCommodities() != null) {
            mPresenter = new AirlineRequirePresenter(this);
            List <String> goodsIds = new ArrayList <>();
            for (WaybillCommodities waybillCommodities : transportDataBase.getSpWaybillCommodities()) {
                goodsIds.add(waybillCommodities.getCargoId());
            }
            if (goodsIds.size() > 0) {
                GoodsIdEntity goodsIdEntity = new GoodsIdEntity();
                goodsIdEntity.setParam(goodsIds);
                goodsIdEntity.setOutlet(1);
                ((AirlineRequirePresenter) mPresenter).getgetCommdityById(goodsIdEntity);
            }


        }
    }

    private void setWaybillInfo(TransportDataBase mBean) {
        if (mBean != null) {
            tvWaybillCode.setText("?????????:   " + mBean.getWaybillCode());
            tvGoodsName.setText("??????:  " + mBean.getCargoCn());
            if (!StringUtil.isEmpty(mBean.getSpecialCode()))
                tvSpecialCode.setText("????????????:  " + mBean.getSpecialCode());
            else
                tvSpecialCode.setText("????????????:  - -");
            tvNumber.setText("??????:  " + mBean.getTotalNumber());
            tvWeight.setText("??????:  " + mBean.getTotalWeight());
            etRemark.setText(mBean.getRemark());
        } else {
            tvWaybillCode.setText("?????????:   ");
            tvGoodsName.setText("??????:  ");
            tvSpecialCode.setText("????????????:  ");
            tvNumber.setText("??????:  ");
            tvWeight.setText("??????:  ");
        }

    }

    @OnClick({R.id.agree_tv, R.id.refuse_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agree_tv:
                VerifyCargoActivity.startActivity(this,
                        mBean,
                        mDecBean,
                        mFilePath,//????????????
                        insCheck,//??????????????????0?????? 1?????????
                        0,//??????????????????0?????? 1?????????
                        mSpotResult,
                        UserInfoSingle.getInstance().getUserId(),
                        mAcTestInfoListBean
                );
                break;
            case R.id.refuse_tv:
//                InputForStrDialog inputForStrDialog = new InputForStrDialog(this);
//                inputForStrDialog.isCanceledOnTouchOutside(false);
//                inputForStrDialog.isCanceled(true)
//                        .setTitle("??????")
//                        .setHint("?????????????????????????????????")
//                        .setPositiveButton("??????")
//                        .setNegativeButton("??????");
//                inputForStrDialog.setOnClickListener(new InputForStrDialog.OnClickListener() {
//                    @Override
//                    public void onClick(Dialog dialog, boolean confirm, String result) {
//                        if (!confirm)
//                            return;
//                        mPresenter = new SubmissionPresenter(VerifyFileActivity.this);
//                        StorageCommitEntity mStorageCommitEntity = new StorageCommitEntity();
//                        if (!StringUtil.isEmpty(result)) {
//                            mStorageCommitEntity.setRemark(result);
//                        } else {
//                            mStorageCommitEntity.setRemark(null);
//                        }
//                        mStorageCommitEntity.setWaybillId(mBean.getId());
//                        mStorageCommitEntity.setAddOrderId(mBean.getId());//??????????????????
//                        mStorageCommitEntity.setWaybillCode(mBean.getWaybillCode());
//                        mStorageCommitEntity.setInsUserId(UserInfoSingle.getInstance().getUserId());
//                        mStorageCommitEntity.setInsFile(mFilePath);
//                        mStorageCommitEntity.setInsCheck(1);
//                        mStorageCommitEntity.setFileCheck(1);
//                        mStorageCommitEntity.setTaskTypeCode(mBean.getTaskTypeCode());
//                        mStorageCommitEntity.setType(1);
//                        mStorageCommitEntity.setTaskId(mBean.getTaskId());
//                        mStorageCommitEntity.setUserId(mDecBean.getFlightNo());
//                        //??????
//                        if (null != mAcTestInfoListBean.getFreightInfo()) {
//                            mStorageCommitEntity.setInsUserHead(mAcTestInfoListBean.getFreightInfo().get(0).getInspectionHead());
//                            mStorageCommitEntity.setInsUserName(mAcTestInfoListBean.getFreightInfo().get(0).getInspectionName());
//                            mStorageCommitEntity.setInsUserId(mAcTestInfoListBean.getFreightInfo().get(0).getId());
//                            mStorageCommitEntity.setInsDangerStart(mAcTestInfoListBean.getFreightInfo().get(0).getDangerBookStart());
//                            mStorageCommitEntity.setInsDangerEnd(mAcTestInfoListBean.getFreightInfo().get(0).getDangerBookEnd());
//                        }
//                        if (null != mAcTestInfoListBean.getInsInfo()) {
//                            mStorageCommitEntity.setInsStartTime(mAcTestInfoListBean.getInsInfo().getInsStartTime());
//                            mStorageCommitEntity.setInsEndTime(mAcTestInfoListBean.getInsInfo().getInsEndTime());
//                        }
//                        ((SubmissionPresenter) mPresenter).submission(mStorageCommitEntity);
//                    }
//                });
//                inputForStrDialog.show();

                mPresenter = new SubmissionPresenter(VerifyFileActivity.this);
                StorageCommitEntity mStorageCommitEntity = new StorageCommitEntity();
                mStorageCommitEntity.setRemark(mBean.getRemark());
                mStorageCommitEntity.setWaybillId(mBean.getId());
                mStorageCommitEntity.setAddOrderId(mBean.getId());//??????????????????
                mStorageCommitEntity.setWaybillCode(mBean.getWaybillCode());
                mStorageCommitEntity.setInsUserId(UserInfoSingle.getInstance().getUserId());
                mStorageCommitEntity.setInsFile(mFilePath);
                mStorageCommitEntity.setInsCheck(1);
                mStorageCommitEntity.setFileCheck(1);
                mStorageCommitEntity.setTaskTypeCode(mBean.getTaskTypeCode());
                mStorageCommitEntity.setType(1);
                mStorageCommitEntity.setTaskId(mBean.getTaskId());
                mStorageCommitEntity.setUserId(mDecBean.getFlightNo());
                //??????
                if (null != mAcTestInfoListBean.getFreightInfo()) {
                    mStorageCommitEntity.setInsUserHead(mAcTestInfoListBean.getFreightInfo().get(0).getInspectionHead());
                    mStorageCommitEntity.setInsUserName(mAcTestInfoListBean.getFreightInfo().get(0).getInspectionName());
                    mStorageCommitEntity.setInsUserId(mAcTestInfoListBean.getFreightInfo().get(0).getId());
                    mStorageCommitEntity.setInsDangerStart(mAcTestInfoListBean.getFreightInfo().get(0).getDangerBookStart());
                    mStorageCommitEntity.setInsDangerEnd(mAcTestInfoListBean.getFreightInfo().get(0).getDangerBookEnd());
                }
                if (null != mAcTestInfoListBean.getInsInfo()) {
                    mStorageCommitEntity.setInsStartTime(mAcTestInfoListBean.getInsInfo().getInsStartTime());
                    mStorageCommitEntity.setInsEndTime(mAcTestInfoListBean.getInsInfo().getInsEndTime());
                }
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
        showProgessDialog("????????????????????????????????????");
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
    public void airlineRequireResult(List <AirlineRequireBean> airlineRequireBeans) {
        if (airlineRequireBeans != null) {
            for (int i = 0; i < airlineRequireBeans.size(); i++) {
                mTvCollectRequire.setText(airlineRequireBeans.get(i).getRequire());
            }
        } else {
            Log.e("airlineRequireBeans", "airlineRequireBeans??????");
        }
    }

    @Override
    public void forwardInfoResult(ForwardInfoBean forwardInfoBean) {
        if (null != forwardInfoBean) {
            String str = Arrays.toString(forwardInfoBean.getFreightAptitudeName());
            str = str.replaceAll(",", "\n");
            mTvCollectRequire.setText(str);
        } else
            Log.e("?????????????????????", "");
    }

    @Override
    public void getgetCommdityByIdResult(List <DocumentsBean> goodsNames) {
        if (goodsNames != null && goodsNames.size() > 0) {
            for (DocumentsBean documentsBean : goodsNames) {
                AddtionInvoicesBean.AddtionInvoices addtionInvoices = new AddtionInvoicesBean.AddtionInvoices();
                addtionInvoices.setId(documentsBean.getId());
                addtionInvoices.setFileTypeName(documentsBean.getName());
                boolean ishave = false;
                for (AddtionInvoicesBean.AddtionInvoices addtionInvoices1 : mList) {
                    if (addtionInvoices1.getId().equals(documentsBean.getId())) {
                        ishave = true;
                    }
                }
                if (!ishave)
                    mList.add(addtionInvoices);
            }
            if (mList.size() == 0) {
                llContent.setVisibility(View.GONE);
                mTvWenjian.setVisibility(View.VISIBLE);
            } else {
                llContent.setVisibility(View.VISIBLE);
                mTvWenjian.setVisibility(View.GONE);
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void toastView(String error) {
        mTvCollectRequire.setText(error);
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
    public void submissionResult(String result) {
        if (!TextUtils.isEmpty(result)) {
            ToastUtil.showToast(result);
            setResult(404);
            finish();
        }
    }
}
