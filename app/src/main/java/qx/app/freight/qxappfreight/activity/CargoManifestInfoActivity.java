package qx.app.freight.qxappfreight.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.loadinglist.CargoManifestEventBusEntity;
import qx.app.freight.qxappfreight.bean.loadinglist.InstallNotifyEventBusEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PrintBean;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.AuditManifestContract;
import qx.app.freight.qxappfreight.contract.GetLastReportInfoContract;
import qx.app.freight.qxappfreight.contract.PrintRequestContract;
import qx.app.freight.qxappfreight.dialog.SingerDialog;
import qx.app.freight.qxappfreight.fragment.HyFragment;
import qx.app.freight.qxappfreight.fragment.ZdFragment;
import qx.app.freight.qxappfreight.presenter.AuditManifestPresenter;
import qx.app.freight.qxappfreight.presenter.GetLastReportInfoPresenter;
import qx.app.freight.qxappfreight.presenter.PrintRequestPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/****
 * ????????????????????????
 */
public class CargoManifestInfoActivity extends BaseActivity implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, GetLastReportInfoContract.getLastReportInfoView, AuditManifestContract.auditManifestView, PrintRequestContract.printRequestView {
    @BindView(R.id.tv_flight_number)
    TextView mTvFlightNumber;//?????????
    @BindView(R.id.tv_plane_info)
    TextView mTvPlaneInfo;//??????
    @BindView(R.id.ll_flight_info_container)
    LinearLayout mLlContainer;//??????????????????
    @BindView(R.id.tv_seat)
    TextView mTvSeat;//??????
    @BindView(R.id.tv_time_take_off)
    TextView mTvTakeOff;//????????????
    @BindView(R.id.tv_time_fall_down)
    TextView mTvFallDown;//????????????
    @BindView(R.id.tv_date)
    TextView mTvDate;//??????
    @BindView(R.id.tv_version)
    TextView mTvVersion;//?????????
    @BindView(R.id.tv_status)
    TextView mTvStatus;//????????????????????????

    //    @BindView(R.id.mfrv_data)
//    RecyclerView mRvData;//????????????????????????
    @BindView(R.id.bt_shifang)
    Button mBtShifang;    //??????

    @BindView(R.id.bt_reload)
    Button btnReload;    // ????????????
    @BindView(R.id.btn_print)
    Button mBtPrint;    //??????
    @BindView(R.id.tb_title)
    RadioGroup mRgTitle;    //????????????

//    @BindView(R.id.sr_refush)
//    SwipeRefreshLayout mSrRefush;


    private LoadAndUnloadTodoBean mBaseData;
    private String mId = null;
    private String flightInfoId;
    private String  writeInfo;
    private String currentVersion;
    private HyFragment mHYragment;
    private ZdFragment mZdFragment;
    private Fragment nowFragment;

    private int flag = 0;//0 ?????????????????? 1 ??????????????????

    private int printFlag = 1;// 1 ???????????? 2 ????????? 3 ????????????

