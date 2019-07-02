package qx.app.freight.qxappfreight.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseFragment;
import qx.app.freight.qxappfreight.bean.response.GetInfosByFlightIdBean;
import qx.app.freight.qxappfreight.bean.response.GetTodoScootersBean;
import qx.app.freight.qxappfreight.contract.TodoScootersContract;

/**复重-舱单列表
 * created by swd
 * 2019/7/2 11:24
 */
public class RepeatWeightManifestFragment extends BaseFragment implements TodoScootersContract.todoScootersView {
    @BindView(R.id.rl_view)
    RecyclerView rlView;

    public static RepeatWeightManifestFragment getInstance(String flightId) {
        RepeatWeightManifestFragment fragment = new RepeatWeightManifestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("flightId", flightId);
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
        initView();
    }

    private void initView() {

    }

    @Override
    public void todoScootersResult(List<GetInfosByFlightIdBean> result) {

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
