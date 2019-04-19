package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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
import qx.app.freight.qxappfreight.app.MyApplication;
import qx.app.freight.qxappfreight.bean.request.GeneralSpinnerBean;
import qx.app.freight.qxappfreight.bean.response.TestInfoListBean;
import qx.app.freight.qxappfreight.bean.response.TransportListBean;
import qx.app.freight.qxappfreight.contract.TestInfoContract;
import qx.app.freight.qxappfreight.contract.UploadsContract;
import qx.app.freight.qxappfreight.presenter.TestInfoPresenter;
import qx.app.freight.qxappfreight.presenter.UploadsPresenter;
import qx.app.freight.qxappfreight.utils.ImageUtils;
import qx.app.freight.qxappfreight.utils.StringUtil;
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
    @BindView(R.id.iv_staff_photo_old)
    ImageView mIvStaffOld;//报检员备案照片
    @BindView(R.id.iv_staff_photo_now)
    ImageView mIvStaffNow;//报检员当前照片照片
    //    @BindView(R.id.tv_aptitude)
//    TextView mTvAptitude;//货代资质
    @BindView(R.id.btn_take_photo)
    ImageButton btnTakePhoto;
    @BindView(R.id.btn_take_photo_re)
    TextView tvTakePhoto;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    private String mStaffId;
    private String mStaffName;
    private String mImageHead;//头像路径
    private TransportListBean.DeclareWaybillAdditionBean mDeclareData;
    private String mTaskId;
    private String fileName;
    private String filePath;
    private String mSpotFlag;
    private String insCheck; //报检是否合格1合格 0不合格
    private String mFlightNumber;//航班号
    private String mShipperCompanyId;

    public static void startActivity(Activity context, TransportListBean.DeclareWaybillAdditionBean declareWaybillAdditionBean, String taskId,String spotFlag,String flightNumber,String shipperCompanyId) {
        Intent intent = new Intent(context, VerifyStaffActivity.class);
        intent.putExtra("DeclareWaybillAdditionBean", declareWaybillAdditionBean);
        intent.putExtra("taskId", taskId);
        intent.putExtra("spotFlag", spotFlag);
        intent.putExtra("flightNumber", flightNumber);
        intent.putExtra("shipperCompanyId", shipperCompanyId);
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
        llBottom.setVisibility(View.GONE);
        tvTakePhoto.setVisibility(View.GONE);
        String text = StringUtil.format(this, R.string.format_certificate_date, "2018-12-12", "2019-12-12");
        mTvCertificateInDate.setText(text);
        ImageUtils.setImageHeightFoyWidth(mIvStaffNow, ImageUtils.getScreenWidth(this), 3, 4);
        mDeclareData = (TransportListBean.DeclareWaybillAdditionBean) getIntent().getSerializableExtra("DeclareWaybillAdditionBean");
        mTaskId = getIntent().getStringExtra("taskId");
        mSpotFlag = getIntent().getStringExtra("spotFlag");
        mFlightNumber = getIntent().getStringExtra("flightNumber");
        mShipperCompanyId = getIntent().getStringExtra("shipperCompanyId");
        initData();
    }

    private void initData() {
        mPresenter = new TestInfoPresenter(this);
        ((TestInfoPresenter) mPresenter).testInfo(mDeclareData.getWaybillId(),mShipperCompanyId);
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

    @OnClick({R.id.agree_tv, R.id.refuse_tv, R.id.iv_staff_photo_now, R.id.btn_take_photo, R.id.btn_take_photo_re})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agree_tv:
                if (!"".equals(filePath)) {
                    ToastUtil.showToast(this, "合格");
                    VerifyFileActivity.startActivity(this, mDeclareData, mTaskId, filePath,mSpotFlag,0,mFlightNumber,mShipperCompanyId);
                } else
                    ToastUtil.showToast(this, "请先上传照片");
                break;
            case R.id.refuse_tv:
//                ToastUtil.showToast(this, "不合格");
                if (!"".equals(filePath)) {
                    ToastUtil.showToast(this, "不合格");
                    VerifyFileActivity.startActivity(this, mDeclareData, mTaskId, filePath,mSpotFlag,1,mFlightNumber,mShipperCompanyId);
                } else
                    ToastUtil.showToast(this, "请先上传照片");
                break;
            case R.id.iv_staff_photo_now:
//                ImageSelectorActivity.start(VerifyStaffActivity.this,
//                        1,
//                        ImageSelectorActivity.MODE_SINGLE, true, true, true);
                break;

            case R.id.btn_take_photo_re:
            case R.id.btn_take_photo:
                ImageSelectorActivity.start(VerifyStaffActivity.this,
                        1,
                        ImageSelectorActivity.MODE_SINGLE, true, true, true);

                break;


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
                ((UploadsPresenter)mPresenter).uploads(upFiles);
                llBottom.setVisibility(View.VISIBLE);
                btnTakePhoto.setVisibility(View.GONE);
                tvTakePhoto.setVisibility(View.VISIBLE);

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
            btnTakePhoto.setVisibility(View.GONE);
            tvTakePhoto.setVisibility(View.VISIBLE);
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
