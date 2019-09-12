package qx.app.freight.qxappfreight.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.activity.CargoManifestInfoActivity;
import qx.app.freight.qxappfreight.adapter.ManifestWaybillListAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ManifestMainBean;
import qx.app.freight.qxappfreight.bean.ManifestScooterListBean;
import qx.app.freight.qxappfreight.bean.loadinglist.CargoManifestEventBusEntity;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.GetLastReportInfoContract;
import qx.app.freight.qxappfreight.presenter.GetLastReportInfoPresenter;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 货邮舱单
 */
public class HyFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter{

    @BindView(R.id.mfrv_data)
    RecyclerView mRvData;//货邮舱单信息列表

    @BindView(R.id.sr_refush)
    SwipeRefreshLayout mSrRefush;
    @BindView(R.id.tv_cagn_weight)
    TextView tvCagnWeight;
    @BindView(R.id.tv_email_weight)
    TextView tvEmailWeight;
    @BindView(R.id.tv_name)
    TextView tvName;

    private LoadAndUnloadTodoBean mBaseData;
    private List<ManifestScooterListBean.WaybillListBean> mList = new ArrayList<>();
    private int cagnWeight, emailWeight;
    private String name;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hy, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mRvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mSrRefush.setOnRefreshListener(() ->
                        ((CargoManifestInfoActivity)getActivity()).loadData()
                );
    }
    @Override
    public void onRetry() {

    }
    @Override
    public void onRefresh() {

    }
    @Override
    public void onLoadMore() {

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CargoManifestEventBusEntity cargoManifestEventBusEntity) {
        List<FlightAllReportInfo> result = cargoManifestEventBusEntity.getBeans();
        if (result != null && result.size()>0) {
//            mTvVersion.setText("版本号" + result.getVersion());
//        mRvData.finishRefresh();
            cagnWeight = 0;
            emailWeight = 0;
            mSrRefush.setRefreshing(false);
            mList.clear();
            Gson mGson = new Gson();
            name = result.get(0).getCreateUserName();
            ManifestMainBean[] datas = mGson.fromJson(result.get(0).getContent(), ManifestMainBean[].class);

//            List<ManifestMainBean> manifestMainBeans = Arrays.asList(datas);
//            if (Build.VERSION.SDK_INT >= 24) {
//                mList.addAll(manifestMainBeans.parallelStream().map(ManifestMainBean::getCargos).flatMap(Collection::stream).map(ManifestMainBean.CargosBean::getScooters).flatMap(Collection::stream).map(ManifestScooterListBean::getWaybillList).flatMap(Collection::stream).collect(Collectors.toList()));
//            }
//            else {
                for (int i = 0; i < datas.length; i++) {
                    for (int j = 0; j < datas[i].getCargos().size(); j++) {
                        for (int k = 0; k < datas[i].getCargos().get(j).getScooters().size(); k++) {
                            for (int l = 0; l < datas[i].getCargos().get(j).getScooters().get(k).getWaybillList().size(); l++) {
                                datas[i].getCargos().get(j).getScooters().get(k).getWaybillList().get(l).setRouteEn(datas[i].getRouteEn());
                            }
                            mList.addAll(datas[i].getCargos().get(j).getScooters().get(k).getWaybillList());
                        }
                    }
                }
//            }
            for (int i = 0; i < mList.size(); i++) {
                if (!"".equals(mList.get(i).getWeight())) {
                    //TODO C 货物
                    if ("C".equals(mList.get(i).getMailType())) {
                        cagnWeight += Double.valueOf(mList.get(i).getWeight());
                    }
                    //TODO M 邮件
                    else if ("M".equals(mList.get(i).getMailType())) {
                        emailWeight += Double.valueOf(mList.get(i).getWeight());
                    }
                }
            }
            tvCagnWeight.setText("货物总重量：" + cagnWeight + "");
            tvEmailWeight.setText("邮件总重量：" + emailWeight + "");
            tvName.setText("配载员：" + name);
            ManifestScooterListBean.WaybillListBean title = new ManifestScooterListBean.WaybillListBean();
            title.setWaybillCode("运单号");
//            title.setModel("型号");
            title.setRouteEn("航程");
            title.setNumber("件数");
            title.setWeight("重量");
//            title.setVolume("体积");
            title.setSpecialCode("特货代码");
            title.setCargoCn("货物名称");
//            title.setSuggestRepository("仓位");
            title.setInfo("备注");
//            title.setCargoCn("货物名称");
            mList.add(0, title);
            ManifestWaybillListAdapter adapter = new ManifestWaybillListAdapter(mList);
            mRvData.setAdapter(adapter);
        }
    }
}
