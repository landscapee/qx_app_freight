package qx.app.freight.qxappfreight.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.LnstallationListAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.loadinglist.InstallNotifyEventBusEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.PrintBean;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;
import qx.app.freight.qxappfreight.bean.response.LnstallationInfoBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.GetFlightAllReportInfoContract;
import qx.app.freight.qxappfreight.contract.PrintRequestContract;
import qx.app.freight.qxappfreight.contract.ReOpenLoadTaskContract;
import qx.app.freight.qxappfreight.contract.SynchronousLoadingContract;
import qx.app.freight.qxappfreight.dialog.SingerDialog;
import qx.app.freight.qxappfreight.dialog.WaitCallBackDialog;
import qx.app.freight.qxappfreight.presenter.GetFlightAllReportInfoPresenter;
import qx.app.freight.qxappfreight.presenter.PrintRequestPresenter;
import qx.app.freight.qxappfreight.presenter.ReOpenLoadTaskPresenter;
import qx.app.freight.qxappfreight.presenter.SynchronousLoadingPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.Tools;
import qx.app.freight.qxappfreight.widget.CommonDialog;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;

/**
 * 装机单详情页面
 */
public class LnstallationInfoActivity extends BaseActivity implements EmptyLayout.OnRetryLisenter, GetFlightAllReportInfoContract.getFlightAllReportInfoView, SynchronousLoadingContract.synchronousLoadingView, ReOpenLoadTaskContract.ReOpenLoadTaskView, PrintRequestContract.printRequestView {
    @BindView(R.id.tv_flight_number)
    TextView mTvFlightNumber;//航班号
    @BindView(R.id.tv_plane_info)
    TextView mTvPlaneInfo;//机型

    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;//确认人
    @BindView(R.id.tv_confim_date)
    TextView mTvConfirmDate;//确认时间
    @BindView(R.id.tv_confirm_jz)
    TextView mTvConfirmJZ;//结载确认人
    @BindView(R.id.tv_confim_date_jz)
    TextView mTvConfirmDateJZ;//结载 录入时间

    @BindView(R.id.ll_flight_info_container)
    LinearLayout mLlContainer;//航线数据控件
    @BindView(R.id.tv_seat)
    TextView mTvSeat;//机位
    @BindView(R.id.tv_time_take_off)
    TextView mTvTakeOff;//起飞时间
    @BindView(R.id.tv_time_fall_down)
    TextView mTvFallDown;//降落时间
    @BindView(R.id.tv_date)
    TextView mTvDate;//日期
    @BindView(R.id.tv_version)
    TextView mTvVersion;//版本号
    @BindView(R.id.mfrv_data)
    RecyclerView mRvData;//货邮舱单信息列表
    @BindView(R.id.bt_sure)
    Button mBtSure;//通知录入
    @BindView(R.id.btn_refuse)
    Button mBtRefuse;//打印
    @BindView(R.id.sr_refush)
    SwipeRefreshLayout mSrRefush;

    @BindView(R.id.tb_title)
    RadioGroup mRgTitle;    //切换按钮


    @BindView(R.id.tv_sure_install)
    TextView tvSureInstall;//确认按此装机覆盖层

    private LoadAndUnloadTodoBean mBaseData;
    private List <String> mListVerson = new ArrayList <>();
    private List <String> mListVersonCode = new ArrayList <>();
    private int currentVersion = 0;//最新版本号
    private HashMap <String, List <LnstallationInfoBean.ScootersBean>> map = new HashMap <>();
    private HashMap <String, String> mapPresen = new HashMap <>();
    private HashMap <String, String> mapDate = new HashMap <>();
    private HashMap <String, String> mapPresenJZ = new HashMap <>();
    private HashMap <String, String> mapDateJZ = new HashMap <>();

    private HashMap <String, String> mapMid = new HashMap <>();

    private int loadFlag = -1;//用 刷新 请求装机单或者 建议装机单

