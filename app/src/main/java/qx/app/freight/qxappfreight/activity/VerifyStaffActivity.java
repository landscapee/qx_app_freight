package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import qx.app.freight.qxappfreight.adapter.GeneralSpinnerAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.request.GeneralSpinnerBean;
import qx.app.freight.qxappfreight.bean.response.TestInfoListBean;
import qx.app.freight.qxappfreight.constant.HttpConstant;
import qx.app.freight.qxappfreight.contract.TestInfoContract;
import qx.app.freight.qxappfreight.contract.UploadsContract;
import qx.app.freight.qxappfreight.presenter.TestInfoPresenter;
import qx.app.freight.qxappfreight.presenter.UploadsPresenter;
import qx.app.freight.qxappfreight.utils.GlideUtil;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 核查报检员身份页面
 */
public class VerifyStaffActivity extends BaseActivity implements UploadsContract.uploadsView, TestInfoContract.testInfoView {
    @BindView(R.id.sp_select_staff)
    Spinner mSpSelectStaff; //选择报检员
    @BindView(R.id.tv_certificate_in_date)
    TextView mTvCertificateInDate;//证件有效期
    @BindView(R.id.iv_staff_photo_old1)
    ImageView mIvStaffOld1;//报检员备案照片
    @BindView(R.id.iv_staff_photo_now)
    ImageView mIvStaffNow;//报检员当前照片照片
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

    private String mStaffId;
    private String mStaffName;
    private String mImageHead;//头像路径
    private String mAdditionTypeArr;
    private String mTaskId;
    private String fileName;
    private String filePath;
    private String mWaybillId;
    private String mSpotFlag;
    private int mSpotResult;
    private String insCheck; //报检是否合格1合格 0不合格
    private String mFlightNumber;//航班号
    private String mShipperCompanyId;
    private String mId;
    private String mTaskTypeCode;
    private String mWaybillCode;
    private String Tid;

    private TestInfoListBean mAcTestInfoListBean = new TestInfoListBean();

    public static void startActivity(Activity context,
                                     String taskTypeCode,
                                     String id,
                                     String additionTypeArr,
                                     String taskId,
                                     String waybillId,
                                     String spotFlag,
                                     String flightNumber,
                                     String shipperCompanyId,
                                     String waybillCode,
                                     String tid

    ) {
        Intent intent = new Intent(context, VerifyStaffActivity.class);
        intent.putExtra("taskTypeCode", taskTypeCode);
        intent.putExtra("id", id);
        intent.putExtra("additionTypeArr", additionTypeArr);
        intent.putExtra("taskId", taskId);
        intent.putExtra("waybillId", waybillId);
        intent.putExtra("spotFlag", spotFlag);
        intent.putExtra("flightNumber", flightNumber);
        intent.putExtra("shipperCompanyId", shipperCompanyId);
        intent.putExtra("waybillCode", waybillCode);
        intent.putExtra("tid", tid);
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
        toolbar.setMainTitle(Color.WHITE, "核查报检员身份");
        String text = StringUtil.format(this, R.string.format_certificate_date, "2018-12-12", "2019-12-12");
        mTvCertificateInDate.setText(text);
        mAdditionTypeArr = getIntent().getStringExtra("additionTypeArr");
        mTaskTypeCode = getIntent().getStringExtra("taskTypeCode");
        mId = getIntent().getStringExtra("id");
        mWaybillId = getIntent().getStringExtra("waybillId");
        mWaybillCode = getIntent().getStringExtra("waybillCode");
        mTaskId = getIntent().getStringExtra("taskId");
        mSpotFlag = getIntent().getStringExtra("spotFlag");
        mFlightNumber = getIntent().getStringExtra("flightNumber");
        mShipperCompanyId = getIntent().getStringExtra("shipperCompanyId");
        Tid = getIntent().getStringExtra("tid");
        initData();
    }

