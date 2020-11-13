package qx.app.freight.qxappfreight.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnMultiCompressListener;
import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ImageRvAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.ErrorFilingEntity;
import qx.app.freight.qxappfreight.contract.InPortTallyErrorFilingContract;
import qx.app.freight.qxappfreight.contract.UploadsContract;
import qx.app.freight.qxappfreight.presenter.InPortTallyErrorFilingPresenter;
import qx.app.freight.qxappfreight.presenter.UploadsPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 异常立案页面
 */
public class ExceptionFilingActivity extends BaseActivity implements UploadsContract.uploadsView, InPortTallyErrorFilingContract.InPortErrorFilingView {
    @BindView(R.id.tv_flight_number)
    TextView mTvFlightNumber;
    @BindView(R.id.cb_number_error)
    CheckBox mCbNumberError;//件数异常checkBox
    @BindView(R.id.tv_number_error)
    TextView mTvNumberError;//件数异常
    @BindView(R.id.et_error_number)
    EditText mEtNumberError;//件数异常输入框
    @BindView(R.id.cb_no_goods)
    CheckBox mCbNoGoods;//有单无货checkBox
    @BindView(R.id.tv_no_goods)
    TextView mTvNoGoods;//有单无货
    @BindView(R.id.cb_no_bills)
    CheckBox mCbNoBills;//有货无单checkBox
    @BindView(R.id.tv_no_bills)
    TextView mTvNoBills;//有货无单
    @BindView(R.id.cb_broken)
    CheckBox mCbBroken;//破损checkBox
    @BindView(R.id.tv_broken)
    TextView mTvBroken;//破损
    @BindView(R.id.cb_burke)
    CheckBox mCbBurke;//扣货checkBox
    @BindView(R.id.tv_burke)
    TextView mTvBurke;//扣货
    @BindView(R.id.cb_wrong_transport)
    CheckBox mCbWrongTransport;//错运checkBox
    @BindView(R.id.tv_wrong_transport)
    TextView mTvWrongTransport;//错运
    @BindView(R.id.cb_decompose)
    CheckBox mCbDecompose;//腐烂checkBox
    @BindView(R.id.tv_decompose)
    TextView mTvDecompose;//腐烂
    @BindView(R.id.cb_leakage)
    CheckBox mCbLeakage;//泄露checkBox
    @BindView(R.id.tv_leakage)
    TextView mTvLeakage;//泄露
    @BindView(R.id.rv_photo)
    RecyclerView mRvPhoto;
    @BindView(R.id.iv_chose_photo)
    ImageView mIvChosePhoto;
    @BindView(R.id.et_detail_info)
    EditText mEtDetailPhoto;
    @BindView(R.id.et_remark)
    EditText mEtRemark;
    @BindView(R.id.tv_commit_exception_filing)
    Button mTvCommitErrorFiling;//提交
    private static final int REQUEST_IMAGE = 5;
    private List<String> mPhotoPath = new ArrayList<>();
    private static final String[] mAuthPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int mAuthBaseRequestCode = 1;
    private ImageRvAdapter mAdapter;
    private List<Integer> mErrorCodeList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_exception_filing;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        initNavi();//权限方法
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "异常立案");
        mTvFlightNumber.setText(getIntent().getStringExtra("flight_number"));
        CheckBox[] checkBoxes = {mCbNumberError, mCbNoGoods, mCbBroken, mCbDecompose, mCbNoBills, mCbLeakage, mCbWrongTransport, mCbBurke};
        TextView[] textViews = {mTvNumberError, mTvNoGoods, mTvBroken, mTvDecompose, mTvNoBills, mTvLeakage, mTvWrongTransport, mTvBurke};
        for (int i = 0; i < checkBoxes.length; i++) {
            CheckBox checkBox = checkBoxes[i];
            TextView textView = textViews[i];
            int finalI = i;
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    mErrorCodeList.add(finalI + 1);
                    checkBox.setBackgroundColor(Color.RED);
                    textView.setTextColor(Color.RED);
                } else {
                    mErrorCodeList = mErrorCodeList.stream().filter(o -> o != finalI + 1).collect(Collectors.toList());
                    checkBox.setBackgroundColor(Color.WHITE);
                    textView.setTextColor(Color.parseColor("#738598"));
                }
            });
        }
        mIvChosePhoto.setOnClickListener(v -> {
            List<String> originList = new ArrayList<>();
            if (mAdapter == null) {
                originList.addAll(mPhotoPath);
            } else {
                originList.addAll(mPhotoPath);
            }
            MultiImageSelector.create()
                    .showCamera(true) // 是否显示相机. 默认为显示
                    .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                    .multi() // 多选模式, 默认模式;
                    .origin((ArrayList<String>) originList) // 默认已选择图片. 只有在选择模式为多选时有效
                    .start(ExceptionFilingActivity.this, REQUEST_IMAGE);
        });
        mTvCommitErrorFiling.setOnClickListener(v -> {
            if (mPhotoPath.size() == 0) {
                commitErrorFiling();
            } else {
                pressImage(mPhotoPath);
            }
        });
        mAdapter = new ImageRvAdapter(mPhotoPath);
        mRvPhoto.setLayoutManager(new GridLayoutManager(this, 4));
        mRvPhoto.setAdapter(mAdapter);
    }

    private void commitErrorFiling() {
        mPresenter = new InPortTallyErrorFilingPresenter(ExceptionFilingActivity.this);
        ErrorFilingEntity entity = new ErrorFilingEntity();
        entity.setCreateUser(UserInfoSingle.getInstance().getUserId());
        entity.setCreateUserName(UserInfoSingle.getInstance().getLoginName());
        entity.setFlightNum(mTvFlightNumber.getText().toString());
        entity.setRemark(mEtRemark.getText().toString());
        entity.setUbnormalDescription(null);
        entity.setUbnormalPic(null);
        entity.setUbnormalNum(Integer.valueOf(mEtNumberError.getText().toString()));
        entity.setWaybillId(null);
        entity.setUbnormalType(mErrorCodeList);
        ((InPortTallyErrorFilingPresenter) mPresenter).errorFiling(entity);
    }

    /**
     * 压缩图片上传
     *
     * @param paths 文件路径集合
     */
    private void pressImage(List<String> paths) {
        if (paths == null || paths.size() == 0) {
            return;
        }
        List<File> files = new ArrayList<>();
        for (String path : paths) {
            files.add(new File(path));
        }
        Luban.get(this).load(files).setMaxSize(150)
                .setMaxHeight(1920)
                .setMaxWidth(1080)
                .putGear(Luban.CUSTOM_GEAR).launch(new OnMultiCompressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List<File> fileList) {
                List<MultipartBody.Part> upFiles = Tools.filesToMultipartBodyParts(fileList);
                mPresenter = new UploadsPresenter(ExceptionFilingActivity.this);
                ((UploadsPresenter) mPresenter).uploads(upFiles);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void initNavi() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        // 申请权限
        if (!hasBasePhoneAuth()) {
            this.requestPermissions(mAuthPermissions, mAuthBaseRequestCode);
        }
    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = getPackageManager();
        for (String auth : mAuthPermissions) {
            if (pm.checkPermission(auth, getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void uploadsResult(Object result) {
        commitErrorFiling();
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

    @Override
    public void errorFilingResult(String result) {
        ToastUtil.showToast("异常立案成功");
        finish();
    }
}