    private WaitCallBackDialog mWaitCallBackDialog;

    private int flag = 0;//0 展示或有舱单 1 展示装舱建议

    private String mId = null;//装机单id

    List <LnstallationInfoBean.ScootersBean> mList1 = new ArrayList <>();
    LnstallationListAdapter adapter;


    public static void startActivity(Context context, LoadAndUnloadTodoBean loadAndUnloadTodoBean, int flag) {
        Intent intent = new Intent(context, LnstallationInfoActivity.class);
        intent.putExtra("flag", flag);
        intent.putExtra("data", loadAndUnloadTodoBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_lnstalla_tion_info;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "装机单详情");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

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
//        mTvVersion.setText(mBaseData.getVersion() == null ? "版本号：- -" : "版本号：" + mBaseData.getVersion());
        mRvData.setLayoutManager(new LinearLayoutManager(this));
        mBtSure.setOnClickListener(v -> {
            if (!Tools.isFastClick())
                return;
            loadFlag = -1;
            showYesOrNoDialog("", "重新获取离港数据并发送至监装", 5);
//            mPresenter = new SynchronousLoadingPresenter(this);
//            BaseFilterEntity entity = new BaseFilterEntity();
//            entity.setFlightId(mBaseData.getFlightId());
//            entity.setOperationUserName(UserInfoSingle.getInstance().getUsername());
//            entity.setOperationUser(UserInfoSingle.getInstance().getUserId());
//            String userName = UserInfoSingle.getInstance().getUsername();
//            entity.setOperationUserName((userName.contains("-")) ? userName.substring(0, userName.indexOf("-")) : userName);
//            ((SynchronousLoadingPresenter) mPresenter).synchronousLoading(entity);
        });
        Button btnReOpen = findViewById(R.id.btn_reopen_task);
        btnReOpen.setOnClickListener(v -> {
            if (!Tools.isFastClick())
                return;
            loadFlag = -1;
            showYesOrNoDialog("", "确认二次开启舱门", 4);
//            mPresenter = new ReOpenLoadTaskPresenter(LnstallationInfoActivity.this);
//            BaseFilterEntity entity = new BaseFilterEntity();
//            entity.setFlightId(mBaseData.getFlightId());
//            entity.setWorkerId(UserInfoSingle.getInstance().getUserId());
//            entity.setRemark("");
//            ((ReOpenLoadTaskPresenter) mPresenter).reOpenLoadTask(entity);
        });
        mSrRefush.setOnRefreshListener(() -> {
            if (loadFlag == -1) {
                loadFlag = getRadioBtnFlag();
            }
            loadData();
        });
        mTvVersion.setOnClickListener((v -> {
            if (!Tools.isFastClick())
                return;
            showStoragePickView();
        }
        ));
        mBtRefuse.setOnClickListener(v -> {
            if (!Tools.isFastClick())
                return;
            loadFlag = -1;
            String intro ="";
            if (getRadioBtnFlag() == 5|| getRadioBtnFlag() == 6){
                intro = "打印【版本号" + currentVersion + "】最终装机单";
            }
            else {
                intro = "打印【版本号" + currentVersion + "】预装机单";
            }

            SingerDialog singerDialog = new SingerDialog(this, intro);
            singerDialog.isCanceledOnTouchOutside(false)
                    .isCanceled(true)
                    .setOnClickListener(oAuserInfo -> {
                        printManifest(oAuserInfo);
                        singerDialog.dismiss();
                    });
            List <PrintBean> list = new ArrayList <>();
            list.add(new PrintBean("1", "打印机（1）"));
            list.add(new PrintBean("2", "打印机（2）"));
            list.add(new PrintBean("3", "打印机（3）"));
            singerDialog.setData(list);
//            mPresenter = new PrintRequestPresenter(this);
//            BaseFilterEntity entity = new BaseFilterEntity();
//            entity.setFlightId(mBaseData.getFlightId());
//            entity.setReportInfoId(mId);
//            entity.setType(2);
//            entity.setPrintName("1");
//            ((PrintRequestPresenter) mPresenter).printRequest(entity);
        });

        adapter = new LnstallationListAdapter(mList1, true);
        mRvData.setAdapter(adapter);

        if (flag == 1) {
            mRgTitle.check(R.id.rb_advise_install);
            loadFlag = 3;
        } else {
            mRgTitle.check(R.id.rb_install);
            loadFlag = 2;
        }
        loadData();
        mRgTitle.setOnCheckedChangeListener((group, checkedId) -> {
            tvSureInstall.setVisibility(View.GONE); //隐藏按此装机
            mTvConfirm.setVisibility(View.GONE);
            mTvConfirmDate.setVisibility(View.GONE);
            mTvConfirmJZ.setVisibility(View.GONE);
            mTvConfirmDateJZ.setVisibility(View.GONE);
            mSrRefush.setBackgroundColor(getResources().getColor(R.color.white));
            switch (checkedId) {
                case R.id.rb_install: //装机单
                    loadFlag = 2;
                    loadData();
                    break;
                case R.id.rb_advise_install://建议装机单
                    loadFlag = 3;
                    loadData();
                    break;
                case R.id.rb_end_install://最终装机单
                    loadFlag = 5;
                    loadData();
                    break;
                case R.id.rb_end_advise_install://建议装机单
                    loadFlag = 6;
                    loadData();
                    break;
            }
        });
    }

