package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ManifestWaybillListjianyiAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ManifestMainBean;
import qx.app.freight.qxappfreight.bean.ManifestScooterListBean;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.FlightAllReportInfo;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.GetLastReportInfoContract;
import qx.app.freight.qxappfreight.presenter.GetLastReportInfoPresenter;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

public class ZdFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, GetLastReportInfoContract.getLastReportInfoView {


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
        View view = inflater.inflate(R.layout.fragment_zd, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRvData.setLayoutManager(new LinearLayoutManager(getContext()));

        mBaseData = (LoadAndUnloadTodoBean) getActivity().getIntent().getSerializableExtra("data");
        loadData();
        mSrRefush.setOnRefreshListener(() -> loadData());
    }

    private void loadData() {
        mPresenter = new GetLastReportInfoPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightId(mBaseData.getFlightId());
        //货邮舱单
        entity.setDocumentType(1);
        entity.setSort(1);
        ((GetLastReportInfoPresenter) mPresenter).getLastReportInfo(entity);
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

    @Override
    public void toastView(String error) {
        mSrRefush.setRefreshing(false);
//        mRvData.finishRefresh();
    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }

    @Override
    public void getLastReportInfoResult(List<FlightAllReportInfo> result) {
        if (result != null) {
//            mTvVersion.setText("版本号" + result.getVersion());
            cagnWeight = 0;
            emailWeight = 0;
            name = "";
            mSrRefush.setRefreshing(false);
            mList.clear();
            Gson mGson = new Gson();
            ManifestMainBean[] datas = mGson.fromJson(result.get(0).getContent(), ManifestMainBean[].class);
//            for (ManifestMainBean data : datas) {
//                for (ManifestMainBean.CargosBean bean : data.getCargos()) {
//                    for (ManifestScooterListBean data1 : bean.getScooters()) {
//                        data1.getWaybillList().get(0).setRouteEn();
//                        mList.addAll(data1.getWaybillList());
//                    }
//                }
//            }
            for (int i = 0; i < datas.length; i++) {
                name = datas[i].getCreateUserName();
                for (int j = 0; j < datas[i].getCargos().size(); j++) {
                    for (int k = 0; k < datas[i].getCargos().get(j).getScooters().size(); k++) {
                        datas[i].getCargos().get(j).getScooters().get(k).getWaybillList().get(k).setRouteEn(datas[i].getRouteEn());
                        mList.addAll(datas[i].getCargos().get(j).getScooters().get(k).getWaybillList());
                    }
                }
            }

            for (int i = 0; i < mList.size(); i++) {
                //TODO C 货物
                if (!"".equals(mList.get(i).getWeight())) {
                    if ("C".equals(mList.get(i).getMailType()))
                        cagnWeight += Double.valueOf(mList.get(i).getWeight());
                        //TODO M 邮件
                    else if ("M".equals(mList.get(i).getMailType()))
                        emailWeight += Double.valueOf(mList.get(i).getWeight());
                }
            }

            tvCagnWeight.setText("货物总重量：" + cagnWeight + "");
            tvEmailWeight.setText("邮件总重量：" + emailWeight + "");
            tvName.setText("配载员：" + name);
            //TODO 是否是宽体机 0 宽体机 1 窄体机
            if (1 == mBaseData.getWidthAirFlag()) {
                ManifestScooterListBean.WaybillListBean title = new ManifestScooterListBean.WaybillListBean();
                title.setWaybillCode("运单号");
                title.setNumber("件数");
                title.setWeight("重量");
                title.setRouteEn("航程");
                title.setCargoCn("货物名称");
                title.setSpecialCode("特货代码");
                title.setInfo("备注");
                mList.add(0, title);
            } else if (0 == mBaseData.getWidthAirFlag()) {
                ManifestScooterListBean.WaybillListBean title = new ManifestScooterListBean.WaybillListBean();
                title.setModel("型号");
                title.setWaybillCode("集装箱板号");
                title.setNumber("件数");
                title.setWeight("重量");
                title.setSpecialCode("特货代码");
                title.setMailType("货邮代码");
                mList.add(0, title);
            }

            ManifestWaybillListjianyiAdapter adapter = new ManifestWaybillListjianyiAdapter(mList, mBaseData.getWidthAirFlag());
            mRvData.setAdapter(adapter);
        }
    }
}
