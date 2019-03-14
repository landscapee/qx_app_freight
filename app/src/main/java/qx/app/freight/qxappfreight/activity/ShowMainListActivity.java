package qx.app.freight.qxappfreight.activity;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ouyben.empty.EmptyLayout;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import qx.app.freight.qxappfreight.R;
import qx.app.freight.qxappfreight.app.BaseActivity;
import qx.app.freight.qxappfreight.bean.response.MainListBean;
import qx.app.freight.qxappfreight.constant.Constants;
import qx.app.freight.qxappfreight.widget.CustomToolbar;
import qx.app.freight.qxappfreight.widget.MultiFunctionRecylerView;

/**
 * 展示收验、收运、入库列表数据
 */
public class ShowMainListActivity extends BaseActivity implements MultiFunctionRecylerView.OnRefreshListener, EmptyLayout.OnRetryLisenter {
    private MultiFunctionRecylerView mMfrvData;

    @Override
    public int getLayoutId() {
        return R.layout.activity_show_main_list;
    }

    @Override
    public void businessLogic(Bundle savedInstanceState) {
        CustomToolbar toolbar = getToolbar();
        setToolbarShow(View.VISIBLE);
        toolbar.setLeftIconView(View.VISIBLE, R.mipmap.icon_back, v -> Toast.makeText(this, "左边图标", Toast.LENGTH_LONG).show());
        toolbar.setMainTitle(Color.BLUE, "我的代办(23)");
        toolbar.setRightIconView(View.VISIBLE, R.mipmap.icon_query, v -> Toast.makeText(this, "右边图标", Toast.LENGTH_LONG).show());
        mMfrvData = findViewById(R.id.mfrv_main);
        mMfrvData.setLayoutManager(new LinearLayoutManager(this));
        mMfrvData.setRefreshListener(this);
        mMfrvData.setOnRetryLisenter(this);
        initData();
    }

    private void initData() {
        int type = getIntent().getIntExtra("type", Constants.TYPE_MAIN_LIST_CHECK);
        Gson gson = new Gson();
        switch (type) {
            case 1:
                String chectText = getJson("data1.json");
                MainListBean checkBean = gson.fromJson(chectText, MainListBean.class);
//                MainListRvAdapter adapter1 = new MainListRvAdapter(checkBean.getData());
//                mMfrvData.setAdapter(adapter1);
//                adapter1.setOnItemClickListener((adapter, view, position) -> {
//                    Intent intent = new Intent(ShowMainListActivity.this, VerifyStaffActivity.class);
//                    intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) checkBean.getData().get(position).getSingeData());
//                    ShowMainListActivity.this.startActivity(intent);
//                });
                break;
            case 2:
                String transportText = getJson("data2.json");
                MainListBean transportBean = gson.fromJson(transportText, MainListBean.class);
//                MainListRvAdapter adapter2 = new MainListRvAdapter(transportBean.getData());
//                mMfrvData.setAdapter(adapter2);
//                adapter2.setOnItemClickListener((adapter, view, position) -> {
//                    Intent intent = new Intent(ShowMainListActivity.this, ReceiveGoodsActivity.class);
//                    intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) transportBean.getData().get(position).getSingeData());
//                    ShowMainListActivity.this.startActivity(intent);
//                });
                break;
            case 3:
                String storeText = getJson("data3.json");
                MainListBean storeBean = gson.fromJson(storeText, MainListBean.class);
//                MainListRvAdapter adapter3 = new MainListRvAdapter(storeBean.getData());
//                mMfrvData.setAdapter(adapter3);
//                adapter3.setOnItemClickListener((adapter, view, position) -> {
//                    Intent intent = new Intent(ShowMainListActivity.this, StoreGoodsActivity.class);
//                    intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) storeBean.getData().get(position).getSingeData());
//                    intent.putExtra("goods_type", storeBean.getData().get(position).getGoodsType());
//                    ShowMainListActivity.this.startActivity(intent);
//                });
                break;
        }
    }

    /**
     * 读取assets文件
     *
     * @param fileName 文件名
     * @return 结果字符串
     */
    private  String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assets = getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assets.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
}