    private void showConfirm(int i) {
        if (i == 3) {
            mTvConfirm.setVisibility(View.GONE);
            mTvConfirmDate.setVisibility(View.GONE);
            mTvConfirmJZ.setVisibility(View.GONE);
            mTvConfirmDateJZ.setVisibility(View.GONE);
        } else {
            mTvConfirm.setVisibility(View.VISIBLE);
            mTvConfirmDate.setVisibility(View.VISIBLE);
            mTvConfirmJZ.setVisibility(View.VISIBLE);
            mTvConfirmDateJZ.setVisibility(View.VISIBLE);
            mSrRefush.setBackgroundColor(getResources().getColor(R.color.blue_btn_bg_color));
        }

    }

    private int getRadioBtnFlag() {
        switch (mRgTitle.getCheckedRadioButtonId()) {
            case R.id.rb_install:
                return 2;
            case R.id.rb_advise_install:
                return 3;
            case R.id.rb_end_install:
                return 5;
            case R.id.rb_end_advise_install:
                return 6;
        }
        return 0;
    }

    private void showDialogWait() {
        mWaitCallBackDialog = new WaitCallBackDialog(this, R.style.dialog2);
        mWaitCallBackDialog.setCancelable(false);
        mWaitCallBackDialog.setCanceledOnTouchOutside(false);
        mWaitCallBackDialog.show();
        new Handler().postDelayed(() -> {
            if (mWaitCallBackDialog.isShowing()) {
                mWaitCallBackDialog.dismiss();
                ToastUtil.showToast("通知录入可能失败，请重新通知录入！");
            }
        }, 30000);

    }

    private void loadData() {
        mPresenter = new GetFlightAllReportInfoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightId(mBaseData.getFlightId());
        //装机单
        entity.setDocumentType(getRadioBtnFlag());
        //1:倒序 2:正序
        entity.setSort(2);
        ((GetFlightAllReportInfoPresenter) mPresenter).getFlightAllReportInfoView(entity);
    }

