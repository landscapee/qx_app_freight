package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yongchun.library.view.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;
import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.StorageCommitEntity;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.bean.response.TestInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.constant.HttpConstant;
import qx.app.freight.qxappfreight.contract.SubmissionContract;
import qx.app.freight.qxappfreight.contract.TestInfoContract;
import qx.app.freight.qxappfreight.contract.UploadsContract;
import qx.app.freight.qxappfreight.dialog.InputForStrDialog;
import qx.app.freight.qxappfreight.presenter.SubmissionPresenter;
import qx.app.freight.qxappfreight.presenter.TestInfoPresenter;
import qx.app.freight.qxappfreight.presenter.UploadsPresenter;
import qx.app.freight.qxappfreight.utils.GlideUtil;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * ???????????????????????????
 */
public class VerifyStaffActivity extends BaseActivity implements UploadsContract.uploadsView, TestInfoContract.testInfoView, SubmissionContract.submissionView {
    @BindView(R.id.sp_select_staff)
    Spinner mSpSelectStaff; //???????????????
    @BindView(R.id.tv_certificate_in_date)
    TextView mTvCertificateInDate;//???????????????
    @BindView(R.id.iv_staff_photo_old1)
    ImageView mIvStaffOld1;//?????????????????????
    @BindView(R.id.iv_staff_photo_now)
    ImageView mIvStaffNow;//???????????????????????????
    //    @BindView(R.id.btn_take_photo)
//    ImageButton btnTakePhoto;
//    @BindView(R.id.btn_take_photo_re)
//    TextView tvTakePhoto;
    @BindView(R.id.tv_baojianyuan)
    TextView tvBaoJianYuan;
    @BindView(R.id.verify_tv_bj_start)
    TextView tvBjStart;
    @BindView(R.id.verify_tv_bj_end)
    TextView tvBjEnd;
    @BindView(R.id.verify_tv_wx_start)
    TextView tvWxStart;
    @BindView(R.id.verify_tv_wx_end)
    TextView tvWxEnd;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.gh_user)
    TextView GhUser;
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

    private String mImageHead;//????????????
    private String fileName;
    private String filePath;
    private int mSpotResult;
    private TransportDataBase mBean;
    private DeclareWaybillBean mDecBean;

    private StorageCommitEntity mStorageCommitEntity = new StorageCommitEntity();

    private TestInfoListBean mAcTestInfoListBean = new TestInfoListBean();

    public static void startActivity(Activity context,
                                     TransportDataBase mBean,
                                     DeclareWaybillBean mDecBean

    ) {
        Intent intent = new Intent(context, VerifyStaffActivity.class);
        intent.putExtra("mBean", mBean);
        intent.putExtra("mDecBean", mDecBean);
        context.startActivityForResult(intent, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_verify_staff;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "?????????????????????");
        String text = StringUtil.format(this, R.string.format_certificate_date, "2018-12-12", "2019-12-12");
        mTvCertificateInDate.setText(text);
        mBean = (TransportDataBase) getIntent().getSerializableExtra("mBean");
        mDecBean = (DeclareWaybillBean) getIntent().getSerializableExtra("mDecBean");

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
        initData();
        setWaybillInfo(mBean);
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
        } else {
            tvWaybillCode.setText("?????????:   ");
            tvGoodsName.setText("??????:  ");
            tvSpecialCode.setText("????????????:  ");
            tvNumber.setText("??????:  ");
            tvWeight.setText("??????:  ");
        }

    }

    private void initData() {
        mPresenter = new TestInfoPresenter(this);
        ((TestInfoPresenter) mPresenter).testInfo(mBean.getWaybillId(), mDecBean.getFreightId(), mBean.getTaskTypeCode());
    }


    @OnClick({R.id.agree_tv, R.id.refuse_tv, R.id.iv_staff_photo_now, R.id.gh_user})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agree_tv:
                if (!"".equals(filePath)) {
                    VerifyFileActivity.startActivity(this,
                            mBean,
                            mDecBean,
                            filePath,
                            mSpotResult,
                            0,
                            mAcTestInfoListBean
                    );

                } else
                    ToastUtil.showToast(this, "??????????????????");
                break;
            case R.id.refuse_tv:
                if (!"".equals(filePath)) {

//                    InputForStrDialog inputForStrDialog = new InputForStrDialog(this);
//                    inputForStrDialog.isCanceledOnTouchOutside(false);
//                    inputForStrDialog.isCanceled(true)
//                            .setTitle("??????")
//                            .setHint("?????????????????????????????????")
//                            .setPositiveButton("??????")
//                            .setNegativeButton("??????");
//                    inputForStrDialog.setOnClickListener(new InputForStrDialog.OnClickListener() {
//                        @Override
//                        public void onClick(Dialog dialog, boolean confirm, String result) {
//                            if (!confirm)
//                                return;
//                            if (!StringUtil.isEmpty(result)) {
//                                mStorageCommitEntity.setRemark(result);
//                            } else {
//                                mStorageCommitEntity.setRemark(null);
//                            }
//
//                        }
//                    });
//                    inputForStrDialog.show();
                    mPresenter = new SubmissionPresenter(VerifyStaffActivity.this);
                    mStorageCommitEntity.setWaybillId(mBean.getId());
                    mStorageCommitEntity.setWaybillCode(mBean.getWaybillCode());
                    mStorageCommitEntity.setAddOrderId(mBean.getId());//??????????????????
                    mStorageCommitEntity.setInsUserId(UserInfoSingle.getInstance().getUserId());
                    mStorageCommitEntity.setInsFile(filePath);
                    mStorageCommitEntity.setInsCheck(1);
                    mStorageCommitEntity.setFileCheck(1);
                    mStorageCommitEntity.setTaskTypeCode(mBean.getTaskTypeCode());
                    mStorageCommitEntity.setType(1);
                    mStorageCommitEntity.setTaskId(mBean.getTaskId());
                    mStorageCommitEntity.setUserId(mDecBean.getFlightNo());
                    mStorageCommitEntity.setRemark(mBean.getRemark());
                    ((SubmissionPresenter) mPresenter).submission(mStorageCommitEntity);
                } else
                    ToastUtil.showToast(this, "??????????????????");
                break;
            case R.id.iv_staff_photo_now:
                ImageSelectorActivity.start(VerifyStaffActivity.this,
                        1,
                        ImageSelectorActivity.MODE_SINGLE, true, true, true);
                break;

            case R.id.gh_user:
                if (null == mAcTestInfoListBean.getFreightInfo()) {
                    ToastUtil.showToast("??????????????????????????????????????????");
                } else
                    ChoiceUserActivity.startActivity(this, mAcTestInfoListBean);
                break;

