package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.request.StorageCommitEntity;
import qx.app.freight.qxappfreight.bean.response.FreightInfoBean;
import qx.app.freight.qxappfreight.contract.FreightInfoContract;
import qx.app.freight.qxappfreight.contract.SubmissionContract;
import qx.app.freight.qxappfreight.presenter.FreightInfoPresenter;
import qx.app.freight.qxappfreight.presenter.SubmissionPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 核查货物页面
 */
public class VerifyCargoActivity extends BaseActivity implements SubmissionContract.submissionView, FreightInfoContract.freightInfoView {
    @BindView(R.id.et_reason)
    EditText etReason;
    @BindView(R.id.tv_string_length)
    TextView mTvStrLength;
    @BindView(R.id.tv_check)
    TextView mTvCheck;
    @BindView(R.id.ll_reason)
    LinearLayout llReason;
    @BindView(R.id.cb_pack)
    CheckBox CbPack;
    @BindView(R.id.cb_collect_require)
    CheckBox CbCollectRequire;
    @BindView(R.id.btn_receive_good)
    Button BtnReceiveGood;
    @BindView(R.id.iv_choice)
    ImageView IvChoice;
    @BindView(R.id.rl_choice)
    RelativeLayout choiceRelativeLayout;
    @BindView(R.id.tv_collect_require)
    TextView mTvCollectRequire;

    private String waybillId; //运单id
    private String insFile;  //报检员资质路径
    private int insCheck; //报检是否合格1合格 0不合格
    private int fileCheck;//资质是否合格1合格 0不合格
    private String taskId; //当前任务id
    private String userId; //当前提交人id
    private int isPack; //是否包装  1 包装 0 不包装
    private int isRequire; //是否满足航空公司要求1勾选 0不勾选
    private int isSpSpot;//是否通过;
    private String id;
    private StorageCommitEntity mStorageCommitEntity;
    private PopupWindow popupWindow;
    private String mSpotFlag,mFlightNumber;


