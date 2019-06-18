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

import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.StorageCommitEntity;
import qx.app.freight.qxappfreight.bean.response.DeclareWaybillBean;
import qx.app.freight.qxappfreight.bean.response.MarketCollectionRequireBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
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
    @BindView(R.id.ll_baozhuang)
    LinearLayout mLlBaoZhuang;
    @BindView(R.id.ll_yaoqiu)
    LinearLayout mLlYaoQiu;
    @BindView(R.id.ll_spot)
    LinearLayout mLlSpot;

    private String insFile;  //报检员资质路径
    private int insCheck; //报检是否合格1合格 0不合格
    private int fileCheck;//资质是否合格1合格 0不合格
    private String userId; //当前提交人id
    private int isPack; //是否包装  1 包装 0 不包装
    private int isRequire; //是否满足航空公司要求1勾选 0不勾选
    private int isSpSpot;//是否通过;
    private StorageCommitEntity mStorageCommitEntity;
    private PopupWindow popupWindow;
    private int mSportResult;//抽验结果  0是通过 1是不通过

    private TransportDataBase mBean;
    private DeclareWaybillBean mDecBean;


    public static void startActivity(Activity context,
                                     TransportDataBase mBean,
                                     DeclareWaybillBean mDecBean,
                                     String insFile,
                                     int insCheck,
                                     int fileCheck,
                                     int spotResult,
                                     String userId
    ) {
        Intent intent = new Intent(context, VerifyCargoActivity.class);
        intent.putExtra("mBean", mBean);
        intent.putExtra("mDecBean", mDecBean);
        intent.putExtra("insFile", insFile);
        intent.putExtra("insCheck", insCheck);
        intent.putExtra("fileCheck", fileCheck);
        intent.putExtra("userId", userId);
        intent.putExtra("spotResult", spotResult);
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
        mBean = (TransportDataBase) getIntent().getSerializableExtra("mBean");
        mDecBean = (DeclareWaybillBean) getIntent().getSerializableExtra("mDecBean");
        insFile = getIntent().getStringExtra("insFile");
        insCheck = getIntent().getIntExtra("insCheck", 0);
        fileCheck = getIntent().getIntExtra("fileCheck", 0);
        userId = getIntent().getStringExtra("userId");
        //抽查结果
        mSportResult = getIntent().getIntExtra("spotResult", -1);
        //货代信息
        mPresenter = new FreightInfoPresenter(this);
        ((FreightInfoPresenter) mPresenter).freightInfo(mDecBean.getFlightNumber().substring(0, 2));

        Log.e("dime", "spotFlat=" + mDecBean.getSpotFlag() + ", spotResult=" + mSportResult);
        if (mDecBean.getSpotFlag().equals("0")) {
            //抽查，显示抽查操作按钮
            mLlSpot.setVisibility(View.VISIBLE);
        }


        //默认选中
        CbPack.setChecked(true);
        mLlBaoZhuang.setOnClickListener(v -> {
            if (CbPack.isChecked()) {
                CbPack.setChecked(false);
            } else {
                CbPack.setChecked(true);
            }
        });
        CbCollectRequire.setChecked(true);
        mLlYaoQiu.setOnClickListener(v -> {
            if (CbCollectRequire.isChecked()) {
                CbCollectRequire.setChecked(false);
            } else {
                CbCollectRequire.setChecked(true);
            }
        });

        mStorageCommitEntity = new StorageCommitEntity();
        llReason.setVisibility(View.GONE);
        mTvCheck.setOnClickListener(v -> {
            //0抽检，要弹，1不抽检，不需要弹
            if (1 == mSportResult) {
                ToastUtil.showToast("不需要抽检，默认通过");
            } else if (0 == mSportResult) {
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
            mStorageCommitEntity.setWaybillId(mBean.getId());
            mStorageCommitEntity.setWaybillCode(mBean.getWaybillCode());
            mStorageCommitEntity.setInsUserId(UserInfoSingle.getInstance().getUserId());
            mStorageCommitEntity.setInsFile(insFile);
            mStorageCommitEntity.setInsCheck(insCheck);
            mStorageCommitEntity.setFileCheck(fileCheck);
            mStorageCommitEntity.setFileCheck(isPack);
            mStorageCommitEntity.setRequire(isRequire);
            mStorageCommitEntity.setSpotResult(isSpSpot);
            mStorageCommitEntity.setTaskTypeCode(mBean.getTaskTypeCode());
            mStorageCommitEntity.setUnspotReson(etReason.getText().toString().trim());
            mStorageCommitEntity.setType(1);
            mStorageCommitEntity.setTaskId(mBean.getTaskId());
            mStorageCommitEntity.setUserId(userId);
            //新加
            mStorageCommitEntity.setInsUserName("");
            mStorageCommitEntity.setInsDangerEnd(123);
            mStorageCommitEntity.setInsDangerStart(123);
            mStorageCommitEntity.setInsStartTime(123);
            mStorageCommitEntity.setInsEndTime(123);
            mStorageCommitEntity.setInsUserHead("");
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
    public void freightInfoResult(List<MarketCollectionRequireBean> beanList) {
        if (beanList != null) {
            String str = "";
            for (MarketCollectionRequireBean bean : beanList) {
                str += bean.getColRequire() + "\n";
            }
            mTvCollectRequire.setText(str);
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
        showProgessDialog("数据提交中……");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
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
                mTvCheck.setTextColor(Color.parseColor("#FF0000"));
                isSpSpot = 1;
                llReason.setVisibility(View.VISIBLE);
            } else if (cb_adopt.isChecked()) {
                mTvCheck.setText("通过");
                mTvCheck.setTextColor(Color.parseColor("#05B324"));
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
