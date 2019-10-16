package qx.app.freight.qxappfreight.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.CustomCaptureActivity;
import qx.app.freight.qxappfreight.activity.ScanManagerActivity;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.IOqrcodeEntity;
import qx.app.freight.qxappfreight.bean.ScanDataBean;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 库房管理
 */
public class IOManifestFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    CustomToolbar mToolBar;
    @BindView(R.id.tv_warehouse_name)
    TextView tvWarehouseName;
    @BindView(R.id.btn_switch)
    Button btnSwitch;
    @BindView(R.id.tb_title)
    RadioGroup mRgTitle;    //切换按钮

    private Fragment nowFragment;
    private InWarehouseFragment inWarehouseFragment;
    private OutWarehouseFragment outWarehouseFragment;
    private InventoryFragment inventoryFragment;

    public static IOqrcodeEntity iOqrcodeEntity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_io_manifest, container, false);
        unbinder = ButterKnife.bind(this, view);
        mToolBar.setMainTitle(Color.WHITE, "库房管理");
        initView();
        return view;
    }

    /**
     * 激光扫码回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ScanDataBean result) {
        String daibanCode = result.getData();
        Log.e("daibanCode", daibanCode + "");
        if (!TextUtils.isEmpty(result.getData()) && result.getFunctionFlag().equals("IOManifestFragment")) {

            IOqrcodeEntity iOqrcodeEntity1 = JSON.parseObject(daibanCode, IOqrcodeEntity.class);
            if (iOqrcodeEntity1 != null) {
                try {
                    iOqrcodeEntity = Tools.IOclone(iOqrcodeEntity1);
                } catch (Exception e) {
                    ToastUtil.showToast(e.getMessage());
                    return;
                }
                tvWarehouseName.setText(iOqrcodeEntity.getBusinessName() + "-" + iOqrcodeEntity.getDepotName());

                inWarehouseFragment.loadData(1);
                outWarehouseFragment.loadData(1);
                inventoryFragment.loadData(1);
//                inWarehouseFragment.loadData();
//                storageType = iOqrcodeEntity.getDepotID();
            }

        }

    }


    private void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mRgTitle.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_in:
                    nowFragment = inWarehouseFragment; //进库单
                    break;
                case R.id.rb_out:
                    nowFragment = outWarehouseFragment; //出库单
                    break;
                case R.id.rb_current:
                    nowFragment = inventoryFragment; //当前库存
                    break;
            }
            showFragment(nowFragment);
        });
        btnSwitch.setOnClickListener(v -> {
            ScanManagerActivity.startActivity(getContext(), "IOManifestFragment");
        });
        initFragment();
    }

    private void initFragment() {
        inWarehouseFragment = new InWarehouseFragment();
        outWarehouseFragment = new OutWarehouseFragment();
        inventoryFragment = new InventoryFragment();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.fl_content, inWarehouseFragment)
                .add(R.id.fl_content, outWarehouseFragment)
                .add(R.id.fl_content, inventoryFragment)
                .commit();
        showFragment(inWarehouseFragment);
    }

    public void showFragment(Fragment fragment) {

        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction()
                .hide(outWarehouseFragment)
                .hide(inventoryFragment)
                .hide(inWarehouseFragment);
        transaction.show(fragment).commit();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TestFragment===========", "onDestroy");
    }
}
