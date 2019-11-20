package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.response.SmInventoryEntryandexit;
import qx.app.freight.qxappfreight.contract.SubmitIOManiFestContract;
import qx.app.freight.qxappfreight.fragment.IOManifestFragment;
import qx.app.freight.qxappfreight.presenter.SubmitIOManifestPresenter;
import qx.app.freight.qxappfreight.utils.TimeUtils;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;


public class DoItIOManifestActivity extends BaseActivity implements SubmitIOManiFestContract.submitIOManiFestView {


    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_waybill_code)
    TextView tvWaybillCode;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private  SmInventoryEntryandexit smInventoryEntryandexit;

    public static void startActivity(Activity context, SmInventoryEntryandexit smInventoryEntryandexit) {
        Intent intent = new Intent(context, DoItIOManifestActivity.class);
        intent.putExtra("data", smInventoryEntryandexit);
        intent.putExtras(intent);
        context.startActivityForResult(intent, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_do_it_iomanifest;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "库存作业");
        initView();
    }

    private void initView() {
        smInventoryEntryandexit = (SmInventoryEntryandexit) getIntent().getSerializableExtra("data");
        if (smInventoryEntryandexit == null){
            ToastUtil.showToast("数据为空");
            finish();
            return;
        }
        tvType.setText("I".equals(smInventoryEntryandexit.getType())?"入库":"出库");
        tvWaybillCode.setText(smInventoryEntryandexit.getWaybillCode());
        tvNum.setText(smInventoryEntryandexit.getNumber()+"");
        tvWeight.setText(smInventoryEntryandexit.getWeight()+"");
        btnSubmit.setOnClickListener(v -> {
            if (!Tools.isFastClick())
                return;
            smInventoryEntryandexit.setExecUserId(UserInfoSingle.getInstance().getUserId());
            smInventoryEntryandexit.setExecUserName(UserInfoSingle.getInstance().getUsername());
            smInventoryEntryandexit.setExecTime(TimeUtils.getTime());
            smInventoryEntryandexit.setAreaId(IOManifestFragment.iOqrcodeEntity.getDepotID());
            smInventoryEntryandexit.setAreaTypeCode(IOManifestFragment.iOqrcodeEntity.getDepotCode());
            smInventoryEntryandexit.setAreaName(IOManifestFragment.iOqrcodeEntity.getDepotName());
            submitData(smInventoryEntryandexit);
        });
    }

    private void submitData(SmInventoryEntryandexit smInventoryEntryandexit1){
        mPresenter = new SubmitIOManifestPresenter(this);
        ((SubmitIOManifestPresenter)mPresenter).submitIOManifestList(smInventoryEntryandexit1);
    }

    @Override
    public void setSubmitIOManiFestResult(String result) {
        if (result!=null){
            ToastUtil.showToast(result);
            if ("I".equals(smInventoryEntryandexit.getType()))
                EventBus.getDefault().post("inventory_refresh_in");
            else
                EventBus.getDefault().post("inventory_refresh_out");
        }

        finish();
    }

    @Override
    public void toastView(String error) {
        if (error!=null)
            ToastUtil.showToast(error);
        else
            ToastUtil.showToast("提交失败");
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }
}