//            case R.id.btn_take_photo_re:
            //????????????
//            case R.id.btn_take_photo:
//                ImageSelectorActivity.start(VerifyStaffActivity.this,
//                        1,
//                        ImageSelectorActivity.MODE_SINGLE, true, true, true);

//                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 404) {
            finish();
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 66:
                    ArrayList <String> images = (ArrayList <String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
                    if (images != null && images.size() > 0) {
                        mImageHead = images.get(0);
                        pressImage(new File(mImageHead));
                    }
                    break;
            }
        }
        if (resultCode == 33) {
            TestInfoListBean.FreightInfoBean mFreightBean = (TestInfoListBean.FreightInfoBean) data.getSerializableExtra("Choice");
            if (mFreightBean != null) {
                //???????????????????????????
                mStorageCommitEntity.setInsUserHead(mFreightBean.getInspectionHead() + "");
                mStorageCommitEntity.setInsUserName(mFreightBean.getInspectionName());
                mStorageCommitEntity.setInsUserId(mFreightBean.getId());
                mStorageCommitEntity.setInsDangerStart(mFreightBean.getDangerBookStart());
                mStorageCommitEntity.setInsDangerEnd(mFreightBean.getDangerBookEnd());
                if (null != mAcTestInfoListBean.getInsInfo()) {
                    mStorageCommitEntity.setInsStartTime(mAcTestInfoListBean.getInsInfo().getInsStartTime());
                    mStorageCommitEntity.setInsEndTime(mAcTestInfoListBean.getInsInfo().getInsEndTime());
                }
                //???????????????
                tvBaoJianYuan.setText(mFreightBean.getInspectionName());
                //??????????????????
//                tvBjStart.setText(mFreightBean.getInspectionBookStart() == 0 ? "- -???" : TimeUtils.date3time(mFreightBean.getInspectionBookStart()) + "-");
                tvBjStart.setText(TimeUtils.date3time(mFreightBean.getInspectionBookStart()) + "???");
                //??????????????????
                tvBjEnd.setText(TimeUtils.date3time(mFreightBean.getInspectionBookEnd()));
                //??????????????????
                tvWxStart.setText(TimeUtils.date3time(mFreightBean.getDangerBookStart()) + "???");
                //??????????????????
                tvWxEnd.setText(TimeUtils.date3time(mFreightBean.getDangerBookEnd()));
                tvBaoJianYuan.setBackgroundResource(R.mipmap.bg_inspect_all);
                //??????
                GlideUtil.load(HttpConstant.IMAGEURL + mFreightBean.getInspectionHead()).into(mIvStaffOld1);
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param file ??????
     */
    private void pressImage(File file) {
        mPresenter = new UploadsPresenter(this);
        if (file == null)
            return;
        int size = 150;
        Luban.get(this).load(file)
                .setMaxSize(size)
                .setMaxHeight(1920)
                .setMaxWidth(1080)
                .putGear(Luban.CUSTOM_GEAR).launch(new OnCompressListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(File file) {
                Glide.with(VerifyStaffActivity.this).load(file).into(mIvStaffNow);
                List <File> files = new ArrayList <>();
                files.add(file);
                List <MultipartBody.Part> upFiles = Tools.filesToMultipartBodyParts(files);
                ((UploadsPresenter) mPresenter).uploads(upFiles);
                llBottom.setVisibility(View.VISIBLE);
//                btnTakePhoto.setVisibility(View.GONE);
//                tvTakePhoto.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast(VerifyStaffActivity.this, e.getMessage());
            }
        });
    }

    @Override
    public void uploadsResult(Object result) {
        if (result != null) {
            llBottom.setVisibility(View.VISIBLE);
//            btnTakePhoto.setVisibility(View.GONE);
//            tvTakePhoto.setVisibility(View.VISIBLE);
            //key????????????value?????????
            Map <String, String> map = new HashMap <>();
            map = (Map <String, String>) result;
            Set <Map.Entry <String, String>> entries = map.entrySet();
            for (Map.Entry <String, String> entry : entries) {
                fileName = entry.getValue();
                filePath = entry.getKey();
            }
        } else
            ToastUtil.showToast(this, "??????????????????");
    }

    @Override
    public void testInfoResult(TestInfoListBean testInfoListBeanList) {
        if (testInfoListBeanList != null) {
            if (testInfoListBeanList.getFreightInfo().size() > 0) {
                mAcTestInfoListBean = testInfoListBeanList;
                //??????????????????
                if (mAcTestInfoListBean.getInsInfo() == null) {
                    mSpotResult = 0;
                } else
                    mSpotResult = mAcTestInfoListBean.getInsInfo().getSpotResult();
                //???????????????
//                tvBaoJianYuan.setText(testInfoListBeanList.getFreightInfo().get(0).getInspectionName());
                //??????????????????
//                tvBjStart.setText(testInfoListBeanList.getFreightInfo().get(0).getInspectionBookStart() == 0 ? "- -???" : TimeUtils.date3time(testInfoListBeanList.getFreightInfo().get(0).getInspectionBookStart()) + "???");
                tvBjStart.setText("- -???" + "- -");
                //??????????????????
//                tvBjEnd.setText(testInfoListBeanList.getFreightInfo().get(0).getInspectionBookEnd() == 0 ? "- -" : TimeUtils.date3time(testInfoListBeanList.getFreightInfo().get(0).getInspectionBookEnd()));
                tvBjEnd.setText("");
                //??????????????????
//                tvWxStart.setText(testInfoListBeanList.getFreightInfo().get(0).getDangerBookStart() == 0 ? "- -???" : TimeUtils.date3time(testInfoListBeanList.getFreightInfo().get(0).getDangerBookStart()) + "???");
                tvWxStart.setText("- -???" + "- -");
                //??????????????????
//                tvWxEnd.setText(testInfoListBeanList.getFreightInfo().get(0).getDangerBookEnd() == 0 ? "- -" : TimeUtils.date3time(testInfoListBeanList.getFreightInfo().get(0).getDangerBookEnd()));
                tvWxEnd.setText("");
                //?????????????????????
//                GlideUtil.load(HttpConstant.IMAGEURL + testInfoListBeanList.getFreightInfo().get(0).getInspectionHead()).into(mIvStaffOld1);
//                GlideUtil.load(HttpConstant.IMAGEURL + testInfoListBeanList.getFreightInfo().get(0).getInspectionHead()).placeholder().into(mIvStaffOld1);
//            GlideUtil.load("https://www.baidu.com/img/bd_logo1.png?where=super").into(mIvStaffOld1);
            } else {
//                ToastUtil.showToast("????????????");
            }
        } else {
//            ToastUtil.showToast("????????????");
        }

    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(this, error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("???????????????");
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
