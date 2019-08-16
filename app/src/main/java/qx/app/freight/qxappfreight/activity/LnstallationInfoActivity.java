package qx.app.freight.qxappfreight.activity;

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
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.LnstallationListAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;
import qx.app.freight.qxappfreight.bean.response.LnstallationInfoBean;
import qx.app.freight.qxappfreight.bean.response.TransportDataBase;
import qx.app.freight.qxappfreight.contract.GetFlightAllReportInfoContract;
import qx.app.freight.qxappfreight.contract.SynchronousLoadingContract;
import qx.app.freight.qxappfreight.presenter.GetFlightAllReportInfoPresenter;
import qx.app.freight.qxappfreight.presenter.SynchronousLoadingPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.FlightInfoLayout;

public class LnstallationInfoActivity extends BaseActivity implements EmptyLayout.OnRetryLisenter, GetFlightAllReportInfoContract.getFlightAllReportInfoView, SynchronousLoadingContract.synchronousLoadingView {

    @BindView(R.id.tv_flight_number)
    TextView mTvFlightNumber;//航班号
    @BindView(R.id.tv_plane_info)
    TextView mTvPlaneInfo;//机型
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;//确认人
    @BindView(R.id.tv_confim_date)
    TextView mTvConfirmDate;//确认时间
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
    @BindView(R.id.ll_storage_version)
    LinearLayout LlStorageVersion;


    private TransportDataBase mBaseData;
    private List<LnstallationInfoBean.ScootersBean> mList = new ArrayList<>();
    private List<String> mListVerson = new ArrayList<>();

    private HashMap<Integer, List<LnstallationInfoBean.ScootersBean>> map = new HashMap<>();
    private HashMap<Integer, String> mapPresen = new HashMap<>();
    private HashMap<Integer, String> mapDate = new HashMap<>();

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
        mBaseData = (TransportDataBase) getIntent().getSerializableExtra("data");
        mTvFlightNumber.setText(mBaseData.getFlightNo());
        mTvPlaneInfo.setText(mBaseData.getAircraftNo());
        FlightInfoLayout layout = new FlightInfoLayout(this, mBaseData.getFlightCourseByAndroid());
        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLlContainer.removeAllViews();
        mLlContainer.addView(layout, paramsMain);
        mTvSeat.setText(mBaseData.getSeat());
        mTvTakeOff.setText(StringUtil.getTimeTextByRegix(mBaseData.getEtd(), "HH:mm"));
        mTvFallDown.setText(StringUtil.getTimeTextByRegix(mBaseData.getAta(), "HH:mm"));
        mTvDate.setText(StringUtil.getTimeTextByRegix(mBaseData.getScheduleTime(), "yyyy-MM-dd"));
//        mTvVersion.setText(mBaseData.getVersion() == null ? "版本号：- -" : "版本号：" + mBaseData.getVersion());
        mRvData.setLayoutManager(new LinearLayoutManager(this));

        loadData();
        mBtSure.setOnClickListener(v -> {
            mPresenter = new SynchronousLoadingPresenter(this);
            BaseFilterEntity entity = new BaseFilterEntity();
            entity.setFlightInfoId(mBaseData.getFlightId());
            entity.setOperationUserName(UserInfoSingle.getInstance().getUsername());
            entity.setOperationUser(UserInfoSingle.getInstance().getUserId());
            String userName = UserInfoSingle.getInstance().getUsername();
            entity.setOperationUserName((userName.contains("-"))?userName.substring(0,userName.indexOf("-")):userName);
            ((SynchronousLoadingPresenter) mPresenter).synchronousLoading(entity);
        });
        mSrRefush.setOnRefreshListener(() -> loadData());
        LlStorageVersion.setOnClickListener((v -> showStoragePickView()));
    }

    private void loadData() {
        mPresenter = new GetFlightAllReportInfoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightInfoId(mBaseData.getFlightId());
        //装机单
        entity.setDocumentType(2);
        //1:倒序 2:正序
        entity.setSort(1);
        ((GetFlightAllReportInfoPresenter) mPresenter).getFlightAllReportInfoView(entity);
    }

    @Override
    public void getFlightAllReportInfoResult(List<FlightAllReportInfo> flightAllReportInfos) {
        map.clear();
        mSrRefush.setRefreshing(false);
        mListVerson.clear();
        mapPresen.clear();
        mapDate.clear();
        mTvVersion.setText("版本号:" + flightAllReportInfos.get(0).getVersion());
        if (null != flightAllReportInfos && flightAllReportInfos.size() > 0) {
            Gson mGson = new Gson();
            for (int i = 0; i < flightAllReportInfos.size(); i++) {
                if (flightAllReportInfos.get(i).getContent() != null) {
                    LnstallationInfoBean[] datas = mGson.fromJson(flightAllReportInfos.get(i).getContent(), LnstallationInfoBean[].class);
                    List<LnstallationInfoBean.ScootersBean> list = new ArrayList<>();
                    for (LnstallationInfoBean data : datas) {
                        list.addAll(data.getScooters());
                    }
                    map.put(i, list);

                }
                if (flightAllReportInfos.get(i).getInstalledSingleConfirm() == 1) {
                    mListVerson.add("监装确认(版本" + flightAllReportInfos.get(i).getVersion() + ")");
                    mapPresen.put(i, flightAllReportInfos.get(i).getInstalledSingleConfirmUser());
                    mapDate.put(i, StringUtil.getTimeTextByRegix(flightAllReportInfos.get(i).getCreateTime(), "yyyy-MM-DD HH:mm"));

                } else {
                    mListVerson.add("版本号:" + flightAllReportInfos.get(i).getVersion());
                    mapPresen.put(i, "");
                    mapDate.put(i, "");

                }

            }
            screenData(0);
        }
    }

    private void showStoragePickView() {
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (mListVerson.size() > 0) {
                    if (mListVerson.get(options1).contains("监装建议")) {
                        mTvConfirm.setVisibility(View.VISIBLE);
                        mTvConfirmDate.setVisibility(View.VISIBLE);
                        mTvConfirm.setText("监装员:" + mapPresen.get(options1));
                        mTvConfirmDate.setText("发送时间:" + mapDate.get(options1));

                    } else {
                        mTvConfirm.setVisibility(View.GONE);
                        mTvConfirmDate.setVisibility(View.GONE);
                    }
                    mTvVersion.setText(mListVerson.get(options1));
                    screenData(options1);
                }
            }
        }).build();
        pickerView.setPicker(mListVerson);
        pickerView.setTitleText("版本号");
        pickerView.show();
    }

    private void screenData(int verson) {
        LnstallationInfoBean.ScootersBean title = new LnstallationInfoBean.ScootersBean();
        title.setSuggestRepository("舱位");
        title.setGoodsPosition("货位");
        title.setSerialInd("板车号");
        title.setUldCode("ULD号");
        title.setDest("目的站");
        title.setType("类型");
        title.setActWgt("重量");
        title.setRestrictedCargo("件数");
        title.setSpecialNumber("特货代码");

//        mList.add(0, title);
        List<LnstallationInfoBean.ScootersBean> mList1 = new ArrayList<>();
        mList1.add(title);
        mList1.addAll(map.get(verson));
        LnstallationListAdapter adapter = new LnstallationListAdapter(mList1);
        mRvData.setAdapter(adapter);
    }

    @Override
    public void toastView(String error) {
        mSrRefush.setRefreshing(false);
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


    @Override
    public void synchronousLoadingResult(String result) {
        ToastUtil.showToast(result);
        finish();
    }


}
