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

import com.google.gson.Gson;
import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.ManifestWaybillListAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.ManifestMainBean;
import qx.app.freight.qxappfreight.bean.ManifestScooterListBean;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.LastReportInfoListBean;
import qx.app.freight.qxappfreight.bean.response.LoadAndUnloadTodoBean;
import qx.app.freight.qxappfreight.contract.GetLastReportInfoContract;
import qx.app.freight.qxappfreight.presenter.GetLastReportInfoPresenter;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

public class ZdFragment extends BaseFragment implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, GetLastReportInfoContract.getLastReportInfoView {


    @BindView(R.id.mfrv_data)
    RecyclerView mRvData;//货邮舱单信息列表
    @BindView(R.id.sr_refush)
    SwipeRefreshLayout mSrRefush;


    private LoadAndUnloadTodoBean mBaseData;
    private List<ManifestScooterListBean.WaybillListBean> mList = new ArrayList<>();
    private String mId;


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
    public void getLastReportInfoResult(LastReportInfoListBean result) {
        if (result != null) {
            mId = result.getId();
//            mTvVersion.setText("版本号" + result.getVersion());
//        mRvData.finishRefresh();
            mSrRefush.setRefreshing(false);
            mList.clear();
            Gson mGson = new Gson();
            ManifestMainBean[] datas = mGson.fromJson(result.getContent(), ManifestMainBean[].class);
            for (ManifestMainBean data : datas) {
                for (ManifestMainBean.CargosBean bean : data.getCargos()) {
                    for (ManifestScooterListBean data1 : bean.getScooters()) {
                        mList.addAll(data1.getWaybillList());
                    }
                }
            }
            ManifestScooterListBean.WaybillListBean title = new ManifestScooterListBean.WaybillListBean();
            title.setWaybillCode("运单号");
            title.setWeight("重量");
            title.setNumber("件数");
            title.setCargoCn("货物名称");
            title.setVolume("体积");
            mList.add(0, title);
            ManifestWaybillListAdapter adapter = new ManifestWaybillListAdapter(mList);
            mRvData.setAdapter(adapter);
        }
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
}
