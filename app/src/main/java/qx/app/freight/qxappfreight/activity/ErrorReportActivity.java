package qx.app.freight.qxappfreight.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnMultiCompressListener;
import okhttp3.MultipartBody;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ImageRvAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.ExceptionReportEntity;
import qx.app.freight.qxappfreight.bean.request.TransportEndEntity;
import qx.app.freight.qxappfreight.bean.response.OutFieldTaskBean;
import qx.app.freight.qxappfreight.contract.ExceptionReportContract;
import qx.app.freight.qxappfreight.contract.UploadsContract;
import qx.app.freight.qxappfreight.presenter.ExceptionReportPresenter;
import qx.app.freight.qxappfreight.presenter.UploadsPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
import qx.app.freight.qxappfreight.utils.PushDataUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 异常上报页面
 */
public class ErrorReportActivity extends BaseActivity implements UploadsContract.uploadsView, ExceptionReportContract.exceptionReportView {
    @BindView(R.id.tv_flight_info)
    TextView mTvFlightInfo;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.rv_photo)
    RecyclerView mRvPhoto;
    @BindView(R.id.et_detail_info)
    EditText mEtDetailInfo;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    @BindView(R.id.sp_filight_num)
    Spinner mSpinner;

    private static final int REQUEST_IMAGE = 5;
    private List<String> mPhotoPath = new ArrayList<>();
    private static final String[] mAuthPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int mAuthBaseRequestCode = 1;
    private ImageRvAdapter mAdapter;
    private String mFlightNumber;
    private ArrayList<OutFieldTaskBean> mFlightberList = null; //传过来的航班列表
    private ArrayList<String> mFlightNumberList = null; //传过来的航班号列表
    private String mCurrentTaskId;//当前任务ID
    private String mFlightId;//被选中的 板车号


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        PushDataUtil.handlePushInfo(result, mCurrentTaskId, this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result != null && result.equals(getIntent().getStringExtra("flight_id"))) {
            finish();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_error_report;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initNavi();//权限方法
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "异常上报");
        mPhotoPath.add("111");
        mAdapter = new ImageRvAdapter(mPhotoPath);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mPhotoPath.get(position).equals("111")) {
                    choosePictrue();
                } else {
                    previewPictrue(position);
                }

            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_delete) {
                    mPhotoPath.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        mRvPhoto.setLayoutManager(new GridLayoutManager(this, 4));
        mRvPhoto.setAdapter(mAdapter);
        mFlightberList = (ArrayList<OutFieldTaskBean>) getIntent().getSerializableExtra("plane_info_list");
        mFlightId = getIntent().getStringExtra("flight_id");
        if (mFlightberList != null) {
            mFlightNumberList = new ArrayList<>();
            mSpinner.setVisibility(View.VISIBLE);
            for (OutFieldTaskBean mOutFieldTaskBean : mFlightberList) {
                mFlightNumberList.add(mOutFieldTaskBean.getFlightNo());
            }
            mFlightNumber = mFlightNumberList.get(0);
            mFlightId = mFlightberList.get(0).getFlightId();
            mCurrentTaskId = mFlightberList.get(0).getTaskId();
            mTvFlightInfo.setText(mFlightNumber);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.item_spinner_general, mFlightNumberList);
            mSpinner.setAdapter(spinnerAdapter);
            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mFlightNumber = mFlightNumberList.get(position);
                    mFlightId = mFlightberList.get(position).getFlightId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            mTvFlightInfo.setVisibility(View.VISIBLE);
            mFlightNumber = getIntent().getStringExtra("flight_number");
            mCurrentTaskId = getIntent().getStringExtra("task_id");
            mTvFlightInfo.setText(mFlightNumber);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        mTvDate.setText(sdf.format(new Date()));
        mBtnCommit.setOnClickListener(v -> {
            if (getNoAddPictureList().size() == 0 && TextUtils.isEmpty(mEtDetailInfo.getText().toString())) {
                ToastUtil.showToast("请添加文字或图片说明（至少一种）");
            } else {
                if (getNoAddPictureList().size() == 0) {
                    mPresenter = new ExceptionReportPresenter(ErrorReportActivity.this);
                    ExceptionReportEntity model = new ExceptionReportEntity();
                    model.setFlightNum(mFlightNumber);
                    model.setFlightId(Long.valueOf(mFlightId));
                    model.setExceptionDesc(mEtDetailInfo.getText().toString());
                    model.setReOperator(UserInfoSingle.getInstance().getUserId());
                    model.setReType(getIntent().getIntExtra("error_type", 1));
                    String deptCode = UserInfoSingle.getInstance().getDeptCode();
//                    String deptId = "";
//                    if (deptCode != null) {
//                        deptId = (deptCode.contains("-") ? deptCode.substring(0, deptCode.indexOf("-")) : deptCode);
//                    }
                    model.setDeptId(deptCode);
                    model.setArea(getIntent().getStringExtra("area_id"));
                    model.setExceptionCode(getIntent().getStringExtra("step_code"));
                    model.setFiles(null);
                    TransportEndEntity endEntity = new TransportEndEntity();
                    endEntity.setTaskId(mCurrentTaskId);
                    model.setTransportAppDto(endEntity);
//                    ((ExceptionReportPresenter) mPresenter).exceptionReport(model);
                } else {
                    pressImage(getNoAddPictureList());
                }
            }
        });
    }

    /**
     * 选择相册照片
     */
    private void choosePictrue() {
        List<String> originList = new ArrayList<>();
        if (mAdapter == null) {
            originList.addAll(mPhotoPath);
        } else {
            originList.addAll(mPhotoPath);
            originList.remove(originList.size() - 1);
        }
        MultiImageSelector.create(ErrorReportActivity.this)
                .showCamera(true) // 是否显示相机. 默认为显示
                .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .multi() // 多选模式, 默认模式;
                .origin((ArrayList<String>) originList) // 默认已选择图片. 只有在选择模式为多选时有效
                .start(ErrorReportActivity.this, REQUEST_IMAGE);
    }

    /**
     * 预览照片
     * 忽略最后一个
     */
    private void previewPictrue(int position) {
        ImgPreviewAct.startPreview(ErrorReportActivity.this, getNoAddPictureList(), position);
    }

    private List<String> getNoAddPictureList() {
        List<String> list = new ArrayList<>();
        list.addAll(mPhotoPath);
        list.remove(list.size() - 1);
        return list;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initNavi() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        // 申请权限
        if (!hasBasePhoneAuth()) {
            this.requestPermissions(mAuthPermissions, mAuthBaseRequestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 5:
                    mPhotoPath.clear();
                    mPhotoPath.addAll(data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT));
                    mPhotoPath.add("111");
//                    mAdapter = new ImageRvAdapter(mPhotoPath);
//                    mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                            if (mPhotoPath.get(position)=="111"){
//                                choosePictrue();
//                            }else {
//                                ImgPreviewAct.startPreview(ErrorReportActivity.this, mPhotoPath,position);
//                            }
//
//                        }
//                    });
//                    mRvPhoto.setLayoutManager(new GridLayoutManager(this, 4));
//                    mRvPhoto.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    /**
     * 压缩图片上传
     *
     * @param paths 文件路径集合
     */
    private void pressImage(List<String> paths) {
        if (paths == null || paths.size() == 0)
            return;
        List<File> files = new ArrayList<>();
        for (String path : paths) {
            files.add(new File(path));
        }

        Luban.get(this).load(files)
                .setMaxSize(150)
                .setMaxHeight(1920)
                .setMaxWidth(1080)
                .putGear(Luban.CUSTOM_GEAR).launch(new OnMultiCompressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List<File> fileList) {
                List<MultipartBody.Part> upFiles = Tools.filesToMultipartBodyParts(fileList);
                mPresenter = new UploadsPresenter(ErrorReportActivity.this);
                ((UploadsPresenter) mPresenter).uploads(upFiles);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @Override
    public void uploadsResult(Object result) {
        mPresenter = new ExceptionReportPresenter(this);
        Map<String, String> map = (Map<String, String>) result;
        Set<Map.Entry<String, String>> entries = map.entrySet();
        List<String> filePaths = new ArrayList<>();
        for (Map.Entry<String, String> entry : entries) {
            filePaths.add(entry.getKey());
        }
        ExceptionReportEntity model = new ExceptionReportEntity();
        model.setFlightNum(mFlightNumber);
        model.setExceptionDesc(mEtDetailInfo.getText().toString());
        model.setFiles(filePaths);
        model.setFlightId(Long.valueOf(mFlightId));
        model.setReOperator(UserInfoSingle.getInstance().getUserId());
        model.setReType(getIntent().getIntExtra("error_type", 1));
        String deptCode = UserInfoSingle.getInstance().getDeptCode();
        String deptId = "";
        if (deptCode != null) {
            deptId = (deptCode.contains("-") ? deptCode.substring(0, deptCode.indexOf("-")) : deptCode);
        }
        model.setDeptId(deptId);
        model.setArea(getIntent().getStringExtra("area_id"));
        model.setExceptionCode(getIntent().getStringExtra("step_code"));
        TransportEndEntity endEntity = new TransportEndEntity();
        endEntity.setTaskId(mCurrentTaskId);
        model.setTransportAppDto(endEntity);
        ((ExceptionReportPresenter) mPresenter).exceptionReport(model);
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(this, error);
    }

    @Override
    public void showNetDialog() {
        if (mPhotoPath.size() != 0) {
            showProgessDialog("图片上传中");
        } else {
            showProgessDialog("");
        }
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void exceptionReportResult(String result) {
        ToastUtil.showToast("异常上报成功");
        finish();
    }

    @Override
    public void exceptionTpEndResult(String result) {

    }
}