    public static void startActivity(Context context, LoadAndUnloadTodoBean loadAndUnloadTodoBean, int flag) {
        Intent intent = new Intent(context, CargoManifestInfoActivity.class);
        intent.putExtra("flag", flag);
        intent.putExtra("data", loadAndUnloadTodoBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cargo_manifest_info;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "??????", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "??????????????????");
        mBaseData = (LoadAndUnloadTodoBean) getIntent().getSerializableExtra("data");
        flag = getIntent().getIntExtra("flag", 0);
        mTvFlightNumber.setText(mBaseData.getFlightNo());
        mTvPlaneInfo.setText(mBaseData.getAircraftno());
        FlightInfoLayout layout = new FlightInfoLayout(this, mBaseData.getFlightInfoList());
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLlContainer.removeAllViews();
        mLlContainer.addView(layout, paramsMain);
        mTvSeat.setText(mBaseData.getSeat());
        mTvTakeOff.setText(StringUtil.getTimeTextByRegix(mBaseData.getEtd(), "HH:mm"));
        mTvFallDown.setText(StringUtil.getTimeTextByRegix(mBaseData.getAta(), "HH:mm"));
        mTvDate.setText(StringUtil.getTimeTextByRegix(mBaseData.getScheduleTime(), "yyyy-MM-dd"));
//        mRvData.setLayoutManager(new LinearLayoutManager(this));
//        mRvData.setRefreshListener(this);
//        mRvData.setOnRetryLisenter(this);
//        mRvData.setRefreshStyle(false);
        initFragment();
        initView();
        loadData();
        //??????
        mBtShifang.setOnClickListener(v -> {
            showYesOrNoDialog("", "??????????????????" + currentVersion + "???????????????", 4);
        });
        mBtPrint.setOnClickListener(v -> {

            String intro = "";
            if (printFlag == 1)
                intro = "??????????????????" + currentVersion + "???????????????";
            else if (printFlag == 3)
                intro = "??????????????????" + currentVersion + "???????????????";

            SingerDialog singerDialog = new SingerDialog(this, intro);
            singerDialog.isCanceledOnTouchOutside(false)
                    .isCanceled(true)
                    .setOnClickListener(oAuserInfo -> {
                        printManifest(oAuserInfo);
                        singerDialog.dismiss();
                    });
            List <PrintBean> list = new ArrayList <>();
            list.add(new PrintBean("1", "????????????1???"));
            list.add(new PrintBean("2", "????????????2???"));
            list.add(new PrintBean("3", "????????????3???"));
            singerDialog.setData(list);
//            mPresenter = new PrintRequestPresenter(this);
//            BaseFilterEntity entity = new BaseFilterEntity();
//            entity.setFlightId(mBaseData.getFlightId());
//            entity.setReportInfoId(mId);
//            // 1 ???????????? 2 ????????? 3????????????
//            entity.setType(printFlag);
//            entity.setPrintName("1");
//            ((PrintRequestPresenter) mPresenter).printRequest(entity);
        });
        btnReload.setOnClickListener(v->{
            showYesOrNoDialog("", "??????????????????????????????", 5);

        });
//        mSrRefush.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadData();
//            }
//        });
    }

    private void printManifest(String printName) {

        mPresenter = new PrintRequestPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightId(mBaseData.getFlightId());
        entity.setReportInfoId(mId);
        // 1 ???????????? 2 ????????? 3????????????
        entity.setType(printFlag);
        entity.setPrintName(printName);
        ((PrintRequestPresenter) mPresenter).printRequest(entity);

    }

    private void releaseFlight() {
        mPresenter = new AuditManifestPresenter(this);
        if (mId != null) {
            BaseFilterEntity entity = new BaseFilterEntity();
            entity.setReportInfoId(mId);
            entity.setOperationUser(UserInfoSingle.getInstance().getUserId());
            entity.setAuditType("2");
            entity.setFlightId(mBaseData.getFlightId());
            entity.setReturnReason("?????????????????????");
            ((AuditManifestPresenter) mPresenter).auditManifest(entity);
        } else
            ToastUtil.showToast("?????????????????????????????????????????????");
    }

    private void repartWriteLoading() {
        mPresenter = new AuditManifestPresenter(this);
        if (flightInfoId != null&& !StringUtil.isEmpty(writeInfo)) {
            BaseFilterEntity entity = new BaseFilterEntity();
            entity.setFlightInfoId(flightInfoId);
            ((AuditManifestPresenter) mPresenter).repartWriteLoading(entity);
        } else
            ToastUtil.showToast("??????????????????????????????????????????");

    }

    public void loadData() {
        mPresenter = new GetLastReportInfoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightId(mBaseData.getFlightId());
        //????????????
        entity.setDocumentType(1);
        entity.setSort(1);
        ((GetLastReportInfoPresenter) mPresenter).getLastReportInfo(entity);
    }

