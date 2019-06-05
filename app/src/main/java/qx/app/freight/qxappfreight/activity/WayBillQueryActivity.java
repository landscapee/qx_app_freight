package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.WaybillQueryResultAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.WayBillQueryBean;
import qx.app.freight.qxappfreight.bean.response.ListWaybillCodeBean;
import qx.app.freight.qxappfreight.contract.AddInventoryDetailContract;
import qx.app.freight.qxappfreight.presenter.AddInventoryDetailPresenter;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 运单查询页面,使用方法类似LoginActivity中第181-182行，回调为188-193行
 */
public class WayBillQueryActivity extends BaseActivity implements AddInventoryDetailContract.addInventoryDetailView {
    @BindView(R.id.et_input_key)
    EditText mEtInputKey;
    @BindView(R.id.rv_search_result)
    RecyclerView mRvSearchResult;

    private List<ListWaybillCodeBean.DataBean> resultData = new ArrayList<>();
    private WaybillQueryResultAdapter adapter;

    private String taskId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_waybill_query;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        taskId = getIntent().getStringExtra("taskId");
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "运单查询");

        adapter = new WaybillQueryResultAdapter(resultData);
        mRvSearchResult.setLayoutManager(new LinearLayoutManager(this));
        mRvSearchResult.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {

            EventBus.getDefault().post(new WayBillQueryBean(resultData.get(position).getWaybillCode(), resultData.get(position).getId()));
            finish();

        });

        mEtInputKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    mPresenter = new AddInventoryDetailPresenter(WayBillQueryActivity.this);
                    ((AddInventoryDetailPresenter) mPresenter).listWaybillCode(s.toString(), taskId);
                } else {
                    resultData.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void addInventoryDetailResult(String result) {

    }

    @Override
    public void uploadsResult(Object result) {

    }

    @Override
    public void listWaybillCodeResult(ListWaybillCodeBean listWaybillCodeBean) {
        if ("200".equals(listWaybillCodeBean.getStatus())) {
            resultData.clear();
            List<ListWaybillCodeBean.DataBean> result = listWaybillCodeBean.getData();
            if (result == null || result.size() == 0) {
                ToastUtil.showToast("未查询到对应的运单数据");
            } else {
                resultData.addAll(result);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {
        showProgessDialog("请求中......");
    }

    @Override
    public void dissMiss() {
        dismissProgessDialog();
    }
}
