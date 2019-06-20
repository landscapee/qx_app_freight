package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ouyben.empty.EmptyLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.InPortTallyListAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.InPortTallyListEntity;
import qx.app.freight.qxappfreight.bean.InportTallyBean;
import qx.app.freight.qxappfreight.bean.UserInfoSingle;
import qx.app.freight.qxappfreight.bean.request.BaseFilterEntity;
import qx.app.freight.qxappfreight.bean.request.InPortTallyCommitEntity;
import qx.app.freight.qxappfreight.contract.InPortTallyContract;
import qx.app.freight.qxappfreight.presenter.InPortTallyPresenter;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 进港理货页面
 */
public class InPortTallyActivity extends BaseActivity implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter, InPortTallyContract.InPortTallyListView {
    @BindView(R.id.mfrv_in_port_tally)
    MultiFunctionRecylerView mMfrvInPort;
    @BindView(R.id.tv_ship_bill_info)
    TextView mTvBill;//舱单总信息
    @BindView(R.id.tv_tally_info)
    TextView mTvTally;//理货总信息
    @BindView(R.id.tv_exception_filing)
    TextView mExceptionFiling;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;
    private int mCurrentPage = 1;
    private List<InPortTallyListEntity> mList = new ArrayList<>();
    private int billTotal = 0, tallyTotal = 0;
    private double billWeightTotal = 0, tallyWeightTotal = 0;
    private InPortTallyListAdapter mAdapter;
    private int mModifyPos = -1;
    private List<InportTallyBean> mBaseList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_in_port_tally;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "上一步", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "进港理货");
        mMfrvInPort.setRefreshListener(this);
        mMfrvInPort.setOnRetryLisenter(this);
        initData();
        mExceptionFiling.setOnClickListener(v -> {
            Intent intent = new Intent(InPortTallyActivity.this, ExceptionFilingActivity.class);
            intent.putExtra("flight_number", getIntent().getStringExtra("flight_number"));
            InPortTallyActivity.this.startActivity(intent);
        });
        mTvConfirm.setOnClickListener(v -> {
            InPortTallyCommitEntity entity = new InPortTallyCommitEntity();
            entity.setFlightId(getIntent().getStringExtra("flight_id"));
            entity.setFlightNo(getIntent().getStringExtra("flight_number"));
            entity.setInWaybills(mBaseList);
            entity.setTaskId(getIntent().getStringExtra("task_id"));
            entity.setUserId(UserInfoSingle.getInstance().getUserId());
            entity.setUserName(UserInfoSingle.getInstance().getLoginName());
            ((InPortTallyPresenter) mPresenter).inPortTallyCommit(entity);
        });
    }

    private void initData() {
        String flightId = getIntent().getStringExtra("flight_id");
        BaseFilterEntity entity = new BaseFilterEntity();
        entity.setFlightId(flightId);
        mPresenter = new InPortTallyPresenter(this);
        ((InPortTallyPresenter) mPresenter).getInPortTallyList(entity);
    }

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据。。。。。。");
        new Handler().postDelayed(() -> {
            initData();
            dismissProgessDialog();
        }, 2000);
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMore() {
        initData();
    }

    @Override
    public void getInPortTallyListResult(List<InportTallyBean> result) {
        mBaseList.clear();
        mBaseList.addAll(result);
        if (mCurrentPage == 1) {
            mList.clear();
            mMfrvInPort.finishRefresh();
        } else {
            mCurrentPage++;
            mMfrvInPort.finishLoadMore();
        }
        for (InportTallyBean bean : result) {
            InPortTallyListEntity model = new InPortTallyListEntity();
            model.setWaybill(bean.getWaybillCode());
            model.setFlightInfoList(StringUtil.getFlightList(bean.getRoute()));
            model.setDocName(bean.getMailType());
            model.setDocArrived(bean.getDocumentDelivery() == 1);
            model.setDocNumber(bean.getTotalNumberPackages());
            model.setDocWeight(bean.getTotalWeight());
            model.setManifestNumber(bean.getManifestTotal());
            model.setManifestWeight(bean.getManifestWeight());
            model.setTallyNumber(bean.getTallyingTotal());
            model.setTallyWeight(bean.getTallyingWeight());
            model.setCustomsCont(false);
            model.setTransport(bean.getTransit() == 1);
            model.setUnpackagePlate(bean.getDevanning() == 1);
            model.setChill(bean.getRefrigerated() == 1);
            model.setStoreName("");
            model.setStoreNumber(0);
            int error = bean.getTallyingTotal() - bean.getManifestTotal();
            String exceptionSituation = "无异常";
            if (error != 0) {
                if (error < 0) {
                    exceptionSituation = "缺货" + (-error) + "件";
                } else {
                    exceptionSituation = "多货" + error + "件";
                }
            }
            model.setNumberError(error);
            model.setExceptionSituation(exceptionSituation);
            mList.add(model);
            billTotal += bean.getManifestTotal();
            tallyTotal += bean.getTallyingTotal();
            billWeightTotal += bean.getManifestWeight();
            tallyWeightTotal += bean.getTallyingWeight();
        }
        mTvBill.setText(String.format(getString(R.string.format_tally_info), billTotal, billWeightTotal));
        mTvTally.setText(String.format(getString(R.string.format_tally_info), tallyTotal, tallyWeightTotal));
        mAdapter = new InPortTallyListAdapter(mList);
        mAdapter.setOnModifyListener(pos -> mModifyPos = pos);
        mMfrvInPort.setLayoutManager(new LinearLayoutManager(this));
        mMfrvInPort.setAdapter(mAdapter);
    }

    @Override
    public void inPortTallyCommitResult(String result) {
        ToastUtil.showToast("舱单理货数据提交成功");
        EventBus.getDefault().post("InPortTallyFragment_refresh");
        finish();
    }

    @Override
    public void toastView(String error) {
        if (mCurrentPage == 1) {
            mMfrvInPort.finishRefresh();
        } else {
            mMfrvInPort.finishLoadMore();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 234) {
            mList.remove(mModifyPos);
            InPortTallyListEntity bean = (InPortTallyListEntity) data.getSerializableExtra("data");
            InportTallyBean baseEntity = mBaseList.remove(mModifyPos);
            int error = bean.getNumberError();
            baseEntity.setUbnormalNum(error);
            baseEntity.setTallyingTotal(bean.getTallyNumber());
            baseEntity.setTallyingWeight(bean.getTallyWeight());
            baseEntity.setUbnormalType(bean.getErrorList());
            mBaseList.add(mModifyPos, baseEntity);
            String exceptionSituation = "无异常";
            if (error != 0) {
                if (error < 0) {
                    exceptionSituation = "缺货" + (-error) + "件";
                } else {
                    exceptionSituation = "多货" + error + "件";
                }
            }
            bean.setExceptionSituation(exceptionSituation);
            mList.add(mModifyPos, bean);
            billTotal = 0;
            tallyTotal = 0;
            billWeightTotal = 0;
            tallyWeightTotal = 0;
            for (InPortTallyListEntity entity : mList) {
                billTotal += entity.getManifestNumber();
                tallyTotal += entity.getTallyNumber();
                billWeightTotal += entity.getManifestWeight();
                tallyWeightTotal += entity.getTallyWeight();
            }
            mTvBill.setText(String.format(getString(R.string.format_tally_info), billTotal, billWeightTotal));
            mTvTally.setText(String.format(getString(R.string.format_tally_info), tallyTotal, tallyWeightTotal));
            mAdapter = new InPortTallyListAdapter(mList);
            mAdapter.setOnModifyListener(pos -> mModifyPos = pos);
            mMfrvInPort.setLayoutManager(new LinearLayoutManager(this));
            mMfrvInPort.setAdapter(mAdapter);
        }
    }
}