    public static void startActivity(Activity context, String waybillId, String id, String insFile, int insCheck, int fileCheck, String taskId, String spotFlag,String userId,String flightNumber) {
        Intent intent = new Intent(context, VerifyCargoActivity.class);
        intent.putExtra("waybillId", waybillId);
        intent.putExtra("id", id);
        intent.putExtra("insFile", insFile);
        intent.putExtra("insCheck", insCheck);
        intent.putExtra("fileCheck", fileCheck);
        intent.putExtra("taskId", taskId);
        intent.putExtra("userId", userId);
        intent.putExtra("spotFlag", spotFlag);
        intent.putExtra("flightNumber", flightNumber);
        context.startActivityForResult(intent, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_verify_cargo;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "核查货物");

        waybillId = getIntent().getStringExtra("waybillId");
        id = getIntent().getStringExtra("id");
        insFile = getIntent().getStringExtra("insFile");
        insCheck = getIntent().getIntExtra("insCheck", 0);
        fileCheck = getIntent().getIntExtra("fileCheck", 0);
        taskId = getIntent().getStringExtra("taskId");
        userId = getIntent().getStringExtra("userId");
        mFlightNumber = getIntent().getStringExtra("flightNumber");
        //0是通过 1是不通过
        mSpotFlag = getIntent().getStringExtra("spotFlag");
        //货代信息
        mPresenter = new FreightInfoPresenter(this);
        ((FreightInfoPresenter) mPresenter).freightInfo(mFlightNumber.substring(0,2));

        mStorageCommitEntity = new StorageCommitEntity();
        llReason.setVisibility(View.GONE);
        mTvCheck.setOnClickListener(v -> {
            Log.e("SpotFlag", mSpotFlag);
            //0抽检，要弹，1不抽检，不需要弹
            if ("1".equals(mSpotFlag)) {
                ToastUtil.showToast( "不需要抽检，默认通过");
            } else if ("0".equals(mSpotFlag)) {
                if ("通过".equals(mTvCheck.getText()))
                    initPop1(true);
                else
                    initPop1(false);
            }

        });

        BtnReceiveGood.setOnClickListener(v -> {
            mPresenter = new SubmissionPresenter(this);
            if (CbPack.isChecked()) {
                isPack = 0;
            } else {
                isPack = 1;
            }
            if (CbCollectRequire.isChecked()) {
                isRequire = 0;
            } else {
                isRequire = 1;
            }
            mStorageCommitEntity.setWaybillId(waybillId);
            mStorageCommitEntity.setInsFile(insFile);
            mStorageCommitEntity.setInsCheck(insCheck);
            mStorageCommitEntity.setFileCheck(fileCheck);
            mStorageCommitEntity.setFileCheck(isPack);
            mStorageCommitEntity.setRequire(isRequire);
            mStorageCommitEntity.setSpotResult(isSpSpot);
            mStorageCommitEntity.setUnspotReson(etReason.getText().toString().trim());
            mStorageCommitEntity.setType(1);
            mStorageCommitEntity.setTaskId(taskId);
            mStorageCommitEntity.setUserId(userId);
            ((SubmissionPresenter) mPresenter).submission(mStorageCommitEntity);
        });

        etReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvStrLength.setText(s.length() + "/50");
            }
        });
    }

    //货代公司资质
    @Override
    public void freightInfoResult(FreightInfoBean freightInfoBean) {
        if (freightInfoBean != null) {
            mTvCollectRequire.setText(freightInfoBean.getRequire());
        } else {
            Log.i("freightInfoBean", "freightInfoBean为空");
        }

    }

    @Override
    public void submissionResult(String result) {
        //刷新代办列表
        EventBus.getDefault().post("collectVerify_refresh");
        //关闭
        setResult(404);
        finish();
        ToastUtil.showToast(this, result);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return false;
        }
        return super.dispatchTouchEvent(event);
    }

    private void initPop1(boolean tag) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.popwindow, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //背景变暗
        makeWindowEnight();
        //点击外面popupWindow消失
        popupWindow.setOutsideTouchable(false);
        //popupWindow获取焦点
        popupWindow.setFocusable(false);
        // 设置背景图片， 必须设置，不然动画没作用
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        popupWindow.setBackgroundDrawable(dw);
        //显示窗口
        popupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view, tag);
        //设置popupWindow消失时的监听
        popupWindow.setOnDismissListener(() ->
                makeWindowLight()
        );
        popupWindow.showAsDropDown(view);
    }

    private void setOnPopupViewClick(View view, boolean tag) {
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        CheckBox cb_adopt = view.findViewById(R.id.cb_adopt);
        CheckBox cb_notpass = view.findViewById(R.id.cb_notpass);
        Button bt_commit = view.findViewById(R.id.btn_commit);
        if (tag)
            cb_adopt.setChecked(true);
        else
            cb_notpass.setChecked(true);

        cb_adopt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                cb_notpass.setChecked(false);
        });
        cb_notpass.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                cb_adopt.setChecked(false);
        });
        bt_commit.setOnClickListener(v -> {
            if (cb_notpass.isChecked()) {
                mTvCheck.setText("不通过");
                isSpSpot = 1;
                llReason.setVisibility(View.VISIBLE);
            } else if (cb_adopt.isChecked()) {
                mTvCheck.setText("通过");
                llReason.setVisibility(View.GONE);
                isSpSpot = 0;
            }
            popupWindow.dismiss();
        });
        tv_cancel.setOnClickListener(v -> {
            if (cb_adopt.isChecked() == false && cb_notpass.isChecked() == false) {
                ToastUtil.showToast(this, "请选择");
            } else
                popupWindow.dismiss();
        });
    }

    /**
     * 设置手机屏幕亮度变亮
     */
    public void makeWindowLight() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1f;
        getWindow().setAttributes(lp);
    }

    /**
     * 设置手机屏幕亮度变暗
     */
    public void makeWindowEnight() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
    }


}
