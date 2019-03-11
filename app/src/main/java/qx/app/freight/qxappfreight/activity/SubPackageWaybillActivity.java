package qx.app.freight.qxappfreight.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.response.FtGroupScooter;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 运单分装
 */
public class SubPackageWaybillActivity extends BaseActivity {

    @BindView(R.id.tv_waybill_number)
    TextView tvWayBillNum;
    @BindView(R.id.tv_flight_number)
    TextView tvFlightNum;
    @BindView(R.id.tv_flight_route)
    TextView tvFlightRoute;
    @BindView(R.id.tv_etd)
    TextView tvEtd;
    @BindView(R.id.tv_subpackage_weight_total)
    TextView tvWeightTotal;
    @BindView(R.id.tv_subpackage_count_total)
    TextView tvCountTotal;
    @BindView(R.id.tv_subpackage_volume_total)
    TextView tvVolumeTotal;
    @BindView(R.id.et_subpackage_weight)
    TextView tvSubpackageWeight;
    @BindView(R.id. et_subpackage_count)
    TextView tvSubpackageCount;
    @BindView(R.id. et_subpackage_volume)
    TextView tvSubpackageVolume;


    private FtGroupScooter mRcInfoEd;//被分装的收运记录
    private FtGroupScooter mRcInfo;//分装出来收运记录

    private double subpackageWeight;
    private double subpackageVolume;
    private int subpackageCount;

    public static void startActivity(Context context, FtGroupScooter mRcInfo ) {
        Intent intent = new Intent(context, SubPackageWaybillActivity.class);
        intent.putExtra("mRcInfo",mRcInfo);
        ((Activity)context).startActivityForResult(intent,0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_subpackage;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initView() {
        setToolbarShow(View.VISIBLE);
        CustomToolbar toolbar = getToolbar();
        toolbar.setMainTitle(Color.WHITE, "运单分装");
    }

    private void initData() {
        mRcInfoEd = (FtGroupScooter) getIntent().getSerializableExtra("mRcInfo");
        if (mRcInfoEd != null){
            tvWayBillNum.setText(mRcInfoEd.getWaybillCode());
            tvWeightTotal.setText(mRcInfoEd.getWeight()+"");
            tvCountTotal.setText(mRcInfoEd.getNumber()+"");
            tvVolumeTotal.setText(mRcInfoEd.getVolume()+"");
        }
        else {
            ToastUtil.showToast(this,"无效的收运记录");
            finish();
        }

    }

    @OnClick(R.id.btn_sure_subpackage)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_sure_subpackage:
                mRcInfo = new FtGroupScooter();
                try {
                    mRcInfo = Tools.IOclone(mRcInfoEd);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("Tools.IOclone",e.getMessage());
                }
                if (!"".equals(tvSubpackageWeight.getText().toString()))
                    subpackageWeight = Double.valueOf(tvSubpackageWeight.getText().toString());
                else
                {
                    ToastUtil.showToast(this,"请填写分装重量");
                    return;
                }
                if (!"".equals(tvSubpackageCount.getText().toString()))
                    subpackageCount = Integer.valueOf(tvSubpackageCount.getText().toString());
                else
                {
                    ToastUtil.showToast(this,"请填写分装件数");
                    return;
                }
                if (!"".equals(tvSubpackageVolume.getText().toString()))
                    subpackageVolume = Double.valueOf(tvSubpackageVolume.getText().toString());
                else
                {
                    ToastUtil.showToast(this,"请填写分装体积");
                    return;
                }
                if (mRcInfoEd.getWeight() > subpackageWeight){
                    mRcInfoEd.setWeight(mRcInfoEd.getWeight() - subpackageWeight);
                    mRcInfo.setWeight(subpackageWeight);
                }
                else {
                    ToastUtil.showToast(this,"分装重量必须小于可分装重量");
                    return;
                }
                if (mRcInfoEd.getNumber() > subpackageCount){
                    mRcInfoEd.setNumber(mRcInfoEd.getNumber() - subpackageCount);
                    mRcInfo.setNumber(subpackageCount);
                }
                else {
                    ToastUtil.showToast(this,"分装件数必须小于可分装件数");
                    return;
                }
                if (mRcInfoEd.getVolume() > subpackageVolume){
                    mRcInfoEd.setVolume(mRcInfoEd.getVolume()- subpackageVolume);
                    mRcInfo.setVolume(subpackageVolume);
                }
                else {
                    ToastUtil.showToast(this,"分装体积必须小于可分装体积");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("mRcInfoEd",mRcInfoEd);
                intent.putExtra("mRcInfo",mRcInfo);
                mRcInfo.setId(null);
                setResult(Constants.FINISH_SUBPACKAGE,intent);
                finish();
                break;
        }


    }
}