    @Override
    public void getFlightAllReportInfoResult(List <FlightAllReportInfo> flightAllReportInfos) {
        map.clear();
        mSrRefush.setRefreshing(false);
        mListVerson.clear();
        mListVersonCode.clear();
        mapPresen.clear();
        mapDate.clear();
        mapMid.clear();
//        mTvVersion.setText("版本号:" + flightAllReportInfos.get(0).getVersion());
        if (flightAllReportInfos.size() > 0) {
            Gson mGson = new Gson();
            for (int i = 0; i < flightAllReportInfos.size(); i++) {

                if (flightAllReportInfos.get(i).getContent() != null && !"[]".equals(flightAllReportInfos.get(i).getContent())) {
                    LnstallationInfoBean[] datas = mGson.fromJson(flightAllReportInfos.get(i).getContent(), LnstallationInfoBean[].class);
                    List <LnstallationInfoBean.ScootersBean> list = new ArrayList <>();
                    for (LnstallationInfoBean data : datas) {
                        list.addAll(data.getScooters());
                    }
                    map.put(flightAllReportInfos.get(i).getVersion(), list);
                    mapMid.put(flightAllReportInfos.get(i).getVersion(), flightAllReportInfos.get(i).getId());//储存装机单 id
                    if (flightAllReportInfos.get(i).getInstalledSingleConfirm() == 1) {
                        if (loadFlag == 3) {//确认按此装机版本
                            mListVerson.add("确认按此装机(版本" + (flightAllReportInfos.get(i).getVersion()) + ")");
                            mapPresen.put(flightAllReportInfos.get(i).getVersion(), flightAllReportInfos.get(i).getInstalledSingleConfirmUser());
                            mapDate.put(flightAllReportInfos.get(i).getVersion(), StringUtil.getTimeTextByRegix(flightAllReportInfos.get(i).getInstalledSingleConfirmTime(), "yyyy-MM-dd HH:mm"));
                        } else {
                            mListVerson.add("监装确认(版本" + (flightAllReportInfos.get(i).getVersion()) + ")");
                            mapPresen.put(flightAllReportInfos.get(i).getVersion(), flightAllReportInfos.get(i).getInstalledSingleConfirmUser());
                            mapDate.put(flightAllReportInfos.get(i).getVersion(), StringUtil.getTimeTextByRegix(flightAllReportInfos.get(i).getInstalledSingleConfirmTime(), "yyyy-MM-dd HH:mm"));
                        }

                    } else {
                        mListVerson.add("版本号:" + flightAllReportInfos.get(i).getVersion());
                        mapPresen.put(flightAllReportInfos.get(i).getVersion(), "");
                        mapDate.put(flightAllReportInfos.get(i).getVersion(), "");
                    }
                    mapPresenJZ.put(flightAllReportInfos.get(i).getVersion(), flightAllReportInfos.get(i).getCreateUserName());
                    mapDateJZ.put(flightAllReportInfos.get(i).getVersion(), StringUtil.getTimeTextByRegix(flightAllReportInfos.get(i).getCreateTime(), "yyyy-MM-dd HH:mm"));

                    mListVersonCode.add(flightAllReportInfos.get(i).getVersion());
                }
            }
            //倒序
            Collections.reverse(mListVerson);
            Collections.reverse(mListVersonCode);
            screenData(0);

        } else {
            mList1.clear();
            adapter.notifyDataSetChanged();
            mTvVersion.setText("版本号:无");
        }

    }

