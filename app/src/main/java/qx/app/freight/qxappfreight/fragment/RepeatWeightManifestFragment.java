package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.RepeatWeightManifestAdapter;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.contract.TodoScootersContract;
import qx.app.freight.qxappfreight.model.ManifestBillModel;
import qx.app.freight.qxappfreight.model.WaybillsBean;
import qx.app.freight.qxappfreight.presenter.TodoScootersPresenter;

/**复重-舱单列表
 * created by swd
 * 2019/7/2 11:24
 */
public class RepeatWeightManifestFragment extends BaseFragment implements TodoScootersContract.todoScootersView {
    @BindView(R.id.rl_view)
    RecyclerView rlView;

    private String flightInfoId;

    private RepeatWeightManifestAdapter adapter;
    private List<WaybillsBean> list;

    public static RepeatWeightManifestFragment getInstance(String flightInfoId) {
        RepeatWeightManifestFragment fragment = new RepeatWeightManifestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("flightInfoId", flightInfoId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.only_recyle_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flightInfoId = getArguments().getString("flightInfoId");
        initView();
        initData();
    }

    private void initView() {
        list = new ArrayList<>();
        adapter = new RepeatWeightManifestAdapter(list);
        rlView.setLayoutManager(new LinearLayoutManager(getContext()));
        rlView.setAdapter(adapter);
    }

    private void initData() {
        mPresenter = new TodoScootersPresenter(this);
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightInfoId(flightInfoId);
        ((TodoScootersPresenter) mPresenter).getManifest(entity);
    }

    @Override
    public void todoScootersResult(List<GetInfosByFlightIdBean> result) {

    }

    @Override
    public void getManifestResult(List<ManifestBillModel> result) {
        if (result!=null&&result.size()!=0){
            for (ManifestBillModel item:result) {
                list.addAll(item.getWaybills());
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {

    }

    @Override
    public void dissMiss() {

    }
}
