package qx.app.freight.qxappfreight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.WaybillQueryResultAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
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


    @Override
    public int getLayoutId() {
        return R.layout.activity_waybill_query;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.WHITE, "运单查询");
        mEtInputKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPresenter = new AddInventoryDetailPresenter(WayBillQueryActivity.this);
                ((AddInventoryDetailPresenter) mPresenter).listWaybillCode(s.toString());
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
            List<String> result = listWaybillCodeBean.getData();
            if (result == null || result.size() == 0) {
                ToastUtil.showToast("未查询到对应的运单数据");
            } else {
                WaybillQueryResultAdapter adapter = new WaybillQueryResultAdapter(result);
                mRvSearchResult.setLayoutManager(new LinearLayoutManager(this));
                mRvSearchResult.setAdapter(adapter);
                adapter.setOnItemClickListener((adapter1, view, position) -> {
                    Intent intent = new Intent();
                    intent.putExtra("query_result", result.get(position));
                    setResult(RESULT_OK, intent);
                    finish();
                });
            }
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
}