    @Override
    public void reOpenLoadTaskResult(String result) {
        ToastUtil.showToast("操作成功");
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(InstallNotifyEventBusEntity installNotifyEventBusEntity) {
        if (installNotifyEventBusEntity.getFlightNo().equals(mBaseData.getFlightNo()) && installNotifyEventBusEntity.getType() == 1) {
            if (mWaitCallBackDialog != null) {
                mWaitCallBackDialog.dismiss();
            }
            switch (mRgTitle.getCheckedRadioButtonId()) {
                case R.id.rb_install:
                    mRgTitle.check(R.id.rb_install);// 切换到 装机单
                    break;
                case R.id.rb_advise_install:
                    mRgTitle.check(R.id.rb_advise_install);// 切换到
                    break;
                case R.id.rb_end_install:
                    mRgTitle.check(R.id.rb_end_install);// 切换到
                    break;
                case R.id.rb_end_advise_install:
                    mRgTitle.check(R.id.rb_end_advise_install);// 切换到
                    break;
            }
            loadFlag = getRadioBtnFlag();
            loadData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String installNotifyEventBusEntity) {
        if ("LoadInstall_Sure_Update".equals(installNotifyEventBusEntity)) {
            if (mWaitCallBackDialog != null) {
                mWaitCallBackDialog.dismiss();
            }
            switch (mRgTitle.getCheckedRadioButtonId()) {
                case R.id.rb_install:
                    mRgTitle.check(R.id.rb_install);// 切换到 装机单
                    break;
                case R.id.rb_advise_install:
                    mRgTitle.check(R.id.rb_advise_install);// 切换到
                    break;
                case R.id.rb_end_install:
                    mRgTitle.check(R.id.rb_end_install);// 切换到
                    break;
                case R.id.rb_end_advise_install:
                    mRgTitle.check(R.id.rb_end_advise_install);// 切换到
                    break;
            }
            loadFlag = getRadioBtnFlag();
            loadData();

        }

    }

    private void showStoragePickView() {
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (mListVerson.size() > 0) {
//                    if (mListVerson.get(options1).contains("监装确认")) {
//                        showConfirm(loadFlag);
//                        mTvConfirm.setText("监装员:" + mapPresen.get(options1));
//                        mTvConfirmDate.setText("发送时间:" + mapDate.get(options1));
//                    } else {
//                        mTvConfirm.setVisibility(View.GONE);
//                        mTvConfirmDate.setVisibility(View.GONE);
//                    }
//                    mTvVersion.setText(mListVerson.get(options1));
                    screenData(options1);
                }
            }
        }).build();
        pickerView.setPicker(mListVerson);
        pickerView.setTitleText("版本号");
        if (!map.isEmpty())
            pickerView.show();
        else {
            ToastUtil.showToast("还没有装机单！");
        }
    }

    private void screenData(int verson) {

        tvSureInstall.setVisibility(View.GONE);
        if (mListVerson.get(verson).contains("确认按此装机")) {
            tvSureInstall.setVisibility(View.VISIBLE);
            mTvConfirm.setText("监装员:" + mapPresen.get(mListVersonCode.get(verson)));
            mTvConfirmDate.setText("发送时间:" + mapDate.get(mListVersonCode.get(verson)));
        } else if (mListVerson.get(verson).contains("监装确认")) {
            showConfirm(loadFlag);
            mTvConfirm.setText("监装员:" + mapPresen.get(mListVersonCode.get(verson)));
            mTvConfirmDate.setText("发送时间:" + mapDate.get(mListVersonCode.get(verson)));
        } else {
            mTvConfirm.setVisibility(View.GONE);
            mTvConfirmDate.setVisibility(View.GONE);
            mSrRefush.setBackgroundColor(getResources().getColor(R.color.white));
        }
        mTvVersion.setText(mListVerson.get(verson));
        currentVersion = Integer.valueOf(mListVersonCode.get(verson));

        mTvConfirmJZ.setText("结载员:" + mapPresenJZ.get(mListVersonCode.get(verson)));
        mTvConfirmDateJZ.setText("录入时间:" + mapDateJZ.get(mListVersonCode.get(verson)));

        LnstallationInfoBean.ScootersBean title = new LnstallationInfoBean.ScootersBean();
        title.setCargoName("舱位");
        title.setLocation("货位");
        title.setScooterCode("板车号");
        title.setSerialInd("ULD号");
        title.setDestinationStation("目的站");
        title.setType("类型");
        title.setWeight("重量");
        title.setTotal("体积");
        title.setSpecialCode("特货代码");
//        title.setSpecialNumber("特货代码");
        title.setExceptionFlag(1);
//        mList.add(0, title);
//        List<LnstallationInfoBean.ScootersBean> mList1 = new ArrayList<>();
        mList1.clear();
        mList1.add(title);
        mList1.addAll(map.get(mListVersonCode.get(verson)));
        mId = mapMid.get(mListVersonCode.get(verson));

        for (LnstallationInfoBean.ScootersBean scootersBean:mList1){
            if (scootersBean.getCargoName()!=null&&scootersBean.getCargoName().length()==1){
                scootersBean.setCargoName(scootersBean.getCargoName()+"H");
            }
            if (scootersBean.getOldCargoName()!=null&&scootersBean.getOldCargoName().length()==1){
                scootersBean.setOldCargoName(scootersBean.getOldCargoName()+"H");
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 打印
     */
    private void printManifest(String printName) {

        mPresenter = new PrintRequestPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightId(mBaseData.getFlightId());
        entity.setReportInfoId(mId);
        entity.setCurrentVersion(currentVersion);
        if (getRadioBtnFlag() == 5|| getRadioBtnFlag() == 6){
            entity.setType(4);
        }
        else {
        // 1 货邮舱单 2 装机单 3装舱建议 4最终装机单
            entity.setType(2);
        }

        entity.setPrintName(printName);
        ((PrintRequestPresenter) mPresenter).printRequest(entity);

    }

    /**
     * 二次打开舱门
     */
    private void openAirDoor() {
        mPresenter = new ReOpenLoadTaskPresenter(LnstallationInfoActivity.this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightId(mBaseData.getFlightId());
        entity.setWorkerId(UserInfoSingle.getInstance().getUserId());
        entity.setRemark("");
        ((ReOpenLoadTaskPresenter) mPresenter).reOpenLoadTask(entity);
    }

    /**
     * 通知录入
     */
    private void notifyInput() {
        mPresenter = new SynchronousLoadingPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightId(mBaseData.getFlightId());
        entity.setOperationUserName(UserInfoSingle.getInstance().getUsername());
        entity.setOperationUser(UserInfoSingle.getInstance().getUserId());
        if (getRadioBtnFlag() == 5 || getRadioBtnFlag() == 6)
            entity.setLocation(1);
        else {
            entity.setLocation(2);
        }
        String userName = UserInfoSingle.getInstance().getUsername();
        entity.setOperationUserName((userName.contains("-")) ? userName.substring(0, userName.indexOf("-")) : userName);
        ((SynchronousLoadingPresenter) mPresenter).synchronousLoading(entity);
    }

    /**
     * 二次确认弹出框
     *
     * @param title
     * @param msg
     * @param flag
     */
    private void showYesOrNoDialog(String title, String msg, int flag) {
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定")
                .setNegativeButton("取消")
                .isCanceledOnTouchOutside(false)
                .isCanceled(true)
                .setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            switch (flag) {
                                case 2:
                                    printManifest("1");
                                    break;
                                case 4:
                                    openAirDoor();
                                    break;
                                case 5:
                                    notifyInput();
                                    break;


                            }
                        } else {
//                            ToastUtil.showToast("点击了右边的按钮");
                        }
                    }
                })
                .show();

    }

    @Override
    public void toastView(String error) {
        mSrRefush.setRefreshing(false);
        if (loadFlag != -1) {
            mList1.clear();
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void showNetDialog() {
        showProgessDialog("请求中......");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据……");
        new Handler().postDelayed(() -> {
            loadData();
            dismissProgessDialog();
        }, 2000);
    }

    /**
     * 通知录入 结果返回
     *
     * @param result
     */
    @Override
    public void synchronousLoadingResult(String result) {
//        if (result != null)
//            ToastUtil.showToast(result);
        showDialogWait();
//        finish();
    }

    @Override
    public void printRequestResult(String result) {
        ToastUtil.showToast("打印成功");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWaitCallBackDialog != null && mWaitCallBackDialog.isShowing()) {
            mWaitCallBackDialog.dismiss();
        }
    }
}