    private void initData() {
        mPresenter = new TestInfoPresenter(this);
        ((TestInfoPresenter) mPresenter).testInfo(mWaybillId, mShipperCompanyId, mTaskTypeCode);
        List<GeneralSpinnerBean.StaffCheckInfo> staffCheckInfos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GeneralSpinnerBean.StaffCheckInfo staffCheckInfo = new GeneralSpinnerBean.StaffCheckInfo();
            staffCheckInfo.setId(i + "");
            staffCheckInfo.setValue("李" + i + "蛋");
            staffCheckInfos.add(staffCheckInfo);
        }
        initSpinnerData(staffCheckInfos);
    }

    private void initSpinnerData(List<GeneralSpinnerBean.StaffCheckInfo> staffCheckInfos) {
        GeneralSpinnerAdapter<GeneralSpinnerBean.StaffCheckInfo> typeAdapter = new GeneralSpinnerAdapter(this, staffCheckInfos);
        mSpSelectStaff.setAdapter(typeAdapter);
        mSpSelectStaff.setDropDownVerticalOffset(10);
        mStaffId = staffCheckInfos.get(0).getId();
        mStaffName = staffCheckInfos.get(0).getValue();
        mSpSelectStaff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mStaffId = staffCheckInfos.get(position).getId();
                mStaffName = staffCheckInfos.get(position).getValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @OnClick({R.id.agree_tv, R.id.refuse_tv, R.id.iv_staff_photo_now, R.id.gh_user})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agree_tv:
                if (!"".equals(filePath)) {
                    ToastUtil.showToast(this, "合格");
                    VerifyFileActivity.startActivity(this,
                            mTaskTypeCode,
                            mWaybillId,
                            mId,
                            mAdditionTypeArr,
                            mTaskId,
                            filePath,
                            mSpotFlag,
                            mSpotResult,
                            0,
                            mFlightNumber,
                            mShipperCompanyId,
                            mWaybillCode,
                            Tid
                    );
                } else
                    ToastUtil.showToast(this, "请先上传照片");
                break;
            case R.id.refuse_tv:
//                ToastUtil.showToast(this, "不合格");
                if (!"".equals(filePath)) {
                    ToastUtil.showToast(this, "不合格");
                    VerifyFileActivity.startActivity(this, mTaskTypeCode, mWaybillId, mId, mAdditionTypeArr, mTaskId, filePath, mSpotFlag, mSpotResult, 1, mFlightNumber, mShipperCompanyId, mWaybillCode, Tid);
                } else
                    ToastUtil.showToast(this, "请先上传照片");
                break;
            case R.id.iv_staff_photo_now:
                ImageSelectorActivity.start(VerifyStaffActivity.this,
                        1,
                        ImageSelectorActivity.MODE_SINGLE, true, true, true);
                break;

            case R.id.gh_user:
                ChoiceUserActivity.startActivity(this, mAcTestInfoListBean);
                break;

//            case R.id.btn_take_photo_re:
            //图片点击
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
                    ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
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
                //报检员姓名
                tvBaoJianYuan.setText(mFreightBean.getInspectionName());
                //报检开始时间
                tvBjStart.setText(mFreightBean.getInspectionBookStart() == 0 ? "- -至" : TimeUtils.date3time(mFreightBean.getInspectionBookStart()) + "至");
                //报检结束时间
                tvBjEnd.setText(mFreightBean.getInspectionBookEnd() == 0 ? "- -" : TimeUtils.date3time(mFreightBean.getInspectionBookEnd()));
                //危险开始时间
                tvWxStart.setText(mFreightBean.getDangerBookStart() == 0 ? "- -至" : TimeUtils.date3time(mFreightBean.getDangerBookStart()) + "至");
                //危险结束时间
                tvWxEnd.setText(mFreightBean.getDangerBookEnd() == 0 ? "- -" : TimeUtils.date3time(mFreightBean.getDangerBookEnd()));
            }
        }
    }

    /**
     * 压缩图片上传
     *
     * @param file 文件
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
                List<File> files = new ArrayList<>();
                files.add(file);
                List<MultipartBody.Part> upFiles = Tools.filesToMultipartBodyParts(files);
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
            //key是地址，value是名字
            Map<String, String> map = new HashMap<>();
            map = (Map<String, String>) result;
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                fileName = entry.getValue();
                filePath = entry.getKey();
            }
        } else
            ToastUtil.showToast(this, "图片返回为空");
    }

    @Override
    public void testInfoResult(TestInfoListBean testInfoListBeanList) {
        if (testInfoListBeanList != null) {
            if (testInfoListBeanList.getFreightInfo().size() > 0) {
                mAcTestInfoListBean = testInfoListBeanList;
                //获取抽验结果
                mSpotResult = mAcTestInfoListBean.getInsInfo().getSpotResult();
                //报检员姓名
                tvBaoJianYuan.setText(testInfoListBeanList.getFreightInfo().get(0).getInspectionName());
                //报检开始时间
                tvBjStart.setText(testInfoListBeanList.getFreightInfo().get(0).getInspectionBookStart() == 0 ? "- -至" : TimeUtils.date3time(testInfoListBeanList.getFreightInfo().get(0).getInspectionBookStart()) + "至");
                //报检结束时间
                tvBjEnd.setText(testInfoListBeanList.getFreightInfo().get(0).getInspectionBookEnd() == 0 ? "- -" : TimeUtils.date3time(testInfoListBeanList.getFreightInfo().get(0).getInspectionBookEnd()));
                //危险开始时间
                tvWxStart.setText(testInfoListBeanList.getFreightInfo().get(0).getDangerBookStart() == 0 ? "- -至" : TimeUtils.date3time(testInfoListBeanList.getFreightInfo().get(0).getDangerBookStart()) + "至");
                //危险结束时间
                tvWxEnd.setText(testInfoListBeanList.getFreightInfo().get(0).getDangerBookEnd() == 0 ? "- -" : TimeUtils.date3time(testInfoListBeanList.getFreightInfo().get(0).getDangerBookEnd()));
                //报检员备案照片
                GlideUtil.load(HttpConstant.IMAGEURL + testInfoListBeanList.getFreightInfo().get(0).getInspectionHead()).into(mIvStaffOld1);
//            GlideUtil.load("https://www.baidu.com/img/bd_logo1.png?where=super").into(mIvStaffOld1);
            }else
                ToastUtil.showToast("数据为空");
        } else
            ToastUtil.showToast("数据为空");
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(this, error);
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("图片上传中");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }


}
