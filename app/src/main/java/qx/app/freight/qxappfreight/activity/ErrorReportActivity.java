package qx.app.freight.qxappfreight.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
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
import qx.app.freight.qxappfreight.contract.ExceptionReportContract;
import qx.app.freight.qxappfreight.contract.UploadsContract;
import qx.app.freight.qxappfreight.presenter.ExceptionReportPresenter;
import qx.app.freight.qxappfreight.presenter.UploadsPresenter;
import qx.app.freight.qxappfreight.utils.CommonJson4List;
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
    @BindView(R.id.iv_chose_photo)
    ImageView mIvChosePhoto;
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
    private String[] mInfoList;
    private ArrayList<String> mFlightNumberList; //传过来的航班号列表
    private String mCurrentTaskId;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommonJson4List result) {
        if (result != null) {
            if (result.isCancelFlag()) {
                String taskId = result.getTaskId();
                if (taskId.equals(mCurrentTaskId)) {
                    ToastUtil.showToast("当前卸机任务已取消");
                    Observable.timer(2, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()) // timer 默认在新线程，所以需要切换回主线程
                            .subscribe(aLong -> finish());
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String result) {
        if (result != null && result.equals(mInfoList[7])) {

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
        ;
        initNavi();//权限方法
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "异常上报");
        mAdapter = new ImageRvAdapter(mPhotoPath);
        mRvPhoto.setLayoutManager(new GridLayoutManager(this, 4));
        mRvPhoto.setAdapter(mAdapter);
        //根据过来的参数判断
        //传过来的航班信息 用*号隔开
        String info = getIntent().getStringExtra("plane_info");
        mFlightNumberList = getIntent().getStringArrayListExtra("plane_info_list");
        if (TextUtils.isEmpty(info) && mFlightNumberList != null) {
            mSpinner.setVisibility(View.VISIBLE);
            mFlightNumber = mFlightNumberList.get(0);
            mTvFlightInfo.setText(mFlightNumber);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.item_spinner_general, mFlightNumberList);
            mSpinner.setAdapter(spinnerAdapter);
            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mFlightNumber = mFlightNumberList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            mTvFlightInfo.setVisibility(View.VISIBLE);
            mInfoList = info.split("\\*");
            mFlightNumber = mInfoList[0];
            mCurrentTaskId = mInfoList[12];
            mTvFlightInfo.setText(mFlightNumber);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        mTvDate.setText(sdf.format(new Date()));
        mIvChosePhoto.setOnClickListener(v -> {
            List<String> originList = new ArrayList<>();
            if (mAdapter == null) {
                originList.addAll(mPhotoPath);
            } else {
                originList.addAll(mAdapter.getFinalPhotoList());
            }
            MultiImageSelector.create(ErrorReportActivity.this)
                    .showCamera(true) // 是否显示相机. 默认为显示
                    .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                    .multi() // 多选模式, 默认模式;
                    .origin((ArrayList<String>) originList) // 默认已选择图片. 只有在选择模式为多选时有效
                    .start(ErrorReportActivity.this, REQUEST_IMAGE);
        });
        mBtnCommit.setOnClickListener(v -> {
            if (mAdapter.getFinalPhotoList().size()==0&&TextUtils.isEmpty(mEtDetailInfo.getText().toString())){
                ToastUtil.showToast("请添加文字或图片说明（至少一种）");
            }else {
                if (mAdapter.getFinalPhotoList().size() == 0) {
                    mPresenter = new ExceptionReportPresenter(ErrorReportActivity.this);
                    ExceptionReportEntity model = new ExceptionReportEntity();
                    model.setFlightNum(mFlightNumber);
                    model.setExceptionDesc(mEtDetailInfo.getText().toString());
                    model.setReOperator(UserInfoSingle.getInstance().getUserId());
                    model.setReType(getIntent().getIntExtra("error_type", 1));
                    model.setFiles(null);
                    ((ExceptionReportPresenter) mPresenter).exceptionReport(model);
                } else {
                    pressImage(mAdapter.getFinalPhotoList());
                }
            }
        });
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
                    mPhotoPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    mAdapter = new ImageRvAdapter(mPhotoPath);
                    mRvPhoto.setLayoutManager(new GridLayoutManager(this, 4));
                    mRvPhoto.setAdapter(mAdapter);
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
        model.setFlightId(Long.valueOf(mInfoList[7]));
        model.setReOperator(UserInfoSingle.getInstance().getUserId());
        model.setReType(getIntent().getIntExtra("error_type", 1));
        ((ExceptionReportPresenter) mPresenter).exceptionReport(model);
    }

    @Override
    public void toastView(String error) {
        ToastUtil.showToast(this, error);
    }

    @Override
    public void showNetDialog() {
        if (mAdapter.getFinalPhotoList().size()!=0){
            showProgessDialog("图片上传中");
        }else {
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
}