    public void initView() {
        mRgTitle.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_hy:
                    nowFragment = mHYragment; //????????????
                    printFlag = 1;
                    break;
                case R.id.rb_zd:
                    nowFragment = mZdFragment; //????????????
                    printFlag = 3;
                    break;
            }
            showFragment(nowFragment);
        });
    }

    public void showFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .hide(mHYragment)
                .hide(mZdFragment);
        transaction.show(fragment).commit();
    }

    private void initFragment() {
        mHYragment = new HyFragment();
        mZdFragment = new ZdFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_content, mHYragment)
                .add(R.id.fl_content, mZdFragment)
                .commit();
        if (flag == 1) {
            mRgTitle.check(R.id.rb_zd);
            showFragment(mZdFragment);
        } else
            showFragment(mHYragment);
    }

    /**
     * ?????????????????????
     *
     * @param title
     * @param msg
     * @param flag
     */
    private void showYesOrNoDialog(String title, String msg, int flag) {
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("??????")
                .setNegativeButton("??????")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            switch (flag) {
                                case 1:
                                case 3:
                                    printManifest("1");
                                    break;
                                case 4:
                                    releaseFlight();
                                    break;
                                case 5:
                                    repartWriteLoading();
                                    break;

                            }
                        } else {
//                            ToastUtil.showToast("????????????????????????");
                        }
                    }
                })
                .show();

    }

    @Override
    public void getLastReportInfoResult(List <FlightAllReportInfo> results) {
        if (results != null && results.get(0) != null) {
            FlightAllReportInfo result = results.get(0);
            flightInfoId = result.getFlightInfoId();
            writeInfo = result.getWriteInfo();
            mId = result.getId();
            mTvVersion.setText("?????????:" + result.getVersion());

            if (result.getWriteResult() != null && result.getWriteResult().contains("??????")) {
                mTvStatus.setTextColor(getResources().getColor(R.color.green));
                mTvStatus.setText("????????????" + result.getWriteResult());
                mTvStatus.getPaint().setFakeBoldText(false);
            } else if (result.getWriteResult() != null && result.getWriteResult().contains("??????")) {
                mTvStatus.setTextColor(getResources().getColor(R.color.red));
                mTvStatus.setText("????????????" + result.getWriteResult());
                mTvStatus.getPaint().setFakeBoldText(false);
            } else {
                mTvStatus.setTextColor(getResources().getColor(R.color.gray_8f));
                mTvStatus.setText("???????????? ?????????");
                mTvStatus.getPaint().setFakeBoldText(true);
            }

            currentVersion = result.getVersion();
            CargoManifestEventBusEntity cargoManifestEventBusEntity = new CargoManifestEventBusEntity(results);
            EventBus.getDefault().post(cargoManifestEventBusEntity);
            //?????????????????????
            if (!results.get(0).isCanRelease()) {
                mBtShifang.setBackgroundColor(getResources().getColor(R.color.gray_cc));
                mBtShifang.setEnabled(false);
            } else {
                mBtShifang.setBackgroundResource(R.drawable.shape_dynamic_blue);
                mBtShifang.setEnabled(true);
                mBtShifang.setFocusableInTouchMode(true);
                mBtShifang.requestFocus();
            }
        }
    }

    @Override
    public void lockOrUnlockScooterResult(String result) {

    }

    @Override
    public void toastView(String error) {
        if (error != null) {
            ToastUtil.showToast(error);
        }
//        mSrRefush.setRefreshing(false);
//        mRvData.finishRefresh();
    }

    @Override
    public void showNetDialog() {
        showProgessDialog("?????????......");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void onRetry() {
        showProgessDialog("????????????????????????");
        new Handler().postDelayed(() -> {
//            loadData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void onRefresh() {
//        loadData();
    }

    @Override
    public void onLoadMore() {
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(InstallNotifyEventBusEntity result) {
        if (result.getFlightNo().equals(mBaseData.getFlightNo())){
            if (result.getType() == 3){
                loadData();
            }
        }
    }

    @Override
    public void auditManifestResult(String result) {
        if (result != null) {
            ToastUtil.showToast("???????????????");
            loadData();
        }
        EventBus.getDefault().post("CargoManifestFragment_refresh");
//        finish();
    }

    @Override
    public void repartWriteLoadingResult(String result) {
        if (result != null) {
            ToastUtil.showToast("?????????????????????");
            loadData();
        }
    }

    @Override
    public void printRequestResult(String result) {
        ToastUtil.showToast("????????????");
    }
}
