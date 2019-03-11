package qx.app.freight.qxappfreight.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.ouyben.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.adapter.StoreInfoRvAdapter;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.response.MainListBean;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 入库列表页面
 */
public class StoreGoodsActivity extends BaseActivity implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    private int mSize = 1;
    private int index = 0;
    private MultiFunctionRecylerView mMfrvData;
    private String mGoodsType;
    private List<MainListBean.DataBean.SingeDataBean> list;
    private StoreInfoRvAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_store_goods;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> finish());
        toolbar.setLeftTextView(View.VISIBLE, Color.WHITE, "返回", v -> finish());
        toolbar.setMainTitle(Color.BLUE, "入库");
        initShow();
    }

    private void initShow() {
        mMfrvData = findViewById(R.id.mfrv_store_goods);
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        initData(mSize);
        mGoodsType = getIntent().getStringExtra("goods_type");
        adapter = new StoreInfoRvAdapter(list, mGoodsType);
        mMfrvData.setLayoutManager(new LinearLayoutManager(this));
        mMfrvData.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        mSize = 1;
        index += 1;
        if (index % 3 == 0) {
            list.clear();
        } else {
            initData(mSize);
        }
        mMfrvData.notifyForAdapter(adapter);
    }

    @Override
    public void onLoadMore() {
        mSize += 1;
        index += 1;
        if (index % 3 == 0) {
            list.clear();
        } else {
            initData(mSize);
        }
        mMfrvData.notifyForAdapter(adapter);
    }

    private void initData(int size) {
        if (list == null)
            list = new ArrayList <>();
        for (int i = 0; i < size; i++) {
            ArrayList<MainListBean.DataBean.SingeDataBean> list1 = getIntent().getParcelableArrayListExtra("data");
            list.addAll(list1);
        }
    }

    @Override
    public void onRetry() {
        showProgessDialog("正在加载数据。。。。。。");
        new Handler().postDelayed(() -> {
            onRefresh();
            dismissProgessDialog();
        }, 2000);
    }
}
