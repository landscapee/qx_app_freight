package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.WaybillQueryResultAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.WayBillQueryBean;
import qx.app.freight.qxappfreight.bean.response.BaseEntity;
import qx.app.freight.qxappfreight.bean.response.ListWaybillCodeBean;
import qx.app.freight.qxappfreight.bean.response.WaybillsBean;
import qx.app.freight.qxappfreight.contract.AddInventoryDetailContract;
import qx.app.freight.qxappfreight.presenter.AddInventoryDetailPresenter;
import qx.app.freight.qxappfreight.utils.MyKeyListener;
import qx.app.freight.qxappfreight.utils.StringUtil;
import qx.app.freight.qxappfreight.utils.ToastUtil;
import qx.app.freight.qxappfreight.utils.TransInformation;
import qx.app.freight.qxappfreight.widget.CustomToolbar;

/**
 * 运单查询页面,使用方法类似LoginActivity中第181-182行，回调为188-193行
 */
public class WayBillQueryActivity extends BaseActivity implements AddInventoryDetailContract.addInventoryDetailView {
    @BindView(R.id.et_input_key)
    EditText mEtInputKey;
    @BindView(R.id.rv_search_result)
    RecyclerView mRvSearchResult;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.btn_no_flag)
    Button btnNoFlag;

    private List <ListWaybillCodeBean.DataBean> resultData = new ArrayList <>();
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
        btnSure.setOnClickListener(v -> {
            if (!StringUtil.isEmpty(mEtInputKey.getText().toString())) {
                if (mEtInputKey.getText().toString().startsWith("DN") && mEtInputKey.getText().toString().length() == 11) {
                    EventBus.getDefault().post(new WayBillQueryBean(mEtInputKey.getText().toString(), null));
                    finish();
                } else if (mEtInputKey.getText().toString().length() == 12) {
                    EventBus.getDefault().post(new WayBillQueryBean(mEtInputKey.getText().toString(), null));
                    finish();
                } else {
                    ToastUtil.showToast("请输入正确的运单号");
                }

            } else {
                ToastUtil.showToast("请输入正确的运单号");
            }
        });

        btnNoFlag.setOnClickListener(v -> {
            mPresenter = new AddInventoryDetailPresenter(WayBillQueryActivity.this);
            ((AddInventoryDetailPresenter) mPresenter).getWaybillCode();
        });
        mEtInputKey.setTransformationMethod(new TransInformation());
        mEtInputKey.setKeyListener(new MyKeyListener());
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
                    if (s.toString().length() > 3) {
                        mPresenter = new AddInventoryDetailPresenter(WayBillQueryActivity.this);
                        ((AddInventoryDetailPresenter) mPresenter).listWaybillCode(s.toString(), taskId);
                    }
                } else {
                    resultData.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        mEtInputKey.requestFocus();
    }

    @Override
    public void addInventoryDetailResult(BaseEntity result) {

    }

    @Override
    public void uploadsResult(Object result) {

    }

    @Override
    public void listWaybillCodeResult(ListWaybillCodeBean listWaybillCodeBean) {
        if ("200".equals(listWaybillCodeBean.getStatus())) {
            resultData.clear();
            List <ListWaybillCodeBean.DataBean> result = listWaybillCodeBean.getData();
            if (result == null || result.size() == 0) {
                ToastUtil.showToast("未查询到对应的运单数据");
            } else {
                resultData.addAll(result);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getWaybillCodeResult(String result) {
        EventBus.getDefault().post(new WayBillQueryBean(result, null));
        finish();
    }

    /**
     * 运单 内容返回
     * @param result
     */
    @Override
    public void getWaybillInfoByWaybillCodeResult(WaybillsBean result) {

    }

    @Override
    public void getWaybillInfoByWaybillCodeResultFail() {

    }

    @Override
    public void toastView(String error) {

    }

    @Override
    public void showNetDialog() {
//        showProgessDialog("请求中......");
    }

    @Override
    public void dissMiss() {
//        dismissProgessDialog();
    }
}
