package qx.app.freight.qxappfreight.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.AllocateVehiclesAdapter;
import qx.app.freight.qxappfreight.adapter.ReturnGoodAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.request.LoginEntity;
import qx.app.freight.qxappfreight.bean.response.ItemBean;
import qx.app.freight.qxappfreight.bean.response.LoginResponseBean;
import qx.app.freight.qxappfreight.contract.LoginContract;
import qx.app.freight.qxappfreight.presenter.LoginPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * TODO : 出港退货
 * Created by pr
 */
public class ReturnGoodsActivity extends BaseActivity implements LoginContract.loginView, MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    @BindView(R.id.mfrv_returngood_list)
    MultiFunctionRecylerView mMfrvAllocateList;

    private ReturnGoodAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_return_good;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        InitDatas();
    }

    private void InitDatas() {
        mPresenter = new LoginPresenter(this);
        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setUsername("zhuyuhang");
        loginEntity.setPassword("111111");
        ((LoginPresenter) mPresenter).login(loginEntity);
        mMfrvAllocateList.setLayoutManager(new LinearLayoutManager(this));
        mMfrvAllocateList.setRefreshListener(this);
        mMfrvAllocateList.setOnRetryLisenter(this);
    }

    @Override
    public void loginResult(LoginResponseBean loginBean) {
        if (loginBean != null) {
            adapter = new ReturnGoodAdapter(loginBean.getRoleRS());
            mMfrvAllocateList.setAdapter(adapter);
        } else {
            ToastUtil.showToast(this, "数据错误");
        }
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

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRetry() {

    }
}
